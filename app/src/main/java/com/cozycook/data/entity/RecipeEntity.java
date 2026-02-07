package com.cozycook.data.entity;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Recipe (user-created or imported). Source identifies origin for flexibility.
 */
@Entity(tableName = "recipes", indices = {@Index("title"), @Index("source")})
public class RecipeEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public String title;
    public String source;       // "local", "spoonacular", etc.
    public String externalId;   // id from external API when source != local
    public String imageUrl;
    public int servings;
    public int readyInMinutes;
    public String instructions; // plain text or JSON for steps
    public String ingredientsJson; // JSON array of {name, amount, unit}
    public String summary;

    public RecipeEntity() {
        this.source = "local";
        this.servings = 1;
        this.readyInMinutes = 0;
    }
}
