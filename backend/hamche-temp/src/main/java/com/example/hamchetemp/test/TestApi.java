package com.example.hamchetemp.test;

public class TestApi {
    private float temp = 0;
    private float humi = 0;
    public float getTemp(){
        return temp;
    }

    public float getHumi(){
        return humi;
    }

    public void setTemp(float temp){
        this.temp = temp;
    }

    public void setHumi(float humi){
        this.humi = humi;
    }
}
