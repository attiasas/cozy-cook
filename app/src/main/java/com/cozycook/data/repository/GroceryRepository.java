package com.cozycook.data.repository;

import android.content.Context;

import com.cozycook.CozyCookApp;
import com.cozycook.data.entity.GroceryItemEntity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GroceryRepository {

    private final com.cozycook.data.dao.GroceryItemDao dao;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public GroceryRepository(Context context) {
        dao = CozyCookApp.getDatabase().groceryItemDao();
    }

    public void getAll(KitchenItemRepository.ResultCallback<List<GroceryItemEntity>> callback) {
        executor.execute(() -> callback.onResult(dao.getAll()));
    }

    public void insert(GroceryItemEntity item, KitchenItemRepository.ResultCallback<Long> callback) {
        executor.execute(() -> callback.onResult(dao.insert(item)));
    }

    public void update(GroceryItemEntity item, KitchenItemRepository.ResultCallback<Void> callback) {
        executor.execute(() -> {
            dao.update(item);
            callback.onResult(null);
        });
    }

    public void setChecked(long id, boolean checked, KitchenItemRepository.ResultCallback<Void> callback) {
        executor.execute(() -> {
            dao.setChecked(id, checked);
            callback.onResult(null);
        });
    }

    public void delete(GroceryItemEntity item, KitchenItemRepository.ResultCallback<Void> callback) {
        executor.execute(() -> {
            dao.delete(item);
            callback.onResult(null);
        });
    }
}
