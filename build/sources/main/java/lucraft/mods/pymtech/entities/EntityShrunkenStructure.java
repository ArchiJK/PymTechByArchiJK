package lucraft.mods.pymtech.entities;

import com.mojang.authlib.GameProfile;
import lucraft.mods.lucraftcore.util.helper.PlayerHelper;
import lucraft.mods.pymtech.PymTech;
import lucraft.mods.pymtech.items.ItemShrunkenStructure;
import lucraft.mods.pymtech.items.PTItems;
import lucraft.mods.pymtech.network.MessageSyncStructureEntity;
import lucraft.mods.pymtech.network.PTPacketDispatcher;
import lucraft.mods.pymtech.sounds.PTSoundEvents;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class EntityShrunkenStructure extends Entity {

    private static final DataParameter<Boolean> SHRINKING = EntityDataManager.<Boolean>createKey(EntityShrunkenStructure.class, DataSerializers.BOOLEAN);

    public ItemShrunkenStructure.ShrunkenStructure shrunkenStructure;
    public float scale;
    public float prevScale;
    public EntityPlayer placer;

    public EntityShrunkenStructure(World worldIn) {
        super(worldIn);
        this.setSize(0.5F, 0.5F);
        this.setScale(1F);
    }

    public EntityShrunkenStructure(World worldIn, ItemShrunkenStructure.ShrunkenStructure shrunkenStructure) {
        super(worldIn);
        this.shrunkenStructure = shrunkenStructure;
        this.setSize(Math.max(this.shrunkenStructure.getSize().getX() * getScale(), this.shrunkenStructure.getSize().getZ()) * getScale(), this.shrunkenStructure.getSize().getY() * getScale());
        this.setShrinking(true);
        this.setScale(0.1F);
    }

    public EntityShrunkenStructure(World worldIn, AxisAlignedBB axisAlignedBB, Predicate<BlockPos> predicate, boolean destroy) {
        this(worldIn);
        this.setPosition((axisAlignedBB.minX + axisAlignedBB.maxX) / 2D, axisAlignedBB.minY, (axisAlignedBB.minZ + axisAlignedBB.maxZ) / 2D);
        BlockPos size = new BlockPos((int) (axisAlignedBB.maxX - axisAlignedBB.minX), (int) (axisAlignedBB.maxY - axisAlignedBB.minY), (int) (axisAlignedBB.maxZ - axisAlignedBB.minZ));
        this.setSize(Math.max(size.getX() * getScale(), size.getZ()) * getScale(), size.getY() * getScale());
        ItemShrunkenStructure.BlockData[][][] blockData = new ItemShrunkenStructure.BlockData[size.getX()][size.getY()][size.getZ()];

        for (int x = 0; x < axisAlignedBB.maxX - axisAlignedBB.minX; x++) {
            for (int y = 0; y < axisAlignedBB.maxY - axisAlignedBB.minY; y++) {
                for (int z = 0; z < axisAlignedBB.maxZ - axisAlignedBB.minZ; z++) {
                    BlockPos pos = new BlockPos(axisAlignedBB.minX + x, axisAlignedBB.minY + y, axisAlignedBB.minZ + z);
                    if (!worldIn.isAirBlock(pos) && predicate.test(pos)) {
                        if (worldIn.getTileEntity(pos) != null) {
                            clearShrunkenStructureItems(worldIn.getTileEntity(pos));
                            blockData[x][y][z] = new ItemShrunkenStructure.BlockData(worldIn.getBlockState(pos), worldIn.getTileEntity(pos).writeToNBT(new NBTTagCompound()));
                        } else {
                            blockData[x][y][z] = new ItemShrunkenStructure.BlockData(worldIn.getBlockState(pos), null);
                        }
                        if (destroy) {
                            if (this.world.getTileEntity(pos) != null)
                                this.world.removeTileEntity(pos);
                            this.world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
                        }
                    }
                }
            }
        }

        this.shrunkenStructure = new ItemShrunkenStructure.ShrunkenStructure(blockData, size);
    }

    public static void clearShrunkenStructureItems(TileEntity tileEntity) {
        if (tileEntity.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
            IItemHandler itemHandler = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
            for (int i = 0; i < itemHandler.getSlots(); i++) {
                ItemStack stack = itemHandler.getStackInSlot(i);

                if (stack.getItem() instanceof ItemShrunkenStructure || stack.getItem() instanceof ItemShulkerBox) {
                    InventoryHelper.spawnItemStack(tileEntity.getWorld(), tileEntity.getPos().getX(), tileEntity.getPos().getY(), tileEntity.getPos().getZ(), stack);
                    itemHandler.extractItem(i, stack.getCount(), false);
                }
            }
        } else if (tileEntity instanceof IInventory) {
            IInventory inventory = (IInventory) tileEntity;

            for (int i = 0; i < inventory.getSizeInventory(); i++) {
                ItemStack stack = inventory.getStackInSlot(i);

                if (stack.getItem() instanceof ItemShrunkenStructure || stack.getItem() instanceof ItemShulkerBox) {
                    InventoryHelper.spawnItemStack(tileEntity.getWorld(), tileEntity.getPos().getX(), tileEntity.getPos().getY(), tileEntity.getPos().getZ(), stack);
                    inventory.setInventorySlotContents(i, ItemStack.EMPTY);
                }
            }
        }
    }

    @Override
    protected void entityInit() {
        this.dataManager.register(SHRINKING, true);
    }

    public float getScale() {
        return this.scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public boolean isShrinking() {
        return this.dataManager.get(SHRINKING);
    }

    public void setShrinking(boolean shrinking) {
        this.dataManager.set(SHRINKING, shrinking);
    }

    @Override
    public EnumActionResult applyPlayerInteraction(EntityPlayer player, Vec3d vec, EnumHand hand) {
        if (this.world.isRemote || this.isDead)
            return EnumActionResult.PASS;

        if (player.isSneaking()) {
            if (this.timeUntilPortal == 0) {
                this.shrunkenStructure = ItemShrunkenStructure.rotateCounterclockwise(this.shrunkenStructure);
                this.sync();
                this.timeUntilPortal = 10;
            }
            return EnumActionResult.SUCCESS;
        } else {
            ItemStack stack = new ItemStack(PTItems.SHRUNKEN_STRUCTURE);
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setTag("ShrunkenStructure", this.shrunkenStructure.serializeNBT());
            stack.setTagCompound(nbt);
            PlayerHelper.givePlayerItemStack(player, stack);
            this.setDead();
            return EnumActionResult.SUCCESS;
        }
    }

    public void placeBlocks(@Nullable EntityPlayer player) {
        if (this.world.isRemote || !(this.world instanceof WorldServer))
            return;

        for (int x = 0; x < this.shrunkenStructure.getSize().getX(); x++) {
            for (int y = 0; y < this.shrunkenStructure.getSize().getY(); y++) {
                for (int z = 0; z < this.shrunkenStructure.getSize().getZ(); z++) {
                    ItemShrunkenStructure.BlockData data = this.shrunkenStructure.getData()[x][y][z];
                    IBlockState state = data == null ? null : data.getBlock();
                    NBTTagCompound tileEntity = data == null ? null : data.getTileEntityData();

                    if (state != null && state.getBlock() != Blocks.AIR) {
                        Vec3d origin = new Vec3d(this.getPosition().getX(), this.getPosition().getY(), this.getPosition().getZ());
                        Vec3d v = new Vec3d(x - (this.shrunkenStructure.getSize().getX() / 2), y, z - (this.shrunkenStructure.getSize().getZ() / 2));
                        BlockPos pos = new BlockPos(origin.x + v.x, origin.y + v.y, origin.z + v.z);
                        IBlockState current = this.world.getBlockState(pos);
                        EntityPlayer p = player == null ? new FakePlayer((WorldServer) this.world, new GameProfile(null, PymTech.NAME)) : player;

                        if (current.getBlockHardness(world, pos) == -1F || MinecraftForge.EVENT_BUS.post(new BlockEvent.BreakEvent(this.world, pos, current, p))) {
                            state.getBlock().dropBlockAsItem(this.world, pos, state, 0);
                        } else if (current.getBlockHardness(world, pos) > state.getBlockHardness(world, pos)) {
                            state.getBlock().dropBlockAsItem(this.world, pos, state, 0);
                        } else {
                            this.world.destroyBlock(pos, true);
                            this.world.setBlockState(pos, state, 2);

                            if (tileEntity != null && this.world.getTileEntity(pos) != null) {
                                tileEntity.setInteger("x", pos.getX());
                                tileEntity.setInteger("y", pos.getY());
                                tileEntity.setInteger("z", pos.getZ());
                                this.world.getTileEntity(pos).readFromNBT(tileEntity);
                            }
                        }
                    }
                }
            }
        }
        setDead();
    }

    @Override
    protected void setSize(float width, float height) {
        Vec3d pos = this.getPositionVector();
        float w = width / 2F;
        this.width = w;
        this.height = height;
        this.setEntityBoundingBox(new AxisAlignedBB(pos.x - w, pos.y, pos.z - w, pos.x + w, pos.y + height, pos.z + w));
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        this.setShrinking(compound.getBoolean("Shrinking"));
        this.setScale(compound.getFloat("Scale"));
        this.shrunkenStructure = new ItemShrunkenStructure.ShrunkenStructure(compound.getCompoundTag("ShrunkenStructure"));
        this.setSize(Math.max(this.shrunkenStructure.getSize().getX() * getScale(), this.shrunkenStructure.getSize().getZ()) * getScale(), this.shrunkenStructure.getSize().getY() * getScale());
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        compound.setBoolean("Shrinking", this.isShrinking());
        compound.setFloat("Scale", this.getScale());
        compound.setTag("ShrunkenStructure", this.shrunkenStructure.serializeNBT());
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return !this.isDead;
    }

    @Override
    public void onEntityUpdate() {
        if (this.shrunkenStructure == null) {
            if (!this.world.isRemote)
                this.setDead();
            return;
        }
        super.onEntityUpdate();
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        double d0 = this.motionX;
        double d1 = this.motionY;
        double d2 = this.motionZ;

        if (!this.hasNoGravity()) {
            this.motionY -= 0.03999999910593033D;
        }

        if (this.world.isRemote) {
            this.noClip = false;
        } else {
            this.noClip = this.pushOutOfBlocks(this.posX, (this.getEntityBoundingBox().minY + this.getEntityBoundingBox().maxY) / 2.0D, this.posZ);
        }

        this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
        boolean flag = (int) this.prevPosX != (int) this.posX || (int) this.prevPosY != (int) this.posY || (int) this.prevPosZ != (int) this.posZ;

        if (flag || this.ticksExisted % 25 == 0) {
            if (this.world.getBlockState(new BlockPos(this)).getMaterial() == Material.LAVA) {
                this.motionY = 0.20000000298023224D;
                this.motionX = (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
                this.motionZ = (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
                this.playSound(SoundEvents.ENTITY_GENERIC_BURN, 0.4F, 2.0F + this.rand.nextFloat() * 0.4F);
            }
        }

        float f = 0.98F;

        if (this.onGround) {
            BlockPos underPos = new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.getEntityBoundingBox().minY) - 1, MathHelper.floor(this.posZ));
            net.minecraft.block.state.IBlockState underState = this.world.getBlockState(underPos);
            f = underState.getBlock().getSlipperiness(underState, this.world, underPos, this) * 0.98F;
        }

        this.motionX *= (double) f;
        this.motionY *= 0.9800000190734863D;
        this.motionZ *= (double) f;

        if (this.onGround) {
            this.motionY *= -0.5D;
        }

        this.handleWaterMovement();

        if (!this.world.isRemote) {
            double d3 = this.motionX - d0;
            double d4 = this.motionY - d1;
            double d5 = this.motionZ - d2;
            double d6 = d3 * d3 + d4 * d4 + d5 * d5;

            if (d6 > 0.01D) {
                this.isAirBorne = true;
            }
        }

        this.prevScale = this.getScale();

        if (!this.world.isRemote) {
            if (this.isShrinking() && this.getScale() >= 1F) {
                PlayerHelper.playSoundToAll(world, posX, posY, posZ, 50, PTSoundEvents.SHRINK, SoundCategory.NEUTRAL);
            }

            if (!this.isShrinking() && this.getScale() <= 0.1F) {
                PlayerHelper.playSoundToAll(world, posX, posY, posZ, 50, PTSoundEvents.GROW, SoundCategory.NEUTRAL);
            }
        }

        if (this.isShrinking() && this.getScale() > 0.1F) {
            this.setScale(getScale() - 0.05F);

            if (this.ticksExisted % 2 == 0 && this.world.isRemote) {
                this.world.spawnEntity(new EntityShrunkenStructureTrail(this.world, this, 10));
            }
        } else if (!this.isShrinking() && this.getScale() < 1F) {
            this.setScale(getScale() + 0.05F);
            if (this.ticksExisted % 2 == 0 && this.world.isRemote) {
                this.world.spawnEntity(new EntityShrunkenStructureTrail(this.world, this, 10));
            }
        }

        if (this.getScale() >= 1F && !this.isShrinking()) {
            this.placeBlocks(this.placer);
        }

        this.setSize(Math.max(this.shrunkenStructure.getSize().getX() * getScale(), this.shrunkenStructure.getSize().getZ()) * getScale(), this.shrunkenStructure.getSize().getY() * getScale());
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        sync();
    }

    public void sync() {
        if (this.getEntityWorld() instanceof WorldServer) {
            for (EntityPlayer players : ((WorldServer) this.getEntityWorld()).getEntityTracker().getTrackingPlayers(this)) {
                if (players instanceof EntityPlayerMP) {
                    PTPacketDispatcher.sendTo(new MessageSyncStructureEntity(this), (EntityPlayerMP) players);
                }
            }
        }
    }

    @Mod.EventBusSubscriber(modid = PymTech.MODID)
    public static class EventHandler {

        @SubscribeEvent
        public static void onPlayerStartTracking(PlayerEvent.StartTracking e) {
            if (e.getTarget() instanceof EntityShrunkenStructure && e.getEntityPlayer() instanceof EntityPlayerMP) {
                PTPacketDispatcher.sendTo(new MessageSyncStructureEntity((EntityShrunkenStructure) e.getTarget()), (EntityPlayerMP) e.getEntityPlayer());
            }
        }

    }

}
