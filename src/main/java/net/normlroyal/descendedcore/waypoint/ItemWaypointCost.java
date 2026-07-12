package net.normlroyal.descendedcore.waypoint;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public record ItemWaypointCost(List<WaypointCostEntry> entries) {
    public static final ItemWaypointCost FREE = new ItemWaypointCost(List.of());
    private static final int MAX_NETWORK_ENTRIES = 32;

    public ItemWaypointCost {
        entries = List.copyOf(entries.stream().filter(entry -> entry.amount() > 0).toList());
    }

    public static Builder builder() {
        return new Builder();
    }

    public static ItemWaypointCost read(FriendlyByteBuf buf) {
        int size = buf.readVarInt();
        if (size < 0 || size > MAX_NETWORK_ENTRIES) {
            throw new IllegalArgumentException("Invalid waypoint cost entry count: " + size);
        }
        List<WaypointCostEntry> entries = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            entries.add(WaypointCostEntry.read(buf));
        }
        return new ItemWaypointCost(entries);
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeVarInt(entries.size());
        for (WaypointCostEntry entry : entries) {
            entry.write(buf);
        }
    }

    public boolean isFree() {
        return entries.isEmpty();
    }

    public boolean canAfford(ServerPlayer player) {
        if (player.isCreative()) {
            return true;
        }

        for (WaypointCostEntry entry : entries) {
            Item item = entry.item();
            if (item == null || count(player, item) < entry.amount()) {
                return false;
            }
        }
        return true;
    }

    public boolean consume(ServerPlayer player) {
        if (!canAfford(player)) {
            return false;
        }
        if (player.isCreative()) {
            return true;
        }

        for (WaypointCostEntry entry : entries) {
            Item item = entry.item();
            if (item != null) {
                remove(player, item, entry.amount());
            }
        }
        player.getInventory().setChanged();
        return true;
    }

    public String label() {
        if (isFree()) {
            return "Free";
        }

        StringBuilder builder = new StringBuilder();
        for (WaypointCostEntry entry : entries) {
            if (!builder.isEmpty()) {
                builder.append(", ");
            }
            ItemStack stack = entry.stack();
            String name = stack.isEmpty() ? entry.itemId().toString() : stack.getHoverName().getString();
            builder.append(entry.amount()).append(' ').append(name);
        }
        return builder.toString();
    }

    private static int count(ServerPlayer player, Item item) {
        int total = 0;
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.is(item)) {
                total += stack.getCount();
            }
        }
        return total;
    }

    private static void remove(ServerPlayer player, Item item, int amount) {
        int remaining = amount;
        for (int i = 0; i < player.getInventory().getContainerSize() && remaining > 0; i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (!stack.is(item)) {
                continue;
            }

            int taken = Math.min(remaining, stack.getCount());
            stack.shrink(taken);
            remaining -= taken;
        }
    }

    public static final class Builder {
        private final Map<ResourceLocation, Integer> costs = new LinkedHashMap<>();

        public Builder add(ItemLike itemLike, int amount) {
            if (amount <= 0) {
                return this;
            }
            Item item = itemLike.asItem();
            ResourceLocation id = Objects.requireNonNull(
                    BuiltInRegistries.ITEM.getKey(item),
                    "Cannot create waypoint cost for an unregistered item"
            );
            costs.merge(id, amount, Integer::sum);
            return this;
        }

        public Builder add(ResourceLocation itemId, int amount) {
            if (amount > 0) {
                costs.merge(itemId, amount, Integer::sum);
            }
            return this;
        }

        public ItemWaypointCost build() {
            List<WaypointCostEntry> entries = costs.entrySet().stream()
                    .map(entry -> new WaypointCostEntry(entry.getKey(), entry.getValue()))
                    .toList();
            return entries.isEmpty() ? FREE : new ItemWaypointCost(entries);
        }
    }
}
