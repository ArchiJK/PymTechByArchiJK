package lucraft.mods.pymtech.entities;

import lucraft.mods.pymtech.PymTech;
import lucraft.mods.pymtech.client.gui.GuiQRTransition;
import lucraft.mods.pymtech.dimensions.PTDimensions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityQRTransition extends Entity {

    public EntityQRTransition(World worldIn) {
        super(worldIn);
        this.setSize(0.1F, 0.1F);
    }

    @Override
    protected void entityInit() {

    }

    public EntityQRTransition(EntityPlayer player) {
        super(player.world);
        this.setSize(0.1F, 0.1F);
        this.setPosition(player.posX, player.posY, player.posZ);
        player.startRiding(this, true);
    }

    @Override
    public void updatePassenger(Entity passenger) {
        if (this.isPassenger(passenger))
            passenger.setPosition(this.posX, this.posY + 0.1F, this.posZ);
    }

    @Override
    public boolean shouldRiderSit() {
        return false;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (this.world.isRemote && (this.getPassengers().size() == 0 || this.ticksExisted > PTDimensions.maxTransitionTime)) {
            this.setDead();
        }
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {

    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {

    }

    @Mod.EventBusSubscriber(modid = PymTech.MODID)
    public static class EventHandler {

        public static boolean isInTransition(EntityLivingBase entity) {
            return entity.getRidingEntity() instanceof EntityQRTransition;
        }

        @SubscribeEvent
        public static void onHurt(LivingHurtEvent e) {
            if (isInTransition(e.getEntityLiving()))
                e.setCanceled(true);
        }

        @SideOnly(Side.CLIENT)
        @SubscribeEvent
        public static void onRender(RenderPlayerEvent.Pre e) {
            if (isInTransition(e.getEntityLiving())) {
                e.setCanceled(true);
            }
        }

        @SubscribeEvent
        public static void visibility(PlayerEvent.Visibility e) {
            if (isInTransition(e.getEntityLiving()))
                e.modifyVisibility(0);
        }

        @SideOnly(Side.CLIENT)
        @SubscribeEvent
        public static void onSetupCamera(EntityViewRenderEvent.FOVModifier e) {
            if (e.getEntity() instanceof EntityLivingBase && isInTransition((EntityLivingBase) e.getEntity())) {
                EntityQRTransition entity = (EntityQRTransition) e.getEntity().getRidingEntity();
                GuiScreen gui = Minecraft.getMinecraft().currentScreen;
                if (gui instanceof GuiQRTransition) {
                    float f = (float) (((GuiQRTransition) gui).progress + e.getRenderPartialTicks()) / (PTDimensions.maxTransitionTime / 2F);
                    f = PTDimensions.isQuantumDimension(e.getEntity().dimension) ? 1 + f : 1 - f;
                    e.setFOV(e.getFOV() * f);
                }
            }
        }

    }

}
