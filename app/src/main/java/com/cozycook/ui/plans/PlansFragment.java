package com.cozycook.ui.plans;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cozycook.R;
import com.cozycook.calendar.CalendarHelper;
import com.cozycook.data.entity.MealPlanEntity;
import com.cozycook.data.entity.RecipeEntity;
import com.cozycook.data.repository.KitchenItemRepository;
import com.cozycook.data.repository.MealPlanRepository;
import com.cozycook.data.repository.RecipeRepository;
import com.cozycook.util.MealPlanRepositoryHelper;
import com.cozycook.util.PlanGenerator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PlansFragment extends Fragment {

    private MealPlanRepository planRepo;
    private RecipeRepository recipeRepo;
    private long fromMillis;
    private long toMillis;

    public static PlansFragment newInstance() {
        return new PlansFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        planRepo = new MealPlanRepository(requireContext());
        recipeRepo = new RecipeRepository(requireContext());
        setWeekRange();
    }

    private void setWeekRange() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        fromMillis = cal.getTimeInMillis();
        cal.add(Calendar.DAY_OF_MONTH, 7);
        toMillis = cal.getTimeInMillis();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_plans, container, false);

        TextView title = root.findViewById(R.id.plan_title);
        title.setText("This week");

        RecyclerView recycler = root.findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        PlanDayAdapter adapter = new PlanDayAdapter(new ArrayList<>(), this::onAddToCalendar);
        recycler.setAdapter(adapter);

        root.findViewById(R.id.btn_generate).setOnClickListener(v -> showGenerateDialog(adapter));
        loadPlans(adapter);
        return root;
    }

    private void loadPlans(PlanDayAdapter adapter) {
        planRepo.getInRange(fromMillis, toMillis, new KitchenItemRepository.ResultCallback<List<MealPlanEntity>>() {
            @Override
            public void onResult(List<MealPlanEntity> result) {
                if (getActivity() == null) return;
                List<PlanDayItem> days = MealPlanRepositoryHelper.groupByDay(result, recipeRepo);
                runOnUiThread(() -> adapter.setItems(days));
            }
        });
    }

    private void runOnUiThread(Runnable r) {
        if (getActivity() != null) getActivity().runOnUiThread(r);
    }

    private void showGenerateDialog(PlanDayAdapter adapter) {
        recipeRepo.getAll(new KitchenItemRepository.ResultCallback<List<RecipeEntity>>() {
            @Override
            public void onResult(List<RecipeEntity> recipes) {
                if (getActivity() == null || recipes == null || recipes.isEmpty()) {
                    runOnUiThread(() -> Toast.makeText(requireContext(), "Add recipes first", Toast.LENGTH_SHORT).show());
                    return;
                }
                runOnUiThread(() -> {
                    PlanGenerator gen = new PlanGenerator();
                    String group = "week_" + System.currentTimeMillis();
                    List<MealPlanEntity> plans = gen.generateForDays(recipes, fromMillis, 7, 3, group);
                    planRepo.insertPlans(plans, new KitchenItemRepository.ResultCallback<Void>() {
                        @Override
                        public void onResult(Void v) {
                            runOnUiThread(() -> {
                                loadPlans(adapter);
                                Toast.makeText(requireContext(), "Plan generated", Toast.LENGTH_SHORT).show();
                            });
                        }
                    });
                });
            }
        });
    }

    private void onAddToCalendar(PlanDayItem day, RecipeEntity recipe) {
        Intent i = CalendarHelper.prepEventForDay(recipe.title, day.dayMillis);
        if (i.resolveActivity(requireContext().getPackageManager()) != null) {
            startActivity(i);
        } else {
            Toast.makeText(requireContext(), "No calendar app", Toast.LENGTH_SHORT).show();
        }
    }
}
