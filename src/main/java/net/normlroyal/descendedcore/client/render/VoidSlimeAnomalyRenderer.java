package net.normlroyal.descendedcore.client.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SlimeRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Slime;
import net.normlroyal.descendedcore.DescendedCore;

public class VoidSlimeAnomalyRenderer extends SlimeRenderer {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(DescendedCore.MOD_ID, "textures/entity/void_anomaly_slime.png");

    public VoidSlimeAnomalyRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(Slime entity) {
        return TEXTURE;
    }
}
