package net.normlroyal.descendedcore.void_pocket.platform.forge;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.normlroyal.descendedcore.DescendedCore;
import net.normlroyal.descendedcore.content.entity.void_anomaly.VoidAnomaly;
import net.normlroyal.descendedcore.void_pocket.VoidPocketManager;

@Mod.EventBusSubscriber(modid = DescendedCore.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class VoidPocketForgeEvents {
    private VoidPocketForgeEvents() {
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void preventVoidPocketDeath(LivingHurtEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)
                || !VoidPocketManager.isVoidPocket(player.level())
                || player.getHealth() - event.getAmount() > 0.0F) {
            return;
        }

        event.setCanceled(true);
        player.setHealth(1.0F);
        VoidPocketManager.ejectPlayer(
                player,
                Component.literal("The void refuses your death and casts you out.")
                        .withStyle(ChatFormatting.DARK_PURPLE)
        );
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void preventVoidPocketDeathFallback(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)
                || !VoidPocketManager.isVoidPocket(player.level())) {
            return;
        }

        event.setCanceled(true);
        player.setHealth(1.0F);
        VoidPocketManager.ejectPlayer(
                player,
                Component.literal("The void refuses your death and casts you out.")
                        .withStyle(ChatFormatting.DARK_PURPLE)
        );
    }

    @SubscribeEvent
    public static void onVoidAnomalyKilled(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof VoidAnomaly anomaly)) {
            return;
        }

        Entity entity = event.getEntity();
        if (!(entity.level() instanceof ServerLevel level) || !VoidPocketManager.isVoidPocket(level)) {
            return;
        }

        if (event.getSource().getEntity() instanceof ServerPlayer player) {
            VoidPocketManager.recordAnomalyKill(
                    level,
                    entity.blockPosition(),
                    player,
                    anomaly.getVoidPocketKillValue()
            );
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END
                && !event.player.level().isClientSide
                && event.player instanceof ServerPlayer player) {
            VoidPocketManager.tickPlayer(player);
        }
    }
}
