package net.normlroyal.descendedcore.action;

import net.minecraft.server.level.ServerPlayer;

public record ActionContext(ServerPlayer player, ActionDefinition action, long gameTime) {
    public static ActionContext create(ServerPlayer player, ActionDefinition action) {
        return new ActionContext(player, action, player.level().getGameTime());
    }
}
