package com.cozycook.ui.recipes;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cozycook.R;
import com.cozycook.data.entity.RecipeEntity;
import com.cozycook.data.repository.KitchenItemRepository;
import com.cozycook.data.repository.RecipeRepository;
import com.cozycook.ui.MainActivity;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class RecipesFragment extends Fragment {

    private RecipeRepository repository;
    private RecipesAdapter adapter;
    private final List<RecipeEntity> items = new ArrayList<>();
    private TextInputEditText searchInput;

    public static RecipesFragment newInstance() {
        return new RecipesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repository = new RecipeRepository(requireContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_recipes, container, false);
        searchInput = root.findViewById(R.id.search_input);
        RecyclerView recycler = root.findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new RecipesAdapter(items, this::openRecipe);
        recycler.setAdapter(adapter);

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString().trim());
            }
        });

        root.findViewById(R.id.fab).setOnClickListener(v -> {
            Intent i = new Intent(requireContext(), AddRecipeActivity.class);
            startActivityForResult(i, 100);
        });
        load();
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) load();
    }

    private void load() {
        repository.getAll(new KitchenItemRepository.ResultCallback<List<RecipeEntity>>() {
            @Override
            public void onResult(List<RecipeEntity> result) {
                if (getActivity() == null) return;
                items.clear();
                if (result != null) items.addAll(result);
                filter(searchInput != null ? searchInput.getText().toString().trim() : "");
            }
        });
    }

    private void filter(String query) {
        if (query.isEmpty()) {
            adapter.setFiltered(items);
            return;
        }
        repository.searchByTitle(query, new KitchenItemRepository.ResultCallback<List<RecipeEntity>>() {
            @Override
            public void onResult(List<RecipeEntity> result) {
                if (getActivity() == null) return;
                adapter.setFiltered(result != null ? result : new ArrayList<>());
            }
        });
    }

    private void openRecipe(RecipeEntity recipe) {
        Intent i = new Intent(requireContext(), RecipeDetailActivity.class);
        i.putExtra("recipe_id", recipe.id);
        startActivityForResult(i, 101);
    }
}
