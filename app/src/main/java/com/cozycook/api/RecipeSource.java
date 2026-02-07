package com.cozycook.api;

/**
 * Abstraction for recipe sources (Spoonacular, future APIs).
 * Implementations fetch from their schema and return normalized RecipeSourceResult.
 */
public interface RecipeSource {

    String getSourceId();

    /**
     * Search recipes by query. Call on background thread or use callback.
     */
    void searchRecipes(String query, int maxResults, RecipeSourceCallback callback);

    /**
     * Fetch full recipe by external id (e.g. for "add to my recipes").
     */
    void getRecipeById(String externalId, RecipeSourceCallback callback);

    interface RecipeSourceCallback {
        void onResult(RecipeSourceResult result);
        void onError(Throwable t);
    }
}
