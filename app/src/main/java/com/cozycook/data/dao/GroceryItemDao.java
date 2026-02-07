package com.cozycook.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.cozycook.data.entity.GroceryItemEntity;

import java.util.List;

@Dao
public interface GroceryItemDao {

    @Insert
    long insert(GroceryItemEntity item);

    @Update
    void update(GroceryItemEntity item);

    @Delete
    void delete(GroceryItemEntity item);

    @Query("SELECT * FROM grocery_items ORDER BY checked, name")
    List<GroceryItemEntity> getAll();

    @Query("SELECT * FROM grocery_items WHERE id = :id")
    GroceryItemEntity getById(long id);

    @Query("UPDATE grocery_items SET checked = :checked WHERE id = :id")
    void setChecked(long id, boolean checked);
}
