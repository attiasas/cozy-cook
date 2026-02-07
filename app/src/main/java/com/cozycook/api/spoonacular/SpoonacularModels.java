package com.cozycook.api.spoonacular;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Response models for Spoonacular API (only fields we use).
 */
public final class SpoonacularModels {

    public static class SearchResponse {
        @SerializedName("results")
        public List<SearchResult> results;
    }

    public static class SearchResult {
        @SerializedName("id")
        public long id;
        @SerializedName("title")
        public String title;
        @SerializedName("image")
        public String image;
    }

    public static class RecipeInfo {
        @SerializedName("id")
        public long id;
        @SerializedName("title")
        public String title;
        @SerializedName("image")
        public String image;
        @SerializedName("servings")
        public int servings;
        @SerializedName("readyInMinutes")
        public int readyInMinutes;
        @SerializedName("summary")
        public String summary;
        @SerializedName("instructions")
        public String instructions;
        @SerializedName("extendedIngredients")
        public List<ExtendedIngredient> extendedIngredients;
    }

    public static class ExtendedIngredient {
        @SerializedName("original")
        public String original;
        @SerializedName("name")
        public String name;
        @SerializedName("amount")
        public double amount;
        @SerializedName("unit")
        public String unit;
    }
}
