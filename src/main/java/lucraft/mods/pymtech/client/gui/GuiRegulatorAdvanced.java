package lucraft.mods.pymtech.client.gui;

import lucraft.mods.lucraftcore.LucraftCore;
import lucraft.mods.lucraftcore.infinity.render.ItemRendererInfinityGauntlet;
import lucraft.mods.lucraftcore.infinity.render.ModelInfinityGauntlet;
import lucraft.mods.lucraftcore.sizechanging.capabilities.CapabilitySizeChanging;
import lucraft.mods.lucraftcore.util.helper.LCMathHelper;
import lucraft.mods.lucraftcore.util.helper.StringHelper;
import lucraft.mods.pymtech.PymTech;
import lucraft.mods.pymtech.client.render.shrunkenstructure.ShrunkenStructureAccess;
import lucraft.mods.pymtech.container.ContainerRegulatorAdvanced;
import lucraft.mods.pymtech.network.MessageSetRegulatedSize;
import lucraft.mods.pymtech.network.PTPacketDispatcher;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiPageButtonList;
import net.minecraft.client.gui.GuiSlider;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

public class GuiRegulatorAdvanced extends GuiContainer implements GuiPageButtonList.GuiResponder {

    public static ResourceLocation TEXTURE = new ResourceLocation(PymTech.MODID, "textures/gui/regulator_advanced.png");
    public static ResourceLocation RED = new ResourceLocation(PymTech.MODID, "textures/gui/regulator_advanced_red.png");
    public static ResourceLocation BLUE = new ResourceLocation(PymTech.MODID, "textures/gui/regulator_advanced_blue.png");

    public GuiSlider slider;
    public ItemStack stack;

    public GuiRegulatorAdvanced(EntityPlayer player, ItemStack stack) {
        super(new ContainerRegulatorAdvanced(player, stack));
        this.xSize = 256;
        this.ySize = 224;
        this.stack = stack;
    }

    @Override
    public void initGui() {
        super.initGui();
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        float size = !stack.hasTagCompound() || stack.getTagCompound().getFloat("RegulatedSize") <= 0 ? 1F : stack.getTagCompound().getFloat("RegulatedSize");
        this.addButton(slider = new GuiSlider(this, 0, i + 24, j + 138, "size", 0.1F, 8F, size, (id, name, value) -> StringHelper.translateToLocal("pymtech.info.size_display", LCMathHelper.round(value, 2))));
        this.addButton(new GuiButtonExt(1, i + 177, j + 138, 55, 20, StringHelper.translateToLocal("pymtech.info.reset")));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);

        if (button.id == 1) {
            slider.setSliderValue(1F, true);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.mc.getTextureManager().bindTexture(TEXTURE);
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
        float f = this.slider.getSliderPosition();
        GlStateManager.color(1, 1, 1, f);
        this.mc.getTextureManager().bindTexture(BLUE);
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
        GlStateManager.color(1, 1, 1, 1F - f);
        this.mc.getTextureManager().bindTexture(RED);
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.disableBlend();

        float cachedSize = Minecraft.getMinecraft().player.getCapability(CapabilitySizeChanging.SIZE_CHANGING_CAP, null).getSize();
        Minecraft.getMinecraft().player.getCapability(CapabilitySizeChanging.SIZE_CHANGING_CAP, null).setSizeDirectly(1F);
        GlStateManager.pushMatrix();
        GlStateManager.translate(i + this.xSize / 2 + 30, j + 120, 0);
        float playerScale = MathHelper.clamp(this.slider.getSliderValue(), 0F, 1F) * 10;
        GlStateManager.scale(playerScale, playerScale, playerScale);
        GuiInventory.drawEntityOnScreen(0, 0, 4, 1, 1, Minecraft.getMinecraft().player);
        Minecraft.getMinecraft().player.getCapability(CapabilitySizeChanging.SIZE_CHANGING_CAP, null).setSizeDirectly(cachedSize);
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        GlStateManager.translate(i + this.xSize / 2 - 30, j + 122, 0);
        float blockScale = (this.slider.getSliderValue() <= 1F ? 1F : (-0.125F) * this.slider.getSliderValue() + 1.125F) * 40;
        GlStateManager.scale(blockScale, blockScale, blockScale);
        GlStateManager.disableCull();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-10, 0, 1, 0);
        GlStateManager.rotate(180, 1, 0, 0);
        GlStateManager.translate(0, 0.5, 0);
        Minecraft.getMinecraft().getRenderItem().renderItem(new ItemStack(Blocks.GRASS), ItemCameraTransforms.TransformType.NONE);
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();

        GlStateManager.popMatrix();
    }

    @Override
    public void setEntryValue(int id, boolean value) {

    }

    @Override
    public void setEntryValue(int id, float value) {
        PTPacketDispatcher.sendToServer(new MessageSetRegulatedSize((float) LCMathHelper.round(value, 2)));
    }

    @Override
    public void setEntryValue(int id, String value) {

    }
}
