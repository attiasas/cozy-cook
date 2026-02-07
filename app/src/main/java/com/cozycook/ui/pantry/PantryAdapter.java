package com.cozycook.ui.pantry;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cozycook.R;
import com.cozycook.data.entity.PantryItemEntity;

import java.util.List;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.VH> {

    private final List<PantryItemEntity> items;
    private final OnDeleteListener onDelete;

    public interface OnDeleteListener {
        void onDelete(PantryItemEntity item);
    }

    public PantryAdapter(List<PantryItemEntity> items, OnDeleteListener onDelete) {
        this.items = items;
        this.onDelete = onDelete;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pantry, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        PantryItemEntity item = items.get(position);
        holder.name.setText(item.name);
        String qty = item.quantity + (item.unit != null && !item.unit.isEmpty() ? " " + item.unit : "");
        holder.quantity.setText(qty);
        holder.btnDelete.setOnClickListener(v -> onDelete.onDelete(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView name, quantity;
        ImageButton btnDelete;

        VH(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            quantity = itemView.findViewById(R.id.quantity);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
