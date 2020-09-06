package com.java.liurunda.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

@Database(entities = {News.class, MetaNews.class, NewsStack.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class NewsBase extends RoomDatabase {
    public abstract NewsDao newsDao();
    public abstract MetaDao metaDao();
    public abstract StackDao stackDao();
}
