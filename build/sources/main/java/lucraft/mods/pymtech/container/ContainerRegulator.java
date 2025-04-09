package lucraft.mods.pymtech.container;

import lucraft.mods.lucraftcore.util.inventory.InventoryItem;
import lucraft.mods.pymtech.items.ItemPymParticleArmor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerRegulator extends Container {

	public InventoryItem inv;

	public ContainerRegulator(EntityPlayer player, ItemStack stack) {
		this.inv = ItemPymParticleArmor.getInventory(stack);

		this.addSlotToContainer(new Slot(inv, 0, 80, 22) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				return ItemPymParticleArmor.fitsInRegulatorSlot(stack);
			}
		});

		for (int l = 0; l < 3; ++l) {
			for (int j1 = 0; j1 < 9; ++j1) {
				this.addSlotToContainer(new Slot(player.inventory, j1 + (l + 1) * 9, 8 + j1 * 18, 74 + l * 18));
			}
		}

		for (int j = 0; j < 9; j++) {
			this.addSlotToContainer(new Slot(player.inventory, j, 8 + j * 18, 132));
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

            if (index == 0) {
                if (!this.mergeItemStack(itemstack1, 1, 37, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else {
                if (ItemPymParticleArmor.fitsInRegulatorSlot(itemstack1)) {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 1 && index < 28) {
                    if (!this.mergeItemStack(itemstack1, 28, 37, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 28 && index < 37 && !this.mergeItemStack(itemstack1, 1, 28, false)) {
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
