package lucraft.mods.pymtech.abilities;

import lucraft.mods.lucraftcore.sizechanging.capabilities.CapabilitySizeChanging;
import lucraft.mods.lucraftcore.sizechanging.sizechanger.SizeChanger;
import lucraft.mods.lucraftcore.superpowers.abilities.AbilityAction;
import lucraft.mods.lucraftcore.superpowers.abilities.data.AbilityData;
import lucraft.mods.lucraftcore.superpowers.abilities.data.AbilityDataFloat;
import lucraft.mods.lucraftcore.superpowers.abilities.supplier.EnumSync;
import lucraft.mods.lucraftcore.superpowers.suitsets.SuitSet;
import lucraft.mods.lucraftcore.util.helper.PlayerHelper;
import lucraft.mods.lucraftcore.util.helper.StringHelper;
import lucraft.mods.lucraftcore.util.inventory.InventoryItem;
import lucraft.mods.pymtech.dimensions.PTDimensions;
import lucraft.mods.pymtech.fluids.PTFluids;
import lucraft.mods.pymtech.helper.PTIconHelper;
import lucraft.mods.pymtech.items.ItemPymParticleArmor;
import lucraft.mods.pymtech.items.PTItems;
import lucraft.mods.pymtech.sounds.PTSoundEvents;
import lucraft.mods.pymtech.suitsets.SuitSetPymParticles;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.SizeChangerPymParticles;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class AbilityPymParticleSize extends AbilityAction {

    public static final AbilityData<Float> SIZE = new AbilityDataFloat("size").disableSaving().setSyncType(EnumSync.SELF).enableSetting("size", "Sets the size you want to change to");

    public AbilityPymParticleSize(EntityLivingBase entity) {
        super(entity);
    }

    @Override
    public void registerData() {
        super.registerData();
        this.dataManager.register(SIZE, 0.1F);
    }

    @Override
    public boolean isUnlocked() {
        return super.isUnlocked() && !getChestplate().isEmpty();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void drawIcon(Minecraft mc, Gui gui, int x, int y) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 0);
        GlStateManager.scale(0.5F, 0.5F, 1F);
        mc.renderEngine.bindTexture(PTIconHelper.ICON_TEX);
        gui.drawTexturedModalRect(0, 0, this.dataManager.get(SIZE) < 1F ? 0 : 32, 0, 32, 32);
        String s = "" + this.getSize();
        int length = mc.fontRenderer.getStringWidth(s);
        mc.fontRenderer.drawString(s, 16 - length / 2, 25, 0xfefefe);
        GlStateManager.popMatrix();
    }

    @Override
    public String getDisplayName() {
        if (this.getChestplate().isEmpty())
            return super.getDisplayName();
        boolean shrink = this.dataManager.get(SIZE) < 1F;
        String s = super.getDisplayName();
        ItemPymParticleArmor armor = (ItemPymParticleArmor) this.getChestplate().getItem();
        FluidStack particles = armor.getPymParticles(this.getChestplate(), shrink);
        if (this.getChestplate().getTagCompound().getBoolean("Infinite"))
            return s + " (" + StringHelper.translateToLocal("pymtech.info.infinite") + ")";
        else
            return s + " (" + (particles == null ? 0 : particles.amount) + "/" + armor.getCapacity(this.getChestplate(), shrink) + ")";
    }

    public ItemStack getChestplate() {
        return this.entity == null ? ItemStack.EMPTY : (this.entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() instanceof ItemPymParticleArmor ? this.entity.getItemStackFromSlot(EntityEquipmentSlot.CHEST) : ItemStack.EMPTY);
    }

    @Override
    public boolean action() {
        if (entity instanceof EntityPlayer)
            PlayerHelper.playSound(entity.world, (EntityPlayer) entity, entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ, PTSoundEvents.BUTTON, SoundCategory.PLAYERS);

        if (SuitSet.getSuitSet(entity) instanceof SuitSetPymParticles) {
            ItemStack stack = this.getChestplate();
            float f = entity.getCapability(CapabilitySizeChanging.SIZE_CHANGING_CAP, null).getSize() - this.getSize();
            FluidStack fluidStack = new FluidStack(f > 0F ? PTFluids.SHRINK_PYM_PARTICLES : PTFluids.GROW_PYM_PARTICLES, 500);

            if ((f != 0F || PTDimensions.isQuantumDimension(entity.dimension)) && stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
                InventoryItem inv = ItemPymParticleArmor.getInventory(stack);
                ItemStack regulator = inv.getStackInSlot(0);
                if (regulator.isEmpty() || ItemPymParticleArmor.fitsInRegulatorSlot(regulator)) {
                    boolean grow = regulator.getItem() == PTItems.GROW_DISC && f >= 0F;

                    if (regulator.getItem() == PTItems.SHRINK_DISC && f <= 0F)
                        return false;
                    else if (grow && !PTDimensions.isQuantumDimension(entity.dimension))
                        return false;
                    else if (regulator.isEmpty() && f <= 0F)
                        return false;
                    else if (PTDimensions.isQuantumDimension(entity.dimension) && f > 0F)
                        return false;
                    else {

                        FluidStack drained = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null).drain(fluidStack, false);
                        float size = grow && PTDimensions.isQuantumDimension(entity.dimension) ? 16 : (regulator.isEmpty() ? 0.1F : this.getSize());
                        SizeChanger sizeChanger = (regulator.getItem() == PTItems.GROW_DISC && f < 1F && PTDimensions.isQuantumDimension(entity.dimension)) || regulator.isEmpty() ? SizeChangerPymParticles.PYM_PARTICLES_SUBATOMIC : SizeChangerPymParticles.PYM_PARTICLES;

                        if (sizeChanger == SizeChangerPymParticles.PYM_PARTICLES_SUBATOMIC)
                            entity.timeUntilPortal = 10;

                        if (drained != null && drained.amount == 500) {
                            if (entity.getCapability(CapabilitySizeChanging.SIZE_CHANGING_CAP, null).setSize(size, sizeChanger)) {
                                stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null).drain(fluidStack, true);
                                return true;
                            }
                        } else {
                            if (entity instanceof EntityPlayer)
                                ((EntityPlayer) entity).sendStatusMessage(new TextComponentTranslation(f > 0F ? "pymtech.info.not_enough_shrink_pp" : "pymtech.info.not_enough_grow_pp"), true);
                            return false;
                        }
                    }
                }
            }
        }

        return false;
    }

    public float getSize() {
        float playerSize = entity.getCapability(CapabilitySizeChanging.SIZE_CHANGING_CAP, null).getSize();
        float customSize = !getChestplate().hasTagCompound() || getChestplate().getTagCompound().getFloat("RegulatedSize") <= 0 ? 1F : getChestplate().getTagCompound().getFloat("RegulatedSize");

        if ((playerSize < 1F && this.dataManager.get(SIZE) > 1F) || (playerSize > 1F && this.dataManager.get(SIZE) < 1F))
            return 1F;

        return customSize < 1F && this.dataManager.get(SIZE) < 1F ? customSize : (customSize > 1F && this.dataManager.get(SIZE) > 1F ? customSize : this.dataManager.get(SIZE));
    }

}
