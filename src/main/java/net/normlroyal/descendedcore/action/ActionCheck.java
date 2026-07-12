package net.normlroyal.descendedcore.action;

import net.minecraft.network.chat.Component;

public record ActionCheck(boolean allowed, Component message) {
    public static final ActionCheck ALLOW = new ActionCheck(true, Component.empty());

    public static ActionCheck deny(Component message) {
        return new ActionCheck(false, message);
    }
}
