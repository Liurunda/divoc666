package com.java.liurunda.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;


@Entity
@TypeConverters({Converters.class})
public class MetaNews {
    @PrimaryKey
    public InfoType meta;

    public int start_id;//current id of latest news
    public int count;
    public MetaNews(){

    }
    public MetaNews(InfoType meta,int start, int count){
        this.meta = meta;start_id = start; this.count = count;
    }
}
