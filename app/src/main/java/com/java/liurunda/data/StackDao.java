package com.java.liurunda.data;

import androidx.room.*;

@Dao
public interface StackDao {
    @Update
    public void updateStack(NewsStack ns);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertStack(NewsStack ns);
    @Query("SELECT * FROM NewsStack WHERE TypeNum = :sig")
    public NewsStack[] getStack(String sig);
}
