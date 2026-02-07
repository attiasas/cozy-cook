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
    indices = {@Index("recipeId"), @Index("dateMillis"), @Index("dateMillis, slot")}
)
public class MealPlanEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public long recipeId;
    public long dateMillis;     // day (start of day UTC or local)
    public int slot;           // 0=breakfast, 1=lunch, 2=dinner, 3=snack
    public String planGroup;    // e.g. "week_2025-02-03" for weekly batch

    public MealPlanEntity(long recipeId, long dateMillis, int slot, String planGroup) {
        this.recipeId = recipeId;
        this.dateMillis = dateMillis;
        this.slot = slot;
        this.planGroup = planGroup != null ? planGroup : "";
    }
}
