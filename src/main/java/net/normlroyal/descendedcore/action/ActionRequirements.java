package net.normlroyal.descendedcore.action;

import net.minecraft.network.chat.Component;

import java.util.Arrays;

public final class ActionRequirements {
    private ActionRequirements() {
    }

    public static ActionRequirement all(ActionRequirement... requirements) {
        return (player, action) -> Arrays.stream(requirements)
                .map(requirement -> requirement.test(player, action))
                .filter(check -> !check.allowed())
                .findFirst()
                .orElse(ActionCheck.ALLOW);
    }

    public static ActionRequirement persistentFlag(String key, Component failureMessage) {
        return (player, action) -> player.getPersistentData().getBoolean(key)
                ? ActionCheck.ALLOW
                : ActionCheck.deny(failureMessage);
    }
}
