package lucraft.mods.pymtech.blocks;

import lucraft.mods.pymtech.PymTech;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockBlueSandstoneStairs extends BlockStairs {
    public BlockBlueSandstoneStairs(String name, IBlockState state) {
        super(state);
        this.setTranslationKey(name);
        this.setRegistryName(name);
        this.setCreativeTab(PymTech.CREATIVE_TAB);
    }

    public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return MapColor.BLUE;
    }
}
