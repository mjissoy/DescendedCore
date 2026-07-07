package net.normlroyal.descendedcore.client.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Zombie;
import net.normlroyal.descendedcore.DescendedCore;

public class VoidAnomalyRenderer extends ZombieRenderer {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(DescendedCore.MOD_ID, "textures/entity/void_anomaly.png");

    public VoidAnomalyRenderer(EntityRendererProvider.Context p_174456_) {
        super(p_174456_);
    }

    @Override
    public ResourceLocation getTextureLocation(Zombie entity) {
        return TEXTURE;
    }
}