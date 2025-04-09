package lucraft.mods.pymtech.fluids;

import lucraft.mods.lucraftcore.sizechanging.capabilities.CapabilitySizeChanging;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.entity.SizeChangerPymParticles;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import javax.annotation.Nonnull;

public class BlockPymParticlesFluid extends BlockFluidClassic {

	public float size;

	public BlockPymParticlesFluid(Fluid fluid, Material material, float size) {
		super(fluid, material);
		this.size = size;
	}

	@Nonnull
	@Override
	public String getTranslationKey() {
		Fluid fluid = FluidRegistry.getFluid(fluidName);
		if (fluid != null) {
			return fluid.getUnlocalizedName();
		}
		return super.getTranslationKey();
	}

	@Override
	public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		if(entityIn.hasCapability(CapabilitySizeChanging.SIZE_CHANGING_CAP, null)) {
			entityIn.getCapability(CapabilitySizeChanging.SIZE_CHANGING_CAP, null).setSize(this.size, SizeChangerPymParticles.PYM_PARTICLES);
		}
	}
}
