package net.normlroyal.descendedcore.client.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SkeletonRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.normlroyal.descendedcore.DescendedCore;

public class VoidSkeletonAnomalyRenderer extends SkeletonRenderer {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(DescendedCore.MOD_ID, "textures/entity/void_anomaly_skeleton.png");

    public VoidSkeletonAnomalyRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(AbstractSkeleton entity) {
        return TEXTURE;
    }
}
