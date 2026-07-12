package net.normlroyal.descendedcore.action;

import net.minecraft.server.level.ServerPlayer;

@FunctionalInterface
public interface ActionRequirement {
    ActionCheck test(ServerPlayer player, ActionDefinition action);
}
