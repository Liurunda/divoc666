package com.java.liurunda.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface MetaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertMeta(MetaNews meta);//only first, once
    @Query("DELETE FROM MetaNews")
    public void clearMeta();

    @Query("SELECT * FROM MetaNews WHERE meta = :metaid LIMIT 1")
    public MetaNews[] queryMeta(InfoType metaid);
}
