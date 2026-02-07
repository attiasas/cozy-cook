package com.cozycook.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.view.MenuItem;

import com.cozycook.CozyCookApp;
import com.cozycook.R;
import com.cozycook.api.RecipeSourceRegistry;
import com.cozycook.api.spoonacular.SpoonacularRecipeSource;
import com.cozycook.ui.grocery.GroceryFragment;
import com.cozycook.ui.kitchen.KitchenItemsFragment;
import com.cozycook.ui.pantry.PantryFragment;
import com.cozycook.ui.plans.PlansFragment;
import com.cozycook.ui.recipes.RecipesFragment;
import com.cozycook.ui.settings.SettingsActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * Main container: bottom nav and fragment swap.
 * Registers Spoonacular as first recipe source (API key in BuildConfig or preferences).
 */
public class MainActivity extends AppCompatActivity {

    private static final String KEY_API = "spoonacular_api_key";
    private RecipeSourceRegistry recipeSourceRegistry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recipeSourceRegistry = CozyCookApp.getRecipeSourceRegistry();
        String apiKey = getApiKey();
        if (apiKey != null && !apiKey.isEmpty()) {
            recipeSourceRegistry.register(new SpoonacularRecipeSource(apiKey));
        }

        BottomNavigationView nav = findViewById(R.id.bottom_nav);
        nav.setOnNavigationItemSelectedListener(this::onNavSelected);

        if (savedInstanceState == null) {
            showFragment(PantryFragment.newInstance());
        }

        com.google.android.material.appbar.MaterialToolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.action_settings) {
                    startActivity(new Intent(this, SettingsActivity.class));
                    return true;
                }
                return false;
            });
        }
    }

    public RecipeSourceRegistry getRecipeSourceRegistry() {
        return CozyCookApp.getRecipeSourceRegistry();
    }

    private String getApiKey() {
        return getSharedPreferences("cozycook", MODE_PRIVATE).getString(KEY_API, "");
    }

    public void setSpoonacularApiKey(String key) {
        getSharedPreferences("cozycook", MODE_PRIVATE).edit().putString(KEY_API, key).apply();
        if (recipeSourceRegistry.getSource(SpoonacularRecipeSource.SOURCE_ID) == null && key != null && !key.isEmpty()) {
            recipeSourceRegistry.register(new SpoonacularRecipeSource(key));
        }
    }

    private boolean onNavSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_pantry) {
            showFragment(PantryFragment.newInstance());
            return true;
        }
        if (id == R.id.nav_recipes) {
            showFragment(RecipesFragment.newInstance());
            return true;
        }
        if (id == R.id.nav_plans) {
            showFragment(PlansFragment.newInstance());
            return true;
        }
        if (id == R.id.nav_grocery) {
            showFragment(GroceryFragment.newInstance());
            return true;
        }
        if (id == R.id.nav_kitchen) {
            showFragment(KitchenItemsFragment.newInstance());
            return true;
        }
        return false;
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commit();
    }
}
