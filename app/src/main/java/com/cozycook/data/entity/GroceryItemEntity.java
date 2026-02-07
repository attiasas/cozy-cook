package com.cozycook.data.entity;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Grocery list item (checklist to buy). Can be linked to plan/recipe for origin.
 */
@Entity(tableName = "grocery_items", indices = {@Index("checked")})
public class GroceryItemEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public String name;
    public double amount;
    public String unit;
    public boolean checked;
    public String sourcePlanGroup; // optional: which plan generated this

    public GroceryItemEntity(String name, double amount, String unit, boolean checked, String sourcePlanGroup) {
        this.name = name;
        this.amount = amount;
        this.unit = unit != null ? unit : "";
        this.checked = checked;
        this.sourcePlanGroup = sourcePlanGroup != null ? sourcePlanGroup : "";
    }
}
