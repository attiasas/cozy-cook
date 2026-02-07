package com.cozycook.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.cozycook.data.entity.MealPlanEntity;

import java.util.List;

@Dao
public interface MealPlanDao {

    @Insert
    void insert(MealPlanEntity plan);

    @Insert
    void insertAll(List<MealPlanEntity> plans);

    @Delete
    void delete(MealPlanEntity plan);

    @Query("DELETE FROM meal_plans WHERE dateMillis >= :fromMillis AND dateMillis < :toMillis")
    void deleteInRange(long fromMillis, long toMillis);

    @Query("SELECT * FROM meal_plans WHERE dateMillis = :dayStartMillis ORDER BY slot")
    List<MealPlanEntity> getByDay(long dayStartMillis);

    @Query("SELECT * FROM meal_plans WHERE dateMillis >= :fromMillis AND dateMillis < :toMillis ORDER BY dateMillis, slot")
    List<MealPlanEntity> getInRange(long fromMillis, long toMillis);

    @Query("SELECT * FROM meal_plans WHERE planGroup = :group ORDER BY dateMillis, slot")
    List<MealPlanEntity> getByPlanGroup(String group);
}
