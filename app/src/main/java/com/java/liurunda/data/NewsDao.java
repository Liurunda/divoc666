package com.java.liurunda.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface NewsDao {
    @Insert
    void insertNews(News news);
    @Query("SELECT * FROM News WHERE (new_id BETWEEN :minId AND :maxId) AND (infoType = :t)")
    public News[] loadNewsBetweenIdsWithType(int minId, int maxId, int t);
    //e.g. loadNewsBetweenIdsWithType(metanews.start_id,start_id+10,InfoType.news.ordinal())
}
