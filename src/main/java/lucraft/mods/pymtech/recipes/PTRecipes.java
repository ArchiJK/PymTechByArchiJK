package lucraft.mods.pymtech.recipes;

import lucraft.mods.lucraftcore.materials.MaterialsRecipes;
import lucraft.mods.lucraftcore.utilities.items.UtilitiesItems;
import lucraft.mods.pymtech.PymTech;
import lucraft.mods.pymtech.blocks.PTBlocks;
import lucraft.mods.pymtech.fluids.PTFluids;
import lucraft.mods.pymtech.items.PTItems;
import lucraft.mods.pymtech.suitsets.PTSuitSet;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

@Mod.EventBusSubscriber(modid = PymTech.MODID)
public class PTRecipes {

	@SubscribeEvent
	public static void onRegisterRecipes(RegistryEvent.Register<net.minecraft.item.crafting.IRecipe> e) {
		boolean generate = false;

		if (generate) {
			MaterialsRecipes.setupDir();

			MaterialsRecipes.addShapedRecipe(new ItemStack(PTItems.FILTER), "IBI", "BRB", "IBI", 'I', "plateIron", 'B', Blocks.IRON_BARS, 'R', "plateSilver");
			MaterialsRecipes.addShapedRecipe(new ItemStack(PTItems.EMP_COMMUNICATION_DEVICE), "PP ", "PRG", " II", 'I', "ingotIron", 'P', "plateIron", 'R', "dustRedstone", 'G', "dustGlowstone");
			MaterialsRecipes.addShapedRecipe(new ItemStack(PTItems.ANT_ANTENNA), "  I", " I ", "P  ", 'I', "ingotIron", 'P', PTItems.EMP_COMMUNICATION_DEVICE);
			MaterialsRecipes.addShapedRecipe(new ItemStack(PTItems.SHRINK_SIZE_COIL), "WIW", "PXP", "WRW", 'W', "wireCopper", 'I', "ingotIntertium", 'P', "plateIron", 'X', "ingotPalladium", 'R', "dustRedstone");
			MaterialsRecipes.addShapedRecipe(new ItemStack(PTItems.GROWTH_SIZE_COIL), "WIW", "PXP", "WRW", 'W', "wireCopper", 'I', "ingotLead", 'P', "plateIron", 'X', "ingotPalladium", 'R', new ItemStack(Items.DYE, 1, 15));
			MaterialsRecipes.addShapedRecipe(new ItemStack(PTItems.REGULATOR), "NPN", "SCG", "NPN", 'N', "nuggetIron", 'P', "plateIron", 'S', PTItems.SHRINK_SIZE_COIL, 'C', "circuitAdvanced", 'G', PTItems.GROWTH_SIZE_COIL);
			MaterialsRecipes.addShapedRecipe(new ItemStack(PTItems.SHRINK_DISC, 16), " I ", "IBI", " I ", 'I', "ingotIron", 'B', FluidUtil.getFilledBucket(new FluidStack(PTFluids.SHRINK_PYM_PARTICLES, 1000)));
			MaterialsRecipes.addShapedRecipe(new ItemStack(PTItems.GROW_DISC, 16), " I ", "IBI", " I ", 'I', "ingotIron", 'B', FluidUtil.getFilledBucket(new FluidStack(PTFluids.GROW_PYM_PARTICLES, 1000)));
			MaterialsRecipes.addShapedRecipe(new ItemStack(PTBlocks.STRUCTURE_SHRINKER), "PIP", "BCX", "PRP", 'P', "plateIron", 'I', "ingotIron", 'B', Items.BUCKET, 'C', PTItems.SHRINK_SIZE_COIL, 'X', UtilitiesItems.LV_CAPACITOR, 'R', PTItems.REGULATOR);

			// AM1
			MaterialsRecipes.addShapedRecipe(new ItemStack(PTSuitSet.ANT_MAN.getHelmet()), new ResourceLocation(PymTech.MODID, "ant_man_1"), "APA", "GPG", "IFI", 'I', "ingotIron", 'A', PTItems.ANT_ANTENNA, 'P', "plateIron", 'F', PTItems.FILTER, 'G', "paneGlassRed");
			MaterialsRecipes.addShapedRecipe(new ItemStack(PTSuitSet.ANT_MAN.getChestplate()), new ResourceLocation(PymTech.MODID, "ant_man_1"), "SBS", "RCR", "IXI", 'I', "ingotIron", 'S', UtilitiesItems.TRI_POLYMER.get(EnumDyeColor.BLACK), 'R', UtilitiesItems.TRI_POLYMER.get(EnumDyeColor.RED), 'B', Items.BUCKET, 'C', PTItems.SHRINK_SIZE_COIL, 'X', PTItems.REGULATOR, 'I', "ingotIron");
			MaterialsRecipes.addShapedRecipe(new ItemStack(PTSuitSet.ANT_MAN.getLegs()), new ResourceLocation(PymTech.MODID, "ant_man_1"), "RPR", "S S", "S S", 'S', UtilitiesItems.TRI_POLYMER.get(EnumDyeColor.BLACK), 'R', UtilitiesItems.TRI_POLYMER.get(EnumDyeColor.RED), 'P', "plateIron");
			MaterialsRecipes.addShapedRecipe(new ItemStack(PTSuitSet.ANT_MAN.getBoots()), new ResourceLocation(PymTech.MODID, "ant_man_1"), "G G", "S S", 'S', UtilitiesItems.TRI_POLYMER.get(EnumDyeColor.BLACK), 'G', UtilitiesItems.TRI_POLYMER.get(EnumDyeColor.GRAY));

			// AM2
			MaterialsRecipes.addShapedRecipe(new ItemStack(PTSuitSet.ANT_MAN_2.getHelmet()), new ResourceLocation(PymTech.MODID, "ant_man_2"), "PPP", "GHG", "IFI", 'P', "plateSilver", 'G', "paneGlassRed", 'H', PTSuitSet.ANT_MAN.getHelmet(), 'I', "ingotSilver", 'F', PTItems.FILTER);
			MaterialsRecipes.addShapedRecipe(new ItemStack(PTSuitSet.ANT_MAN_2.getChestplate()), new ResourceLocation(PymTech.MODID, "ant_man_2"), "PCP", "PHP", "IZI", 'P', "plateIntertium", 'C', PTItems.GROWTH_SIZE_COIL, 'H', PTSuitSet.ANT_MAN.getChestplate(), 'I', "ingotSilver", 'Z', "plateSilver");
			MaterialsRecipes.addShapedRecipe(new ItemStack(PTSuitSet.ANT_MAN_2.getLegs()), new ResourceLocation(PymTech.MODID, "ant_man_2"), "PIP", "ILI", "B B", 'P', "plateIntertium", 'I', "plateSilver", 'L', PTSuitSet.ANT_MAN.getLegs(), 'B', "ingotSilver");
			MaterialsRecipes.addShapedRecipe(new ItemStack(PTSuitSet.ANT_MAN_2.getBoots()), new ResourceLocation(PymTech.MODID, "ant_man_2"), "IBI", "T T", 'I', "ingotIntertium", 'B', PTSuitSet.ANT_MAN.getBoots(), 'T', UtilitiesItems.TRI_POLYMER.get(EnumDyeColor.BLACK));

			// AM3
			MaterialsRecipes.addShapedRecipe(new ItemStack(PTSuitSet.ANT_MAN_3.getHelmet()), new ResourceLocation(PymTech.MODID, "ant_man_3"), "PSP", "GHG", "IFI", 'P', "plateIron", 'G', "paneGlassRed", 'H', PTSuitSet.ANT_MAN.getHelmet(), 'I', "ingotIron", 'F', PTItems.FILTER, 'S', "servoMotor");
			MaterialsRecipes.addShapedRecipe(new ItemStack(PTSuitSet.ANT_MAN_3.getChestplate()), new ResourceLocation(PymTech.MODID, "ant_man_3"), "PCP", "PHP", "IZI", 'P', UtilitiesItems.TRI_POLYMER.get(EnumDyeColor.RED), 'C', PTItems.GROWTH_SIZE_COIL, 'H', PTSuitSet.ANT_MAN.getChestplate(), 'I', "ingotIron", 'Z', "plateIron");
			MaterialsRecipes.addShapedRecipe(new ItemStack(PTSuitSet.ANT_MAN_3.getLegs()), new ResourceLocation(PymTech.MODID, "ant_man_3"), "PIP", "ILI", "B B", 'P', UtilitiesItems.TRI_POLYMER.get(EnumDyeColor.RED), 'I', "plateIron", 'L', PTSuitSet.ANT_MAN.getLegs(), 'B', "ingotIron");
			MaterialsRecipes.addShapedRecipe(new ItemStack(PTSuitSet.ANT_MAN_3.getBoots()), new ResourceLocation(PymTech.MODID, "ant_man_3"), "IBI", "T T", 'I', UtilitiesItems.TRI_POLYMER.get(EnumDyeColor.RED), 'B', PTSuitSet.ANT_MAN.getBoots(), 'T', UtilitiesItems.TRI_POLYMER.get(EnumDyeColor.BLACK));

			// WASP
			MaterialsRecipes.addShapedRecipe(new ItemStack(PTSuitSet.WASP.getHelmet()), new ResourceLocation(PymTech.MODID, "wasp"), "APA", "GPG", "IFI", 'A', PTItems.ANT_ANTENNA, 'P', "plateIron", 'G', "paneGlassYellow", 'I', "ingotIron", 'F', PTItems.FILTER);
			MaterialsRecipes.addShapedRecipe(new ItemStack(PTSuitSet.WASP.getChestplate()), new ResourceLocation(PymTech.MODID, "wasp"), "YBY", "XCX", "IRI", 'Y', UtilitiesItems.TRI_POLYMER.get(EnumDyeColor.YELLOW), 'X', UtilitiesItems.TRI_POLYMER.get(EnumDyeColor.BLUE), 'B', Items.BUCKET, 'C', PTItems.SHRINK_SIZE_COIL, 'I', "ingotIron", 'R', PTItems.REGULATOR);
			MaterialsRecipes.addShapedRecipe(new ItemStack(PTSuitSet.WASP.getLegs()), new ResourceLocation(PymTech.MODID, "wasp"), "YPY", "R R", "B B", 'Y', UtilitiesItems.TRI_POLYMER.get(EnumDyeColor.YELLOW), 'R', UtilitiesItems.TRI_POLYMER.get(EnumDyeColor.RED), 'B', UtilitiesItems.TRI_POLYMER.get(EnumDyeColor.BLUE), 'P', "plateIron");
			MaterialsRecipes.addShapedRecipe(new ItemStack(PTSuitSet.WASP.getBoots()), new ResourceLocation(PymTech.MODID, "wasp"), "B B", "R R", 'B', UtilitiesItems.TRI_POLYMER.get(EnumDyeColor.BLUE), 'R', UtilitiesItems.TRI_POLYMER.get(EnumDyeColor.RED));

			MaterialsRecipes.generateConstants();
		}
	}

}
