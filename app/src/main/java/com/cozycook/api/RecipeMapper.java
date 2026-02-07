package com.cozycook.api;

import com.cozycook.data.IngredientLine;
import com.cozycook.data.entity.RecipeEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Maps API RecipeDto to local RecipeEntity. Used when saving an external recipe.
 */
public final class RecipeMapper {

    public static RecipeEntity toEntity(RecipeSourceResult.RecipeDto dto, String sourceId) {
        RecipeEntity e = new RecipeEntity();
        e.source = sourceId;
        e.externalId = dto.externalId;
        e.title = dto.title;
        e.imageUrl = dto.imageUrl;
        e.servings = dto.servings > 0 ? dto.servings : 1;
        e.readyInMinutes = dto.readyInMinutes;
        e.summary = dto.summary;
        e.instructions = dto.instructions != null ? dto.instructions : "";
        List<IngredientLine> lines = new ArrayList<>();
        if (dto.ingredients != null) {
            for (RecipeSourceResult.IngredientDto i : dto.ingredients) {
                lines.add(new IngredientLine(i.name, i.amount, i.unit));
            }
        }
        e.ingredientsJson = new com.google.gson.Gson().toJson(lines);
        return e;
    }
}
