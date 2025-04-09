package lucraft.mods.pymtech.abilities;

import lucraft.mods.lucraftcore.superpowers.abilities.AbilityAction;
import lucraft.mods.lucraftcore.superpowers.abilities.data.AbilityData;
import lucraft.mods.lucraftcore.superpowers.abilities.data.AbilityDataBoolean;
import lucraft.mods.lucraftcore.superpowers.abilities.supplier.EnumSync;
import lucraft.mods.lucraftcore.util.helper.PlayerHelper;
import lucraft.mods.pymtech.fluids.PTFluids;
import lucraft.mods.pymtech.items.ItemShrunkenSuit;
import lucraft.mods.pymtech.items.PTItems;
import lucraft.mods.pymtech.sounds.PTSoundEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public class AbilityShrinkSuit extends AbilityAction {

    public static final AbilityData<Boolean> NEEDS_PYM_PARTICLES = new AbilityDataBoolean("needs_pym_particles").disableSaving().setSyncType(EnumSync.SELF).enableSetting("advanced", "Determines if you need Pym Particles to shrink the suit");

    public AbilityShrinkSuit(EntityLivingBase entity) {
        super(entity);
    }

    @Override
    public void registerData() {
        super.registerData();
        this.dataManager.register(NEEDS_PYM_PARTICLES, true);
    }

    @Override
    public boolean isUnlocked() {
        return super.isUnlocked() && hasArmorOn();
    }

    @Override
    public void drawIcon(Minecraft mc, Gui gui, int x, int y) {
        ItemStack stack = new ItemStack(PTItems.SHRUNKEN_SUIT);
        ItemShrunkenSuit.storeSuit(stack, entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD), entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST), entity.getItemStackFromSlot(EntityEquipmentSlot.LEGS), entity.getItemStackFromSlot(EntityEquipmentSlot.FEET));
        float zLevel = Minecraft.getMinecraft().getRenderItem().zLevel;
        Minecraft.getMinecraft().getRenderItem().zLevel = -100.5F;
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 0);
        Minecraft.getMinecraft().getRenderItem().renderItemIntoGUI(stack, 0, 0);
        GlStateManager.popMatrix();
        Minecraft.getMinecraft().getRenderItem().zLevel = zLevel;
    }

    @Override
    public boolean action() {
        if (entity instanceof EntityPlayer)
            PlayerHelper.playSound(entity.world, (EntityPlayer) entity, entity.posX, entity.posY, entity.posZ, PTSoundEvents.BUTTON, SoundCategory.PLAYERS);

        if (this.dataManager.get(NEEDS_PYM_PARTICLES)) {
            ItemStack currentChestplate = entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
            if (currentChestplate.isEmpty() || !currentChestplate.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null))
                return false;
            FluidStack drainedShrink = currentChestplate.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null).drain(new FluidStack(PTFluids.SHRINK_PYM_PARTICLES, 500), false);
            FluidStack drainedGrow = currentChestplate.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null).drain(new FluidStack(PTFluids.GROW_PYM_PARTICLES, 500), false);

            if (drainedShrink == null || drainedShrink.amount != 500) {
                if (entity instanceof EntityPlayer)
                    ((EntityPlayer) entity).sendStatusMessage(new TextComponentTranslation("pymtech.info.not_enough_shrink_pp"), true);
                return false;
            }

            if (drainedGrow == null || drainedGrow.amount != 500) {
                if (entity instanceof EntityPlayer)
                    ((EntityPlayer) entity).sendStatusMessage(new TextComponentTranslation("pymtech.info.not_enough_grow_pp"), true);
                return false;
            }

            currentChestplate.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null).drain(new FluidStack(PTFluids.SHRINK_PYM_PARTICLES, 500), true);
            currentChestplate.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null).drain(new FluidStack(PTFluids.GROW_PYM_PARTICLES, 500), true);
        }
        ItemStack stack = new ItemStack(PTItems.SHRUNKEN_SUIT);
        ItemShrunkenSuit.storeSuit(stack, entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD), entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST), entity.getItemStackFromSlot(EntityEquipmentSlot.LEGS), entity.getItemStackFromSlot(EntityEquipmentSlot.FEET));
        entity.setItemStackToSlot(EntityEquipmentSlot.HEAD, ItemStack.EMPTY);
        entity.setItemStackToSlot(EntityEquipmentSlot.CHEST, ItemStack.EMPTY);
        entity.setItemStackToSlot(EntityEquipmentSlot.LEGS, ItemStack.EMPTY);
        entity.setItemStackToSlot(EntityEquipmentSlot.FEET, ItemStack.EMPTY);
        if (entity instanceof EntityPlayer)
            PlayerHelper.givePlayerItemStack((EntityPlayer) entity, stack);
        PlayerHelper.playSoundToAll(entity.world, entity.posX, entity.posY, entity.posZ, 50, PTSoundEvents.SHRINK, SoundCategory.PLAYERS);
        return true;
    }

    public boolean hasArmorOn() {
        return !entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty() || !entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST).isEmpty() || !entity.getItemStackFromSlot(EntityEquipmentSlot.LEGS).isEmpty() || !entity.getItemStackFromSlot(EntityEquipmentSlot.FEET).isEmpty();
    }

}
