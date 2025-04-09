package lucraft.mods.pymtech.client.render.tileentities;

import lucraft.mods.lucraftcore.util.helper.LCRenderHelper;
import lucraft.mods.pymtech.tileentities.TileEntityStructureShrinker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class TESRStructureShrinker extends TileEntitySpecialRenderer<TileEntityStructureShrinker> {

	@Override
	public void render(TileEntityStructureShrinker te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
	    if(!te.showBox)
	        return;
		BlockPos area = te.area;
		Tessellator tes = Tessellator.getInstance();
		BufferBuilder buffer = tes.getBuffer();

		GlStateManager.pushMatrix();
		GlStateManager.depthMask(false);
		GlStateManager.disableTexture2D();
		GlStateManager.disableLighting();
		LCRenderHelper.setLightmapTextureCoords(240, 240);
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.translate(x, y, z);

		float a = 0.15F + (MathHelper.sin((Minecraft.getMinecraft().player.ticksExisted + partialTicks) / 10F) + 1F) / 8F;
		RenderGlobal.renderFilledBox(1.05F, 0.05F, 1.05F, area.getX() + 0.95F, area.getY() - 0.05F, area.getZ() + 0.95F, 1.0F, 0F, 0F, a);
		RenderGlobal.drawBoundingBox(1.05F, 0.05F, 1.05F, area.getX() + 0.95F, area.getY() - 0.05F, area.getZ() + 0.95F, 0F, 0F, 0F, 1F);

		LCRenderHelper.restoreLightmapTextureCoords();
		GlStateManager.enableTexture2D();
		GlStateManager.enableLighting();
		GlStateManager.enableCull();
		GlStateManager.disableBlend();
		GlStateManager.depthMask(true);
		GlStateManager.popMatrix();
	}

}
