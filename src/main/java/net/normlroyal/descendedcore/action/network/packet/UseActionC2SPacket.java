package net.normlroyal.descendedcore.action.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;
import net.normlroyal.descendedcore.action.ActionCooldowns;
import net.normlroyal.descendedcore.action.ActionRegistry;
import net.normlroyal.descendedcore.network.CoreNetwork;
import net.normlroyal.descendedcore.network.NetworkContextHelper;

import java.util.function.Supplier;

public record UseActionC2SPacket(ResourceLocation actionId) {
    public static void encode(UseActionC2SPacket message, FriendlyByteBuf buf) {
        buf.writeResourceLocation(message.actionId());
    }

    public static UseActionC2SPacket decode(FriendlyByteBuf buf) {
        return new UseActionC2SPacket(buf.readResourceLocation());
    }

    public static void handle(UseActionC2SPacket message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkContextHelper.enqueueServer(contextSupplier, player -> {
            ActionRegistry.tryUse(player, message.actionId());
            ActionRegistry.get(message.actionId()).ifPresent(registered -> {
                var snapshot = ActionCooldowns.snapshot(player, registered.definition());
                CoreNetwork.sendToPlayer(
                        new ActionCooldownS2CPacket(message.actionId(), snapshot.until(), snapshot.totalTicks()),
                        player
                );
            });
        });
    }
}
