package net.normlroyal.descendedcore.content.void_touched;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.normlroyal.descendedcore.Config;
import net.normlroyal.descendedcore.DescendedCore;
import net.normlroyal.descendedcore.content.item.CoreItems;
import net.normlroyal.descendedcore.content.particle.CoreParticles;

@Mod.EventBusSubscriber(modid = DescendedCore.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class VoidTouchedEvents {
    private VoidTouchedEvents() {
    }

    @SubscribeEvent
    public static void onJoinLevel(EntityJoinLevelEvent event) {
        if (!(event.getLevel() instanceof ServerLevel)) {
            return;
        }
        if (!(event.getEntity() instanceof LivingEntity living)) {
            return;
        }

        double chance = VoidTouchedSpawnChanceModifiers.modify(
                living,
                Config.COMMON.voidTouchedSpawnChance.get()
        );
        VoidTouchedData.tryApplyNaturally(living, chance);
    }

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();

        if (!(entity.level() instanceof ServerLevel level)
                || !VoidTouchedData.isVoidTouched(entity)
                || entity.tickCount % 20 != 0) {
            return;
        }

        level.sendParticles(
                CoreParticles.VOID_TOUCHED.get(),
                entity.getX(),
                entity.getY() + entity.getBbHeight() * 0.55D,
                entity.getZ(),
                5,
                entity.getBbWidth() * 0.25D,
                entity.getBbHeight() * 0.25D,
                entity.getBbWidth() * 0.25D,
                0.005D
        );
    }

    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        LivingEntity dead = event.getEntity();

        if (!(dead.level() instanceof ServerLevel level)
                || !VoidTouchedData.isVoidTouched(dead)) {
            return;
        }

        int looting = 0;
        Entity attacker = event.getSource().getEntity();

        if (attacker instanceof Player player) {
            looting = EnchantmentHelper.getEnchantments(player.getMainHandItem())
                    .getOrDefault(Enchantments.MOB_LOOTING, 0);
        }

        int count = 1 + dead.getRandom().nextInt(looting + 1);
        ItemStack drop = new ItemStack(CoreItems.VOID_TEAR.get(), count);
        drop = VoidTouchedDropModifiers.modify(dead, event.getSource(), looting, drop);

        if (!drop.isEmpty()) {
            event.getDrops().add(new ItemEntity(
                    level,
                    dead.getX(),
                    dead.getY(),
                    dead.getZ(),
                    drop
            ));
        }
    }
}
