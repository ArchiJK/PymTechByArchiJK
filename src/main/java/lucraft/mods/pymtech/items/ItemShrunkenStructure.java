package lucraft.mods.pymtech.items;

import lucraft.mods.lucraftcore.util.items.ItemBase;
import lucraft.mods.pymtech.entities.EntityShrunkenStructure;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;
import org.lwjgl.Sys;

import javax.annotation.Nullable;

public class ItemShrunkenStructure extends ItemBase {

    public ItemShrunkenStructure(String name) {
        super(name);
        this.setMaxStackSize(1);
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {

    }

    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return stack.hasTagCompound() && stack.getTagCompound().hasKey("ShrunkenStructure");
    }

    @Nullable
    @Override
    public Entity createEntity(World world, Entity location, ItemStack itemstack) {
        EntityShrunkenStructure entity = new EntityShrunkenStructure(world, new ShrunkenStructure(itemstack.getTagCompound().getCompoundTag("ShrunkenStructure")));
        entity.scale = 0.1F;
        entity.setPosition(location.posX, location.posY, location.posZ);
        entity.motionX = location.motionX;
        entity.motionY = location.motionY;
        entity.motionZ = location.motionZ;
        return entity;
    }

    @Override
    public boolean getShareTag() {
        return super.getShareTag();
    }

    public static ShrunkenStructure rotateCounterclockwise(ShrunkenStructure shrunkenStructure) {
        BlockPos newSize = new BlockPos(shrunkenStructure.getSize().getZ(), shrunkenStructure.getSize().getY(), shrunkenStructure.getSize().getX());
        BlockData[][][] newBlocks = new BlockData[newSize.getX()][newSize.getY()][newSize.getZ()];

        for (int x = 0; x < shrunkenStructure.getSize().getX(); x++) {
            for (int y = 0; y < shrunkenStructure.getSize().getY(); y++) {
                for (int z = 0; z < shrunkenStructure.getSize().getZ(); z++) {
                    BlockData block = shrunkenStructure.getData()[x][y][z];

                    if (block != null) {
                        newBlocks[z][y][newSize.getZ() - 1 - x] = new BlockData(block.getBlock().withRotation(Rotation.COUNTERCLOCKWISE_90), block.getTileEntityData());
                    }
                }
            }
        }

        return new ShrunkenStructure(newBlocks, newSize);
    }

    public static BlockData[][][] getBlockData(NBTTagList list, BlockPos size) {
        BlockData[][][] data = new BlockData[size.getX()][size.getY()][size.getZ()];

        for (int i = 0; i < list.tagCount(); ++i) {
            NBTTagCompound blockTag = list.getCompoundTagAt(i);
            BlockPos pos = NBTUtil.getPosFromTag(blockTag.getCompoundTag("Pos"));
            data[pos.getX()][pos.getY()][pos.getZ()] = new BlockData(blockTag);
        }

        return data;
    }

    public static NBTTagList createBlockDataTag(BlockData[][][] data, BlockPos size) {
        NBTTagList list = new NBTTagList();
        for (int x = 0; x < size.getX(); x++) {
            for (int y = 0; y < size.getY(); y++) {
                for (int z = 0; z < size.getZ(); z++) {
                    BlockData d = data[x][y][z];

                    if (d != null) {
                        NBTTagCompound nbt = d.serializeNBT();
                        nbt.setTag("Pos", NBTUtil.createPosTag(new BlockPos(x, y, z)));
                        list.appendTag(nbt);
                    }
                }
            }
        }
        return list;
    }

    public static class ShrunkenStructure implements INBTSerializable<NBTTagCompound> {

        protected BlockData[][][] data;
        protected BlockPos size;
        protected long id;

        public ShrunkenStructure(BlockData[][][] data, BlockPos size) {
            this.data = data;
            this.size = size;
            this.id = System.currentTimeMillis();
        }

        public ShrunkenStructure(NBTTagCompound nbt) {
            this.deserializeNBT(nbt);
        }

        public BlockData[][][] getData() {
            return data;
        }

        public BlockPos getSize() {
            return size;
        }

        public long getId() {
            return id;
        }

        @Override
        public NBTTagCompound serializeNBT() {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setTag("Blocks", createBlockDataTag(data, size));
            nbt.setTag("Size", NBTUtil.createPosTag(this.size));
            nbt.setLong("Id", this.id);
            return nbt;
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt) {
            this.size = NBTUtil.getPosFromTag(nbt.getCompoundTag("Size"));
            this.data = getBlockData(nbt.getTagList("Blocks", 10), this.size);
            this.id = nbt.getLong("Id");
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof ShrunkenStructure && ((ShrunkenStructure)obj).getId() == this.getId();
        }
    }

    public static class BlockData implements INBTSerializable<NBTTagCompound> {

        protected IBlockState block;
        protected NBTTagCompound tileEntityData;

        public BlockData(IBlockState block, @Nullable NBTTagCompound tileEntityData) {
            this.block = block;
            this.tileEntityData = tileEntityData;
        }

        public BlockData(NBTTagCompound nbt) {
            this.deserializeNBT(nbt);
        }

        public IBlockState getBlock() {
            return block;
        }

        public NBTTagCompound getTileEntityData() {
            return tileEntityData;
        }

        public boolean hasTileEntity() {
            return getTileEntityData() != null;
        }

        @Override
        public NBTTagCompound serializeNBT() {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setString("Block", Block.REGISTRY.getNameForObject(block.getBlock()).toString());
            nbt.setByte("BlockData", (byte) block.getBlock().getMetaFromState(block));
            if (this.tileEntityData != null)
                nbt.setTag("TileEntity", this.tileEntityData);
            return nbt;
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt) {
            this.block = Block.REGISTRY.getObject(new ResourceLocation(nbt.getString("Block"))).getStateFromMeta(nbt.getByte("BlockData"));
            if (nbt.hasKey("TileEntity"))
                this.tileEntityData = nbt.getCompoundTag("TileEntity");
        }
    }

}
