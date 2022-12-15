package com.example.hamchetemp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView text_temp, text_humi;
    HttpURLConnection connection;
    HttpRequestGET thread;
    String result = "";
    String[] parsedData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyFirebaseInstanceIDService test = new MyFirebaseInstanceIDService();
        test.onTokenRefresh();
        Log.d("june", "Program Started. Token KEY:" + FirebaseInstanceId.getInstance().getToken() );
        text_temp = (TextView) findViewById(R.id.text_temp);
        text_humi = (TextView) findViewById(R.id.text_humi);
        thread = new HttpRequestGET();
        thread.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        thread.setRunningState(false);
    }

    class HttpRequestGET extends Thread {
        private boolean isRunning = true;

        public void run() {
            while (isRunning) {
                try {
                    Thread.sleep(5000);
                    URL url = new URL("http://210.102.142.15:8080/hamche-temp");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET"); //전송방식
                    connection.setDoOutput(false);       //데이터를 쓸 지 설정
                    connection.setDoInput(true);        //데이터를 읽어올지 설정

                    Log.d("log", ">>>>>>>> GET 요청");
                    InputStream is = connection.getInputStream();
                    StringBuffer sb = new StringBuffer();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    String inputLine;
                    while ((inputLine = br.readLine()) != null) {
                        sb.append(inputLine);
                    }
                    result = sb.toString();
                    br.close();
                    Log.d("Log", ">>>>>>>> GET 완료 : " + result);
                    DataParsing dataParsing = new DataParsing();
                    parsedData = dataParsing.getParsedData(result);
                    text_humi.setTextColor(Color.rgb(72,72,72));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            float temp =Float.parseFloat(parsedData[0]);
                            float humi = Float.parseFloat(parsedData[1]);
                            if(temp <16 || temp>24){
                                text_temp.setTextColor(Color.RED);
                            }else{
                                text_temp.setTextColor(Color.rgb(72,72,72));
                            }
                            text_temp.setText(parsedData[0] + "°C");
                            text_humi.setText(parsedData[1] + "%");
                        }
                    });

                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        public void setRunningState(boolean state){
            isRunning = state;
        }
    }
}