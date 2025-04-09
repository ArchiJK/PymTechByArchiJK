package lucraft.mods.pymtech.dimensions.quantumvoid.dimension.generation.plants;

import java.util.Random;


import lucraft.mods.pymtech.blocks.PTBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenLilypads extends WorldGenerator
{
    public boolean generate(World worldIn, Random rand, BlockPos position)
    {
        IBlockState state = worldIn.getBlockState(position);
        for (int i = 0; i < 10; ++i)
        {
            int j = position.getX() + rand.nextInt(8) - rand.nextInt(8);
            int k = position.getY() + rand.nextInt(4) - rand.nextInt(4);
            int l = position.getZ() + rand.nextInt(8) - rand.nextInt(8);


                worldIn.setBlockState(new BlockPos(j, k, l), PTBlocks.BLUE_SANDSTONE_SLAB.getDefaultState(), 2);

        }

        return true;
    }
}
