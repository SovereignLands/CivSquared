package org.civsquared.factories.structure.model;

import lombok.NonNull;
import org.civsquared.factories.CivSquaredFactoriesPlugin;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class StructureModelRegistry {
    private CivSquaredFactoriesPlugin plugin;
    private Map<String, StructureModel> models = new HashMap<>();

    public StructureModelRegistry(@NonNull CivSquaredFactoriesPlugin plugin) {
        this.plugin = plugin;
    }

    public StructureModel get(@NonNull String path) {
        if (this.models.containsKey(path))
            return this.models.get(path);

        final var resource = this.plugin.getResource(path);
        if (resource == null)
            throw new RuntimeException("Failed to fetch embedded resource, '%s', returned null.".formatted(path));

        final var reader = new InputStreamReader(resource);
        final var model = new StructureModel(reader);
        this.models.put(path, model);

        return model;
    }
}
