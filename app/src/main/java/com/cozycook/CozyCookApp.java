package com.cozycook;

import android.app.Application;

import com.cozycook.api.RecipeSourceRegistry;
import com.cozycook.data.AppDatabase;

/**
 * Application entry point. Holds app-wide database and recipe source registry.
 */
public class CozyCookApp extends Application {

    private static AppDatabase database;
    private static RecipeSourceRegistry recipeSourceRegistry;

    @Override
    public void onCreate() {
        super.onCreate();
        database = AppDatabase.get(this);
        recipeSourceRegistry = new RecipeSourceRegistry();
    }

    public static AppDatabase getDatabase() {
        return database;
    }

    public static RecipeSourceRegistry getRecipeSourceRegistry() {
        return recipeSourceRegistry;
    }
}
