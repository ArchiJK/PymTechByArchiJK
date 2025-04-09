package lucraft.mods.pymtech.items;

import com.google.common.collect.Multimap;
import lucraft.mods.lucraftcore.superpowers.suitsets.ItemSuitSetArmor;
import lucraft.mods.lucraftcore.superpowers.suitsets.SuitSet;
import lucraft.mods.lucraftcore.util.helper.StringHelper;
import lucraft.mods.lucraftcore.util.inventory.InventoryItem;
import lucraft.mods.pymtech.fluids.PTFluids;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ItemPymParticleArmor extends ItemSuitSetArmor {

	public ItemPymParticleArmor(SuitSet suitSet, EntityEquipmentSlot slot) {
		super(suitSet, slot);
		if (slot == EntityEquipmentSlot.CHEST)
			this.addPropertyOverride(new ResourceLocation("infinite"), (stack, world, entity) -> stack.hasTagCompound() && stack.getTagCompound().getBoolean("Infinite") ? 1F : 0F);
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		super.getSubItems(tab, items);
		if (this.armorType == EntityEquipmentSlot.CHEST && this.isInCreativeTab(tab)) {
			ItemStack stack = new ItemStack(this);
			stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null).fill(new FluidStack(PTFluids.SHRINK_PYM_PARTICLES, 5000), true);
			stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null).fill(new FluidStack(PTFluids.GROW_PYM_PARTICLES, 5000), true);
			items.add(stack);

			ItemStack infinite = stack.copy();
			NBTTagCompound nbt = infinite.getTagCompound();
			nbt.setBoolean("Infinite", true);
			infinite.setTagCompound(nbt);
			items.add(infinite);
		}
	}

	@Override
	public boolean shouldShiftTooltipAppear(ItemStack stack, EntityPlayer player) {
		return this.armorType == EntityEquipmentSlot.CHEST;
	}

	@Override
	public List<String> getShiftToolTip(ItemStack stack, EntityPlayer player) {
		List<String> tooltip = new ArrayList<>();
		if (stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
			IFluidHandlerItem cap = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
			for (int i = 0; i < cap.getTankProperties().length; i++) {
				IFluidTankProperties properties = cap.getTankProperties()[i];
				String fluidName = i == 0 ? TextFormatting.RED + PTFluids.SHRINK_PYM_PARTICLES.getLocalizedName(null) : TextFormatting.BLUE + PTFluids.GROW_PYM_PARTICLES.getLocalizedName(null);
				int amount = properties.getContents() == null ? 0 : properties.getContents().amount;
				if (!stack.getTagCompound().getBoolean("Infinite"))
					tooltip.add(fluidName + TextFormatting.DARK_GRAY + ": " + TextFormatting.GRAY + amount + TextFormatting.DARK_GRAY + "/" + TextFormatting.GRAY + properties.getCapacity());
				else
					tooltip.add(fluidName + TextFormatting.DARK_GRAY + ": " + TextFormatting.YELLOW + StringHelper.translateToLocal("pymtech.info.infinite"));
			}
		}
		return tooltip;
	}

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
		if (this.getClass() == ItemPymParticleArmor.class && this.armorType == EntityEquipmentSlot.CHEST)
			return new PymParticleWrapper(stack, this);
		return super.initCapabilities(stack, nbt);
	}

	@Override
	public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
		return super.getAttributeModifiers(slot, stack);
	}

	public int getCapacity(ItemStack stack, boolean shrink) {
		return 5000;
	}

	public FluidStack getPymParticles(ItemStack stack, boolean shrink) {
		if (stack.getTagCompound() == null) {
			stack.setTagCompound(new NBTTagCompound());
		}

		if (stack.getTagCompound().getBoolean("Infinite"))
			return new FluidStack(shrink ? PTFluids.SHRINK_PYM_PARTICLES : PTFluids.GROW_PYM_PARTICLES, getCapacity(stack, shrink));

		String tag = shrink ? "ShrinkPymParticles" : "GrowPymParticles";

		if (!stack.getTagCompound().hasKey(tag)) {
			return null;
		}

		return FluidStack.loadFluidStackFromNBT(stack.getTagCompound().getCompoundTag(tag));
	}

	public int fill(ItemStack stack, FluidStack resource, boolean doFill, boolean shrink) {
		if (stack.hasTagCompound() && stack.getTagCompound().getBoolean("Infinite"))
			return 0;

		FluidStack fluid = getPymParticles(stack, shrink);
		int capacity = getCapacity(stack, shrink);

		if (resource == null || resource.amount <= 0) {
			return 0;
		}

		if (!doFill) {
			if (fluid == null) {
				return Math.min(capacity, resource.amount);
			}

			if (!fluid.isFluidEqual(resource)) {
				return 0;
			}

			return Math.min(capacity - fluid.amount, resource.amount);
		}

		if (fluid == null) {
			fluid = new FluidStack(resource, Math.min(capacity, resource.amount));
			NBTTagCompound nbt = fluid.writeToNBT(new NBTTagCompound());
			stack.getTagCompound().setTag(shrink ? "ShrinkPymParticles" : "GrowPymParticles", nbt);
			return fluid.amount;
		}

		if (!fluid.isFluidEqual(resource)) {
			return 0;
		}
		int filled = capacity - fluid.amount;

		if (resource.amount < filled) {
			fluid.amount += resource.amount;
			filled = resource.amount;
		} else {
			fluid.amount = capacity;
		}

		NBTTagCompound nbt = fluid.writeToNBT(new NBTTagCompound());
		stack.getTagCompound().setTag(shrink ? "ShrinkPymParticles" : "GrowPymParticles", nbt);

		return filled;
	}

	public FluidStack drain(ItemStack stack, FluidStack resource, boolean doDrain, boolean shrink) {
		if (resource == null || !resource.isFluidEqual(getPymParticles(stack, shrink)))
			return null;
		return drain(stack, resource.amount, doDrain, shrink);
	}

	public FluidStack drain(ItemStack stack, int maxDrain, boolean doDrain, boolean shrink) {
		FluidStack fluid = getPymParticles(stack, shrink);

		if (stack.getTagCompound().getBoolean("Infinite"))
			return new FluidStack(shrink ? PTFluids.SHRINK_PYM_PARTICLES : PTFluids.GROW_PYM_PARTICLES, maxDrain);

		if (fluid == null || maxDrain <= 0)
			return null;

		int drained = maxDrain;
		if (fluid.amount < drained) {
			drained = fluid.amount;
		}

		FluidStack fluidStack = new FluidStack(fluid, drained);
		if (doDrain) {
			fluid.amount -= drained;
			if (fluid.amount <= 0) {
				fluid = null;
			}

			NBTTagCompound nbt = fluid == null ? new NBTTagCompound() : fluid.writeToNBT(new NBTTagCompound());
			stack.getTagCompound().setTag(shrink ? "ShrinkPymParticles" : "GrowPymParticles", nbt);
		}

		return fluidStack;
	}

	public static boolean fitsInRegulatorSlot(ItemStack stack) {
		Item item = stack.getItem();
		return item == PTItems.SHRINK_DISC || item == PTItems.GROW_DISC || item == PTItems.REGULATOR;
	}

	public static InventoryItem getInventory(ItemStack stack) {
		InventoryItem inventoryItem = new InventoryItem(1, stack);
		if (!stack.hasTagCompound() || !stack.getTagCompound().hasKey("Items"))
			inventoryItem.setInventorySlotContents(0, new ItemStack(PTItems.REGULATOR));
		return inventoryItem;
	}

	public class PymParticleWrapper implements IFluidHandlerItem, ICapabilityProvider {

		@Nonnull
		protected ItemStack container;
		protected ItemPymParticleArmor item;

		public PymParticleWrapper(@Nonnull ItemStack container, ItemPymParticleArmor item) {
			this.container = container;
			this.item = item;
		}

		@Override
		public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
			return capability == CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY;
		}

		@Override
		@Nullable
		public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
			if (capability == CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY) {
				return CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY.cast(this);
			}
			return null;
		}

		@Nonnull
		@Override
		public ItemStack getContainer() {
			return this.container;
		}

		@Override
		public IFluidTankProperties[] getTankProperties() {
			return new IFluidTankProperties[] { new FluidTankProperties(item.getPymParticles(getContainer(), true), item.getCapacity(getContainer(), true)), new FluidTankProperties(item.getPymParticles(getContainer(), false), item.getCapacity(getContainer(), false)) };
		}

		@Override
		public int fill(FluidStack resource, boolean doFill) {
			if (resource.getFluid() == PTFluids.SHRINK_PYM_PARTICLES)
				return item.fill(getContainer(), resource, doFill, true);
			if (resource.getFluid() == PTFluids.GROW_PYM_PARTICLES)
				return item.fill(getContainer(), resource, doFill, false);
			return 0;
		}

		@Nullable
		@Override
		public FluidStack drain(FluidStack resource, boolean doDrain) {
			if (resource.getFluid() == PTFluids.SHRINK_PYM_PARTICLES)
				return item.drain(getContainer(), resource, doDrain, true);
			if (resource.getFluid() == PTFluids.GROW_PYM_PARTICLES)
				return item.drain(getContainer(), resource, doDrain, false);
			return null;
		}

		@Nullable
		@Override
		public FluidStack drain(int maxDrain, boolean doDrain) {
			if (drain(new FluidStack(PTFluids.SHRINK_PYM_PARTICLES, maxDrain), false) != null && drain(new FluidStack(PTFluids.SHRINK_PYM_PARTICLES, maxDrain), false).amount > 0)
				return drain(new FluidStack(PTFluids.SHRINK_PYM_PARTICLES, maxDrain), doDrain);
			if (drain(new FluidStack(PTFluids.GROW_PYM_PARTICLES, maxDrain), false) != null && drain(new FluidStack(PTFluids.GROW_PYM_PARTICLES, maxDrain), false).amount > 0)
				return drain(new FluidStack(PTFluids.GROW_PYM_PARTICLES, maxDrain), doDrain);

			return null;
		}

	}

}
