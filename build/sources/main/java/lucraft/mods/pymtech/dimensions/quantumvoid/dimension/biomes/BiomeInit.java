package lucraft.mods.pymtech.dimensions.quantumvoid.dimension.biomes;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeType;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class BiomeInit 
{
	public static final Biome MADNESS = new BiomeMadness();
	
	public static void registerBiomes()
	{
		initBiome(MADNESS, "Madness", BiomeType.COOL, Type.HILLS, Type.MOUNTAIN, Type.PLAINS);
	}
	
	private static Biome initBiome(Biome biome, String name, BiomeType 	bType, Type... types)
	{
		biome.setRegistryName(name);
		ForgeRegistries.BIOMES.register(biome);
		BiomeDictionary.addTypes(biome, types);
		BiomeManager.addSpawnBiome(biome);
		return biome;
	}
}
