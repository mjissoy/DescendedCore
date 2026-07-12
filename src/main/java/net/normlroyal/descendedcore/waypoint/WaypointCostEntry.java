package net.normlroyal.descendedcore.waypoint;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public record WaypointCostEntry(ResourceLocation itemId, int amount) {
    public WaypointCostEntry {
        if (amount < 0 || amount > 1_000_000) {
            throw new IllegalArgumentException("Invalid waypoint cost amount: " + amount);
        }
    }

    public static WaypointCostEntry read(FriendlyByteBuf buf) {
        return new WaypointCostEntry(buf.readResourceLocation(), buf.readVarInt());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeResourceLocation(itemId);
        buf.writeVarInt(amount);
    }

    public Item item() {
        return BuiltInRegistries.ITEM.getOptional(itemId).orElse(null);
    }

    public ItemStack stack() {
        Item item = item();
        return item == null ? ItemStack.EMPTY : new ItemStack(item);
    }
}
