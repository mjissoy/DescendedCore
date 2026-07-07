package net.normlroyal.descendedcore.content.voidheart;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public final class VoidHeartLinkHooks {
    private static UseHandler handler;

    private VoidHeartLinkHooks() {
    }

    public static void register(UseHandler useHandler) {
        handler = useHandler;
    }

    public static InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand, ItemStack stack) {
        if (handler == null) {
            return InteractionResultHolder.pass(stack);
        }
        return handler.use(level, player, hand, stack);
    }

    @FunctionalInterface
    public interface UseHandler {
        InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand, ItemStack stack);
    }
}
