package lucraft.mods.pymtech.entities;

import lucraft.mods.lucraftcore.sizechanging.capabilities.CapabilitySizeChanging;
import lucraft.mods.pymtech.items.ItemPymParticleDisc;
import net.minecraft.client.renderer.entity.SizeChangerPymParticles;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityPymParticleDisc extends EntityThrowable {

	private static final DataParameter<ItemStack> ITEM = EntityDataManager.<ItemStack> createKey(EntityPymParticleDisc.class, DataSerializers.ITEM_STACK);

	public EntityPymParticleDisc(World worldIn) {
		super(worldIn);
	}

	public EntityPymParticleDisc(World worldIn, EntityLivingBase throwerIn, ItemStack stack) {
		super(worldIn, throwerIn);
		this.setItem(stack);
	}

	@Override
	protected void entityInit() {
		this.getDataManager().register(ITEM, ItemStack.EMPTY);
	}

	@Override
	protected void onImpact(RayTraceResult result) {
		if (!this.world.isRemote && !this.isDead && result.entityHit != null && result.entityHit != this.getThrower() && !getItem().isEmpty() && getItem().getItem() instanceof ItemPymParticleDisc) {
			if(result.entityHit.hasCapability(CapabilitySizeChanging.SIZE_CHANGING_CAP, null)) {
				float current = result.entityHit.getCapability(CapabilitySizeChanging.SIZE_CHANGING_CAP, null).getSize();
				float newSize = ((ItemPymParticleDisc) getItem().getItem()).size;
				result.entityHit.getCapability(CapabilitySizeChanging.SIZE_CHANGING_CAP, null).setSize((current < 1F && newSize > 1F) || (current > 1F && newSize < 1F) ? 1F : newSize, SizeChangerPymParticles.PYM_PARTICLES);
			} else if(result.entityHit instanceof EntityShrunkenStructure && ((ItemPymParticleDisc) getItem().getItem()).size > 1F) {
				((EntityShrunkenStructure)result.entityHit).setShrinking(false);
				((EntityShrunkenStructure)result.entityHit).placer = this.getThrower() != null && this.getThrower() instanceof EntityPlayer ? (EntityPlayer) this.getThrower() : null;
			}
			this.setDead();
		}
	}

	public ItemStack getItem() {
		return (ItemStack) this.getDataManager().get(ITEM);
	}

	public void setItem(ItemStack stack) {
		this.getDataManager().set(ITEM, stack);
		this.getDataManager().setDirty(ITEM);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		NBTTagCompound nbttagcompound = compound.getCompoundTag("Item");
		this.setItem(new ItemStack(nbttagcompound));

		if (this.getItem().isEmpty())
			this.setDead();
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);

		if (!this.getItem().isEmpty())
			compound.setTag("Item", this.getItem().writeToNBT(new NBTTagCompound()));
	}
}
