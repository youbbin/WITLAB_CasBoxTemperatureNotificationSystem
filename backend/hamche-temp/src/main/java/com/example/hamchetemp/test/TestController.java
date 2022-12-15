package com.example.hamchetemp.test;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController // Controller의 역할 수행한다고 명시
@RequiredArgsConstructor
public class TestController {
    TestApi testApi = new TestApi();
    private final FirebaseCloudMessageService firebaseCloudMessageService = new FirebaseCloudMessageService();

    // @RequestMapping : 요청에 대해 Controller를 맵핑하기 위한 어노테이션
    @GetMapping("hamche-temp") // 함체 온도 get
    public TestApi getTest(){
        return testApi;
    }

    @PostMapping("hamche-temp") // 함체 온도 post
    public String postTest(@RequestBody TestApi test){
        testApi.setTemp(test.getTemp());
        testApi.setHumi(test.getHumi());
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
