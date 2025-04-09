package lucraft.mods.pymtech.abilities;

import lucraft.mods.lucraftcore.superpowers.abilities.AbilityEntry;
import lucraft.mods.pymtech.PymTech;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = PymTech.MODID)
public class PTAbilities {

    @SubscribeEvent
    public static void registerAbilities(RegistryEvent.Register<AbilityEntry> e) {
        e.getRegistry().register(new AbilityEntry(AbilityPymParticleSize.class, new ResourceLocation(PymTech.MODID, "pym_particle_size")));
        e.getRegistry().register(new AbilityEntry(AbilityRegulator.class, new ResourceLocation(PymTech.MODID, "regulator")));
        e.getRegistry().register(new AbilityEntry(AbilityShrinkSuit.class, new ResourceLocation(PymTech.MODID, "shrink_suit")));
    }

}
