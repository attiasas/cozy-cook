package com.cozycook.data.entity;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * General kitchen item (food, appliance, etc.) used for linking in pantry and recipes.
 */
@Entity(tableName = "kitchen_items", indices = {@Index("name")})
public class KitchenItemEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public String name;
    public String category; // e.g. "food", "appliance", "spice"
    public String unit;     // optional default unit e.g. "g", "piece"

    public KitchenItemEntity(String name, String category, String unit) {
        this.name = name;
        this.category = category != null ? category : "food";
        this.unit = unit;
    }
}
