package lucraft.mods.pymtech.client.gui;

import lucraft.mods.pymtech.PymTech;
import lucraft.mods.pymtech.dimensions.PTDimensions;
import lucraft.mods.pymtech.network.MessagePTInfoToServer;
import lucraft.mods.pymtech.network.PTPacketDispatcher;
import lucraft.mods.pymtech.sounds.PTSoundEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

public class GuiQRTransition extends GuiScreen {

    public static ResourceLocation TEXTURE_1 = new ResourceLocation(PymTech.MODID, "textures/gui/qr_transition_1.png");
    public static ResourceLocation TEXTURE_2 = new ResourceLocation(PymTech.MODID, "textures/gui/qr_transition_2.png");

    public int progress;

    public GuiQRTransition() {
    }

    @Override
    public void initGui() {
        super.initGui();
        EntityPlayer player = Minecraft.getMinecraft().player;
        SoundEvent soundEvent = PTDimensions.isQuantumDimension(player.dimension) ? PTSoundEvents.QUANTUM_REALM_LEAVE : PTSoundEvents.QUANTUM_REALM_ENTER;
        player.world.playSound(player.posX, player.posY + player.eyeHeight, player.posZ, soundEvent, player.getSoundCategory(), 1, 1, false);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.pushMatrix();
        ScaledResolution resolution = new ScaledResolution(mc);

        if (progress > 20) {
            float scale = (progress - 20F) / (PTDimensions.maxTransitionTime - 20);
            GlStateManager.disableTexture2D();
            drawBackground(scale, new Vec3i(100, 70, 0), new Vec3i(23, 15, 0), resolution);
            GlStateManager.enableTexture2D();
            scale *= scale;
            if (PTDimensions.isQuantumDimension(mc.player.dimension))
                scale = 1F - scale;
            this.drawScalingImage(TEXTURE_1, 1920, 1080, 1F + scale, resolution);
            this.drawScalingImage(TEXTURE_2, 1920, 1080, 0.7F + scale * 0.7F, resolution);
        }

        GlStateManager.disableTexture2D();

        if (progress <= 40) {
            float alpha = (MathHelper.sin((float) (((float) progress / 40F) * Math.PI)) + 1F) / 2F;
            drawBackground(255, 255, 255, (int) (alpha * 255), resolution);
        }

        if (progress >= PTDimensions.maxTransitionTime - 20) {
            float alpha = (progress - PTDimensions.maxTransitionTime + 20F) / 20F;
            drawBackground(255, 255, 255, (int) (alpha * 255), resolution);
        }

        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        PTPacketDispatcher.sendToServer(new MessagePTInfoToServer(MessagePTInfoToServer.InfoType.ENTER_QUANTUM_DIMENSION, this.progress));
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        progress++;
        if (progress >= PTDimensions.maxTransitionTime)
            mc.player.closeScreen();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        mc.player.closeScreen();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public void drawScalingImage(ResourceLocation location, int width, int height, float scale, ScaledResolution resolution) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(resolution.getScaledWidth() / 2, resolution.getScaledHeight() / 2, 0);
        GlStateManager.scale(scale, scale, 1);
        width = (int) (resolution.getScaledWidth() * scale);
        height = (int) (resolution.getScaledHeight() * scale);
        GlStateManager.translate(-width / 2, -height / 2, 0);
        this.mc.renderEngine.bindTexture(location);
        GlStateManager.scale((width / 256F), (height / 256F), 1);
        this.drawTexturedModalRect(0, 0, 0, 0, 256, 256);
        GlStateManager.popMatrix();
    }

    public void drawBackground(float progress, Vec3i innerColor, Vec3i outerColor, ScaledResolution resolution) {
        Tessellator tes = Tessellator.getInstance();
        BufferBuilder bb = tes.getBuffer();

        Vec3i outerBG = new Vec3i(innerColor.getX() - (innerColor.getX() - outerColor.getX()) * (1F - progress), innerColor.getY() - (innerColor.getY() - outerColor.getY()) * (1F - progress), innerColor.getZ() - (innerColor.getZ() - outerColor.getZ()) * (1F - progress));
        GlStateManager.disableTexture2D();
        GlStateManager.disableFog();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.disableLighting();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        GlStateManager.disableCull();

        bb.begin(GL11.GL_POLYGON, DefaultVertexFormats.POSITION_COLOR);
        bb.pos(0, 0, 0).color(outerBG.getX(), outerBG.getY(), outerBG.getZ(), 255).endVertex();
        bb.pos(resolution.getScaledWidth(), 0, 0).color(outerBG.getX(), outerBG.getY(), outerBG.getZ(), 255).endVertex();
        bb.pos(resolution.getScaledWidth() / 2, resolution.getScaledHeight() / 2, 0).color(innerColor.getX(), innerColor.getY(), innerColor.getZ(), 255).endVertex();
        tes.draw();
        bb.begin(GL11.GL_POLYGON, DefaultVertexFormats.POSITION_COLOR);
        bb.pos(0, resolution.getScaledHeight(), 0).color(outerBG.getX(), outerBG.getY(), outerBG.getZ(), 255).endVertex();
        bb.pos(resolution.getScaledWidth(), resolution.getScaledHeight(), 0).color(outerBG.getX(), outerBG.getY(), outerBG.getZ(), 255).endVertex();
        bb.pos(resolution.getScaledWidth() / 2, resolution.getScaledHeight() / 2, 0).color(innerColor.getX(), innerColor.getY(), innerColor.getZ(), 255).endVertex();
        tes.draw();
        bb.begin(GL11.GL_POLYGON, DefaultVertexFormats.POSITION_COLOR);
        bb.pos(0, 0, 0).color(outerBG.getX(), outerBG.getY(), outerBG.getZ(), 255).endVertex();
        bb.pos(0, resolution.getScaledHeight(), 0).color(outerBG.getX(), outerBG.getY(), outerBG.getZ(), 255).endVertex();
        bb.pos(resolution.getScaledWidth() / 2, resolution.getScaledHeight() / 2, 0).color(innerColor.getX(), innerColor.getY(), innerColor.getZ(), 255).endVertex();
        tes.draw();
        bb.begin(GL11.GL_POLYGON, DefaultVertexFormats.POSITION_COLOR);
        bb.pos(resolution.getScaledWidth(), 0, 0).color(outerBG.getX(), outerBG.getY(), outerBG.getZ(), 255).endVertex();
        bb.pos(resolution.getScaledWidth(), resolution.getScaledHeight(), 0).color(outerBG.getX(), outerBG.getY(), outerBG.getZ(), 255).endVertex();
        bb.pos(resolution.getScaledWidth() / 2, resolution.getScaledHeight() / 2, 0).color(innerColor.getX(), innerColor.getY(), innerColor.getZ(), 255).endVertex();
        tes.draw();

        GlStateManager.disableRescaleNormal();
        GlStateManager.enableLighting();
        GlStateManager.enableTexture2D();
        GlStateManager.enableAlpha();
    }

    public void drawBackground(int red, int green, int blue, int alpha, ScaledResolution resolution) {
        Tessellator tes = Tessellator.getInstance();
        BufferBuilder bb = tes.getBuffer();

        GlStateManager.disableTexture2D();
        GlStateManager.disableFog();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.disableLighting();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        GlStateManager.disableCull();
        GlStateManager.color(red / 255F, green / 255F, blue / 255F, alpha / 255F);

        bb.begin(GL11.GL_POLYGON, DefaultVertexFormats.POSITION);
        bb.pos(0, 0, 0).endVertex();
        bb.pos(resolution.getScaledWidth(), 0, 0).endVertex();
        bb.pos(resolution.getScaledWidth(), resolution.getScaledHeight(), 0).endVertex();
        bb.pos(0, resolution.getScaledHeight(), 0).endVertex();
        tes.draw();

        GlStateManager.disableRescaleNormal();
        GlStateManager.enableLighting();
        GlStateManager.enableTexture2D();
        GlStateManager.enableAlpha();
    }

}
