package com.cozycook.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.cozycook.data.entity.PantryItemEntity;

import java.util.List;

@Dao
public interface PantryItemDao {

    @Insert
    long insert(PantryItemEntity item);

    @Update
    void update(PantryItemEntity item);

    @Delete
    void delete(PantryItemEntity item);

    @Query("SELECT * FROM pantry_items ORDER BY name")
    List<PantryItemEntity> getAll();

    @Query("SELECT * FROM pantry_items WHERE id = :id")
    PantryItemEntity getById(long id);

    @Query("SELECT * FROM pantry_items WHERE name LIKE '%' || :query || '%' ORDER BY name")
    List<PantryItemEntity> search(String query);
}
