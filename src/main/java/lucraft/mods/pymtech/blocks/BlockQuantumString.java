package lucraft.mods.pymtech.blocks;

import lucraft.mods.lucraftcore.util.blocks.BlockBase;
import lucraft.mods.pymtech.tileentities.TileEntityQuantumString;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockQuantumString extends BlockBase {

    public static final PropertyInteger BRIGHTNESS = PropertyInteger.create("brightness", 0, 3);

    public BlockQuantumString(String name, Material material) {
        super(name, material);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BRIGHTNESS, Integer.valueOf(0)));
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        if(world instanceof World && world.getBlockState(neighbor).getBlock() == this && world.getBlockState(pos).getValue(BRIGHTNESS) == 0 && world.getBlockState(neighbor).getValue(BRIGHTNESS) == 2) {
            ((World) world).setBlockState(pos, world.getBlockState(pos).withProperty(BRIGHTNESS, 3));
        }
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        worldIn.setBlockState(pos, state.withProperty(BRIGHTNESS, 3));
        return false;
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        super.updateTick(worldIn, pos, state, rand);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityQuantumString();
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
        return ((Integer) blockState.getValue(BRIGHTNESS)).intValue();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(BRIGHTNESS, Integer.valueOf(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return ((Integer) state.getValue(BRIGHTNESS)).intValue();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[]{BRIGHTNESS});
    }

}
