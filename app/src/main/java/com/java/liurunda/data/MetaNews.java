package com.java.liurunda.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class MetaNews {
    @PrimaryKey
    public InfoType meta;

    public int start_id;//current id of latest news
    public int count;
}
