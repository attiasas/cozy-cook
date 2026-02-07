package com.cozycook.data.repository;

import android.content.Context;

import com.cozycook.CozyCookApp;
import com.cozycook.data.dao.RecipeDao;
import com.cozycook.data.entity.MealPlanEntity;
import com.cozycook.data.entity.RecipeEntity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MealPlanRepository {

    private final com.cozycook.data.dao.MealPlanDao planDao;
    private final RecipeDao recipeDao;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public MealPlanRepository(Context context) {
        planDao = CozyCookApp.getDatabase().mealPlanDao();
        recipeDao = CozyCookApp.getDatabase().recipeDao();
    }

    public void getByDay(long dayStartMillis, KitchenItemRepository.ResultCallback<List<MealPlanEntity>> callback) {
        executor.execute(() -> callback.onResult(planDao.getByDay(dayStartMillis)));
    }

    public void getInRange(long fromMillis, long toMillis, KitchenItemRepository.ResultCallback<List<MealPlanEntity>> callback) {
        executor.execute(() -> callback.onResult(planDao.getInRange(fromMillis, toMillis)));
    }

    public void getRecipesForDay(long dayStartMillis, KitchenItemRepository.ResultCallback<List<RecipeEntity>> callback) {
        executor.execute(() -> {
            List<MealPlanEntity> plans = planDao.getByDay(dayStartMillis);
            List<RecipeEntity> recipes = new ArrayList<>();
            for (MealPlanEntity p : plans) {
                RecipeEntity r = recipeDao.getById(p.recipeId);
                if (r != null) recipes.add(r);
            }
            callback.onResult(recipes);
        });
    }

    public void insertPlans(List<MealPlanEntity> plans, KitchenItemRepository.ResultCallback<Void> callback) {
        executor.execute(() -> {
            planDao.insertAll(plans);
            callback.onResult(null);
        });
    }

    public void deleteInRange(long fromMillis, long toMillis, KitchenItemRepository.ResultCallback<Void> callback) {
        executor.execute(() -> {
            planDao.deleteInRange(fromMillis, toMillis);
            callback.onResult(null);
        });
    }

    /** Returns start of day in millis for a given day (local time). */
    public static long getDayStartMillis(int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTimeInMillis();
    }
}
