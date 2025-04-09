package lucraft.mods.pymtech.entities;

import lucraft.mods.pymtech.PymTech;
import lucraft.mods.pymtech.client.render.entities.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = PymTech.MODID)
public class PTEntities {

    @SubscribeEvent
    public static void onRegisterEntities(RegistryEvent.Register<EntityEntry> e) {
        int id = 0;

        EntityRegistry.registerModEntity(new ResourceLocation(PymTech.MODID, "pym_particle_disc"), EntityPymParticleDisc.class, "pym_particle_disc", id++, PymTech.INSTANCE, 60, 1, true);
        EntityRegistry.registerModEntity(new ResourceLocation(PymTech.MODID, "qr_transition"), EntityQRTransition.class, "qr_transition", id++, PymTech.INSTANCE, 60, 1, true);
        EntityRegistry.registerModEntity(new ResourceLocation(PymTech.MODID, "tardigrade"), EntityTardigrade.class, "tardigrade", id++, PymTech.INSTANCE, 60, 1, true);
        EntityRegistry.registerModEntity(new ResourceLocation(PymTech.MODID, "shrunken_structure"), EntityShrunkenStructure.class, "shrunken_structure", id++, PymTech.INSTANCE, 60, 1, true);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onRegisterModels(ModelRegistryEvent e) {
        RenderingRegistry.registerEntityRenderingHandler(EntityPymParticleDisc.class, RenderPymParticleDisc::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityQRTransition.class, RenderInvisible::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityTardigrade.class, RenderTardigrade::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityShrunkenStructure.class, RenderShrunkenStructure::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityShrunkenStructureTrail.class, RenderShrunkenStructureTrail::new);
    }

}
