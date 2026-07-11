package net.normlroyal.descendedcore.content.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.normlroyal.descendedcore.DescendedCore;
import net.normlroyal.descendedcore.content.block.void_decorations.VoidGrassBlock;
import net.normlroyal.descendedcore.content.block.void_decorations.VoidPointedDripstoneBlock;
import net.normlroyal.descendedcore.content.block.void_decorations.VoidPoppyBlock;
import net.normlroyal.descendedcore.content.block.void_decorations.VoidVineBlock;
import net.normlroyal.descendedcore.content.block.void_decorations.VoidVinePlantBlock;
import net.normlroyal.descendedcore.content.item.CoreItems;

import java.util.function.Supplier;

public final class CoreBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, DescendedCore.MOD_ID);

    public static final RegistryObject<Block> VOID_CAVE_WALL = registerBlock(
            "void_cave_wall",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE))
    );

    public static final RegistryObject<Block> VOID_WALL_BRICKS = registerBlock(
            "void_wall_bricks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_BRICKS))
    );

    public static final RegistryObject<Block> SMOOTH_VOID_WALL = registerBlock(
            "smooth_void_wall",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.POLISHED_DEEPSLATE))
    );

    public static final RegistryObject<Block> VOID_GRASS = registerBlock(
            "void_grass",
            () -> new VoidGrassBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PURPLE)
                    .noCollission()
                    .instabreak()
                    .sound(SoundType.GRASS)
                    .offsetType(BlockBehaviour.OffsetType.XZ))
    );

    public static final RegistryObject<Block> VOID_VINE = registerBlock(
            "void_vine",
            () -> new VoidVineBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PURPLE)
                    .noCollission()
                    .randomTicks()
                    .instabreak()
                    .sound(SoundType.WEEPING_VINES))
    );

    public static final RegistryObject<Block> VOID_VINE_PLANT = BLOCKS.register(
            "void_vine_plant",
            () -> new VoidVinePlantBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PURPLE)
                    .noCollission()
                    .instabreak()
                    .sound(SoundType.WEEPING_VINES))
    );

    public static final RegistryObject<Block> VOID_POINTED_DRIPSTONE = registerBlock(
            "void_pointed_dripstone",
            () -> new VoidPointedDripstoneBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PURPLE)
                    .noOcclusion()
                    .strength(1.5F)
                    .sound(SoundType.POINTED_DRIPSTONE)
                    .randomTicks())
    );

    public static final RegistryObject<Block> VOID_POPPY = registerBlock(
            "void_poppy",
            () -> new VoidPoppyBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PURPLE)
                    .noCollission()
                    .instabreak()
                    .lightLevel(state -> 5)
                    .sound(SoundType.GRASS)
                    .offsetType(BlockBehaviour.OffsetType.XZ))
    );

    private CoreBlocks() {
    }

    private static <T extends Block> RegistryObject<T> registerBlock(
            String name,
            Supplier<T> supplier
    ) {
        RegistryObject<T> block = BLOCKS.register(name, supplier);
        CoreItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
        return block;
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
