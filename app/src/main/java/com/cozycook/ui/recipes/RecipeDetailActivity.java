package com.cozycook.ui.recipes;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.cozycook.R;
import com.cozycook.data.IngredientLine;
import com.cozycook.data.entity.RecipeEntity;
import com.cozycook.data.repository.KitchenItemRepository;
import com.cozycook.data.repository.RecipeRepository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity {

    private RecipeRepository repository;
    private long recipeId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        recipeId = getIntent().getLongExtra("recipe_id", -1);
        if (recipeId < 0) {
            finish();
            return;
        }
        repository = new RecipeRepository(this);
        repository.getById(recipeId, new KitchenItemRepository.ResultCallback<RecipeEntity>() {
            @Override
            public void onResult(RecipeEntity r) {
                if (r == null) return;
                runOnUiThread(() -> bind(r));
            }
        });
    }

    private void bind(RecipeEntity r) {
        setTitle(r.title);
        ImageView image = findViewById(R.id.image);
        if (r.imageUrl != null && !r.imageUrl.isEmpty()) {
            Glide.with(this).load(r.imageUrl).into(image);
        }
        ((TextView) findViewById(R.id.title)).setText(r.title);
        String meta = "";
        if (r.readyInMinutes > 0) meta += r.readyInMinutes + " min";
        if (r.servings > 0) meta += (meta.isEmpty() ? "" : " • ") + r.servings + " servings";
        ((TextView) findViewById(R.id.meta)).setText(meta);

        List<IngredientLine> ing = parseIngredients(r.ingredientsJson);
        StringBuilder sb = new StringBuilder();
        for (IngredientLine line : ing) {
            sb.append("• ").append(line.amount).append(" ").append(line.unit).append(" ").append(line.name).append("\n");
        }
        ((TextView) findViewById(R.id.ingredients)).setText(sb.toString());
        ((TextView) findViewById(R.id.instructions)).setText(r.instructions != null ? r.instructions : "");
    }

    private List<IngredientLine> parseIngredients(String json) {
        if (json == null || json.isEmpty()) return Collections.emptyList();
        try {
            Type type = new TypeToken<List<IngredientLine>>() {}.getType();
            return new Gson().fromJson(json, type);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
