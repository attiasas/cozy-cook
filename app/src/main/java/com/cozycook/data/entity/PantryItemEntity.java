package com.cozycook.data.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Current pantry: what the user has (linked to KitchenItem when applicable).
 */
@Entity(
    tableName = "pantry_items",
    foreignKeys = @ForeignKey(
        entity = KitchenItemEntity.class,
        parentColumns = "id",
        childColumns = "kitchenItemId",
        onDelete = ForeignKey.SET_NULL
    ),
    indices = {@Index("kitchenItemId")}
)
public class PantryItemEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public Long kitchenItemId;  // null if custom name only
    public String name;         // display name (from kitchen item or custom)
    public double quantity;
    public String unit;
    public Long expiryDateMillis; // optional, for future "throw if expired" tasks

    public PantryItemEntity(Long kitchenItemId, String name, double quantity, String unit, Long expiryDateMillis) {
        this.kitchenItemId = kitchenItemId;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit != null ? unit : "";
        this.expiryDateMillis = expiryDateMillis;
    }
}
