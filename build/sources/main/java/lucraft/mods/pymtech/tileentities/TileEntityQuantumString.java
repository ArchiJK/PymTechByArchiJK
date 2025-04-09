package lucraft.mods.pymtech.tileentities;

import lucraft.mods.pymtech.blocks.BlockQuantumString;
import lucraft.mods.pymtech.blocks.PTBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class TileEntityQuantumString extends TileEntity implements ITickable {
    public TileEntityQuantumString() {
    }

    public void update() {
        IBlockState state = this.getWorld().getBlockState(this.getPos());
        if (state != null && state.getBlock() == PTBlocks.QUANTUM_STRING_BLOCK && (Integer)state.getValue(BlockQuantumString.BRIGHTNESS) > 0) {
            this.getWorld().setBlockState(this.getPos(), state.withProperty(BlockQuantumString.BRIGHTNESS, (Integer)state.getValue(BlockQuantumString.BRIGHTNESS) - 1));
        }

    }
}

