package lucraft.mods.pymtech.tileentities;

import com.mojang.authlib.GameProfile;
import lucraft.mods.lucraftcore.util.energy.EnergyStorageExt;
import lucraft.mods.lucraftcore.util.fluids.FluidTankExt;
import lucraft.mods.lucraftcore.util.fluids.LCFluidUtil;
import lucraft.mods.lucraftcore.util.helper.PlayerHelper;
import lucraft.mods.pymtech.PTConfig;
import lucraft.mods.pymtech.PymTech;
import lucraft.mods.pymtech.entities.EntityShrunkenStructure;
import lucraft.mods.pymtech.fluids.PTFluids;
import lucraft.mods.pymtech.sounds.PTSoundEvents;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityStructureShrinker extends TileEntity implements ITickable {

    public static final int TANK_CAPACITY = 5000;

    public BlockPos area = new BlockPos(0, 0, 0);

    protected ItemStackHandler fluidSlots = new ItemStackHandler(2) {
        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            return FluidUtil.getFluidHandler(stack) != null;
        }

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }

        @Override
        protected void onContentsChanged(int slot) {
            ItemStack stack = this.getStackInSlot(slot);
            if (stack.isEmpty())
                return;
            if (slot == 0) {
                ItemStack result = LCFluidUtil.transferFluidFromItemToTank(stack, fluidTank, fluidSlots);
                if (!result.isEmpty()) {
                    fluidTank.update = true;
                    this.setStackInSlot(slot, result);
                    PlayerHelper.playSoundToAll(world, getPos().getX(), getPos().getY(), getPos().getZ(), 50, fluidTank.getFluid().getFluid().getEmptySound(fluidTank.getFluid()), SoundCategory.BLOCKS);
                }
            } else {
                ItemStack result = LCFluidUtil.transferFluidFromTankToItem(stack, fluidTank, fluidSlots);
                if (!result.isEmpty()) {
                    fluidTank.update = true;
                    this.setStackInSlot(slot, result);
                    FluidStack fluidStack = FluidUtil.getFluidContained(result);
                    if (fluidStack != null)
                        PlayerHelper.playSoundToAll(world, getPos().getX(), getPos().getY(), getPos().getZ(), 50, fluidStack.getFluid().getFillSound(fluidStack), SoundCategory.BLOCKS);
                }
            }
        }
    };

    public EnergyStorageExt energyStorage = new EnergyStorageExt(100000, 100000, 100000);

    public FluidTankExt fluidTank = new FluidTankExt(TANK_CAPACITY) {
        @Override
        public boolean canFillFluidType(FluidStack fluid) {
            return fluid.getFluid() == PTFluids.SHRINK_PYM_PARTICLES;
        }
    };

    public boolean showBox;
    private String customName;

    public void changeSettings(BlockPos areaIn, boolean showBox) {
        this.area = areaIn;

        if (area.getX() > PTConfig.STRUCTURE_SHRINKER_LIMIT)
            this.area = new BlockPos(PTConfig.STRUCTURE_SHRINKER_LIMIT, area.getY(), area.getZ());
        if (area.getX() < -PTConfig.STRUCTURE_SHRINKER_LIMIT)
            this.area = new BlockPos(-PTConfig.STRUCTURE_SHRINKER_LIMIT, area.getY(), area.getZ());
        if (area.getY() > PTConfig.STRUCTURE_SHRINKER_LIMIT)
            this.area = new BlockPos(area.getX(), PTConfig.STRUCTURE_SHRINKER_LIMIT, area.getZ());
        if (area.getY() < -PTConfig.STRUCTURE_SHRINKER_LIMIT)
            this.area = new BlockPos(area.getX(), -PTConfig.STRUCTURE_SHRINKER_LIMIT, area.getZ());
        if (area.getZ() > PTConfig.STRUCTURE_SHRINKER_LIMIT)
            this.area = new BlockPos(area.getX(), area.getY(), PTConfig.STRUCTURE_SHRINKER_LIMIT);
        if (area.getZ() < -PTConfig.STRUCTURE_SHRINKER_LIMIT)
            this.area = new BlockPos(area.getX(), area.getY(), -PTConfig.STRUCTURE_SHRINKER_LIMIT);

        this.showBox = showBox;
        this.markDirty();
    }

    public void shrink(@Nullable EntityPlayer player) {
        if (!canShrink(false) || !(world instanceof WorldServer))
            return;

        EntityPlayer player1 = player != null ? player : new FakePlayer((WorldServer) this.world, new GameProfile(null, PymTech.NAME));
        PlayerHelper.playSoundToAll(this.world, this.pos.getX(), this.pos.getY(), this.pos.getZ(), 50, PTSoundEvents.BUTTON, SoundCategory.BLOCKS);
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(this.getPos().getX() + 1, this.getPos().getY(), this.getPos().getZ() + 1, this.getPos().getX() + 1 + area.getX(), this.getPos().getY() + this.area.getY(), this.getPos().getZ() + 1 + this.area.getZ());
        EntityShrunkenStructure entity = new EntityShrunkenStructure(this.world, axisAlignedBB, (p) -> (!MinecraftForge.EVENT_BUS.post(new BlockEvent.BreakEvent(world, p, world.getBlockState(p), player1)) && world.getBlockState(p).getBlockHardness(world, p) != -1F), true);
        entity.setShrinking(true);
        entity.rotationYaw = 90;

        this.world.spawnEntity(entity);
    }

    public boolean canShrink(boolean simulate) {
        if (this.energyStorage.extractEnergy(10000, true) != 10000)
            return false;
        if (this.fluidTank.getFluidAmount() < 1000)
            return false;

        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(this.getPos().getX() + 1, this.getPos().getY(), this.getPos().getZ() + 1, this.getPos().getX() + 1 + area.getX(), this.getPos().getY() + this.area.getY(), this.getPos().getZ() + 1 + this.area.getZ());
        boolean empty = true;
        for (int x = (int) axisAlignedBB.minX; x < axisAlignedBB.maxX; x++) {
            for (int y = (int) axisAlignedBB.minY; y < axisAlignedBB.maxY; y++) {
                for (int z = (int) axisAlignedBB.minZ; z < axisAlignedBB.maxZ; z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    if (!this.world.isAirBlock(pos)) {
                        empty = false;
                    }
                }
            }
        }

        if (empty)
            return false;

        if (!simulate) {
            this.energyStorage.extractEnergy(10000, false);
            this.fluidTank.drain(1000, true);
        }

        return true;
    }

    @Override
    public void update() {
        if (this.world.isRemote)
            return;

        boolean update = false;

        if (energyStorage.update) {
            update = true;
            energyStorage.update = false;
        }

        if (fluidTank.update) {
            update = true;
            fluidTank.update = false;
        }

        if (update)
            this.markDirty();
    }

    @Override
    public void markDirty() {
        super.markDirty();
        this.getWorld().notifyBlockUpdate(getPos(), getWorld().getBlockState(getPos()), getWorld().getBlockState(getPos()), 3);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || capability == CapabilityEnergy.ENERGY || capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return (T) this.fluidSlots;
        else if (capability == CapabilityEnergy.ENERGY)
            return (T) this.energyStorage;
        else if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return (T) this.fluidTank;
        return super.getCapability(capability, facing);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.area = NBTUtil.getPosFromTag(compound.getCompoundTag("Area"));
        this.fluidSlots.deserializeNBT(compound.getCompoundTag("FluidItems"));
        this.energyStorage.deserializeNBT(compound);
        this.fluidTank.readFromNBT(compound.getCompoundTag("FluidTank"));
        this.showBox = compound.getBoolean("ShowBox");
        if (compound.hasKey("CustomName", 8))
            this.customName = compound.getString("CustomName");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setTag("Area", NBTUtil.createPosTag(this.area));
        compound.setTag("FluidItems", fluidSlots.serializeNBT());
        compound.setInteger("Energy", this.energyStorage.getEnergyStored());
        compound.setTag("FluidTank", this.fluidTank.writeToNBT(new NBTTagCompound()));
        compound.setBoolean("ShowBox", this.showBox);
        if (this.customName != null && !this.customName.isEmpty())
            compound.setString("CustomName", this.customName);
        return super.writeToNBT(compound);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        this.area = NBTUtil.getPosFromTag(pkt.getNbtCompound().getCompoundTag("Area"));
        this.energyStorage.deserializeNBT(pkt.getNbtCompound());
        this.fluidTank.readFromNBT(pkt.getNbtCompound().getCompoundTag("FluidTank"));
        this.showBox = pkt.getNbtCompound().getBoolean("ShowBox");
        world.markBlockRangeForRenderUpdate(getPos(), getPos());
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setTag("Area", NBTUtil.createPosTag(this.area));
        tag.setInteger("Energy", this.energyStorage.getEnergyStored());
        tag.setTag("FluidTank", this.fluidTank.writeToNBT(new NBTTagCompound()));
        tag.setBoolean("ShowBox", this.showBox);
        return new SPacketUpdateTileEntity(getPos(), 1, tag);
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound nbt = super.getUpdateTag();
        nbt.setTag("Area", NBTUtil.createPosTag(this.area));
        nbt.setInteger("Energy", this.energyStorage.getEnergyStored());
        nbt.setTag("FluidTank", this.fluidTank.writeToNBT(new NBTTagCompound()));
        nbt.setBoolean("ShowBox", this.showBox);
        return nbt;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        if (!showBox)
            return super.getRenderBoundingBox();
        return super.getRenderBoundingBox().expand(this.area.getX(), this.area.getY(), this.area.getZ());
    }

    public void setCustomInventoryName(String name) {
        this.customName = name;
    }

    @Override
    public ITextComponent getDisplayName() {
        return this.customName != null && !this.customName.isEmpty() ? new TextComponentString(customName) : new TextComponentTranslation("tile.structure_shrinker.name", new Object[0]);
    }

}
