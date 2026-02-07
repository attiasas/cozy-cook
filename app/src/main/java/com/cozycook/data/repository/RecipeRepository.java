package com.cozycook.data.repository;

import android.content.Context;

import com.cozycook.CozyCookApp;
import com.cozycook.data.entity.RecipeEntity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RecipeRepository {

    private final com.cozycook.data.dao.RecipeDao dao;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public RecipeRepository(Context context) {
        dao = CozyCookApp.getDatabase().recipeDao();
    }

    public void getAll(KitchenItemRepository.ResultCallback<List<RecipeEntity>> callback) {
        executor.execute(() -> callback.onResult(dao.getAll()));
    }

    public void getById(long id, KitchenItemRepository.ResultCallback<RecipeEntity> callback) {
        executor.execute(() -> callback.onResult(dao.getById(id)));
    }

    public void searchByTitle(String query, KitchenItemRepository.ResultCallback<List<RecipeEntity>> callback) {
        executor.execute(() -> callback.onResult(dao.searchByTitle(query)));
    }

    public void getBySourceAndExternalId(String source, String externalId, KitchenItemRepository.ResultCallback<RecipeEntity> callback) {
        executor.execute(() -> callback.onResult(dao.getBySourceAndExternalId(source, externalId)));
    }

    public void insert(RecipeEntity recipe, KitchenItemRepository.ResultCallback<Long> callback) {
        executor.execute(() -> callback.onResult(dao.insert(recipe)));
    }

    public void update(RecipeEntity recipe, KitchenItemRepository.ResultCallback<Void> callback) {
        executor.execute(() -> {
            dao.update(recipe);
            callback.onResult(null);
        });
    }

    public void delete(RecipeEntity recipe, KitchenItemRepository.ResultCallback<Void> callback) {
        executor.execute(() -> {
            dao.delete(recipe);
            callback.onResult(null);
        });
    }
}
