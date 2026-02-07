package com.cozycook.api.spoonacular;

import com.cozycook.api.RecipeSource;
import com.cozycook.api.RecipeSourceResult;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Recipe source implementation for Spoonacular API.
 * Flexible: add more endpoints (e.g. findByIngredients) by extending this or adding another RecipeSource.
 */
public class SpoonacularRecipeSource implements RecipeSource {

    private static final String BASE_URL = "https://api.spoonacular.com/";
    public static final String SOURCE_ID = "spoonacular";

    private final SpoonacularApi api;
    private final String apiKey;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public SpoonacularRecipeSource(String apiKey) {
        this.apiKey = apiKey;
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
        this.api = retrofit.create(SpoonacularApi.class);
    }

    @Override
    public String getSourceId() {
        return SOURCE_ID;
    }

    @Override
    public void searchRecipes(String query, int maxResults, RecipeSourceCallback callback) {
        api.complexSearch(query, Math.min(maxResults, 20), apiKey)
            .enqueue(new Callback<SpoonacularModels.SearchResponse>() {
                @Override
                public void onResponse(Call<SpoonacularModels.SearchResponse> call,
                                     Response<SpoonacularModels.SearchResponse> response) {
                    if (!response.isSuccessful() || response.body() == null) {
                        callback.onResult(new RecipeSourceResult(SOURCE_ID, null,
                            "Search failed: " + (response.code() + " " + response.message())));
                        return;
                    }
                    List<SpoonacularModels.SearchResult> results = response.body().results;
                    if (results == null) results = new ArrayList<>();
                    List<RecipeSourceResult.RecipeDto> dtos = new ArrayList<>();
                    for (SpoonacularModels.SearchResult r : results) {
                        dtos.add(new RecipeSourceResult.RecipeDto(
                            String.valueOf(r.id), r.title, r.image, 0, 0, null, null, null));
                    }
                    callback.onResult(new RecipeSourceResult(SOURCE_ID, dtos, null));
                }

                @Override
                public void onFailure(Call<SpoonacularModels.SearchResponse> call, Throwable t) {
                    callback.onError(t);
                }
            });
    }

    @Override
    public void getRecipeById(String externalId, RecipeSourceCallback callback) {
        long id;
        try {
            id = Long.parseLong(externalId);
        } catch (NumberFormatException e) {
            callback.onResult(new RecipeSourceResult(SOURCE_ID, null, "Invalid recipe id"));
            return;
        }
        api.getRecipeInformation(id, apiKey).enqueue(new Callback<SpoonacularModels.RecipeInfo>() {
            @Override
            public void onResponse(Call<SpoonacularModels.RecipeInfo> call,
                                 Response<SpoonacularModels.RecipeInfo> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    callback.onResult(new RecipeSourceResult(SOURCE_ID, null,
                        "Failed to load recipe: " + response.code()));
                    return;
                }
                SpoonacularModels.RecipeInfo info = response.body();
                List<RecipeSourceResult.IngredientDto> ingredients = new ArrayList<>();
                if (info.extendedIngredients != null) {
                    for (SpoonacularModels.ExtendedIngredient e : info.extendedIngredients) {
                        ingredients.add(new RecipeSourceResult.IngredientDto(
                            e.name != null ? e.name : e.original,
                            e.amount,
                            e.unit != null ? e.unit : ""
                        ));
                    }
                }
                String instructions = info.instructions != null ? info.instructions : "";
                RecipeSourceResult.RecipeDto dto = new RecipeSourceResult.RecipeDto(
                    String.valueOf(info.id), info.title, info.image,
                    info.servings, info.readyInMinutes, info.summary, instructions, ingredients);
                callback.onResult(new RecipeSourceResult(SOURCE_ID, java.util.Collections.singletonList(dto), null));
            }

            @Override
            public void onFailure(Call<SpoonacularModels.RecipeInfo> call, Throwable t) {
                callback.onError(t);
            }
        });
    }
}
