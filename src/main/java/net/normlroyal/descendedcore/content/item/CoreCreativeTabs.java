package net.normlroyal.descendedcore.content.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.normlroyal.descendedcore.DescendedCore;

public final class CoreCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, DescendedCore.MOD_ID);

    public static final RegistryObject<CreativeModeTab> DESCENDED_CORE_TAB = CREATIVE_MODE_TABS.register("descendedcore_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(CoreItems.VOID_TEAR.get()))
                    .title(Component.translatable("creativetab.descendedcore"))
                    .displayItems((parameters, output) -> {
                        output.accept(CoreItems.VOID_TEAR.get());
                        output.accept(CoreItems.COMPRESSED_VOID.get());
                        output.accept(CoreItems.VOID_MATRIX.get());
                        output.accept(CoreItems.VOID_HEART_LINK.get());
                        output.accept(CoreItems.DEMON_HEART.get());
                        output.accept(CoreItems.IMP_SPAWN_EGG.get());
                        output.accept(CoreItems.VOID_ANOMALY_SPAWN_EGG.get());
                        output.accept(CoreItems.VOID_SKELETON_ANOMALY_SPAWN_EGG.get());
                        output.accept(CoreItems.VOID_SLIME_ANOMALY_SPAWN_EGG.get());
                    })
                    .build());

    private CoreCreativeTabs() {
    }

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
