package com.cozycook.api;

import java.util.List;

/**
 * Result of fetching recipes from an external source (e.g. Spoonacular).
 * Kept generic so different APIs can return different shapes; mapper converts to RecipeEntity.
 */
public class RecipeSourceResult {

    public final String sourceId;
    public final List<RecipeDto> recipes;
    public final String errorMessage;

    public RecipeSourceResult(String sourceId, List<RecipeDto> recipes, String errorMessage) {
        this.sourceId = sourceId;
        this.recipes = recipes != null ? recipes : java.util.Collections.emptyList();
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return errorMessage == null;
    }

    /**
     * Common DTO for a recipe from any external source.
     */
    public static class RecipeDto {
        public String externalId;
        public String title;
        public String imageUrl;
        public int servings;
        public int readyInMinutes;
        public String summary;
        public String instructions;
        public List<IngredientDto> ingredients;

        public RecipeDto() {}

        public RecipeDto(String externalId, String title, String imageUrl, int servings,
                        int readyInMinutes, String summary, String instructions, List<IngredientDto> ingredients) {
            this.externalId = externalId;
            this.title = title;
            this.imageUrl = imageUrl;
            this.servings = servings;
            this.readyInMinutes = readyInMinutes;
            this.summary = summary;
            this.instructions = instructions;
            this.ingredients = ingredients != null ? ingredients : java.util.Collections.emptyList();
        }
    }

    public static class IngredientDto {
        public String name;
        public double amount;
        public String unit;

        public IngredientDto(String name, double amount, String unit) {
            this.name = name;
            this.amount = amount;
            this.unit = unit != null ? unit : "";
        }
    }
}
