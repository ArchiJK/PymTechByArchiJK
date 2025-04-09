package lucraft.mods.pymtech.container;

import lucraft.mods.lucraftcore.infinity.blocks.TileEntityInfinityGenerator;
import lucraft.mods.lucraftcore.utilities.container.ContainerFurnaceGenerator;
import lucraft.mods.pymtech.items.ItemPymParticleArmor;
import lucraft.mods.pymtech.tileentities.TileEntityStructureShrinker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class ContainerStructureShrinker extends Container {

	public ContainerStructureShrinker(EntityPlayer player, TileEntityStructureShrinker tileEntity) {
		IItemHandler itemHandler = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

		this.addSlotToContainer(new SlotItemHandler(itemHandler, 0, 130, 8) {
			@Override
			public int getSlotStackLimit() {
				return 1;
			}

			@Override
			public boolean isItemValid(ItemStack stack) {
				return FluidUtil.getFluidHandler(stack) != null;
			}
		});

		this.addSlotToContainer(new SlotItemHandler(itemHandler, 1, 130, 52) {
			@Override
			public int getSlotStackLimit() {
				return 1;
			}

			@Override
			public boolean isItemValid(ItemStack stack) {
				return FluidUtil.getFluidHandler(stack) != null;
			}
		});

		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (int k = 0; k < 9; ++k) {
			this.addSlotToContainer(new Slot(player.inventory, k, 8 + k * 18, 142));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (index <= 1) {
				if (!this.mergeItemStack(itemstack1, 2, 38, true)) {
					return ItemStack.EMPTY;
				}

				slot.onSlotChange(itemstack1, itemstack);
			} else {
				if (FluidUtil.getFluidHandler(itemstack1) != null) {
					if (!this.mergeItemStack(itemstack1, 0, 2, false)) {
						return ItemStack.EMPTY;
					}
				} else if (index >= 2 && index < 29) {
					if (!this.mergeItemStack(itemstack1, 29, 38, false)) {
						return ItemStack.EMPTY;
					}
				} else if (index >= 29 && index < 38 && !this.mergeItemStack(itemstack1, 2, 29, false)) {
					return ItemStack.EMPTY;
				}
			}

			if (itemstack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}

			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(playerIn, itemstack1);
		}

		return itemstack;
	}
}
