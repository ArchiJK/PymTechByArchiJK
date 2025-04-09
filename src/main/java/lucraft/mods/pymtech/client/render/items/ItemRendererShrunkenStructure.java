package lucraft.mods.pymtech.client.render.items;

import lucraft.mods.pymtech.client.render.entities.RenderShrunkenStructure;
import lucraft.mods.pymtech.items.ItemShrunkenStructure;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;

public class ItemRendererShrunkenStructure extends TileEntityItemStackRenderer {

    @Override
    public void renderByItem(ItemStack stack, float partialTicks) {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("ShrunkenStructure")) {
            NBTTagCompound nbt = stack.getTagCompound().getCompoundTag("ShrunkenStructure");
            BlockPos size = readSize(nbt);
            float f = 1F / Math.max(size.getX(), Math.max(size.getY(), size.getZ()));
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.5F, 0, 0.5F);
            GlStateManager.scale(f, f, f);
            Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            if (!RenderShrunkenStructure.renderStructure(readId(nbt), size, 1F, Minecraft.getMinecraft().world, partialTicks, false)) {
                RenderShrunkenStructure.storeAndRender(Minecraft.getMinecraft().world, Minecraft.getMinecraft().player.getPosition(), new ItemShrunkenStructure.ShrunkenStructure(nbt), partialTicks, 1F, false);
            }
            GlStateManager.popMatrix();
        }
    }

    public static BlockPos readSize(NBTTagCompound nbt) {
        return NBTUtil.getPosFromTag(nbt.getCompoundTag("Size"));
    }

    public static long readId(NBTTagCompound nbt) {
        return nbt.getLong("Id");
    }

}
