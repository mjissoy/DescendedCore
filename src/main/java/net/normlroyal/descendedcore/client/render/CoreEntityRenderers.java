package net.normlroyal.descendedcore.client.render;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.normlroyal.descendedcore.DescendedCore;
import net.normlroyal.descendedcore.content.entity.CoreEntities;

@Mod.EventBusSubscriber(modid = DescendedCore.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class CoreEntityRenderers {
    private CoreEntityRenderers() {
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(CoreEntities.VOID_ANOMALY.get(), VoidAnomalyRenderer::new);
        event.registerEntityRenderer(CoreEntities.VOID_SKELETON_ANOMALY.get(), VoidSkeletonAnomalyRenderer::new);
        event.registerEntityRenderer(CoreEntities.VOID_SLIME_ANOMALY.get(), VoidSlimeAnomalyRenderer::new);
        event.registerEntityRenderer(CoreEntities.IMP.get(), ImpRenderer::new);
    }
}
