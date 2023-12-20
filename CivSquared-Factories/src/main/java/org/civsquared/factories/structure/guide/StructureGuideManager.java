package org.civsquared.factories.structure.guide;

import com.destroystokyo.paper.ParticleBuilder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.civsquared.factories.CivSquaredFactoriesPlugin;
import org.civsquared.factories.structure.model.StructureModel;
import org.civsquared.factories.util.BlockFaceUtil;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class StructureGuideManager {
    public static final long TICKS_PER_UPDATE = 5;

    private final Map<Player, StructureGuide> guideMap = new HashMap<>();
    @NonNull
    private CivSquaredFactoriesPlugin plugin;

    public void initialize() {
        this.plugin.getServer().getScheduler().runTaskTimer(this.plugin, () -> {
            for (final var es : this.guideMap.entrySet())
                this.updateGuide(es.getKey(), es.getValue());
        }, 0, TICKS_PER_UPDATE);
    }

    public void createGuide(@NonNull Player player, @NonNull StructureModel model) {
        this.guideMap.put(player, new StructureGuide(model));
    }

    public void removeGuide(@NonNull Player player) {
        this.guideMap.remove(player);
    }

    public boolean hasGuide(@NonNull Player player) {
        return this.guideMap.containsKey(player);
    }

    private void updateGuide(@NonNull Player player, @NonNull StructureGuide guide) {
        final var model = guide.getModel();

        // Determine the block/location that we're going to be placing the guide.
        final var block = player.getTargetBlockExact(4);

        if (block == null || block.getType() == Material.AIR)
            return;

        final var location = block.getLocation();
        final var targetFace = BlockFaceUtil.getTargetBlockFace(player);

        if (targetFace == null)
            return;

        if (targetFace != BlockFace.DOWN && targetFace != BlockFace.UP)
            switch (targetFace) {
                case NORTH -> location.add(0, -1, -1);
                case SOUTH -> location.add(0, -1, 1);
                case WEST -> location.add(-1, -1, 0);
                case EAST -> location.add(1, -1, 0);
            }


        // Determine the orientation of the guide.
        final var blockFacing = player.getFacing().getOppositeFace();
        final var angle = Math.abs(BlockFaceUtil.angleBetweenBlockFaces(BlockFace.NORTH, blockFacing));

        var width = model.getWidth();
        var length = model.getLength();

        if (angle == 90) {
            var t = width;
            width = length;
            length = t;
        }

        final var hl = length % 2 == 0 ? length / 2 : (length - 1) / 2;
        final var hw = width % 2 == 0 ? width / 2 : (width - 1) / 2;

        final var numParticles = 50;

        // Check if the structure can actually be placed.
        final var color = model.canBePlaced(location.clone().add(0.0, 1.0, 0.0), blockFacing) ? Color.GREEN : Color.RED;

        // Draw the guides using particles.
        for (int i = 0; i < numParticles; i++) {
            // Width-wise particles.
            double z = 0.5 - (width / 2.0) + i * ((double) width / numParticles);

            final var sl1 = new Vector(1.0 + hl, 1.0, z);
            final var sl2 = new Vector(0.0 - hl, 1.0, z);

            new ParticleBuilder(Particle.REDSTONE)
                .color(color)
                .location(location.clone().add(sl1))
                .count(1)
                .receivers(player)
                .spawn();

            new ParticleBuilder(Particle.REDSTONE)
                .color(color)
                .location(location.clone().add(sl2))
                .count(1)
                .receivers(player)
                .spawn();

            // Length-wise particles.
            double x = 0.5 - (length / 2.0) + i * ((double) length / numParticles);

            final var sw1 = new Vector(x, 1.0, 1.0 + hw);
            final var sw2 = new Vector(x, 1.0, 0.0 - hw);

            new ParticleBuilder(Particle.REDSTONE)
                .color(color)
                .location(location.clone().add(sw1))
                .count(1)
                .receivers(player)
                .spawn();

            new ParticleBuilder(Particle.REDSTONE)
                .color(color)
                .location(location.clone().add(sw2))
                .count(1)
                .receivers(player)
                .spawn();
        }
    }
}
