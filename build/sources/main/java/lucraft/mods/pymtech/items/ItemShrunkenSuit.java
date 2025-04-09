package lucraft.mods.pymtech.items;

import lucraft.mods.lucraftcore.superpowers.suitsets.SuitSet;
import lucraft.mods.lucraftcore.util.helper.PlayerHelper;
import lucraft.mods.lucraftcore.util.items.ItemBase;
import lucraft.mods.pymtech.PymTech;
import lucraft.mods.pymtech.sounds.PTSoundEvents;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;

import java.util.Iterator;

public class ItemShrunkenSuit extends ItemBase {

    public ItemShrunkenSuit(String name) {
        super(name);
        this.setMaxStackSize(1);
        this.setCreativeTab(PymTech.CREATIVE_TAB);
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            Iterator<SuitSet> suitSets = SuitSet.REGISTRY.iterator();
            while (suitSets.hasNext()) {
                SuitSet suitSet = suitSets.next();
                ItemStack stack = new ItemStack(this);
                storeSuit(stack, new ItemStack(suitSet.getHelmet()), new ItemStack(suitSet.getChestplate()), new ItemStack(suitSet.getLegs()), new ItemStack(suitSet.getBoots()));
                items.add(stack);
            }
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        boolean free = true;
        for(EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
            if(slot.getSlotType() == EntityEquipmentSlot.Type.ARMOR) {
                ItemStack stack = getStoredItem(playerIn.getHeldItem(handIn), slot);

                if(!stack.isEmpty()) {
                    free = free && playerIn.getItemStackFromSlot(slot).isEmpty();
                }
            }
        }

        if(!free)
            return new ActionResult<>(EnumActionResult.FAIL, playerIn.getHeldItem(handIn));

        for(EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
            if(slot.getSlotType() == EntityEquipmentSlot.Type.ARMOR) {
                ItemStack stack = getStoredItem(playerIn.getHeldItem(handIn), slot);
                playerIn.setItemStackToSlot(slot, stack);
            }
        }
        PlayerHelper.playSoundToAll(playerIn.world, playerIn.posX, playerIn.posY, playerIn.posZ, 50, PTSoundEvents.GROW, SoundCategory.PLAYERS);
        PlayerHelper.playSound(playerIn.world, playerIn, playerIn.posX, playerIn.posY, playerIn.posZ, PTSoundEvents.BUTTON, SoundCategory.PLAYERS);
        return new ActionResult<>(EnumActionResult.SUCCESS, ItemStack.EMPTY);
    }

    public static ItemStack storeSuit(ItemStack container, ItemStack helmet, ItemStack chest, ItemStack legs, ItemStack boots) {
        NBTTagCompound nbt = container.hasTagCompound() ? container.getTagCompound() : new NBTTagCompound();

        if (!helmet.isEmpty())
            nbt.setTag("Helmet", helmet.serializeNBT());

        if (!chest.isEmpty())
            nbt.setTag("Chestplate", chest.serializeNBT());

        if (!legs.isEmpty())
            nbt.setTag("Legs", legs.serializeNBT());

        if (!boots.isEmpty())
            nbt.setTag("Boots", boots.serializeNBT());

        container.setTagCompound(nbt);
        return container;
    }

    public static ItemStack getStoredItem(ItemStack container, EntityEquipmentSlot slot) {
        if (slot.getSlotType() != EntityEquipmentSlot.Type.ARMOR || container.isEmpty() || !container.hasTagCompound())
            return ItemStack.EMPTY;
        String key = slot == EntityEquipmentSlot.HEAD ? "Helmet" : slot == EntityEquipmentSlot.CHEST ? "Chestplate" : slot == EntityEquipmentSlot.LEGS ? "Legs" : "Boots";
        return new ItemStack(container.getTagCompound().getCompoundTag(key));
    }

}
