package net.normlroyal.descendedcore.compat;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.MissingMappingsEvent;
import net.normlroyal.descendedcore.DescendedCore;
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
            Map.entry("void_slime_anomaly_spawn_egg", CoreItems.VOID_SLIME_ANOMALY_SPAWN_EGG)
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
        for (MissingMappingsEvent.Mapping<Item> mapping : event.getMappings(ForgeRegistries.Keys.ITEMS, OLD_MOD_ID)) {
            Supplier<? extends Item> target = ITEM_REMAPS.get(mapping.getKey().getPath());
            if (target != null) {
                mapping.remap(target.get());
            }
        }

        for (MissingMappingsEvent.Mapping<EntityType<?>> mapping : event.getMappings(ForgeRegistries.Keys.ENTITY_TYPES, OLD_MOD_ID)) {
            Supplier<? extends EntityType<?>> target = ENTITY_REMAPS.get(mapping.getKey().getPath());
            if (target != null) {
                mapping.remap(target.get());
            }
        }

        for (MissingMappingsEvent.Mapping<ParticleType<?>> mapping : event.getMappings(ForgeRegistries.Keys.PARTICLE_TYPES, OLD_MOD_ID)) {
            Supplier<? extends ParticleType<?>> target = PARTICLE_REMAPS.get(mapping.getKey().getPath());
            if (target != null) {
                mapping.remap(target.get());
            }
        }
    }
}
