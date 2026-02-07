package com.cozycook.ui.grocery;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cozycook.R;
import com.cozycook.data.entity.GroceryItemEntity;

import java.util.List;

public class GroceryAdapter extends RecyclerView.Adapter<GroceryAdapter.VH> {

    private final List<GroceryItemEntity> items;
    private final OnCheckListener onCheck;
    private final OnDeleteListener onDelete;

    public interface OnCheckListener {
        void onCheck(GroceryItemEntity item, boolean checked);
    }

    public interface OnDeleteListener {
        void onDelete(GroceryItemEntity item);
    }

    public GroceryAdapter(List<GroceryItemEntity> items, OnCheckListener onCheck, OnDeleteListener onDelete) {
        this.items = items;
        this.onCheck = onCheck;
        this.onDelete = onDelete;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grocery, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        GroceryItemEntity item = items.get(position);
        holder.name.setText(item.name);
        holder.name.setAlpha(item.checked ? 0.5f : 1f);
        String amt = item.amount > 0 ? item.amount + (item.unit != null && !item.unit.isEmpty() ? " " + item.unit : "") : "";
        holder.amount.setText(amt);
        holder.checked.setChecked(item.checked);
        holder.checked.setOnCheckedChangeListener((b, isChecked) -> onCheck.onCheck(item, isChecked));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        CheckBox checked;
        TextView name, amount;

        VH(View itemView) {
            super(itemView);
            checked = itemView.findViewById(R.id.checked);
            name = itemView.findViewById(R.id.name);
            amount = itemView.findViewById(R.id.amount);
        }
    }
}
