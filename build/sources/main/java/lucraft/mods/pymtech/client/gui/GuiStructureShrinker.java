package lucraft.mods.pymtech.client.gui;

import lucraft.mods.lucraftcore.util.energy.EnergyUtil;
import lucraft.mods.lucraftcore.util.fluids.LCFluidUtil;
import lucraft.mods.lucraftcore.util.gui.buttons.GuiButton10x;
import lucraft.mods.lucraftcore.util.helper.LCRenderHelper;
import lucraft.mods.lucraftcore.util.helper.StringHelper;
import lucraft.mods.pymtech.PTConfig;
import lucraft.mods.pymtech.PymTech;
import lucraft.mods.pymtech.container.ContainerStructureShrinker;
import lucraft.mods.pymtech.network.MessageStructureShrinkerSettings;
import lucraft.mods.pymtech.network.MessageStructureShrinkerShrink;
import lucraft.mods.pymtech.network.PTPacketDispatcher;
import lucraft.mods.pymtech.tileentities.TileEntityStructureShrinker;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

import java.io.IOException;

public class GuiStructureShrinker extends GuiContainer {

    private static final ResourceLocation GUI_TEXTURES = new ResourceLocation(PymTech.MODID, "textures/gui/structure_shrinker.png");

    public final EntityPlayer player;
    public final TileEntityStructureShrinker tileEntity;

    public int xBox;
    public int yBox;
    public int zBox;
    public boolean showBox;

    public GuiStructureShrinker(EntityPlayer player, TileEntityStructureShrinker tileEntity) {
        super(new ContainerStructureShrinker(player, tileEntity));
        this.player = player;
        this.tileEntity = tileEntity;
        this.xSize = 176;
        this.ySize = 166;
    }

    @Override
    public void initGui() {
        super.initGui();
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;

        xBox = tileEntity.area.getX();
        yBox = tileEntity.area.getY();
        zBox = tileEntity.area.getZ();
        showBox = this.tileEntity.showBox;

        this.addButton(new GuiButton10x(0, i + 35, j + 59, 80, StringHelper.translateToLocal("pymtech.info.shrink"), true));
        this.addButton(new GuiButton10x(1, i + 35, j + 47, 80, StringHelper.translateToLocal("pymtech.info.bounding_box").replace("%s", StringHelper.translateToLocal(this.showBox ? "pymtech.info.on" : "pymtech.info.off")), true));
        for (int k = 2; k <= 7; k++) {
            this.addButton(new GuiButton10x(k, i + (k < 5 ? 55 : 85), j + 15 + ((k - 2) % 3 * 10), 10, k < 5 ? "<" : ">", false));
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 0) {
            PTPacketDispatcher.sendToServer(new MessageStructureShrinkerShrink(this.tileEntity.getPos()));
        }

        if (button.id == 1) {
            this.showBox = !this.showBox;
            this.buttonList.get(1).displayString = StringHelper.translateToLocal("pymtech.info.bounding_box").replace("%s", StringHelper.translateToLocal(this.showBox ? "pymtech.info.on" : "pymtech.info.off"));
        }

        if (button.id == 2)
            xBox--;
        else if (button.id == 3)
            yBox--;
        else if (button.id == 4)
            zBox--;
        if (button.id == 5)
            xBox++;
        else if (button.id == 6)
            yBox++;
        else if (button.id == 7)
            zBox++;

        PTPacketDispatcher.sendToServer(new MessageStructureShrinkerSettings(this.tileEntity.getPos(), new BlockPos(xBox, yBox, zBox), this.showBox));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
        this.buttonList.get(0).enabled = this.tileEntity.canShrink(true);
        int max = PTConfig.Client.STRUCTURE_SHRINKER_LIMIT;
        this.buttonList.get(2).enabled = this.xBox > -max;
        this.buttonList.get(3).enabled = this.yBox > -max;
        this.buttonList.get(4).enabled = this.zBox > -max;
        this.buttonList.get(5).enabled = this.xBox < max;
        this.buttonList.get(6).enabled = this.yBox < max;
        this.buttonList.get(7).enabled = this.zBox < max;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(GUI_TEXTURES);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);

        int energy = (int) (((float) this.tileEntity.energyStorage.getEnergyStored() / (float) this.tileEntity.energyStorage.getMaxEnergyStored()) * 40);
        drawTexturedModalRect(i + 8, j + 18 + 40 - energy, 176, 40 - energy, 12, energy);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        String s = this.tileEntity.getDisplayName().getUnformattedText();
        this.fontRenderer.drawString(s, 30, 6, 4210752);
        this.fontRenderer.drawString(this.player.inventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);

        {
            String length = this.xBox + "";
            this.fontRenderer.drawString(length, 75 - this.fontRenderer.getStringWidth(length) / 2, 17, 4210752);
        }

        {
            String length = this.yBox + "";
            this.fontRenderer.drawString(length, 75 - this.fontRenderer.getStringWidth(length) / 2, 27, 4210752);
        }

        {
            String length = this.zBox + "";
            this.fontRenderer.drawString(length, 75 - this.fontRenderer.getStringWidth(length) / 2, 37, 4210752);
        }

        LCRenderHelper.renderTiledFluid(152, 8, 16, 0, this.tileEntity.fluidTank);
        mc.renderEngine.bindTexture(GUI_TEXTURES);
        GlStateManager.color(1, 1, 1, 1);
        this.drawTexturedModalRect(151, 7, 188, 0, 18, 62);
        EnergyUtil.drawTooltip(this.tileEntity.energyStorage.getEnergyStored(), this.tileEntity.energyStorage.getMaxEnergyStored(), this, 8, 18, 12, 40, mouseX - i, mouseY - j);
        LCFluidUtil.drawTooltip(this.tileEntity.fluidTank, this, 152, 8, 16, 60, mouseX - i, mouseY - j);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
