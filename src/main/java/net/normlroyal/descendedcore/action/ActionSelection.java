package net.normlroyal.descendedcore.action;

import net.minecraft.world.entity.player.Player;

import java.util.List;

public final class ActionSelection<T extends ActionDefinition> {
    private int index;

    public T get(Player player, List<T> candidates) {
        List<T> visible = visible(player, candidates);
        if (visible.isEmpty()) {
            index = 0;
            return null;
        }
        if (index < 0 || index >= visible.size()) {
            index = 0;
        }
        return visible.get(index);
    }

    public void cycle(Player player, List<T> candidates) {
        List<T> visible = visible(player, candidates);
        if (visible.isEmpty()) {
            index = 0;
            return;
        }
        index = (index + 1) % visible.size();
    }

    public void clamp(Player player, List<T> candidates) {
        int size = visible(player, candidates).size();
        if (size == 0 || index >= size) {
            index = 0;
        }
    }

    public void reset() {
        index = 0;
    }

    public int index() {
        return index;
    }

    private List<T> visible(Player player, List<T> candidates) {
        if (player == null) {
            return List.of();
        }
        return candidates.stream().filter(action -> action.isVisible(player)).toList();
    }
}
