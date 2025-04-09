package lucraft.mods.pymtech.client.render.shrunkenstructure;

import lucraft.mods.pymtech.items.ItemShrunkenStructure;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import org.lwjgl.Sys;

import javax.annotation.Nullable;
import java.util.Arrays;

public class ShrunkenStructureAccess implements IBlockAccess {

    protected World world;
    protected BlockPos pos;
    protected ItemShrunkenStructure.ShrunkenStructure shrunkenStructure;
    protected int volume;
    private final int[] lightvalues;

    public ShrunkenStructureAccess(World world, BlockPos pos, ItemShrunkenStructure.ShrunkenStructure shrunkenStructure) {
        this.world = world;
        this.pos = pos;
        this.shrunkenStructure = shrunkenStructure;
        this.volume = shrunkenStructure.getSize().getX() * shrunkenStructure.getSize().getY() * shrunkenStructure.getSize().getZ();
        this.lightvalues = new int[this.volume];
        Arrays.fill(this.lightvalues, -1);
    }

    @Nullable
    @Override
    public TileEntity getTileEntity(BlockPos pos) {
        return null;
    }

    @Override
    public int getCombinedLight(BlockPos pos, int lightValue) {
        return this.world.getCombinedLight(this.pos, lightValue);
    }

    private int getIndex(final BlockPos pos) {
        return pos.getZ() + this.shrunkenStructure.getSize().getX() * pos.getY() + this.shrunkenStructure.getSize().getX() * this.shrunkenStructure.getSize().getY() * pos.getX();
    }

    @Override
    public IBlockState getBlockState(BlockPos pos) {
        if (pos.getX() < 0 || pos.getY() < 0 || pos.getZ() < 0 || pos.getX() > shrunkenStructure.getSize().getX() - 1 || pos.getY() > shrunkenStructure.getSize().getY() - 1 || pos.getZ() > shrunkenStructure.getSize().getZ() - 1)
            return Blocks.AIR.getDefaultState();
        ItemShrunkenStructure.BlockData data = this.shrunkenStructure.getData()[pos.getX()][pos.getY()][pos.getZ()];
        return data == null ? Blocks.AIR.getDefaultState() : data.getBlock();

    }

    @Override
    public boolean isAirBlock(BlockPos pos) {
        return getBlockState(pos).getMaterial() == Material.AIR;
    }

    @Override
    public Biome getBiome(BlockPos pos) {
        return Minecraft.getMinecraft().player.getEntityWorld().getBiome(Minecraft.getMinecraft().player.getPosition());
    }

    @Override
    public int getStrongPower(BlockPos pos, EnumFacing direction) {
        final IBlockState iblockstate = this.getBlockState(pos);
        return iblockstate.getBlock().getStrongPower(iblockstate, this, pos, direction);
    }

    @Override
    public WorldType getWorldType() {
        return WorldType.FLAT;
    }

    @Override
    public boolean isSideSolid(BlockPos pos, EnumFacing side, boolean _default) {
        final IBlockState iblockstate = this.getBlockState(pos);
        return iblockstate.getBlock().isSideSolid(iblockstate, this, pos, side);
    }
}
