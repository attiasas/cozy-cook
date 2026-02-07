package com.cozycook.ui.grocery;

import android.os.Bundle;
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
import com.cozycook.data.entity.GroceryItemEntity;
import com.cozycook.data.entity.MealPlanEntity;
import com.cozycook.data.entity.RecipeEntity;
import com.cozycook.data.repository.GroceryRepository;
import com.cozycook.data.repository.KitchenItemRepository;
import com.cozycook.data.repository.MealPlanRepository;
import com.cozycook.data.repository.RecipeRepository;
import com.cozycook.util.GroceryListGenerator;
import com.cozycook.util.PlanGenerator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class GroceryFragment extends Fragment {

    private GroceryRepository repository;
    private GroceryAdapter adapter;
    private final List<GroceryItemEntity> items = new ArrayList<>();

    public static GroceryFragment newInstance() {
        return new GroceryFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repository = new GroceryRepository(requireContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_grocery, container, false);
        RecyclerView recycler = root.findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new GroceryAdapter(items, this::onCheck, this::onDelete);
        recycler.setAdapter(adapter);

        root.findViewById(R.id.btn_from_plans).setOnClickListener(v -> generateFromPlans());
        load();
        return root;
    }

    private void load() {
        repository.getAll(new KitchenItemRepository.ResultCallback<List<GroceryItemEntity>>() {
            @Override
            public void onResult(List<GroceryItemEntity> result) {
                if (getActivity() == null) return;
                items.clear();
                if (result != null) items.addAll(result);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void generateFromPlans() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long from = cal.getTimeInMillis();
        cal.add(Calendar.DAY_OF_MONTH, 7);
        long to = cal.getTimeInMillis();

        MealPlanRepository planRepo = new MealPlanRepository(requireContext());
        RecipeRepository recipeRepo = new RecipeRepository(requireContext());
        planRepo.getInRange(from, to, new KitchenItemRepository.ResultCallback<List<MealPlanEntity>>() {
            @Override
            public void onResult(List<MealPlanEntity> plans) {
                if (getActivity() == null || plans == null || plans.isEmpty()) {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> Toast.makeText(requireContext(), "No plan for this week", Toast.LENGTH_SHORT).show());
                    }
                    return;
                }
                List<RecipeEntity> recipes = new ArrayList<>();
                for (MealPlanEntity p : plans) {
                    recipeRepo.getById(p.recipeId, new KitchenItemRepository.ResultCallback<RecipeEntity>() {
                        @Override
                        public void onResult(RecipeEntity r) {
                            if (r != null) recipes.add(r);
                        }
                    });
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignored) {}
                List<GroceryItemEntity> generated = GroceryListGenerator.fromRecipes(recipes, "week");
                for (GroceryItemEntity g : generated) {
                    repository.insert(g, id -> {});
                }
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        load();
                        Toast.makeText(requireContext(), "Grocery list updated", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });
    }

    private void onCheck(GroceryItemEntity item, boolean checked) {
        repository.setChecked(item.id, checked, v -> {});
    }

    private void onDelete(GroceryItemEntity item) {
        repository.delete(item, v -> {
            int i = items.indexOf(item);
            if (i >= 0) {
                items.remove(i);
                adapter.notifyItemRemoved(i);
            }
        });
    }
}
