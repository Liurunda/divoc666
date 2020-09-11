package com.java.liurunda.data;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class EventGroup {
    public ArrayList<NewsEvent> list = new ArrayList<>();
    public String keyword;
    public EventGroup()
    {
        Random random = new Random();
        keyword = Integer.toString(random.nextInt(100));
    }
    public static EventGroup read(Scanner scan){
        EventGroup E = new EventGroup();
        E.keyword = scan.nextLine();
        String next;
        while(!(next=scan.nextLine()).equals("endgroup")){
            String date = next;
            scan.nextLine();
            String e="";
            while(!(next=scan.nextLine()).equals("endevent")){
                e=e+next+"\n";
            }
            E.list.add(new NewsEvent(e,date));
        }
        return E;
    }
    EventGroup(ArrayList<NewsEvent> list, String keyword){
        this.list = list;
        this.keyword = keyword;
    }
}
