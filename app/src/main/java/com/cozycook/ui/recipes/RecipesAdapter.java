package com.cozycook.ui.recipes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.cozycook.R;
import com.cozycook.data.entity.RecipeEntity;

import java.util.ArrayList;
import java.util.List;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.VH> {

    private List<RecipeEntity> filtered = new ArrayList<>();
    private final OnRecipeClick onRecipeClick;

    public interface OnRecipeClick {
        void onClick(RecipeEntity recipe);
    }

    public RecipesAdapter(List<RecipeEntity> items, OnRecipeClick onRecipeClick) {
        this.filtered = items != null ? new ArrayList<>(items) : new ArrayList<>();
        this.onRecipeClick = onRecipeClick;
    }

    public void setFiltered(List<RecipeEntity> list) {
        this.filtered = list != null ? list : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        RecipeEntity r = filtered.get(position);
        holder.title.setText(r.title);
        String sub = "";
        if (r.readyInMinutes > 0) sub += r.readyInMinutes + " min";
        if (r.servings > 0) sub += (sub.isEmpty() ? "" : " • ") + r.servings + " servings";
        if (r.source != null && !"local".equals(r.source)) sub += (sub.isEmpty() ? "" : " • ") + r.source;
        holder.subtitle.setText(sub);
        if (r.imageUrl != null && !r.imageUrl.isEmpty()) {
            Glide.with(holder.image.getContext()).load(r.imageUrl)
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(16)))
                .into(holder.image);
        } else {
            holder.image.setImageDrawable(null);
        }
        holder.itemView.setOnClickListener(v -> onRecipeClick.onClick(r));
    }

    @Override
    public int getItemCount() {
        return filtered.size();
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
