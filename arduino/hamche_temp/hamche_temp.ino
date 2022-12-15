#include <Phpoc.h>
#include <DHT.h>


#define DHTPIN 7
#define DHTTYPE DHT11
#define SERVER_IP "127.0.0.1"
#define PORT 65015

DHT dht(DHTPIN, DHTTYPE);
PhpocClient client;
String jsondata = "";

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  Serial.println("DHT11 Start!");
  dht.begin();
  Phpoc.begin(PF_LOG_SPI | PF_LOG_NET); 
}

void loop() {
  // put your main code here, to run repeatedly:
  float h = dht.readHumidity(); // 습도 측정
  float t = dht.readTemperature(); // 온도 측정
  float f = dht.readTemperature(true); // 화씨 온도 측정

  // 오류 있으면 오류 출력
  if (isnan(h) || isnan(t) || isnan(f)) {
    Serial.println("Failed to read from DHT sensor!");
    return;
  }

  float hif = dht.computeHeatIndex(f, h); // 보정
  float hic = dht.computeHeatIndex(t, h, false); // 보정
  
  Serial.print("Humidity: ");
  Serial.print(h);
  Serial.print(" %\t");
  Serial.print("Temperature: ");
  Serial.print(t);
  Serial.print(" *C ");
  Serial.print(f);
  Serial.print(" *F\t");
  Serial.print("Heat index: ");
  Serial.print(hic);
  Serial.print(" *C ");
  Serial.print(hif);
  Serial.println(" *F");

  postToServer(hic, h); // 온도와 습도 post 요청
  
  delay(60000); // 1분 간격으로 실행
}

void postToServer(float hic, float h){
 
  if(client.connect(SERVER_IP, PORT)){ // 서버와 연결
    Serial.println("Connected To Server");
  }else{
    Serial.println("Connection Error");
    return;
  }
  
  String url = "/hamche-temp";
  String jsondata = String("{\"temp\": ") + hic+",";
  jsondata += String("\"humi\": ") + h+"}";
  Serial.println(jsondata);
  

  client.println("POST /hamche-temp HTTP/1.1");
  client.println(String("Host: ")+SERVER_IP);
  client.println("Content-Type: application/json");
  client.print("Content-Length: ");
  client.println(jsondata.length());
  client.println();
  client.println(jsondata);
    while (client.connected()) {
    if (client.available()) {
      String line = client.readStringUntil("\r");
      Serial.print(line);
    } else {
      delay(50);
    };
  }
  client.stop();
  Serial.println("Connection Closed");
}
