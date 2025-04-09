package lucraft.mods.pymtech.client.gui;

import lucraft.mods.pymtech.container.ContainerRegulatorAdvanced;
import lucraft.mods.pymtech.container.ContainerStructureShrinker;
import lucraft.mods.pymtech.container.ContainerRegulator;
import lucraft.mods.pymtech.tileentities.TileEntityStructureShrinker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class PTGuiHandler implements IGuiHandler {

    public static final int REGULATOR = 0;
    public static final int REGULATOR_ADVANCED = 1;
    public static final int STRUCTURE_SHRINKER = 2;

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if(ID == REGULATOR)
            return new ContainerRegulator(player, player.getItemStackFromSlot(EntityEquipmentSlot.CHEST));
        if(ID == REGULATOR_ADVANCED)
            return new ContainerRegulatorAdvanced(player, player.getItemStackFromSlot(EntityEquipmentSlot.CHEST));
        if(ID == STRUCTURE_SHRINKER)
            return new ContainerStructureShrinker(player, (TileEntityStructureShrinker) world.getTileEntity(new BlockPos(x, y, z)));
        return null;
    }

    @SideOnly(Side.CLIENT)
    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if(ID == REGULATOR)
            return new GuiRegulator(player, player.getItemStackFromSlot(EntityEquipmentSlot.CHEST));
        if(ID == REGULATOR_ADVANCED)
            return new GuiRegulatorAdvanced(player, player.getItemStackFromSlot(EntityEquipmentSlot.CHEST));
        if(ID == STRUCTURE_SHRINKER)
            return new GuiStructureShrinker(player, (TileEntityStructureShrinker) world.getTileEntity(new BlockPos(x, y, z)));
        return null;
    }
}
