package com.example.hamchetemp.test;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
@Slf4j
@RestController // Controller의 역할 수행한다고 명시
@RequiredArgsConstructor
public class TestController {
    TestApi testApi = new TestApi();
    private final FirebaseCloudMessageService firebaseCloudMessageService = new FirebaseCloudMessageService();

    @Autowired
    MongoDBService mongoDBService;

    Boolean alarm_send = false;
    String token = "e0--LzRn-tc:APA91bFe_d_MyAmyzC8y_TePIVHDLGR8YRRdPq4L10g1vy_GUnIftz4kCkmgTMkuYrLio2kdV7j3N8gicHxWNAZ7IY3vT8__mcg5OLEXPiuPgXMMbU_GBEdzxz_mCTmFGME-07RocwvT";

    // @RequestMapping : 요청에 대해 Controller를 맵핑하기 위한 어노테이션
    @GetMapping("hamche-temp") // 함체 온도 get
    public TestApi getTest(){
        return testApi;
    }



    @PostMapping("hamche-temp") // 함체 온도 post
    public String postTest(@RequestBody TestApi test) throws IOException {
        float temp = test.getTemp();
        float humi = test.getHumi();
        testApi.setTemp(temp);
        testApi.setHumi(humi);
        LocalDateTime now;
        now = LocalDateTime.now();
        /*
        함체 적정 온도 : 16-24도
        함체 온도가 16도 미만이거나 24도 초과일 경우 알림 전송
         */

        if((temp < 16 || temp > 24)){
            if(!alarm_send) { // 그 전에 알람 보내지 않았을 경우에만 알람 전송
                String formatedNow = now.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초"));
                firebaseCloudMessageService.sendMessageTo(
                        token,
                        "함체 온도 이상 !!",
                        formatedNow+" 함체 온도가 " + temp + "도 입니다.");
                System.out.println("온도 이상 알림 전송");
                alarm_send = true;
            }
        } else alarm_send = false;
        System.out.println("alarm_send : " +alarm_send);

        mongoDBService.saveTempHumi(now, temp, humi);
        System.out.println("MongoDB 저장 완료!");
        return "Ok";
    }

    @PostMapping("fcm-test")
    public ResponseEntity pushMessage(@RequestBody RequestDTO requestDTO) throws IOException {
        System.out.println(requestDTO.getToken() + " "
                +requestDTO.getTitle() + " " + requestDTO.getBody());

        firebaseCloudMessageService.sendMessageTo(
                requestDTO.getToken(),
                requestDTO.getTitle(),
                requestDTO.getBody());
        return ResponseEntity.ok().build();
    }


}
