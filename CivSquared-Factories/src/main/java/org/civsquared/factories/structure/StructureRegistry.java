package org.civsquared.factories.structure;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.civsquared.factories.structure.annotations.StructureMetadata;
import org.civsquared.factories.structure.factories.StructureItemFactory;
import org.civsquared.factories.util.Keys;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Responsible for managing `Structure` sub-classes and instantiation via an in-game block or egg.
 */
@RequiredArgsConstructor
public class StructureRegistry {

    private final Map<String, Class<? extends Structure>> classMap = new HashMap<>();
    private final Map<String, StructureItemFactory> itemFactoryMap = new HashMap<>();

    public void register(@NonNull Class<? extends Structure> clazz, @NonNull StructureItemFactory itemFactory) throws RuntimeException {
        final var metadata = Structure.getMetadataForClass(clazz);
        if (metadata == null)
            throw new RuntimeException("No metadata found for class, '%s'.".formatted(clazz.getName()));

        final var id = metadata.id();

        if (this.classMap.containsKey(id) || this.itemFactoryMap.containsKey(id))
            throw new RuntimeException("'%s' already exists within the registry.".formatted(id));

        this.classMap.put(id, clazz);
        this.itemFactoryMap.put(id, itemFactory);
    }

    public StructureMetadata getMetadataForId(@NonNull String id) {
        return Structure.getMetadataForClass(this.classMap.get(id));
    }

    /**
     * Creates a new `Structure` instance using a sub-class in the registry.
     *
     * @param id       The ID, or tag, of the `Structure` type.
     * @param location The location of the `Structure`.
     * @return The new instance.
     * @throws RuntimeException Occurs when no factory exists for the given `id`.
     */
    public Structure create(@NonNull String id, @NonNull Location location, @NonNull BlockFace face) throws RuntimeException {
        final var factory = this.classMap.get(id);

        if (factory == null)
            throw new RuntimeException("No factory found for '%s'.".formatted(id));

        Structure structure;
        try {
            structure = factory.getDeclaredConstructor(Location.class, BlockFace.class).newInstance(location, face);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return structure;
    }

    /**
     * Creates a new `ItemStack` for a given `Structure` using a sub-class in the registry.
     *
     * @param id The ID, or tag, of the `Structure` type.
     * @return The item stack.
     * @throws RuntimeException Occurs when no factory exists for the given `id`.
     */
    public ItemStack createItem(@NonNull String id) throws RuntimeException {
        final var factory = this.itemFactoryMap.get(id);

        if (factory == null)
            throw new RuntimeException("No factory found for '%s'.".formatted(id));

        var itemStack = factory.create();
        var itemMeta = Objects.requireNonNull(itemStack.getItemMeta());
        var dataContainer = itemMeta.getPersistentDataContainer();
        dataContainer.set(Keys.STRUCTURE, PersistentDataType.STRING, id);
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }
}
