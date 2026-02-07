package com.cozycook.ui.recipes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cozycook.R;
import com.cozycook.api.RecipeSourceResult;

import java.util.List;

public class SpoonacularSearchAdapter extends RecyclerView.Adapter<SpoonacularSearchAdapter.VH> {

    private final List<RecipeSourceResult.RecipeDto> items;
    private final OnSelectListener onSelect;

    public interface OnSelectListener {
        void onSelect(RecipeSourceResult.RecipeDto dto);
    }

    public SpoonacularSearchAdapter(List<RecipeSourceResult.RecipeDto> items, OnSelectListener onSelect) {
        this.items = items;
        this.onSelect = onSelect;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        RecipeSourceResult.RecipeDto dto = items.get(position);
        holder.title.setText(dto.title);
        holder.subtitle.setText("Tap to save to my recipes");
        if (dto.imageUrl != null && !dto.imageUrl.isEmpty()) {
            Glide.with(holder.image.getContext()).load(dto.imageUrl).into(holder.image);
        } else {
            holder.image.setImageDrawable(null);
        }
        holder.itemView.setOnClickListener(v -> onSelect.onSelect(dto));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title, subtitle;

        VH(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            subtitle = itemView.findViewById(R.id.subtitle);
        }
    }
}
