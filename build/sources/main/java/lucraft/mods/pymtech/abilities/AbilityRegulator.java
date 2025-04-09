package lucraft.mods.pymtech.abilities;

import lucraft.mods.lucraftcore.superpowers.abilities.AbilityAction;
import lucraft.mods.lucraftcore.superpowers.abilities.data.AbilityData;
import lucraft.mods.lucraftcore.superpowers.abilities.data.AbilityDataBoolean;
import lucraft.mods.lucraftcore.superpowers.abilities.supplier.EnumSync;
import lucraft.mods.pymtech.PymTech;
import lucraft.mods.pymtech.client.gui.PTGuiHandler;
import lucraft.mods.pymtech.items.PTItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class AbilityRegulator extends AbilityAction {

    public static final AbilityData<Boolean> ADVANCED = new AbilityDataBoolean("advanced").disableSaving().setSyncType(EnumSync.SELF).enableSetting("advanced", "Determines if you have access to the advanced regulator GUI");

    public AbilityRegulator(EntityLivingBase entity) {
        super(entity);
    }

    @Override
    public void registerData() {
        super.registerData();
        this.dataManager.register(ADVANCED, false);
    }

    @Override
    public boolean isUnlocked() {
        return super.isUnlocked() && !getChestplate().isEmpty();
    }

    public ItemStack getChestplate() {
        return this.entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void drawIcon(Minecraft mc, Gui gui, int x, int y) {
        float zLevel = Minecraft.getMinecraft().getRenderItem().zLevel;
        mc.getRenderItem().zLevel = -100.5F;
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 0);
        mc.getRenderItem().renderItemIntoGUI(new ItemStack(PTItems.REGULATOR), 0, 0);
        GlStateManager.popMatrix();
        mc.getRenderItem().zLevel = zLevel;
    }

    @Override
    public boolean action() {
        if (entity instanceof EntityPlayer) {
            if (this.dataManager.get(ADVANCED))
                ((EntityPlayer) entity).openGui(PymTech.INSTANCE, PTGuiHandler.REGULATOR_ADVANCED, entity.world, entity.getPosition().getX(), entity.getPosition().getY(), entity.getPosition().getZ());
            else
                ((EntityPlayer) entity).openGui(PymTech.INSTANCE, PTGuiHandler.REGULATOR, entity.world, entity.getPosition().getX(), entity.getPosition().getY(), entity.getPosition().getZ());
        }
        return true;
    }
}
