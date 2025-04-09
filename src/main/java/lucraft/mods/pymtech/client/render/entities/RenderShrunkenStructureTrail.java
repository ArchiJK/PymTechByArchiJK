package lucraft.mods.pymtech.client.render.entities;

import lucraft.mods.lucraftcore.superpowers.render.SuperpowerRenderLayer;
import lucraft.mods.pymtech.entities.EntityShrunkenStructureTrail;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

import javax.annotation.Nullable;

public class RenderShrunkenStructureTrail extends Render<EntityShrunkenStructureTrail> {

    public RenderShrunkenStructureTrail(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(EntityShrunkenStructureTrail entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_CONSTANT_ALPHA, GL11.GL_ONE_MINUS_CONSTANT_ALPHA);
        float alpha = 1F - ((float) entity.ticksExisted / (float) entity.lifeTime);
        GL14.glBlendColor(1, 1, 1, alpha / 1.4F);
        this.bindTexture(SuperpowerRenderLayer.WHITE_TEX);
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);

        RenderShrunkenStructure.storeAndRender(entity.world, entity.getPosition(), entity.parent.shrunkenStructure, partialTicks, entity.scale, true);
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
        GL14.glBlendColor(1, 1, 1, alpha / 1.6F);
        RenderShrunkenStructure.storeAndRender(entity.world, entity.getPosition(), entity.parent.shrunkenStructure, partialTicks, entity.scale, true);
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityShrunkenStructureTrail entity) {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }

}
