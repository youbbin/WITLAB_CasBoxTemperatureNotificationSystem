package com.example.hamchetemp.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class MongoDBService {
    @Autowired
    MongoDBRepository mongoDBRepository;

    public void saveTempHumi(LocalDateTime date, float temp, float humi){
        MongoDBModel mongoDBModel = new MongoDBModel();
        mongoDBModel.setDate(date);
        mongoDBModel.setTemp(temp);
        mongoDBModel.setHumi(humi);

        mongoDBRepository.save(mongoDBModel);
    }
}
