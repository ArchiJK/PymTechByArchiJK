package lucraft.mods.pymtech.helper;

import lucraft.mods.pymtech.PymTech;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

public class PTIconHelper {

    public static final ResourceLocation ICON_TEX = new ResourceLocation(PymTech.MODID, "textures/gui/icons.png");

    public static void drawIcon(Minecraft mc, Gui gui, int x, int y, int row, int column) {
        mc.renderEngine.bindTexture(ICON_TEX);
        gui.drawTexturedModalRect(x, y, column * 16, row * 16, 16, 16);
    }

    public static void drawIcon(Minecraft mc, int x, int y, int row, int column) {
        drawIcon(mc, mc.ingameGUI, x, y, row, column);
    }

}
