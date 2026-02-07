package com.cozycook.api;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Registry of recipe sources. Add Spoonacular, and in future more sources (different schemas).
 */
public class RecipeSourceRegistry {

    private final List<RecipeSource> sources = new CopyOnWriteArrayList<>();

    public void register(RecipeSource source) {
        if (source != null) sources.add(source);
    }

    public List<RecipeSource> getSources() {
        return new ArrayList<>(sources);
    }

    public RecipeSource getSource(String sourceId) {
        for (RecipeSource s : sources) {
            if (sourceId.equals(s.getSourceId())) return s;
        }
        return null;
    }

    public void unregister(String sourceId) {
        sources.removeIf(s -> sourceId.equals(s.getSourceId()));
    }
}
