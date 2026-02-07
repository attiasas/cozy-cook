package com.cozycook.data;

/**
 * One ingredient line in a recipe (for JSON serialization).
 */
public class IngredientLine {
    public String name;
    public double amount;
    public String unit;

    public IngredientLine() {}

    public IngredientLine(String name, double amount, String unit) {
        this.name = name;
        this.amount = amount;
        this.unit = unit != null ? unit : "";
    }
}
