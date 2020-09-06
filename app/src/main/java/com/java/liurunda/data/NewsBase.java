package com.java.liurunda.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

@Database(entities = {News.class, MetaNews.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class NewsBase extends RoomDatabase {
    public abstract NewsDao newsDao();
    public abstract MetaDao metaDao();
}
