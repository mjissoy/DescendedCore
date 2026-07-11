package net.normlroyal.descendedcore.compat;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.MissingMappingsEvent;
import net.normlroyal.descendedcore.DescendedCore;
import net.normlroyal.descendedcore.content.block.CoreBlocks;
import net.normlroyal.descendedcore.content.entity.CoreEntities;
import net.normlroyal.descendedcore.content.item.CoreItems;
import net.normlroyal.descendedcore.content.particle.CoreParticles;

import java.util.Map;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = DescendedCore.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class DescendedCoreMissingMappings {
    private static final String OLD_MOD_ID = "descendedangel";

    private static final Map<String, Supplier<? extends Item>> ITEM_REMAPS = Map.ofEntries(
            Map.entry("void_tear", CoreItems.VOID_TEAR),
            Map.entry("compressed_void", CoreItems.COMPRESSED_VOID),
            Map.entry("void_matrix", CoreItems.VOID_MATRIX),
            Map.entry("void_heart_link", CoreItems.VOID_HEART_LINK),
            Map.entry("demon_heart", CoreItems.DEMON_HEART),
            Map.entry("imp_spawn_egg", CoreItems.IMP_SPAWN_EGG),
            Map.entry("void_anomaly_spawn_egg", CoreItems.VOID_ANOMALY_SPAWN_EGG),
            Map.entry("void_skeleton_anomaly_spawn_egg", CoreItems.VOID_SKELETON_ANOMALY_SPAWN_EGG),
            Map.entry("void_slime_anomaly_spawn_egg", CoreItems.VOID_SLIME_ANOMALY_SPAWN_EGG),
            Map.entry("void_cave_wall", () -> CoreBlocks.VOID_CAVE_WALL.get().asItem()),
            Map.entry("void_wall_bricks", () -> CoreBlocks.VOID_WALL_BRICKS.get().asItem()),
            Map.entry("smooth_void_wall", () -> CoreBlocks.SMOOTH_VOID_WALL.get().asItem()),
            Map.entry("void_grass", () -> CoreBlocks.VOID_GRASS.get().asItem()),
            Map.entry("void_vine", () -> CoreBlocks.VOID_VINE.get().asItem()),
            Map.entry("void_pointed_dripstone", () -> CoreBlocks.VOID_POINTED_DRIPSTONE.get().asItem()),
            Map.entry("void_poppy", () -> CoreBlocks.VOID_POPPY.get().asItem())
    );

    private static final Map<String, Supplier<? extends Block>> BLOCK_REMAPS = Map.ofEntries(
            Map.entry("void_cave_wall", CoreBlocks.VOID_CAVE_WALL),
            Map.entry("void_wall_bricks", CoreBlocks.VOID_WALL_BRICKS),
            Map.entry("smooth_void_wall", CoreBlocks.SMOOTH_VOID_WALL),
            Map.entry("void_grass", CoreBlocks.VOID_GRASS),
            Map.entry("void_vine", CoreBlocks.VOID_VINE),
            Map.entry("void_vine_plant", CoreBlocks.VOID_VINE_PLANT),
            Map.entry("void_pointed_dripstone", CoreBlocks.VOID_POINTED_DRIPSTONE),
            Map.entry("void_poppy", CoreBlocks.VOID_POPPY)
    );

    private static final Map<String, Supplier<? extends EntityType<?>>> ENTITY_REMAPS = Map.ofEntries(
            Map.entry("imp", CoreEntities.IMP),
            Map.entry("void_anomaly", CoreEntities.VOID_ANOMALY),
            Map.entry("void_skeleton_anomaly", CoreEntities.VOID_SKELETON_ANOMALY),
            Map.entry("void_slime_anomaly", CoreEntities.VOID_SLIME_ANOMALY)
    );

    private static final Map<String, Supplier<? extends ParticleType<?>>> PARTICLE_REMAPS = Map.ofEntries(
            Map.entry("void_touched", CoreParticles.VOID_TOUCHED),
            Map.entry("blue_portal", CoreParticles.BLUE_PORTAL)
    );

    private DescendedCoreMissingMappings() {
    }

    @SubscribeEvent
    public static void onMissingMappings(MissingMappingsEvent event) {
        remap(event.getMappings(ForgeRegistries.Keys.ITEMS, OLD_MOD_ID), ITEM_REMAPS);
        remap(event.getMappings(ForgeRegistries.Keys.BLOCKS, OLD_MOD_ID), BLOCK_REMAPS);
        remap(event.getMappings(ForgeRegistries.Keys.ENTITY_TYPES, OLD_MOD_ID), ENTITY_REMAPS);
        remap(event.getMappings(ForgeRegistries.Keys.PARTICLE_TYPES, OLD_MOD_ID), PARTICLE_REMAPS);
    }

    private static <T> void remap(
            Iterable<MissingMappingsEvent.Mapping<T>> mappings,
            Map<String, Supplier<? extends T>> targets
    ) {
        for (MissingMappingsEvent.Mapping<T> mapping : mappings) {
            Supplier<? extends T> target = targets.get(mapping.getKey().getPath());
            if (target != null) {
                mapping.remap(target.get());
            }
        }
    }
}
