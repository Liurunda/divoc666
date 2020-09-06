package com.java.liurunda.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import org.jetbrains.annotations.NotNull;

@Entity
@TypeConverters({Converters.class})
public class NewsStack {
    @PrimaryKey @NotNull
    String TypeNum; // "news,1", "news,2", "paper,1"

    String oldest_id;
    String newest_id; //use original _id
}
