package net.normlroyal.descendedcore.content.item;

import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.normlroyal.descendedcore.DescendedCore;
import net.normlroyal.descendedcore.content.entity.CoreEntities;
import net.normlroyal.descendedcore.content.item.custom.VoidHeartLinkItem;

public final class CoreItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, DescendedCore.MOD_ID);

    public static final RegistryObject<Item> VOID_TEAR = ITEMS.register("void_tear",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> COMPRESSED_VOID = ITEMS.register("compressed_void",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> VOID_MATRIX = ITEMS.register("void_matrix",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> VOID_HEART_LINK = ITEMS.register("void_heart_link",
            () -> new VoidHeartLinkItem(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> DEMON_HEART = ITEMS.register("demon_heart",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> IMP_SPAWN_EGG = ITEMS.register("imp_spawn_egg",
            () -> new ForgeSpawnEggItem(CoreEntities.IMP,
                    0x7A1C1C,
                    0xD46B2E,
                    new Item.Properties()));
    public static final RegistryObject<Item> VOID_ANOMALY_SPAWN_EGG = ITEMS.register("void_anomaly_spawn_egg",
            () -> new ForgeSpawnEggItem(CoreEntities.VOID_ANOMALY,
                    0x320F3B,
                    0x6780AC,
                    new Item.Properties()));
    public static final RegistryObject<Item> VOID_SKELETON_ANOMALY_SPAWN_EGG = ITEMS.register("void_skeleton_anomaly_spawn_egg",
            () -> new ForgeSpawnEggItem(CoreEntities.VOID_SKELETON_ANOMALY,
                    0x1D0B24,
                    0x8B95C7,
                    new Item.Properties()));
    public static final RegistryObject<Item> VOID_SLIME_ANOMALY_SPAWN_EGG = ITEMS.register("void_slime_anomaly_spawn_egg",
            () -> new ForgeSpawnEggItem(CoreEntities.VOID_SLIME_ANOMALY,
                    0x190826,
                    0x5BA6B8,
                    new Item.Properties()));

    private CoreItems() {
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
