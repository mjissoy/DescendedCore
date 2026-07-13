package net.normlroyal.descendedcore.content.item.custom;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.normlroyal.descendedcore.DescendedCore;
import net.normlroyal.descendedcore.void_pocket.VoidPocketManager;

public class VoidHeartLinkItem extends Item {
    private static final ResourceLocation ENTRY_ORIGIN =
            new ResourceLocation(DescendedCore.MOD_ID, "void_heart_link");

    public VoidHeartLinkItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide) {
            return InteractionResultHolder.success(stack);
        }

        if (player instanceof ServerPlayer serverPlayer) {
            if (VoidPocketManager.isVoidPocket(level)) {
                VoidPocketManager.exitWithVoidHeart(serverPlayer);
            } else {
                VoidPocketManager.enterNewPocket(serverPlayer, ENTRY_ORIGIN);
            }

            if (!serverPlayer.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }

        return InteractionResultHolder.consume(stack);
    }
}
