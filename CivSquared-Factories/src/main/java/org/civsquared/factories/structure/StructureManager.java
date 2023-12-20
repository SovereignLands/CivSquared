package org.civsquared.factories.structure;

import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.civsquared.core.util.ChatUtil;
import org.civsquared.factories.CivSquaredFactoriesPlugin;
import org.civsquared.factories.structure.guide.StructureGuideManager;
import org.civsquared.factories.structure.model.StructureModelRegistry;
import org.civsquared.factories.util.Keys;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Responsible for managing `Structure` sub-type implementations.
 */
public class StructureManager implements Listener {
    @NonNull
    private final CivSquaredFactoriesPlugin plugin;
    private final Logger logger;

    @Getter
    private final StructureRegistry registry = new StructureRegistry();
    @Getter
    private final StructureModelRegistry modelRegistry;
    @Getter
    private final StructureGuideManager guideManager;

    @Getter
    private final Map<Location, Structure> structures = new HashMap<>();

    /**
     * StructureManager plugin.
     *
     * @param plugin The plugin.
     */
    public StructureManager(@NonNull CivSquaredFactoriesPlugin plugin) {
        this.plugin = plugin;
        this.logger = this.plugin.getLogger();

        this.modelRegistry = new StructureModelRegistry(this.plugin);
        this.guideManager = new StructureGuideManager(this.plugin);
    }

    /**
     * Called on enable.
     */
    public void initialize() {
        this.plugin.getCommandRegistry()
            .register(new StructureCommand(this.plugin, this));

        final var server = this.plugin.getServer();

        server.getPluginManager().registerEvents(this, this.plugin);

        this.guideManager.initialize();
    }

    /**
     * Gives the passed `player`  the structure item with `id`.
     *
     * @param player The player to give the structure item to.
     * @param id     The ID if the structure item.
     */
    public void giveStructureItem(Player player, String id) {
        final var itemStack = this.registry.createItem(id);
        player.getInventory().addItem(itemStack);
    }

    /**
     * Creates a new `Structure` instance for the given `id` at the given `location`.
     *
     * <p>
     * For a structure which is `StructureConstructionType.MANUAL`, only the block at offset [0, 0, 0] will be placed.
     * For a structure which is `StructureConstructionType.AUTO`, every block will be placed.
     * </p>
     *
     * @param id       The ID of the structure to create.
     * @param location The location to create the structure at.
     * @param face     The block face to rotate the structure to face.
     * @return If the structure was created.
     */
    public boolean createStructure(@NonNull String id, @NonNull Location location, @NonNull BlockFace face) {
        final var model = this.modelRegistry.get(this.registry.getMetadataForId(id).model());

//        if (!model.canBePlaced(location.clone(), face))
//            return false;

        this.logger.info("Creating structure '%s' @ %s %s".formatted(id, location, face));

        final var structure = this.registry.create(id, location, face);
        this.structures.put(location, structure);

        this.plugin.getServer().getScheduler().runTaskLater(this.plugin, () -> structure.initialize(this), 2L);

        return true;
    }

    @EventHandler
    public void onBlockPlace(final BlockPlaceEvent event) {
        final var itemStack = event.getItemInHand();

        ItemMeta itemMeta;
        if ((itemMeta = itemStack.getItemMeta()) == null)
            return;

        final var persistence = itemMeta.getPersistentDataContainer();

        String id;
        if ((id = persistence.get(Keys.STRUCTURE, PersistentDataType.STRING)) == null)
            return;

        final var player = event.getPlayer();

        if (!this.createStructure(
            id,
            event.getBlockPlaced().getLocation(),
            player.getFacing().getOppositeFace()
        ))
            ChatUtil.sendErrorMessage(player, "Not enough space to create structure.");

        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(final BlockBreakEvent event) {
        for (final var es : this.structures.entrySet()) {
            final var location = es.getKey();
            final var structure = es.getValue();

            // We'll only check if the block is within 7 blocks of a structure.
            if (event.getBlock().getLocation().distanceSquared(location) > 49)
                continue;

            if (event.getBlock().equals(structure.getRootBlock())) {
                structure.destroy();
                this.structures.remove(location);
                return;
            }

            if (structure.getBlocks().contains(event.getBlock())) {
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onPlayerChangeItem(final PlayerItemHeldEvent event) {
        final var player = event.getPlayer();
        final var itemStack = player.getInventory().getItem(event.getNewSlot());

        if (this.guideManager.hasGuide(player))
            this.guideManager.removeGuide(player);

        if (itemStack == null)
            return;

        ItemMeta itemMeta;
        if ((itemMeta = itemStack.getItemMeta()) == null)
            return;

        final var persistence = itemMeta.getPersistentDataContainer();

        String id;
        if ((id = persistence.get(Keys.STRUCTURE, PersistentDataType.STRING)) == null)
            return;

        final var metadata = this.getRegistry().getMetadataForId(id);
        final var model = this.getModelRegistry().get(metadata.model());

        this.guideManager.createGuide(player, model);
    }
}

