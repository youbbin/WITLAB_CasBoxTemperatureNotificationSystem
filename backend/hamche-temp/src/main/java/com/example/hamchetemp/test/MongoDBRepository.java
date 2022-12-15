package com.example.hamchetemp.test;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.Date;

public interface MongoDBRepository extends MongoRepository<MongoDBModel, LocalDateTime> {
//    MongoDBModel findByName(LocalDateTime datetime);
    MongoDBModel findByDate(LocalDateTime datetime);
}
