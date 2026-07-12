package net.normlroyal.descendedcore.action;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public interface ActionDefinition {
    ResourceLocation id();

    ResourceLocation category();

    default ResourceLocation icon() {
        return id();
    }

    default String cooldownKey() {
        return "descendedcore_action_cooldown_"
                + id().getNamespace()
                + "_"
                + id().getPath().replace('/', '_')
                + "_until";
    }

    default int cooldownTicks(ServerPlayer player) {
        return 0;
    }

    default boolean isVisible(Player player) {
        return true;
    }

    default boolean canUse(ServerPlayer player) {
        return isVisible(player);
    }
}
