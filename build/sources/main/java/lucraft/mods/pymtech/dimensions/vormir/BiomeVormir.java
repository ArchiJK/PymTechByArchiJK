package lucraft.mods.pymtech.dimensions.vormir;

import lucraft.mods.pymtech.blocks.PTBlocks;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber(
        modid = "pymtech"
)
public class BiomeVormir extends Biome {
    public static Biome VORMIR_BIOME = (Biome)(new BiomeVormir((new BiomeProperties("vormir")).setHeightVariation(0.03F))).setRegistryName("pymtech", "vormir");

    @SubscribeEvent
    public static void onRegisterBiomes(RegistryEvent.Register<Biome> e) {
        e.getRegistry().register(VORMIR_BIOME);
        BiomeDictionary.addTypes(VORMIR_BIOME, new Type[]{Type.SANDY});
    }

    public BiomeVormir(BiomeProperties properties) {
        super(properties);
        this.spawnableMonsterList.clear();
        this.spawnableCaveCreatureList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableWaterCreatureList.clear();
        this.topBlock = PTBlocks.BLUE_SAND.getDefaultState();
        this.fillerBlock = PTBlocks.BLUE_SANDSTONE.getDefaultState();
        this.decorator.treesPerChunk = -999;
    }
}
