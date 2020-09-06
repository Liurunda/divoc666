package com.java.liurunda.data;

import android.icu.text.IDNA;
import androidx.room.TypeConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Converters {
    @TypeConverter
    public int fromInfoType(InfoType t){
        return t.ordinal();
    }
    @TypeConverter
    public InfoType toInfoType(int t){
        return InfoType.values()[t];
    }
    @TypeConverter
    public ArrayList<String>  toKeywords(String s){
        return new ArrayList<>(Arrays.asList(s.split(",")));
    }
    @TypeConverter
    public String fromKeywords(ArrayList<String> list){
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for (String s: list) {
            if(first){
                first = false;
                builder.append(s);
            }else{
                builder.append(",");
                builder.append(s);
            }
        }
        return builder.toString();
    }
}
