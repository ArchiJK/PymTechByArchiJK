package lucraft.mods.pymtech.container;

import lucraft.mods.lucraftcore.util.inventory.InventoryItem;
import lucraft.mods.pymtech.items.ItemPymParticleArmor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerRegulatorAdvanced extends Container {

    public InventoryItem inv;

    public ContainerRegulatorAdvanced(EntityPlayer player, ItemStack stack) {
        this.inv = ItemPymParticleArmor.getInventory(stack);

        this.addSlotToContainer(new Slot(inv, 0, 120, 166) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return ItemPymParticleArmor.fitsInRegulatorSlot(stack);
            }
        });

        for (int j = 0; j < 9; j++) {
            this.addSlotToContainer(new Slot(player.inventory, j, 48 + j * 18, 200));
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

            if (index < 1) {
                if (!this.mergeItemStack(itemstack1, 1, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }
}
