package com.java.liurunda.data;

import java.util.Random;

public class NewsEvent {
    public String title;
    public String datetime;
    NewsEvent(){
        Random random = new Random();
        title = Integer.toBinaryString(random.nextInt(100000));
        datetime = Integer.toHexString(random.nextInt(100000));
    }
    NewsEvent(String title, String datetime) {
        this.title = title;
        this.datetime = datetime;
    }
}
