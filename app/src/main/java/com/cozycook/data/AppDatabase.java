package com.cozycook.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.cozycook.data.dao.GroceryItemDao;
import com.cozycook.data.dao.KitchenItemDao;
import com.cozycook.data.dao.MealPlanDao;
import com.cozycook.data.dao.PantryItemDao;
import com.cozycook.data.dao.RecipeDao;
import com.cozycook.data.entity.GroceryItemEntity;
import com.cozycook.data.entity.KitchenItemEntity;
import com.cozycook.data.entity.MealPlanEntity;
import com.cozycook.data.entity.PantryItemEntity;
import com.cozycook.data.entity.RecipeEntity;

@Database(
    entities = {
        KitchenItemEntity.class,
        PantryItemEntity.class,
        RecipeEntity.class,
        MealPlanEntity.class,
        GroceryItemEntity.class
    },
    version = 1,
    exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract KitchenItemDao kitchenItemDao();
    public abstract PantryItemDao pantryItemDao();
    public abstract RecipeDao recipeDao();
    public abstract MealPlanDao mealPlanDao();
    public abstract GroceryItemDao groceryItemDao();

    public static AppDatabase get(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.getApplicationContext(),
                        AppDatabase.class,
                        "cozycook_db"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}
