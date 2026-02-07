package com.cozycook.ui.plans;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cozycook.R;
import com.cozycook.data.entity.RecipeEntity;
import com.cozycook.util.MealPlanRepositoryHelper;

import java.util.ArrayList;
import java.util.List;

public class PlanDayAdapter extends RecyclerView.Adapter<PlanDayAdapter.VH> {

    private List<PlanDayItem> items = new ArrayList<>();
    private final OnAddToCalendarListener onAddToCalendar;

    public interface OnAddToCalendarListener {
        void onAddToCalendar(PlanDayItem day, RecipeEntity recipe);
    }

    public PlanDayAdapter(List<PlanDayItem> items, OnAddToCalendarListener onAddToCalendar) {
        this.items = items != null ? items : new ArrayList<>();
        this.onAddToCalendar = onAddToCalendar;
    }

    public void setItems(List<PlanDayItem> items) {
        this.items = items != null ? items : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plan_day, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        PlanDayItem day = items.get(position);
        holder.dayText.setText(MealPlanRepositoryHelper.formatDay(day.dayMillis));
        StringBuilder sb = new StringBuilder();
        for (RecipeEntity r : day.recipes) {
            sb.append("• ").append(r.title).append("\n");
        }
        holder.recipesText.setText(sb.toString());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView dayText, recipesText;

        VH(View itemView) {
            super(itemView);
            dayText = itemView.findViewById(R.id.day_label);
            recipesText = itemView.findViewById(R.id.recipes_list);
        }
    }
}
