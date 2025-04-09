package lucraft.mods.pymtech.blocks;

import lucraft.mods.lucraftcore.util.blocks.BlockBase;
import lucraft.mods.pymtech.PymTech;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockBlueSandstone extends BlockBase {
    public static final PropertyEnum<EnumType> TYPE = PropertyEnum.create("type", EnumType.class);

    public BlockBlueSandstone(String name) {
        super(name, Material.ROCK);
        this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, EnumType.DEFAULT));
        this.setCreativeTab(PymTech.CREATIVE_TAB);
        this.setSoundType(SoundType.STONE);
        this.setHardness(0.8F);
    }

    public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return MapColor.BLUE;
    }

    public int damageDropped(IBlockState state) {
        return ((EnumType)state.getValue(TYPE)).getMetadata();
    }

    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        EnumType[] var3 = EnumType.values();
        int var4 = var3.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            EnumType type = var3[var5];
            items.add(new ItemStack(this, 1, type.getMetadata()));
        }

    }

    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(TYPE, EnumType.byMetadata(meta));
    }

    public int getMetaFromState(IBlockState state) {
        return ((EnumType)state.getValue(TYPE)).getMetadata();
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[]{TYPE});
    }

    public static enum EnumType implements IStringSerializable {
        DEFAULT(0, "blue_sandstone", "default"),
        CHISELED(1, "chiseled_blue_sandstone", "chiseled"),
        SMOOTH(2, "smooth_blue_sandstone", "smooth");

        private static final EnumType[] META_LOOKUP = new EnumType[values().length];
        private final int meta;
        private final String name;
        private final String unlocalizedName;

        private EnumType(int meta, String name, String unlocalizedName) {
            this.meta = meta;
            this.name = name;
            this.unlocalizedName = unlocalizedName;
        }

        public int getMetadata() {
            return this.meta;
        }

        public String toString() {
            return this.name;
        }

        public static EnumType byMetadata(int meta) {
            if (meta < 0 || meta >= META_LOOKUP.length) {
                meta = 0;
            }

            return META_LOOKUP[meta];
        }

        public String getName() {
            return this.name;
        }

        public String getUnlocalizedName() {
            return this.unlocalizedName;
        }

        static {
            EnumType[] var0 = values();
            int var1 = var0.length;

            for(int var2 = 0; var2 < var1; ++var2) {
                EnumType blockredsandstone$enumtype = var0[var2];
                META_LOOKUP[blockredsandstone$enumtype.getMetadata()] = blockredsandstone$enumtype;
            }

        }
    }
}

