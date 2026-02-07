package com.cozycook.data.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * One slot in a meal plan: which recipe on which day (and meal type).
 */
@Entity(
    tableName = "meal_plans",
    foreignKeys = @ForeignKey(
        entity = RecipeEntity.class,
        parentColumns = "id",
        childColumns = "recipeId",
        onDelete = ForeignKey.CASCADE
    ),
    indices = {@Index("recipeId")}
)
public class MealPlanEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public long recipeId;
    /** Day (start of day in millis). Column name must be exactly dateMillis for Room indices. */
    public long dateMillis;
    public int slot;            // 0=breakfast, 1=lunch, 2=dinner, 3=snack
    public String planGroup;    // e.g. "week_2025-02-03" for weekly batch

    public MealPlanEntity(long recipeId, long dateMillis, int slot, String planGroup) {
        this.recipeId = recipeId;
        this.dateMillis = dateMillis;
        this.slot = slot;
        this.planGroup = planGroup != null ? planGroup : "";
    }
}
