package lucraft.mods.pymtech.blocks;

import lucraft.mods.pymtech.PymTech;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBlueSand extends BlockFalling {
    public BlockBlueSand(String name) {
        this.setTranslationKey(name);
        this.setRegistryName(name);
        this.setHardness(0.5F);
        this.setSoundType(SoundType.SAND);
        this.setCreativeTab(PymTech.CREATIVE_TAB);
    }

    @SideOnly(Side.CLIENT)
    public int getDustColor(IBlockState state) {
        return 86374;
    }

    public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return MapColor.BLUE;
    }
}

