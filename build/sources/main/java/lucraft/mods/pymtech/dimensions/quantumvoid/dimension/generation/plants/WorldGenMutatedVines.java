package lucraft.mods.pymtech.dimensions.quantumvoid.dimension.generation.plants;

import lucraft.mods.pymtech.blocks.PTBlocks;
import net.minecraft.block.BlockVine;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class WorldGenMutatedVines extends WorldGenerator
{
	private final IBlockState mutatedVinesState;
	
	public WorldGenMutatedVines()
    {
        this.mutatedVinesState = PTBlocks.BLUE_SANDSTONE_STAIRS.getDefaultState();
    }
	
	@Override
	public boolean generate(World worldIn, Random rand, BlockPos position) 
	{
		IBlockState state = worldIn.getBlockState(position);
		BlockPos.MutableBlockPos blockpos$mutableblockpos1 = new BlockPos.MutableBlockPos();
		BlockPos blockpos2 = blockpos$mutableblockpos1.west();
        BlockPos blockpos3 = blockpos$mutableblockpos1.east();
        BlockPos blockpos4 = blockpos$mutableblockpos1.north();
        BlockPos blockpos1 = blockpos$mutableblockpos1.south();

        if (state.getBlock() == PTBlocks.BLUE_SANDSTONE_SLAB_DOUBLE || state.getBlock() == PTBlocks.BLUE_SANDSTONE || state.getBlock() == PTBlocks.BLUE_SANDSTONE_SLAB_DOUBLE)
        {
        	if (rand.nextInt(1) == 0 && worldIn.isAirBlock(blockpos2))
            {
                this.addHangingVine(worldIn, blockpos2, BlockVine.EAST);
            }

            if (rand.nextInt(1) == 0 && worldIn.isAirBlock(blockpos3))
            {
                this.addHangingVine(worldIn, blockpos3, BlockVine.WEST);
            }

            if (rand.nextInt(1) == 0 && worldIn.isAirBlock(blockpos4))
            {
                this.addHangingVine(worldIn, blockpos4, BlockVine.SOUTH);
            }

            if (rand.nextInt(1) == 0 && worldIn.isAirBlock(blockpos1))
            {
                this.addHangingVine(worldIn, blockpos1, BlockVine.NORTH);
            }
            return true;
        }
        return false;
	}
	
	private void addVine(World worldIn, BlockPos pos, PropertyBool prop)
	{
		this.setBlockAndNotifyAdequately(worldIn, pos, PTBlocks.BLUE_SANDSTONE_STAIRS.getDefaultState().withProperty(prop, Boolean.valueOf(true)));
	}
	
	private void addHangingVine(World worldIn, BlockPos pos, PropertyBool prop)
    {
        this.addVine(worldIn, pos, prop);
        int i = 4;

        for (BlockPos blockpos = pos.down(); worldIn.isAirBlock(blockpos) && i > 0; --i)
        {
            this.addVine(worldIn, blockpos, prop);
            blockpos = blockpos.down();
        }
    }
}	
