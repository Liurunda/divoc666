package com.java.liurunda.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;


@Entity
@TypeConverters({Converters.class})
public class MetaNews {
    @PrimaryKey
    public InfoType meta;
    public int stack_count;//current id of latest news
    public MetaNews(InfoType meta, int stack_count){
        this.meta = meta;
        this.stack_count = stack_count;
    }
}
