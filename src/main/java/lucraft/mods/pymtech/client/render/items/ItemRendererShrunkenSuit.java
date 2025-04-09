package lucraft.mods.pymtech.client.render.items;

import com.mojang.authlib.GameProfile;
import lucraft.mods.lucraftcore.util.entity.FakePlayerClient;
import lucraft.mods.lucraftcore.util.render.RenderArmor;
import lucraft.mods.pymtech.items.ItemShrunkenSuit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

public class ItemRendererShrunkenSuit extends TileEntityItemStackRenderer {

    public static FakePlayerClient fakePlayer = null;
    public static RenderArmor renderArmor = null;

    @Override
    public void renderByItem(ItemStack stack, float partialTicks) {
        if (!stack.hasTagCompound())
            return;

        Minecraft mc = Minecraft.getMinecraft();
        FakePlayerClient entity = new FakePlayerClient(mc.world, new GameProfile(null, "SHRUNKEN_SUIT"));
        entity.info = "";

        if (fakePlayer == null) {
            fakePlayer = new FakePlayerClient(Minecraft.getMinecraft().world, new GameProfile(null, "SUIT STAND"));
            fakePlayer.info = "suit_stand";
        }
        if (renderArmor == null)
            renderArmor = new RenderShrunkenSuit(Minecraft.getMinecraft().getRenderManager(), false);

        fakePlayer.inventory.armorInventory.set(3, ItemShrunkenSuit.getStoredItem(stack, EntityEquipmentSlot.HEAD));
        fakePlayer.inventory.armorInventory.set(2, ItemShrunkenSuit.getStoredItem(stack, EntityEquipmentSlot.CHEST));
        fakePlayer.inventory.armorInventory.set(1, ItemShrunkenSuit.getStoredItem(stack, EntityEquipmentSlot.LEGS));
        fakePlayer.inventory.armorInventory.set(0, ItemShrunkenSuit.getStoredItem(stack, EntityEquipmentSlot.FEET));

        GlStateManager.pushMatrix();
        fakePlayer.setInvisible(true);
        renderArmor.doRender(fakePlayer, 0.5F, -1.2F, 0.5F, 0, partialTicks);
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.popMatrix();
    }

    public static class RenderShrunkenSuit extends RenderArmor {

        public RenderShrunkenSuit(RenderManager renderManager, boolean useSmallArms) {
            super(renderManager, useSmallArms);
        }

        @Override
        public void doRender(AbstractClientPlayer entity, double x, double y, double z, float entityYaw, float partialTicks) {
            if (!entity.isUser() || this.renderManager.renderViewEntity == entity) {
                GlStateManager.pushMatrix();
                GlStateManager.disableCull();

                GlStateManager.depthMask(true);
                this.mainModel.isChild = entity.isChild();
                GlStateManager.translate(x, y, z);
                GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
                float f4 = this.prepareScale(entity, partialTicks);

                if (!(entity instanceof EntityPlayer) || !((EntityPlayer) entity).isSpectator()) {
                    this.renderLayers(entity, 0, 0, partialTicks, 0, 0, 0, 0.0625F);
                }

                GlStateManager.enableCull();
                GlStateManager.popMatrix();
            }
        }

        @Override
        protected boolean setBrightness(AbstractClientPlayer entitylivingbaseIn, float partialTicks, boolean combineTextures) {
            return false;
        }

        @Override
        protected void unsetBrightness() {

        }

    }

}
