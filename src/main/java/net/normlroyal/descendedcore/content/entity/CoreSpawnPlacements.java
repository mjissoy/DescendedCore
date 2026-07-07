package net.normlroyal.descendedcore.content.entity;

import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.normlroyal.descendedcore.DescendedCore;

@Mod.EventBusSubscriber(modid = DescendedCore.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class CoreSpawnPlacements {
    private CoreSpawnPlacements() {
    }

    @SubscribeEvent
    public static void registerSpawnPlacements(SpawnPlacementRegisterEvent event) {
        event.register(
                CoreEntities.VOID_ANOMALY.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                VoidAnomalyEntity::canSpawnHere,
                SpawnPlacementRegisterEvent.Operation.OR
        );
        event.register(
                CoreEntities.IMP.get(),
                SpawnPlacements.Type.NO_RESTRICTIONS,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                ImpEntity::canSpawnHere,
                SpawnPlacementRegisterEvent.Operation.OR
        );
    }
}
