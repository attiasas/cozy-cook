package com.cozycook.util;

import com.cozycook.data.IngredientLine;
import com.cozycook.data.entity.GroceryItemEntity;
import com.cozycook.data.entity.RecipeEntity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generates grocery list from recipes (aggregates ingredients). Can be extended for plans.
 */
public final class GroceryListGenerator {

    private static final Gson GSON = new Gson();
    private static final Type INGREDIENT_LIST_TYPE = new TypeToken<List<IngredientLine>>() {}.getType();

    /**
     * From a list of recipes, aggregate ingredients by name (sum amounts) and return grocery items.
     */
    public static List<GroceryItemEntity> fromRecipes(List<RecipeEntity> recipes, String sourcePlanGroup) {
        Map<String, AggregatedIngredient> map = new HashMap<>();
        for (RecipeEntity r : recipes) {
            List<IngredientLine> lines = parseIngredients(r.ingredientsJson);
            for (IngredientLine line : lines) {
                String key = normalizeName(line.name);
                AggregatedIngredient agg = map.get(key);
                if (agg == null) {
                    agg = new AggregatedIngredient(line.name, line.amount, line.unit);
                    map.put(key, agg);
                } else {
                    agg.amount += line.amount;
                    if (line.unit != null && !line.unit.isEmpty()) agg.unit = line.unit;
                }
            }
        }
        List<GroceryItemEntity> out = new ArrayList<>();
        for (AggregatedIngredient a : map.values()) {
            out.add(new GroceryItemEntity(a.name, a.amount, a.unit, false, sourcePlanGroup));
        }
        return out;
    }

    private static List<IngredientLine> parseIngredients(String json) {
        if (json == null || json.isEmpty()) return new ArrayList<>();
        try {
            List<IngredientLine> list = GSON.fromJson(json, INGREDIENT_LIST_TYPE);
            return list != null ? list : new ArrayList<>();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private static String normalizeName(String name) {
        if (name == null) return "";
        return name.trim().toLowerCase();
    }

    private static class AggregatedIngredient {
        String name;
        double amount;
        String unit;

        AggregatedIngredient(String name, double amount, String unit) {
            this.name = name;
            this.amount = amount;
            this.unit = unit != null ? unit : "";
        }
    }
}
