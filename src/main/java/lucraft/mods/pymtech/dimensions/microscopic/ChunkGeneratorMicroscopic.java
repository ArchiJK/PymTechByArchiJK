package lucraft.mods.pymtech.dimensions.microscopic;

import net.minecraft.block.BlockConcretePowder;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.IChunkGenerator;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class ChunkGeneratorMicroscopic implements IChunkGenerator {

	private final World worldObj;
	private final Random rand;

	public ChunkGeneratorMicroscopic(World world) {
		this.worldObj = world;
		this.rand = new Random(world.getSeed());
	}

	@Override
	public Chunk generateChunk(int x, int z) {
		ChunkPrimer chunkprimer = new ChunkPrimer();
		this.rand.setSeed((long)x * 341873128712L + (long)z * 132897987541L);

		if(rand.nextInt(5) == 0) {
			generateSphere(chunkprimer, 8, 25 + rand.nextInt( 200), 8, 3 + rand.nextInt(5), Blocks.DIRT.getDefaultState(), Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.COARSE_DIRT), Blocks.SOUL_SAND.getDefaultState());
		}

		Chunk chunk = new Chunk(this.worldObj, chunkprimer, x, z);
		chunk.generateSkylightMap();
		return chunk;
	}

	public void generateSphere(ChunkPrimer chunkPrimer, int posX, int posY, int posZ, int radius, IBlockState... blockStates) {
		BlockPos origin = new BlockPos(posX, posY, posZ);
		Random rand = new Random(origin.hashCode());

		for (int x = posX - radius; x <= posX + radius; x++) {
			for (int y = posY - radius; y <= posY + radius; y++) {
				for (int z = posZ - radius; z <= posZ + radius; z++) {
					BlockPos pos = new BlockPos(x, y, z);

					if (pos.getDistance(origin.getX(), origin.getY(), origin.getZ()) < radius) {
						IBlockState state = blockStates[rand.nextInt(blockStates.length)];

						if(pos.down().getDistance(origin.getX(), origin.getY(), origin.getZ()) < radius && rand.nextInt(5) == 0)
							state = Blocks.CONCRETE_POWDER.getDefaultState().withProperty(BlockConcretePowder.COLOR, EnumDyeColor.SILVER);

						chunkPrimer.setBlockState(x, y, z, state);
					}
				}
			}
		}
	}

	@Override
	public void populate(int x, int z) {

	}

	@Override
	public boolean generateStructures(Chunk chunkIn, int x, int z) {
		return false;
	}

	@Override
	public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
		return this.worldObj.getBiome(pos).getSpawnableList(creatureType);
	}

	@Nullable
	@Override
	public BlockPos getNearestStructurePos(World worldIn, String structureName, BlockPos position, boolean findUnexplored) {
		return null;
	}

	@Override
	public void recreateStructures(Chunk chunkIn, int x, int z) {

	}

	@Override
	public boolean isInsideStructure(World worldIn, String structureName, BlockPos pos) {
		return false;
	}
}
