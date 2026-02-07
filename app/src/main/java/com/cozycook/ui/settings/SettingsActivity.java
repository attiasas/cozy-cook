package com.cozycook.ui.settings;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cozycook.R;
import com.cozycook.api.spoonacular.SpoonacularRecipeSource;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

/**
 * Simple settings: Spoonacular API key. Save and re-register source.
 */
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        com.google.android.material.appbar.MaterialToolbar toolbar = findViewById(R.id.toolbar_settings);
        if (toolbar != null) toolbar.setNavigationOnClickListener(v -> finish());

        TextInputEditText keyEt = findViewById(R.id.api_key);
        keyEt.setText(getSharedPreferences("cozycook", MODE_PRIVATE).getString("spoonacular_api_key", ""));

        findViewById(R.id.btn_save).setOnClickListener(v -> {
            String key = keyEt.getText().toString().trim();
            getSharedPreferences("cozycook", MODE_PRIVATE).edit().putString("spoonacular_api_key", key).apply();
            com.cozycook.CozyCookApp.getRecipeSourceRegistry().unregister(SpoonacularRecipeSource.SOURCE_ID);
            if (!key.isEmpty()) {
                com.cozycook.CozyCookApp.getRecipeSourceRegistry().register(new SpoonacularRecipeSource(key));
            }
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
