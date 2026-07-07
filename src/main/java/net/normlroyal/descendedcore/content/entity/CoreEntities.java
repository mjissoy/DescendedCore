package net.normlroyal.descendedcore.content.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.normlroyal.descendedcore.DescendedCore;

public final class CoreEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, DescendedCore.MOD_ID);

    public static final RegistryObject<EntityType<VoidAnomalyEntity>> VOID_ANOMALY =
            ENTITY_TYPES.register("void_anomaly",
                    () -> EntityType.Builder
                            .of(VoidAnomalyEntity::new, MobCategory.MONSTER)
                            .sized(0.6F, 1.95F)
                            .clientTrackingRange(8)
                            .build("void_anomaly"));

    public static final RegistryObject<EntityType<VoidSkeletonAnomalyEntity>> VOID_SKELETON_ANOMALY =
            ENTITY_TYPES.register("void_skeleton_anomaly",
                    () -> EntityType.Builder
                            .of(VoidSkeletonAnomalyEntity::new, MobCategory.MONSTER)
                            .sized(0.6F, 1.99F)
                            .clientTrackingRange(8)
                            .build("void_skeleton_anomaly"));

    public static final RegistryObject<EntityType<VoidSlimeAnomalyEntity>> VOID_SLIME_ANOMALY =
            ENTITY_TYPES.register("void_slime_anomaly",
                    () -> EntityType.Builder
                            .of(VoidSlimeAnomalyEntity::new, MobCategory.MONSTER)
                            .sized(0.52F, 0.52F)
                            .clientTrackingRange(8)
                            .build("void_slime_anomaly"));

    public static final RegistryObject<EntityType<ImpEntity>> IMP =
            ENTITY_TYPES.register("imp",
                    () -> EntityType.Builder
                            .of(ImpEntity::new, MobCategory.MONSTER)
                            .sized(0.375F, 0.8125F)
                            .clientTrackingRange(16)
                            .build("imp"));

    private CoreEntities() {
    }
}
