package net.normlroyal.descendedcore.action.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;
import net.normlroyal.descendedcore.action.ClientActionCooldowns;
import net.normlroyal.descendedcore.network.NetworkContextHelper;

import java.util.function.Supplier;

public record ActionCooldownS2CPacket(ResourceLocation actionId, long until, int totalTicks) {
    public static void encode(ActionCooldownS2CPacket message, FriendlyByteBuf buf) {
        buf.writeResourceLocation(message.actionId());
        buf.writeLong(message.until());
        buf.writeVarInt(message.totalTicks());
    }

    public static ActionCooldownS2CPacket decode(FriendlyByteBuf buf) {
        return new ActionCooldownS2CPacket(buf.readResourceLocation(), buf.readLong(), buf.readVarInt());
    }

    public static void handle(ActionCooldownS2CPacket message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkContextHelper.enqueue(contextSupplier, () ->
                ClientActionCooldowns.set(message.actionId(), message.until(), message.totalTicks())
        );
    }
}
