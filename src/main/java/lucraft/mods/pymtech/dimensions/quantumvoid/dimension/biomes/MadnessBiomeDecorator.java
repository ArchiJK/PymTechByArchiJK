package lucraft.mods.pymtech.dimensions.quantumvoid.dimension.biomes;

import lucraft.mods.pymtech.dimensions.quantumvoid.dimension.generation.plants.WorldGenBigMushrooms;
import lucraft.mods.pymtech.dimensions.quantumvoid.dimension.generation.plants.WorldGenLilypads;
import lucraft.mods.pymtech.dimensions.quantumvoid.dimension.generation.plants.WorldGenMutatedSugarcane;
import lucraft.mods.pymtech.dimensions.quantumvoid.dimension.generation.plants.WorldGenMutatedVines;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.gen.ChunkGeneratorSettings;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class MadnessBiomeDecorator extends BiomeDecorator
{
	public WorldGenerator mutatedSugarcaneGen = new WorldGenMutatedSugarcane();
	public WorldGenerator bigMushroomGen = new WorldGenBigMushrooms();
	public WorldGenerator mutatedVinesGen = new WorldGenMutatedVines();
	public WorldGenerator mutatedLilypadGen = new WorldGenLilypads();
	public BlockPos chunkPos;

	Random rand = new Random();

	protected void genDecorations(Biome biomeIn, World worldIn, Random random) 
	{
		net.minecraft.util.math.ChunkPos forgeChunkPos = new net.minecraft.util.math.ChunkPos(chunkPos); // actual ChunkPos instead of BlockPos, used for events
		net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.terraingen.DecorateBiomeEvent.Pre(worldIn, random, forgeChunkPos));

		if(net.minecraftforge.event.terraingen.TerrainGen.decorate(worldIn, random, forgeChunkPos, net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.REED)) 
		{
			for(int k4 = 0; k4 < rand.nextInt(100) + 20; ++k4) 
			{
				int i9 = random.nextInt(16) + 8;
				int l12 = random.nextInt(16) + 8;
				int i16 = worldIn.getHeight(this.chunkPos.add(i9, 0, l12)).getY() * 2;

				if (i16 > 0) 
				{
					int l18 = random.nextInt(i16);
					this.mutatedSugarcaneGen.generate(worldIn, random, this.chunkPos.add(i9, l18, l12));
				}
			}

			for(int l4 = 0; l4 < rand.nextInt(100) + 20; ++l4) 
			{
				int j9 = random.nextInt(16) + 8;
				int i13 = random.nextInt(16) + 8;
				int j16 = worldIn.getHeight(this.chunkPos.add(j9, 0, i13)).getY() * 2;

				if (j16 > 0) 
				{
					int i19 = random.nextInt(j16);
					this.mutatedSugarcaneGen.generate(worldIn, random, this.chunkPos.add(j9, i19, i13));
				}
			}
		}
		
		int k1 = rand.nextInt(100) + 10;

        if (random.nextFloat() < 0.5f)
        {
            ++k1;
        }
		
		if(net.minecraftforge.event.terraingen.TerrainGen.decorate(worldIn, random, forgeChunkPos, net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.TREE))
		{
	        for (int j2 = 0; j2 < k1; ++j2)
	        {
	            int k6 = random.nextInt(16) + 8;
	            int l = random.nextInt(16) + 8;
	            WorldGenAbstractTree worldgenabstracttree = biomeIn.getRandomTreeFeature(random);
	            worldgenabstracttree.setDecorationDefaults();
	            BlockPos blockpos = worldIn.getHeight(this.chunkPos.add(k6, 0, l));

	            if (worldgenabstracttree.generate(worldIn, random, blockpos))
	            {
	                worldgenabstracttree.generateSaplings(worldIn, random, blockpos);
	            }
	        }
		}
		
		if(net.minecraftforge.event.terraingen.TerrainGen.decorate(worldIn, random, forgeChunkPos, net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.GRASS))
		{
	        for (int i3 = 0; i3 < rand.nextInt(40) + 10; ++i3)
	        {
	            int j7 = random.nextInt(16) + 8;
	            int i11 = random.nextInt(16) + 8;
	            int k14 = worldIn.getHeight(this.chunkPos.add(j7, 0, i11)).getY() * 2;

	            if (k14 > 0)
	            {
	                int l17 = random.nextInt(k14);
	                biomeIn.getRandomWorldGenForGrass(random).generate(worldIn, random, this.chunkPos.add(j7, l17, i11));
	            }
	        }
		}
		
		if(net.minecraftforge.event.terraingen.TerrainGen.decorate(worldIn, random, forgeChunkPos, net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.CUSTOM))
		{
			for (int j2 = 0; j2 < k1; ++j2)
	        {
	            int k6 = random.nextInt(16) + 8;
	            int l = random.nextInt(16) + 8;
	            BlockPos blockpos = worldIn.getHeight(this.chunkPos.add(k6, 0, l));

	            if (mutatedVinesGen.generate(worldIn, random, blockpos))
	            {
	            	mutatedVinesGen.generate(worldIn, random, blockpos);
	            }
	        }
		}
		
		if(net.minecraftforge.event.terraingen.TerrainGen.decorate(worldIn, random, forgeChunkPos, net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType.LILYPAD))
	        for (int k3 = 0; k3 < rand.nextInt(25) + 10; ++k3)
	        {
	            int l7 = random.nextInt(16) + 8;
	            int k11 = random.nextInt(16) + 8;
	            int i15 = worldIn.getHeight(this.chunkPos.add(l7, 0, k11)).getY() * 2;

	            if (i15 > 0)
	            {
	                int j18 = random.nextInt(i15);
	                BlockPos blockpos4;
	                BlockPos blockpos7;

	                for (blockpos4 = this.chunkPos.add(l7, j18, k11); blockpos4.getY() > 0; blockpos4 = blockpos7)
	                {
	                    blockpos7 = blockpos4.down();

	                    if (!worldIn.isAirBlock(blockpos7))
	                    {
	                        break;
	                    }
	                }

	                this.mutatedLilypadGen.generate(worldIn, random, blockpos4);
	            }
	        }
	}
	
	@Override
	public void decorate(World worldIn, Random random, Biome biome, BlockPos pos) 
	{
		if (this.decorating)
        {
            throw new RuntimeException("Already decorating");
        }
        else
        {
            this.chunkProviderSettings = ChunkGeneratorSettings.Factory.jsonToFactory(worldIn.getWorldInfo().getGeneratorOptions()).build();
            this.chunkPos = pos;
            this.genDecorations(biome, worldIn, random);
            this.decorating = false;
        }
	}
}
