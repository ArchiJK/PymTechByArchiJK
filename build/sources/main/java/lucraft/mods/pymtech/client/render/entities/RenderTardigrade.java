package lucraft.mods.pymtech.client.render.entities;

import lucraft.mods.pymtech.PymTech;
import lucraft.mods.pymtech.client.models.ModelTardigrade;
import lucraft.mods.pymtech.entities.EntityTardigrade;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class RenderTardigrade extends RenderLiving<EntityTardigrade> {

    public static ResourceLocation TEXTURE = new ResourceLocation(PymTech.MODID, "textures/models/tardigrade.png");

    public RenderTardigrade(RenderManager rendermanagerIn) {
        super(rendermanagerIn, new ModelTardigrade(), 1F);
    }

    @Override
    protected void preRenderCallback(EntityTardigrade entitylivingbaseIn, float partialTickTime) {
        float f = 1F;
        GlStateManager.translate(0, 1F, 0);
        GlStateManager.scale(f, f, f);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityTardigrade entity) {
        return TEXTURE;
    }
}
