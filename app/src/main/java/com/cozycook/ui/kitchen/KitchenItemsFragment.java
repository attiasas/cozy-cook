package com.cozycook.ui.kitchen;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cozycook.R;
import com.cozycook.data.entity.KitchenItemEntity;
import com.cozycook.data.repository.KitchenItemRepository;

import java.util.ArrayList;
import java.util.List;

public class KitchenItemsFragment extends Fragment {

    private KitchenItemRepository repository;
    private KitchenItemAdapter adapter;
    private final List<KitchenItemEntity> items = new ArrayList<>();

    public static KitchenItemsFragment newInstance() {
        return new KitchenItemsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repository = new KitchenItemRepository(requireContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_kitchen_items, container, false);
        RecyclerView recycler = root.findViewById(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new KitchenItemAdapter(items, this::onDelete);
        recycler.setAdapter(adapter);

        root.findViewById(R.id.fab).setOnClickListener(v -> showAddDialog());
        load();
        return root;
    }

    private void load() {
        repository.getAll(new KitchenItemRepository.ResultCallback<List<KitchenItemEntity>>() {
            @Override
            public void onResult(List<KitchenItemEntity> result) {
                if (getActivity() == null) return;
                items.clear();
                if (result != null) items.addAll(result);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void showAddDialog() {
        EditText nameEt = new EditText(requireContext());
        nameEt.setHint("Name");
        EditText categoryEt = new EditText(requireContext());
        categoryEt.setHint("Category (food, appliance, spice…)");
        categoryEt.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        EditText unitEt = new EditText(requireContext());
        unitEt.setHint("Unit (optional)");

        android.widget.LinearLayout layout = new android.widget.LinearLayout(requireContext());
        layout.setOrientation(android.widget.LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);
        layout.addView(nameEt);
        layout.addView(categoryEt);
        layout.addView(unitEt);

        new AlertDialog.Builder(requireContext())
            .setTitle("Add kitchen item")
            .setView(layout)
            .setPositiveButton("Add", (d, w) -> {
                String name = nameEt.getText().toString().trim();
                if (name.isEmpty()) {
                    Toast.makeText(requireContext(), "Enter name", Toast.LENGTH_SHORT).show();
                    return;
                }
                String category = categoryEt.getText().toString().trim();
                if (category.isEmpty()) category = "food";
                String unit = unitEt.getText().toString().trim();
                KitchenItemEntity e = new KitchenItemEntity(name, category, unit);
                repository.insert(e, new KitchenItemRepository.ResultCallback<Long>() {
                    @Override
                    public void onResult(Long id) {
                        if (getActivity() != null) {
                            e.id = id;
                            items.add(e);
                            adapter.notifyItemInserted(items.size() - 1);
                            Toast.makeText(requireContext(), "Added", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            })
            .setNegativeButton(android.R.string.cancel, null)
            .show();
    }

    private void onDelete(KitchenItemEntity item) {
        new AlertDialog.Builder(requireContext())
            .setMessage("Remove " + item.name + "?")
            .setPositiveButton(android.R.string.yes, (d, w) -> repository.delete(item, new KitchenItemRepository.ResultCallback<Void>() {
                @Override
                public void onResult(Void v) {
                    int i = items.indexOf(item);
                    if (i >= 0) {
                        items.remove(i);
                        adapter.notifyItemRemoved(i);
                    }
                }
            }))
            .setNegativeButton(android.R.string.no, null)
            .show();
    }
}
