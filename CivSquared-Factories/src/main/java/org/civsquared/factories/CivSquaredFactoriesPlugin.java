package org.civsquared.factories;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.civsquared.core.CivSquaredPlugin;
import org.civsquared.core.util.ChatColorUtil;
import org.civsquared.factories.structure.StructureManager;
import org.civsquared.factories.structures.SmelterStructure;
import org.civsquared.factories.util.Keys;

import java.util.Objects;

public class CivSquaredFactoriesPlugin extends CivSquaredPlugin {
    @Getter
    private final StructureManager structureManager = new StructureManager(this);

    @Override
    public void onEnable() {
        super.onEnable();

        // Configure the namespaced keys used for item identification.
        Keys.setup(this);

        // Enable the sub-modules.
        this.structureManager.initialize();

        this.structureManager.getRegistry().register(SmelterStructure.class, () -> {
            var is = new ItemStack(Material.BLAST_FURNACE);
            var meta = Objects.requireNonNull(is.getItemMeta());
            meta.setDisplayName(ChatColorUtil.colorify("&8[&cSmelter&8]"));

            is.setItemMeta(meta);

            return is;
        });
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
