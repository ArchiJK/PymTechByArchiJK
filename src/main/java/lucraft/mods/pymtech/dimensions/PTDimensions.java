package lucraft.mods.pymtech.dimensions;

import lucraft.mods.lucraftcore.LucraftCore;
import lucraft.mods.lucraftcore.sizechanging.capabilities.CapabilitySizeChanging;
import lucraft.mods.lucraftcore.sizechanging.capabilities.ISizeChanging;
import lucraft.mods.lucraftcore.sizechanging.events.SizeChangeEvent;
import lucraft.mods.lucraftcore.util.helper.PlayerHelper;
import lucraft.mods.lucraftcore.util.particles.ParticleColoredCloud;
import lucraft.mods.pymtech.PTConfig;
import lucraft.mods.pymtech.PymTech;
import lucraft.mods.pymtech.dimensions.microscopic.WorldProviderMicroscopic;
import lucraft.mods.pymtech.dimensions.quantumvoid.WorldProviderQuantumRealm;
import lucraft.mods.pymtech.dimensions.vormir.WorldProviderVormir;
import lucraft.mods.pymtech.entities.EntityQRTransition;

import lucraft.mods.pymtech.network.MessageOpenQRGui;
import lucraft.mods.pymtech.network.PTPacketDispatcher;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.SizeChangerPymParticles;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.storage.MapDecoration;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@Mod.EventBusSubscriber(modid = PymTech.MODID)
public class PTDimensions {

    public static DimensionType QUANTUM_REALM;
    public static DimensionType MICROSCOPIC;
    public static DimensionType VORMIR;
    public static final int maxTransitionTime = 120;




    public static void init() {
        QUANTUM_REALM = DimensionType.register("quantum_realm", "quantum_realm", PTConfig.QUANTUM_REALM_DIMENSION_ID, WorldProviderQuantumRealm.class, false);
        MICROSCOPIC = DimensionType.register("microscopic", "microscopic", PTConfig.MICROSCOPIC_DIMENSION_ID, WorldProviderMicroscopic.class, false);
        VORMIR = DimensionType.register("vormir", "vormir", PTConfig.VORMIR_DIMENSION_ID, WorldProviderVormir.class, false);

        DimensionManager.registerDimension(PTConfig.QUANTUM_REALM_DIMENSION_ID, QUANTUM_REALM);
        DimensionManager.registerDimension(PTConfig.MICROSCOPIC_DIMENSION_ID, MICROSCOPIC);
        DimensionManager.registerDimension(PTConfig.VORMIR_DIMENSION_ID, VORMIR);



    }


    @SubscribeEvent
    public static void onRegisterBiomes(RegistryEvent.Register<Biome> e) {




    }

    public static boolean isQuantumDimension(int dimensionId) {
        return dimensionId == QUANTUM_REALM.getId() || dimensionId == MICROSCOPIC.getId();
    }

    public static void sendIntoQuantumRealm(EntityLivingBase entityLivingBase) {
        if (entityLivingBase instanceof EntityPlayer) {
            EntityQRTransition entity = new EntityQRTransition((EntityPlayer) entityLivingBase);
            entityLivingBase.world.spawnEntity(entity);
            entityLivingBase.startRiding(entity);
            if (entityLivingBase instanceof EntityPlayerMP) {
                PTPacketDispatcher.sendTo(new MessageOpenQRGui(), (EntityPlayerMP) entityLivingBase);
            }
        } else {
            transferBetweenDims(entityLivingBase, QUANTUM_REALM.getId());
        }
    }

    public static void transferBetweenDims(EntityLivingBase entity, int quantumDim) {
        if (entity.getRidingEntity() instanceof EntityQRTransition)
            entity.dismountRidingEntity();

        if (!PTDimensions.isQuantumDimension(entity.dimension)) {
            entity.getEntityData().setInteger("PreQRDim", entity.dimension);
            entity.getEntityData().setDouble("PreQRX", entity.posX);
            entity.getEntityData().setDouble("PreQRY", entity.posY);
            entity.getEntityData().setDouble("PreQRZ", entity.posZ);
            entity.changeDimension(quantumDim, (world, e, yaw) -> entity.setPositionAndUpdate(entity.posX, entity.posY, entity.posZ));
        } else {
            int dim = entity.getEntityData().getInteger("PreQRDim");
            double x = entity.getEntityData().getDouble("PreQRX");
            double y = entity.getEntityData().hasKey("PreQRY") ? entity.getEntityData().getDouble("PreQRY") : 90;
            double z = entity.getEntityData().getDouble("PreQRZ");
            entity.changeDimension(dim, (world, e, yaw) -> entity.setPositionAndUpdate(x, y, z));
        }
    }

    @SubscribeEvent
    public static void onChangeDimensions(PlayerEvent.PlayerChangedDimensionEvent e) {
        if (PTDimensions.isQuantumDimension(e.toDim)) {
            e.player.timeUntilPortal = 10;
            ISizeChanging data = e.player.getCapability(CapabilitySizeChanging.SIZE_CHANGING_CAP, null);
            data.setSizeDirectly(10F);
            data.setSize(1F, SizeChangerPymParticles.PYM_PARTICLES);
        } else if (PTDimensions.isQuantumDimension(e.fromDim)) {
            ISizeChanging data = e.player.getCapability(CapabilitySizeChanging.SIZE_CHANGING_CAP, null);
            data.setSizeDirectly(0.1F);
            data.setSize(1F, SizeChangerPymParticles.PYM_PARTICLES);
        }
    }

    @SubscribeEvent
    public static void onSizeChangePre(SizeChangeEvent.Pre e) {
        if (PTDimensions.isQuantumDimension(e.getEntity().dimension) && e.getSizeChanger() != SizeChangerPymParticles.PYM_PARTICLES_SUBATOMIC && e.getEntity().timeUntilPortal == 0) {
            e.setCanceled(true);
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onClientTickEvent(TickEvent.ClientTickEvent e) {
        EntityPlayer player = Minecraft.getMinecraft().player;

        if (player != null && isQuantumDimension(player.dimension) && !Minecraft.getMinecraft().isGamePaused()) {
            Random rand = new Random();
            if (player.ticksExisted % 1200 == 0) {
                Object[] sound = ForgeRegistries.SOUND_EVENTS.getKeys().toArray();
                ResourceLocation loc = (ResourceLocation) sound[rand.nextInt(sound.length)];
                while (loc.getPath().contains("music")) {
                    loc = (ResourceLocation) sound[rand.nextInt(sound.length)];
                }
                SoundEvent soundEvent = ForgeRegistries.SOUND_EVENTS.getValue(loc);
                player.playSound(soundEvent, 0.75F + (rand.nextFloat() / 2F), 0.5F + rand.nextFloat());
            }

            double spread = 200.0D;
            int dim = player.dimension;
            Biome biome = player.world.getBiome(player.getPosition());
            for (int i = 0; i < 30; i++) {
                if (dim == QUANTUM_REALM.getId()) {
                    LucraftCore.proxy.spawnParticle(ParticleColoredCloud.ID, player.posX + (rand.nextDouble() - 0.5D) * spread - spread / 4.0D * 0.20000000298023224D, player.posY + (rand.nextDouble() - 0.5D) * spread, player.posZ + (rand.nextDouble() - 0.5D) * spread, 0.20000000298023224D, 0.0D, 0.0D, 0, 200, 200);
                }
                else {
                    Vec3d brightColor = dim == PTDimensions.MICROSCOPIC.getId() ? new Vec3d(80, 40, 0) : new Vec3d(130, 80, 0);
                    Vec3d darkColor = dim == PTDimensions.MICROSCOPIC.getId() ? new Vec3d(50, 19, 0) : new Vec3d(100, 60, 0);
                    Vec3d color = brightColor.add(darkColor.subtract(brightColor).scale(rand.nextFloat()));
                    LucraftCore.proxy.spawnParticle(ParticleColoredCloud.ID, player.posX + (rand.nextDouble() - 0.5D) * spread - spread / 4.0D * 0.20000000298023224D, player.posY + (rand.nextDouble() - 0.5D) * spread, player.posZ + (rand.nextDouble() - 0.5D) * spread, 0.5F - rand.nextFloat(), 0.5F - rand.nextFloat(), 0.5F - rand.nextFloat(), (int) color.x, (int) color.y, (int) color.z);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onTick(LivingEvent.LivingUpdateEvent e) {
        if (PTDimensions.isQuantumDimension(e.getEntityLiving().dimension)) {
            if (e.getEntityLiving() instanceof EntityPlayer && ((EntityPlayer) e.getEntityLiving()).capabilities.isFlying)
                return;
            if (!e.getEntityLiving().onGround)
                e.getEntityLiving().motionY = e.getEntityLiving().moveForward != 0F ? -e.getEntityLiving().rotationPitch / 1000D : 0;
        }
    }

    @SubscribeEvent
    public static void onFish(PlayerInteractEvent.RightClickItem e) {
        if (!e.getItemStack().isEmpty() && e.getItemStack().getItem() == Items.FISH) {
            e.getEntityPlayer().changeDimension(e.getEntityPlayer().dimension == VORMIR.getId() ? 0 : VORMIR.getId(), (world, entity, yaw) -> {
                entity.setPositionAndUpdate(0.0, 90.0, 0.0);
            });
            ItemStack itemstack = ItemMap.setupNewMap(e.getEntityPlayer().world, 0.0, 0.0, (byte)2, true, true);
            ItemMap.renderBiomePreviewMap(e.getEntityPlayer().world, itemstack);
            MapData.addTargetDecoration(itemstack, new BlockPos(50, 50, 50), "+", MapDecoration.Type.RED_MARKER);
            PlayerHelper.givePlayerItemStack(e.getEntityPlayer(), itemstack);
        }

    }

}
