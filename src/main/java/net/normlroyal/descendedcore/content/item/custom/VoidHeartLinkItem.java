package net.normlroyal.descendedcore.content.item.custom;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.normlroyal.descendedcore.util.VoidHeartLinkHooks;

public class VoidHeartLinkItem extends Item {
    public VoidHeartLinkItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        return VoidHeartLinkHooks.use(level, player, hand, stack);
    }
}
