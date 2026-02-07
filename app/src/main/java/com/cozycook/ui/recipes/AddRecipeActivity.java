package com.cozycook.ui.recipes;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cozycook.R;
import com.cozycook.data.IngredientLine;
import com.cozycook.data.entity.RecipeEntity;
import com.cozycook.data.repository.KitchenItemRepository;
import com.cozycook.data.repository.RecipeRepository;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Add recipe: local form + optional Spoonacular search to import.
 */
public class AddRecipeActivity extends AppCompatActivity {

    private RecipeRepository repository;
    private TextInputEditText titleEt, servingsEt, minutesEt, ingredientsEt, instructionsEt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        repository = new RecipeRepository(this);

        titleEt = findViewById(R.id.title);
        servingsEt = findViewById(R.id.servings);
        minutesEt = findViewById(R.id.ready_minutes);
        ingredientsEt = findViewById(R.id.ingredients);
        instructionsEt = findViewById(R.id.instructions);

        findViewById(R.id.btn_save).setOnClickListener(v -> saveLocal());
        findViewById(R.id.btn_search_spoonacular).setOnClickListener(v -> {
            startActivityForResult(new Intent(this, SpoonacularSearchActivity.class), 102);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, android.content.Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 102 && resultCode == RESULT_OK && data != null) {
            long recipeId = data.getLongExtra(SpoonacularSearchActivity.EXTRA_RECIPE_ID, -1);
            if (recipeId >= 0) {
                Toast.makeText(this, "Recipe saved from Spoonacular", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            }
        }
    }

    private void saveLocal() {
        String title = titleEt.getText().toString().trim();
        if (title.isEmpty()) {
            Toast.makeText(this, "Enter title", Toast.LENGTH_SHORT).show();
            return;
        }
        int servings = 1;
        try {
            servings = Integer.parseInt(servingsEt.getText().toString());
        } catch (Exception ignored) {}
        int minutes = 0;
        try {
            minutes = Integer.parseInt(minutesEt.getText().toString());
        } catch (Exception ignored) {}
        String ing = ingredientsEt.getText().toString().trim();
        List<IngredientLine> lines = parseIngredients(ing);
        String instructions = instructionsEt.getText().toString().trim();

        RecipeEntity e = new RecipeEntity();
        e.title = title;
        e.servings = servings > 0 ? servings : 1;
        e.readyInMinutes = minutes;
        e.instructions = instructions;
        e.ingredientsJson = new Gson().toJson(lines);
        e.source = "local";

        repository.insert(e, new KitchenItemRepository.ResultCallback<Long>() {
            @Override
            public void onResult(Long id) {
                Toast.makeText(AddRecipeActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    private List<IngredientLine> parseIngredients(String text) {
        List<IngredientLine> out = new ArrayList<>();
        if (text == null) return out;
        for (String line : text.split("\n")) {
            line = line.trim();
            if (line.isEmpty()) continue;
            double amount = 1;
            String unit = "";
            String name = line;
            String[] parts = line.split("\\s+", 3);
            if (parts.length >= 1) {
                try {
                    amount = Double.parseDouble(parts[0].replace(",", "."));
                    if (parts.length >= 2) {
                        if (parts[1].matches("[a-zA-Z]+")) {
                            unit = parts[1];
                            name = parts.length >= 3 ? parts[2] : "";
                        } else {
                            name = parts.length >= 2 ? parts[1] : "";
                        }
                        if (name.isEmpty() && parts.length >= 3) name = parts[2];
                    } else name = "";
                } catch (Exception ignored) {
                    name = line;
                }
            }
            if (name.isEmpty()) name = line;
            out.add(new IngredientLine(name, amount, unit));
        }
        return out;
    }
}
