package lucraft.mods.pymtech.dimensions.quantumvoid.dimension.biomes;

import lucraft.mods.pymtech.blocks.PTBlocks;
import lucraft.mods.pymtech.dimensions.quantumvoid.dimension.generation.plants.WorldGenBigMushrooms;
import lucraft.mods.pymtech.dimensions.quantumvoid.dimension.generation.plants.WorldGenMutatedGrass;
import lucraft.mods.pymtech.dimensions.quantumvoid.dimension.generation.plants.WorldGenMutatedSugarcane;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class BiomeMadness extends Biome 
{
	public BiomeMadness() 
	{
		super(new BiomeProperties("Madness").setBaseHeight(0.125f).setRainDisabled().setTemperature(0.6f).setHeightVariation(0.9f).setWaterColor(16757504));
		
		topBlock = PTBlocks.BLUE_SANDSTONE.getDefaultState();
		fillerBlock = PTBlocks.BLUE_SAND.getDefaultState();
		
		decorator = new MadnessBiomeDecorator();
		
		this.spawnableCaveCreatureList.clear();
		this.spawnableCreatureList.clear();
		this.spawnableMonsterList.clear();
		this.spawnableWaterCreatureList.clear();
		this.spawnableCreatureList.clear();
		this.spawnableWaterCreatureList.clear();
		this.spawnableCreatureList.clear();
		
		Random rand = new Random();
        decorator.reedGen = new WorldGenMutatedSugarcane();
        decorator.reedsPerChunk = rand.nextInt(100) + 20;
	}
	
	@Override
	public WorldGenerator getRandomWorldGenForGrass(Random rand)
    {
        return new WorldGenMutatedGrass();
    }
	
	@Override
	public int getSkyColorByTemp(float currentTemperature) 
	{
		return 8781568;
	}
	
	@Override
	public WorldGenAbstractTree getRandomTreeFeature(Random rand) 
	{
		return new WorldGenBigMushrooms();
	}
}