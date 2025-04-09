package lucraft.mods.pymtech.dimensions.microscopic;

import lucraft.mods.pymtech.PymTech;
import lucraft.mods.pymtech.entities.EntityTardigrade;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = PymTech.MODID)
public class BiomeMicroscopic extends Biome {

    public static Biome MICROSCOPIC_BIOME = new BiomeMicroscopic(new BiomeProperties("microscopic").setRainDisabled()).setRegistryName(PymTech.MODID, "microscopic");

    @SubscribeEvent
    public static void onRegisterBiomes(RegistryEvent.Register<Biome> e) {
        e.getRegistry().register(MICROSCOPIC_BIOME);
    }

    public BiomeMicroscopic(BiomeProperties properties) {
        super(properties);
        this.spawnableMonsterList.clear();
        this.spawnableCaveCreatureList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableWaterCreatureList.clear();

        this.spawnableMonsterList.add(new SpawnListEntry(EntityTardigrade.class, 300, 2, 5));
        this.spawnableCreatureList.add(new SpawnListEntry(EntityTardigrade.class, 300, 2, 5));

    }

}
