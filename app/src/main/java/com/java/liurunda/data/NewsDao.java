package com.java.liurunda.data;

import android.icu.text.Replaceable;
import androidx.room.*;

@Dao
public interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertNews(News news);
    @Update
    public void updateNews(News news);
    @Query("DELETE FROM News")
    public void clearNews();
    @Query("SELECT * FROM News WHERE (id = :id)")
    public News[] loadNewsId(String id);

    @Query("SELECT * FROM News WHERE (id = :id AND infoType = :t)")
    public News[] loadNewsIdAndType(String id, int t);
    //e.g. loadNewsBetweenIdsWithType(metanews.start_id,start_id+10,InfoType.news.ordinal())
    @Query("SELECT * FROM News WHERE(title LIKE :keyword)")
    public News[] searchNewsTitleLikeKeywords(String keyword);

    @Query("SELECT * FROM News WHERE(keywords LIKE :key)")
    public News[] searchNewsEntityLikeKeywords(String key);
}
