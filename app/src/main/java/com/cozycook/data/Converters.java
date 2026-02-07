package com.cozycook.data;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

/**
 * Room type converters for JSON columns.
 */
public final class Converters {

    private static final Gson GSON = new Gson();
    private static final Type INGREDIENT_LIST = new TypeToken<List<IngredientLine>>() {}.getType();

    @TypeConverter
    public static List<IngredientLine> fromIngredientJson(String value) {
        if (value == null || value.isEmpty()) return Collections.emptyList();
        return GSON.fromJson(value, INGREDIENT_LIST);
    }

    @TypeConverter
    public static String toIngredientJson(List<IngredientLine> list) {
        if (list == null) return "[]";
        return GSON.toJson(list);
    }
}
