package com.cozycook.data.repository;

import android.content.Context;

import com.cozycook.CozyCookApp;
import com.cozycook.data.entity.KitchenItemEntity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class KitchenItemRepository {

    private final com.cozycook.data.dao.KitchenItemDao dao;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public KitchenItemRepository(Context context) {
        dao = CozyCookApp.getDatabase().kitchenItemDao();
    }

    public void getAll(ResultCallback<List<KitchenItemEntity>> callback) {
        executor.execute(() -> callback.onResult(dao.getAll()));
    }

    public void search(String query, ResultCallback<List<KitchenItemEntity>> callback) {
        executor.execute(() -> callback.onResult(dao.search(query)));
    }

    public void insert(KitchenItemEntity item, ResultCallback<Long> callback) {
        executor.execute(() -> callback.onResult(dao.insert(item)));
    }

    public void update(KitchenItemEntity item, ResultCallback<Void> callback) {
        executor.execute(() -> {
            dao.update(item);
            callback.onResult(null);
        });
    }

    public void delete(KitchenItemEntity item, ResultCallback<Void> callback) {
        executor.execute(() -> {
            dao.delete(item);
            callback.onResult(null);
        });
    }

    public interface ResultCallback<T> {
        void onResult(T result);
    }
}
