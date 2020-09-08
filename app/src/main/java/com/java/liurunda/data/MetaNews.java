package com.java.liurunda.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;


@Entity
@TypeConverters({Converters.class})
public class MetaNews {
    @PrimaryKey
    public InfoType meta;
    public String history;
    public MetaNews(InfoType meta, String history){
        this.meta = meta; this.history = history;
    }
}
