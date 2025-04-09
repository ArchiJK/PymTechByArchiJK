package lucraft.mods.pymtech.dimensions.quantumvoid.dimension.generation.plants;

import lucraft.mods.pymtech.blocks.PTBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class WorldGenMutatedGrass extends WorldGenerator
{
	private final IBlockState mutatedGrassState;

    public WorldGenMutatedGrass()
    {
        this.mutatedGrassState = PTBlocks.BLUE_SANDSTONE_SLAB.getDefaultState();
    }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position)
    {
        for (IBlockState iblockstate = worldIn.getBlockState(position); (iblockstate.getBlock().isAir(iblockstate, worldIn, position) || iblockstate.getBlock().isLeaves(iblockstate, worldIn, position)) && position.getY() > 0; iblockstate = worldIn.getBlockState(position))
        {
            position = position.down();
        }

        for (int i = 0; i < 128; ++i)
        {
            BlockPos blockpos = position.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));


                worldIn.setBlockState(blockpos, this.mutatedGrassState, 2);

        }

        return true;
    }
}
