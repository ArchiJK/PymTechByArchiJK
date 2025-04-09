package lucraft.mods.pymtech.blocks;

import lucraft.mods.lucraftcore.util.helper.ItemHelper;
import lucraft.mods.pymtech.PymTech;
import lucraft.mods.pymtech.items.ItemBlueSandstoneSlab;
import lucraft.mods.pymtech.tileentities.TileEntityQuantumString;
import lucraft.mods.pymtech.tileentities.TileEntityStructureShrinker;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockRedSandstone.EnumType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemMultiTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

@Mod.EventBusSubscriber(modid = PymTech.MODID)
public class PTBlocks {

	public static Block STRUCTURE_SHRINKER = new BlockStructureShrinker("structure_shrinker");
	public static Block QUANTUM_STRING_BLOCK = new BlockQuantumString("quantum_string_block", Material.ROCK).setCreativeTab(PymTech.CREATIVE_TAB).setBlockUnbreakable().setLightLevel(1F);
	public static Block QUANTUM_CLOUD_BLOCK = new BlockQuantumCloud("quantum_cloud_block", Material.ROCK, false).setCreativeTab(PymTech.CREATIVE_TAB).setBlockUnbreakable();
	public static Block BLUE_SAND = new BlockBlueSand("blue_sand");
	public static Block BLUE_SANDSTONE = new BlockBlueSandstone("blue_sandstone");
	public static Block BLUE_SANDSTONE_STAIRS = new BlockBlueSandstoneStairs("blue_sandstone_stairs", BLUE_SANDSTONE.getDefaultState().withProperty(BlockBlueSandstone.TYPE, lucraft.mods.pymtech.blocks.BlockBlueSandstone.EnumType.SMOOTH));
	public static BlockSlab BLUE_SANDSTONE_SLAB = new BlockBlueSandstoneSlab("blue_sandstone_slab");
	public static BlockSlab BLUE_SANDSTONE_SLAB_DOUBLE = new BlockBlueSandstoneDoubleSlab("blue_sandstone_double_slab");



	@SubscribeEvent
	public static void onRegisterBlocks(RegistryEvent.Register<Block> e) {
		e.getRegistry().register(STRUCTURE_SHRINKER);
		e.getRegistry().register(QUANTUM_STRING_BLOCK);
		e.getRegistry().register(QUANTUM_CLOUD_BLOCK);
		e.getRegistry().register(BLUE_SAND);
		e.getRegistry().register(BLUE_SANDSTONE);
		e.getRegistry().register(BLUE_SANDSTONE_STAIRS);
		e.getRegistry().register(BLUE_SANDSTONE_SLAB);
		e.getRegistry().register(BLUE_SANDSTONE_SLAB_DOUBLE);

		GameRegistry.registerTileEntity(TileEntityStructureShrinker.class, new ResourceLocation(PymTech.MODID, "structure_shrinker"));
		GameRegistry.registerTileEntity(TileEntityQuantumString.class, new ResourceLocation(PymTech.MODID, "quantum_string"));
	}

	@SubscribeEvent
	public static void onRegisterItems(RegistryEvent.Register<Item> e) {
		e.getRegistry().register((new ItemBlock(STRUCTURE_SHRINKER)).setRegistryName(STRUCTURE_SHRINKER.getRegistryName()));
		e.getRegistry().register((new ItemBlock(QUANTUM_STRING_BLOCK)).setRegistryName(QUANTUM_STRING_BLOCK.getRegistryName()));
		e.getRegistry().register((new ItemBlock(QUANTUM_CLOUD_BLOCK)).setRegistryName(QUANTUM_CLOUD_BLOCK.getRegistryName()));
		e.getRegistry().register((new ItemBlock(BLUE_SAND)).setRegistryName(BLUE_SAND.getRegistryName()));
		e.getRegistry().register(((Item)(new ItemMultiTexture(BLUE_SANDSTONE, BLUE_SANDSTONE, (stack) -> {
			return EnumType.byMetadata(stack.getMetadata()).getTranslationKey();
		})).setRegistryName(BLUE_SANDSTONE.getRegistryName())).setTranslationKey(BLUE_SANDSTONE.getTranslationKey()));
		e.getRegistry().register((new ItemBlock(BLUE_SANDSTONE_STAIRS)).setRegistryName(BLUE_SANDSTONE_STAIRS.getRegistryName()));
		e.getRegistry().register((new ItemBlueSandstoneSlab(BLUE_SANDSTONE_SLAB, BLUE_SANDSTONE_SLAB, BLUE_SANDSTONE_SLAB_DOUBLE)).setRegistryName(BLUE_SANDSTONE_SLAB.getRegistryName()));
		OreDictionary.registerOre("sand", BLUE_SAND);
		OreDictionary.registerOre("sandstone", BLUE_SANDSTONE);
		OreDictionary.registerOre("sandstone", new ItemStack(BLUE_SANDSTONE, 1, 1));
		OreDictionary.registerOre("sandstone", new ItemStack(BLUE_SANDSTONE, 1, 2));
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void onRegisterModels(ModelRegistryEvent e) {
		ItemHelper.registerItemModel(Item.getItemFromBlock(STRUCTURE_SHRINKER), PymTech.MODID, "structure_shrinker");
		ItemHelper.registerItemModel(Item.getItemFromBlock(QUANTUM_STRING_BLOCK), "pymtech", "quantum_string_block");
		ItemHelper.registerItemModel(Item.getItemFromBlock(QUANTUM_CLOUD_BLOCK), "pymtech", "quantum_cloud_block");
		ItemHelper.registerItemModel(Item.getItemFromBlock(BLUE_SAND), "pymtech", "blue_sand");
		ItemHelper.registerItemModel(Item.getItemFromBlock(BLUE_SANDSTONE), 0, "pymtech", "blue_sandstone");
		ItemHelper.registerItemModel(Item.getItemFromBlock(BLUE_SANDSTONE), 1, "pymtech", "chiseled_blue_sandstone");
		ItemHelper.registerItemModel(Item.getItemFromBlock(BLUE_SANDSTONE), 2, "pymtech", "smooth_blue_sandstone");
		ItemHelper.registerItemModel(Item.getItemFromBlock(BLUE_SANDSTONE_STAIRS), "pymtech", "blue_sandstone_stairs");
		ItemHelper.registerItemModel(Item.getItemFromBlock(BLUE_SANDSTONE_SLAB), "pymtech", "blue_sandstone_slab");
		
	}

}
