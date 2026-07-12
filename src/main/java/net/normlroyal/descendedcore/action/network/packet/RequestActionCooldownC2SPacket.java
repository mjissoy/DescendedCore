package net.normlroyal.descendedcore.action.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;
import net.normlroyal.descendedcore.action.ActionCooldowns;
import net.normlroyal.descendedcore.action.ActionRegistry;
import net.normlroyal.descendedcore.network.CoreNetwork;
import net.normlroyal.descendedcore.network.NetworkContextHelper;

import java.util.function.Supplier;

public record RequestActionCooldownC2SPacket(ResourceLocation actionId) {
    public static void encode(RequestActionCooldownC2SPacket message, FriendlyByteBuf buf) {
        buf.writeResourceLocation(message.actionId());
    }

    public static RequestActionCooldownC2SPacket decode(FriendlyByteBuf buf) {
        return new RequestActionCooldownC2SPacket(buf.readResourceLocation());
    }

    public static void handle(RequestActionCooldownC2SPacket message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkContextHelper.enqueueServer(contextSupplier, player ->
                ActionRegistry.get(message.actionId()).ifPresent(registered -> {
                    if (!registered.definition().canUse(player)) {
                        return;
                    }
                    var snapshot = ActionCooldowns.snapshot(player, registered.definition());
                    CoreNetwork.sendToPlayer(
                            new ActionCooldownS2CPacket(message.actionId(), snapshot.until(), snapshot.totalTicks()),
                            player
                    );
                })
        );
    }
}
