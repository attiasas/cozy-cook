package com.cozycook.api.spoonacular;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Retrofit interface for Spoonacular API.
 * Docs: https://spoonacular.com/food-api/docs
 */
public interface SpoonacularApi {

    @GET("recipes/complexSearch")
    Call<SpoonacularModels.SearchResponse> complexSearch(
        @Query("query") String query,
        @Query("number") int number,
        @Query("apiKey") String apiKey
    );

    @GET("recipes/{id}/information")
    Call<SpoonacularModels.RecipeInfo> getRecipeInformation(
        @Path("id") long id,
        @Query("apiKey") String apiKey
    );
}
