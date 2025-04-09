package lucraft.mods.pymtech.client.render.entities;

import lucraft.mods.pymtech.entities.EntityPymParticleDisc;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class RenderPymParticleDisc extends Render<EntityPymParticleDisc> {

	public RenderPymParticleDisc(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	public void doRender(EntityPymParticleDisc entity, double x, double y, double z, float entityYaw, float partialTicks) {
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) x, (float) y, (float) z);
		GlStateManager.enableRescaleNormal();
		GlStateManager.scale(0.5F, 0.5F, 0.5F);
		GlStateManager.rotate(-this.renderManager.playerViewY, 5.0F, 1.0F, 0.0F);
		GlStateManager.rotate(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
		Minecraft.getMinecraft().getRenderItem().renderItem(entity.getItem(), ItemCameraTransforms.TransformType.GROUND);
		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

	@Nullable
	@Override
	protected ResourceLocation getEntityTexture(EntityPymParticleDisc entity) {
		return null;
	}
}
