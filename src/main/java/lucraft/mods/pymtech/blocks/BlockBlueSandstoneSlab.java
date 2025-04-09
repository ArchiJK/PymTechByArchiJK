package lucraft.mods.pymtech.blocks;

import java.util.Random;
import lucraft.mods.pymtech.PymTech;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.SoundType;
import net.minecraft.block.BlockSlab.EnumBlockHalf;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBlueSandstoneSlab extends BlockSlab {
    public BlockBlueSandstoneSlab(String name) {
        super(Material.ROCK, MapColor.BLUE);
        this.setTranslationKey(name);
        this.setRegistryName(name);
        this.setCreativeTab(PymTech.CREATIVE_TAB);
        this.setHardness(2.0F);
        this.setResistance(10.0F);
        this.setSoundType(SoundType.STONE);
        this.useNeighborBrightness = true;
        IBlockState iblockstate = this.blockState.getBaseState();
        if (!this.isDouble()) {
            iblockstate = iblockstate.withProperty(HALF, EnumBlockHalf.BOTTOM);
        }

        this.setDefaultState(iblockstate);
    }

    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return this.isDouble() ? BlockFaceShape.SOLID : super.getBlockFaceShape(worldIn, state, pos, face);
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(PTBlocks.BLUE_SANDSTONE_SLAB);
    }

    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack(PTBlocks.BLUE_SANDSTONE_SLAB, 1);
    }

    public String getTranslationKey(int meta) {
        return super.getTranslationKey();
    }

    public boolean isDouble() {
        return false;
    }

    public IProperty<?> getVariantProperty() {
        return HALF;
    }

    public Comparable<?> getTypeForItem(ItemStack stack) {
        return EnumBlockHalf.BOTTOM;
    }

    public IBlockState getStateFromMeta(int meta) {
        IBlockState iblockstate = this.getDefaultState();
        if (!this.isDouble()) {
            iblockstate = iblockstate.withProperty(HALF, EnumBlockHalf.values()[meta % EnumBlockHalf.values().length]);
        }

        return iblockstate;
    }

    public int getMetaFromState(IBlockState state) {
        return this.isDouble() ? 0 : ((EnumBlockHalf)state.getValue(HALF)).ordinal() + 1;
    }

    protected BlockStateContainer createBlockState() {
        return this.isDouble() ? super.createBlockState() : new BlockStateContainer(this, new IProperty[]{HALF});
    }
}