package com.example.hamchetemp.test;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;


import java.time.LocalDateTime;


@Getter
@Setter
@Document(collection = "hamche-temp-humi")
public class MongoDBModel {
    private LocalDateTime date;
    private float temp;
    private float humi;
}