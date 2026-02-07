package com.cozycook.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.cozycook.data.entity.RecipeEntity;

import java.util.List;

@Dao
public interface RecipeDao {

    @Insert
    long insert(RecipeEntity recipe);

    @Update
    void update(RecipeEntity recipe);

    @Delete
    void delete(RecipeEntity recipe);

    @Query("SELECT * FROM recipes ORDER BY title")
    List<RecipeEntity> getAll();

    @Query("SELECT * FROM recipes WHERE id = :id")
    RecipeEntity getById(long id);

    @Query("SELECT * FROM recipes WHERE source = :source AND externalId = :externalId LIMIT 1")
    RecipeEntity getBySourceAndExternalId(String source, String externalId);

    @Query("SELECT * FROM recipes WHERE title LIKE '%' || :query || '%' ORDER BY title")
    List<RecipeEntity> searchByTitle(String query);

    @Query("SELECT * FROM recipes WHERE source = :source ORDER BY title")
    List<RecipeEntity> getBySource(String source);
}
