package com.cozycook.util;

import com.cozycook.data.entity.MealPlanEntity;
import com.cozycook.data.entity.RecipeEntity;
import com.cozycook.data.repository.RecipeRepository;
import com.cozycook.ui.plans.PlanDayItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Groups meal plan entities by day and loads recipe titles.
 */
public final class MealPlanRepositoryHelper {

    public static List<PlanDayItem> groupByDay(List<MealPlanEntity> plans, RecipeRepository recipeRepo) {
        Map<Long, List<MealPlanEntity>> byDay = new HashMap<>();
        for (MealPlanEntity p : plans) {
            byDay.computeIfAbsent(p.dateMillis, k -> new ArrayList<>()).add(p);
        }
        List<PlanDayItem> result = new ArrayList<>();
        for (Map.Entry<Long, List<MealPlanEntity>> e : byDay.entrySet()) {
            List<RecipeEntity> recipes = new ArrayList<>();
            for (MealPlanEntity p : e.getValue()) {
                RecipeEntity r = getRecipeSync(recipeRepo, p.recipeId);
                if (r != null) recipes.add(r);
            }
            result.add(new PlanDayItem(e.getKey(), recipes));
        }
        result.sort((a, b) -> Long.compare(a.dayMillis, b.dayMillis));
        return result;
    }

    private static RecipeEntity getRecipeSync(RecipeRepository repo, long recipeId) {
        final RecipeEntity[] out = { null };
        Object lock = new Object();
        repo.getById(recipeId, result -> {
            synchronized (lock) {
                out[0] = result;
                lock.notify();
            }
        });
        synchronized (lock) {
            try {
                lock.wait(2000);
            } catch (InterruptedException ignored) {}
        }
        return out[0];
    }

    public static String formatDay(long dayMillis) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(dayMillis);
        return String.format("%1$tA %1$tb %1$td", c);
    }
}
