package com.cozycook.data.repository;

import android.content.Context;

import com.cozycook.CozyCookApp;
import com.cozycook.data.entity.PantryItemEntity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PantryRepository {

    private final com.cozycook.data.dao.PantryItemDao dao;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public PantryRepository(Context context) {
        dao = CozyCookApp.getDatabase().pantryItemDao();
    }

    public void getAll(KitchenItemRepository.ResultCallback<List<PantryItemEntity>> callback) {
        executor.execute(() -> callback.onResult(dao.getAll()));
    }

    public void search(String query, KitchenItemRepository.ResultCallback<List<PantryItemEntity>> callback) {
        executor.execute(() -> callback.onResult(dao.search(query)));
    }

    public void insert(PantryItemEntity item, KitchenItemRepository.ResultCallback<Long> callback) {
        executor.execute(() -> callback.onResult(dao.insert(item)));
    }

    public void update(PantryItemEntity item, KitchenItemRepository.ResultCallback<Void> callback) {
        executor.execute(() -> {
            dao.update(item);
            callback.onResult(null);
        });
    }

    public void delete(PantryItemEntity item, KitchenItemRepository.ResultCallback<Void> callback) {
        executor.execute(() -> {
            dao.delete(item);
            callback.onResult(null);
        });
    }
}
