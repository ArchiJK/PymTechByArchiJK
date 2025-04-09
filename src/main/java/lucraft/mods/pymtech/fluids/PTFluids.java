package lucraft.mods.pymtech.fluids;

import lucraft.mods.pymtech.PymTech;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = PymTech.MODID)
public class PTFluids {

	public static Fluid SHRINK_PYM_PARTICLES;
	public static Fluid GROW_PYM_PARTICLES;

    public static ResourceLocation PYM_PARTICLES_STILL = new ResourceLocation(PymTech.MODID, "fluids/pym_particles");
    public static ResourceLocation PYM_PARTICLES_FLOW = new ResourceLocation(PymTech.MODID, "fluids/pym_particles_flow");

	@SubscribeEvent
	public static void onRegisterBlocks(RegistryEvent.Register<Block> e) {
		FluidRegistry.registerFluid(SHRINK_PYM_PARTICLES = new Fluid("shrink_pym_particles", PYM_PARTICLES_STILL, PYM_PARTICLES_FLOW, 0xffce100a));
		FluidRegistry.registerFluid(GROW_PYM_PARTICLES = new Fluid("grow_pym_particles", PYM_PARTICLES_STILL, PYM_PARTICLES_FLOW, 0xff0a5ccb));

		e.getRegistry().register(new BlockPymParticlesFluid(SHRINK_PYM_PARTICLES, Material.WATER, 0.11F).setRegistryName("shrink_pym_particles"));
		e.getRegistry().register(new BlockPymParticlesFluid(GROW_PYM_PARTICLES, Material.WATER, 3F).setRegistryName("grow_pym_particles"));

		FluidRegistry.addBucketForFluid(SHRINK_PYM_PARTICLES);
		FluidRegistry.addBucketForFluid(GROW_PYM_PARTICLES);
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void onRegisterModels(ModelRegistryEvent e) {
		ModelLoader.setCustomStateMapper(SHRINK_PYM_PARTICLES.getBlock(), new FluidStateMapper(SHRINK_PYM_PARTICLES));
        ModelLoader.setCustomStateMapper(GROW_PYM_PARTICLES.getBlock(), new FluidStateMapper(GROW_PYM_PARTICLES));
	}

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onRegisterTexture(TextureStitchEvent.Pre e) {
        e.getMap().registerSprite(PYM_PARTICLES_STILL);
        e.getMap().registerSprite(PYM_PARTICLES_FLOW);
    }

	@SideOnly(Side.CLIENT)
	public static class FluidStateMapper extends StateMapperBase implements ItemMeshDefinition {

		public final ModelResourceLocation location;

		public FluidStateMapper(Fluid fluid) {
			this.location = new ModelResourceLocation(new ResourceLocation(PymTech.MODID, "pym_particles"), fluid.getName());
		}

		@Nonnull
		@Override
		protected ModelResourceLocation getModelResourceLocation(@Nonnull IBlockState state) {
			return location;
		}

		@Nonnull
		@Override
		public ModelResourceLocation getModelLocation(@Nonnull ItemStack stack) {
			return location;
		}
	}

}
