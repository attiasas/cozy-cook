package com.cozycook.ui.recipes;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cozycook.R;
import com.cozycook.api.RecipeSource;
import com.cozycook.api.RecipeSourceResult;
import com.cozycook.api.RecipeMapper;
import com.cozycook.data.entity.RecipeEntity;
import com.cozycook.data.repository.KitchenItemRepository;
import com.cozycook.data.repository.RecipeRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Search Spoonacular and save selected recipe to local DB.
 */
public class SpoonacularSearchActivity extends AppCompatActivity {

    public static final int REQUEST_PICK = 200;
    public static final String EXTRA_RECIPE_ID = "recipe_id";

    private RecipeSource source;
    private RecipeRepository repository;
    private List<RecipeSourceResult.RecipeDto> results = new ArrayList<>();
    private SpoonacularSearchAdapter adapter;

    public static void start(android.content.Context context) {
        context.startActivity(new Intent(context, SpoonacularSearchActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spoonacular_search);

        this.source = com.cozycook.CozyCookApp.getRecipeSourceRegistry().getSource("spoonacular");
        this.repository = new RecipeRepository(this);

        if (source == null) {
            Toast.makeText(this, "Spoonacular API key not set. Add key in Settings.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        com.google.android.material.textfield.TextInputEditText queryEt = findViewById(R.id.query);
        RecyclerView recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SpoonacularSearchAdapter(results, this::onSelectRecipe);
        recycler.setAdapter(adapter);

        findViewById(R.id.search_btn).setOnClickListener(v -> {
            String q = queryEt.getText().toString().trim();
            if (q.isEmpty()) {
                Toast.makeText(this, "Enter search term", Toast.LENGTH_SHORT).show();
                return;
            }
            results.clear();
            adapter.notifyDataSetChanged();
            source.searchRecipes(q, 15, new RecipeSource.RecipeSourceCallback() {
                @Override
                public void onResult(RecipeSourceResult result) {
                    runOnUiThread(() -> {
                        if (result.isSuccess()) {
                            results.clear();
                            results.addAll(result.recipes);
                            adapter.notifyDataSetChanged();
                            if (results.isEmpty()) Toast.makeText(SpoonacularSearchActivity.this, "No results", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SpoonacularSearchActivity.this, result.errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                @Override
                public void onError(Throwable t) {
                    runOnUiThread(() -> Toast.makeText(SpoonacularSearchActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show());
                }
            });
        });
    }

    private void onSelectRecipe(RecipeSourceResult.RecipeDto dto) {
        source.getRecipeById(dto.externalId, new RecipeSource.RecipeSourceCallback() {
            @Override
            public void onResult(RecipeSourceResult result) {
                runOnUiThread(() -> {
                    if (!result.isSuccess() || result.recipes.isEmpty()) {
                        Toast.makeText(SpoonacularSearchActivity.this, "Could not load recipe", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    RecipeSourceResult.RecipeDto full = result.recipes.get(0);
                    RecipeEntity entity = RecipeMapper.toEntity(full, source.getSourceId());
                    repository.insert(entity, new KitchenItemRepository.ResultCallback<Long>() {
                        @Override
                        public void onResult(Long id) {
                            runOnUiThread(() -> {
                                Intent data = new Intent();
                                data.putExtra(EXTRA_RECIPE_ID, id);
                                setResult(RESULT_OK, data);
                                finish();
                            });
                        }
                    });
                });
            }
            @Override
            public void onError(Throwable t) {
                runOnUiThread(() -> Toast.makeText(SpoonacularSearchActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });
    }
}
