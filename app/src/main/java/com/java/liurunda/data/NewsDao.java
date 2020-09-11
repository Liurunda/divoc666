package com.java.liurunda.data;

import androidx.room.*;

@Dao
public interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertNews(News news);
    @Update
    public void updateNews(News news);
    @Query("DELETE FROM News")
    public void clearNews();
    @Query("SELECT * FROM News WHERE id LIKE :id LIMIT 1")
    public News[] loadNewsId(String id);

    @Query("SELECT * FROM News WHERE (id = :id AND infoType = :t)")
    public News[] loadNewsIdAndType(String id, int t);
    //e.g. loadNewsBetweenIdsWithType(metanews.start_id,start_id+10,InfoType.news.ordinal())
    @Query("SELECT * FROM News WHERE(title LIKE :keyword) LIMIT 30")
    public News[] searchNewsTitleLikeKeywords(String keyword);

    @Query("SELECT * FROM News WHERE(keywords LIKE :key)  LIMIT 30")
    public News[] searchNewsEntityLikeKeywords(String key);
    @Query("SELECT * FROM News WHERE(content LIKE :key)  LIMIT 30")
    public News[] searchNewsContentLikeKeywords(String key);
}
