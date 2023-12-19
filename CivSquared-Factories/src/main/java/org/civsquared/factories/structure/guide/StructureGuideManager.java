package org.civsquared.factories.structure.guide;

import com.destroystokyo.paper.ParticleBuilder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.civsquared.factories.CivSquaredFactoriesPlugin;
import org.civsquared.factories.structure.model.StructureModel;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class StructureGuideManager {
    private final Map<Player, StructureGuide> guideMap = new HashMap<>();
    @NonNull
    private CivSquaredFactoriesPlugin plugin;

    public void initialize() {
        this.plugin.getServer().getScheduler().runTaskTimer(this.plugin, () -> {
            for (final var es : this.guideMap.entrySet())
                this.updateGuide(es.getKey(), es.getValue());
        }, 0, 10);
    }

    public void createGuide(@NonNull Player player, @NonNull StructureModel model) {
        player.sendMessage("guide started");
        this.guideMap.put(player, new StructureGuide(model));
    }

    public void removeGuide(@NonNull Player player) {
        player.sendMessage("guide ended");
        this.guideMap.remove(player);
    }

    public boolean hasGuide(@NonNull Player player) {
        return this.guideMap.containsKey(player);
    }

    private void updateGuide(@NonNull Player player, @NonNull StructureGuide guide) {

        new ParticleBuilder(Particle.WHITE_SMOKE)
            .receivers(player)
            .color(Color.RED)
            .count(4)
            .location();
    }
}
