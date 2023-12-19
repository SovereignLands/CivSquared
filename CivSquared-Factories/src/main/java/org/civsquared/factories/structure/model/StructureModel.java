package org.civsquared.factories.structure.model;

import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.util.Vector;

import java.io.Reader;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class StructureModel {
    @Getter
    private List<StructureModelBlock> blockDescriptors;

    @Getter
    private int width, length, height;

    StructureModel(@NonNull List<StructureModelBlock> blockDescriptors) {
        this.blockDescriptors = blockDescriptors;
    }

    public StructureModel(@NonNull Reader reader) {
        this.initialize(reader);
    }

    private void initialize(@NonNull Reader reader) {
        var layers = new ArrayList<int[][]>();

        // Defines the size of the "canvas". This is basically how big each layer in the layout will be.
        // Each layer should be the same size, and each row within the layer should also be of the same size.
        int canvasWidth = -1, canvasLength = -1;

        // Read the configuration data.
        final var config = YamlConfiguration.loadConfiguration(reader);

        // Parse the layout section.
        final var layout = Objects.requireNonNull(config.getConfigurationSection("layout"));

        int i = 1;
        List<String> currentLayer;

        // Location of the root '$' character.
        int rootX = -1, rootZ = -1;

        while (true) {
            try {
                currentLayer = layout.getStringList("" + i);
            } catch (NullPointerException exception) {
                break;
            }

            if (currentLayer.isEmpty())
                break;

            int layerLength, layerWidth;
            layerWidth = currentLayer.size();
            layerLength = currentLayer.get(0).length();

            if (canvasLength == -1) {
                canvasLength = layerLength;
                canvasWidth = layerWidth;
            } else if (layerLength != canvasLength || layerWidth != canvasWidth)
                throw new RuntimeException("Layer sizes are not all the same.");

            var layer = new int[canvasWidth][canvasLength];

            int rowIndex = 0;
            int cursorZ = 0;

            for (var row : currentLayer) {
                if (row.length() != layerLength)
                    throw new RuntimeException("Layout entry '%d' has rows which are different sizes.".formatted(i));

                layer[rowIndex] = row.chars().toArray();

                int index;
                if ((index = row.indexOf("$")) != -1) {
                    rootX = index;
                    rootZ = cursorZ;
                }

                rowIndex++;
                cursorZ++;
            }

            layers.add(layer);

            i++;
        }

        if (rootX == -1)
            throw new RuntimeException("Failed to find root character ($)");

        // Parse the definitions section.
        final var definitions = config.getConfigurationSection("definitions");
        if (definitions == null)
            throw new RuntimeException("Failed to find 'definitions' section for model.");

        final var blockDataMap = new HashMap<String, BlockData>();

        for (final var key : definitions.getKeys(true)) {
            final var data = definitions.getString(key);

            try {
                blockDataMap.put(key, Bukkit.getServer().createBlockData(Objects.requireNonNull(data)));
            } catch (InvalidParameterException exception) {
                throw new RuntimeException("Failed to parse definition: '%s'".formatted(exception.getMessage()));
            }
        }

        // Determine the specific offsets for each block, and place them in an array.
        final var blockDescriptors = new ArrayList<StructureModelBlock>();

        for (int y = 0; y < layers.size(); y++) {
            var layer = layers.get(y);
            for (int z = 0; z < layer.length; z++) {
                var row = layer[z];
                for (int x = 0; x < row.length; x++) {
                    var id = row[x];

                    // Skip '0', it's our air character.
                    if (id == '0')
                        continue;

                    var blockData =
                        blockDataMap.get(Character.valueOf((char) id).toString());

                    if (blockData == null)
                        throw new RuntimeException("Definition not found for '%c'.".formatted(id));

                    blockDescriptors.add(new StructureModelBlock(
                        new Vector(x - rootX, y, z - rootZ),
                        blockData.clone()
                    ));
                }
            }
        }

        this.blockDescriptors = blockDescriptors;

        this.width = canvasWidth;
        this.length = canvasLength;
        this.height = layers.size();
    }
}
