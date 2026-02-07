package com.cozycook.ui.plans;

import com.cozycook.data.entity.RecipeEntity;

import java.util.List;

public class PlanDayItem {
    public final long dayMillis;
    public final List<RecipeEntity> recipes;

    public PlanDayItem(long dayMillis, List<RecipeEntity> recipes) {
        this.dayMillis = dayMillis;
        this.recipes = recipes != null ? recipes : new java.util.ArrayList<>();
    }
}
