package com.cozycook.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.cozycook.data.entity.KitchenItemEntity;

import java.util.List;

@Dao
public interface KitchenItemDao {

    @Insert
    long insert(KitchenItemEntity item);

    @Update
    void update(KitchenItemEntity item);

    @Delete
    void delete(KitchenItemEntity item);

    @Query("SELECT * FROM kitchen_items ORDER BY name")
    List<KitchenItemEntity> getAll();

    @Query("SELECT * FROM kitchen_items WHERE category = :category ORDER BY name")
    List<KitchenItemEntity> getByCategory(String category);

    @Query("SELECT * FROM kitchen_items WHERE id = :id")
    KitchenItemEntity getById(long id);

    @Query("SELECT * FROM kitchen_items WHERE name LIKE '%' || :query || '%' ORDER BY name")
    List<KitchenItemEntity> search(String query);
}
