package com.cozycook.util;

import com.cozycook.data.entity.MealPlanEntity;
import com.cozycook.data.entity.RecipeEntity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 * Generates meal plans (daily / weekly) from recipes with pluggable strategy.
 */
public final class PlanGenerator {

    public static final int SLOT_BREAKFAST = 0;
    public static final int SLOT_LUNCH = 1;
    public static final int SLOT_DINNER = 2;
    public static final int SLOT_SNACK = 3;

    private final Random random = new Random();

    /**
     * Strategy: random selection from list. Easy to add more (e.g. by difficulty, by pantry match).
     */
    public List<MealPlanEntity> generateForDays(
        List<RecipeEntity> recipes,
        long startDayMillis,
        int numberOfDays,
        int slotsPerDay,
        String planGroup
    ) {
        if (recipes == null || recipes.isEmpty()) return new ArrayList<>();
        List<MealPlanEntity> plans = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(startDayMillis);
        for (int d = 0; d < numberOfDays; d++) {
            long dayMillis = cal.getTimeInMillis();
            for (int slot = 0; slot < slotsPerDay; slot++) {
                RecipeEntity picked = recipes.get(random.nextInt(recipes.size()));
                plans.add(new MealPlanEntity(picked.id, dayMillis, slot, planGroup));
            }
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
        return plans;
    }

    public static long getStartOfTodayMillis() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTimeInMillis();
    }
}
