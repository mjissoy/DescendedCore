package net.normlroyal.descendedcore.content.entity;

import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.normlroyal.descendedcore.DescendedCore;

@Mod.EventBusSubscriber(modid = DescendedCore.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class CoreEntityAttributes {
    private CoreEntityAttributes() {
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(CoreEntities.VOID_ANOMALY.get(),
                VoidAnomalyEntity.createAttributes().build());
        event.put(CoreEntities.VOID_SKELETON_ANOMALY.get(),
                VoidSkeletonAnomalyEntity.createAttributes().build());
        event.put(CoreEntities.VOID_SLIME_ANOMALY.get(),
                VoidSlimeAnomalyEntity.createAttributes().build());
        event.put(CoreEntities.IMP.get(),
                ImpEntity.createAttributes().build());
    }
}
