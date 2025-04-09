package net.minecraft.client.renderer.entity;

import lucraft.mods.lucraftcore.karma.potions.PotionKnockOut;
import lucraft.mods.lucraftcore.network.LCPacketDispatcher;
import lucraft.mods.lucraftcore.sizechanging.capabilities.ISizeChanging;
import lucraft.mods.lucraftcore.sizechanging.entities.EntitySizeChanging;
import lucraft.mods.lucraftcore.sizechanging.sizechanger.DefaultSizeChanger;
import lucraft.mods.lucraftcore.sizechanging.sizechanger.SizeChanger;
import lucraft.mods.lucraftcore.superpowers.render.SuperpowerRenderLayer;
import lucraft.mods.lucraftcore.superpowers.suitsets.SuitSet;
import lucraft.mods.lucraftcore.util.attributes.LCAttributes;
import lucraft.mods.lucraftcore.util.helper.PlayerHelper;
import lucraft.mods.lucraftcore.util.items.OpenableArmor;
import lucraft.mods.lucraftcore.util.network.MessageSyncPotionEffects;
import lucraft.mods.pymtech.PymTech;
import lucraft.mods.pymtech.sounds.PTSoundEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@Mod.EventBusSubscriber(modid = PymTech.MODID)
public class SizeChangerPymParticles extends DefaultSizeChanger {

    public static final DamageSource SUFFOCATE = (new DamageSource("suffocate")).setDamageBypassesArmor();
    public static final SizeChanger PYM_PARTICLES = new SizeChangerPymParticles().setRegistryName(PymTech.MODID, "pym_particles");
    public static final SizeChanger PYM_PARTICLES_SUBATOMIC = new SizeChangerPymParticlesSubatomic().setRegistryName(PymTech.MODID, "pym_particles_subatomic");


    @SubscribeEvent
    public static void onRegisterSizeChanger(RegistryEvent.Register<SizeChanger> e) {
        e.getRegistry().register(PYM_PARTICLES);
        e.getRegistry().register(PYM_PARTICLES_SUBATOMIC);
    }

    @Override
    public int getSizeChangingTime(EntityLivingBase entity, ISizeChanging data, float estimatedSize) {
        return 10;
    }

    @Override
    public void onSizeChanged(EntityLivingBase entity, ISizeChanging data, float size) {
        AbstractAttributeMap map = entity.getAttributeMap();
        setAttribute(map, SharedMonsterAttributes.MOVEMENT_SPEED, size > 1F ? 0 : (size - 1F) * 0.5D, size > 1F ? 0 : 2, SizeChanger.ATTRIBUTE_UUID);
        setAttribute(map, LCAttributes.JUMP_HEIGHT, size > 1F ? (size - 1F) * 1D : (size == 1F ? 0 : size), 0, SizeChanger.ATTRIBUTE_UUID);
        setAttribute(map, LCAttributes.FALL_RESISTANCE, size > 1F ? 1F / size : size, 1, SizeChanger.ATTRIBUTE_UUID);
        setAttribute(map, LCAttributes.STEP_HEIGHT, size, 1, SizeChanger.ATTRIBUTE_UUID);
        setAttribute(map, SharedMonsterAttributes.ATTACK_DAMAGE, size > 1F ? (size - 1F) * 1D : (size == 1F ? 0 : (1F - size) * 10), 0, SizeChanger.ATTRIBUTE_UUID);
        setAttribute(map, EntityPlayer.REACH_DISTANCE, (size - 1F) * 1D, 0, SizeChanger.ATTRIBUTE_UUID);
        setAttribute(map, SharedMonsterAttributes.KNOCKBACK_RESISTANCE, (size - 1F) * 0.5D, 0, SizeChanger.ATTRIBUTE_UUID);

        if (entity.world.isRemote && entity.ticksExisted % 2 == 0) {
            data.spawnEntity(data.getSizeChanger(), 10, size);
        }
    }

    @Override
    public void onUpdate(EntityLivingBase entity, ISizeChanging data, float size) {
        if (entity.world.isRemote)
            return;

        if ((size < 0.5F || size > 3) && entity instanceof EntityPlayer) {
            SuitSet suitSet = SuitSet.getSuitSet(entity);
            ItemStack helmet = entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD);

            if (suitSet == null || helmet.isEmpty() || suitSet.getData() == null || !suitSet.getData().getBoolean("protect_from_suffocating") || (helmet.getItem() instanceof OpenableArmor.IOpenableArmor && ((OpenableArmor.IOpenableArmor) helmet.getItem()).isArmorOpen(entity, helmet))) {
                entity.attackEntityFrom(SUFFOCATE, 1);
            }
        }

        String nbtKey = PymTech.MODID + ":PassOutTimer";
        if (size > 1) {
            int timer = (int) (entity.getEntityData().getInteger(nbtKey) + size);
            entity.getEntityData().setInteger(nbtKey, timer);

            if (timer >= 9600) {
                entity.addPotionEffect(new PotionEffect(PotionKnockOut.POTION_KNOCK_OUT, 20));
                entity.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 20));
                LCPacketDispatcher.sendToAll(new MessageSyncPotionEffects(entity));

                if (timer >= 12000) {
                    data.setSize(1, this);
                }
            }
        } else if (entity.getEntityData().getInteger(nbtKey) > 0) {
            entity.getEntityData().setInteger(nbtKey, 0);
        }
    }

    @Override
    public boolean start(EntityLivingBase entity, ISizeChanging data, float size, float estimatedSize) {
        PlayerHelper.playSoundToAll(entity.world, entity.posX, entity.posY, entity.posZ, 50, estimatedSize < size ? PTSoundEvents.SHRINK : PTSoundEvents.GROW, SoundCategory.NEUTRAL);
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void renderEntity(EntitySizeChanging entity, double x, double y, double z, float entityYaw, float partialTicks) {
        if (entity.renderer == null)
            return;

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.enableBlend();
        float alpha = 1F - ((float) entity.ticksExisted / (float) entity.lifeTime);
        GlStateManager.color(1, 1, 1, alpha / 1.4F);

        GlStateManager.rotate(180, 1, 0, 0);
        GlStateManager.translate(0, -1.4375F * entity.size + 0, 0);
        GlStateManager.rotate(entity.parent.renderYawOffset, 0, 1, 0);

        // 0.76 -> -1.1
        // 0.6
        // 0.44
        // 0.28 -> -0.41

        float f = entity.size;
        GlStateManager.scale(f, f, f);
        entity.renderer.preRenderCallback(entity.parent, partialTicks);

        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
        Minecraft.getMinecraft().renderEngine.bindTexture(SuperpowerRenderLayer.WHITE_TEX);
        entity.renderer.getMainModel().render(entity.parent, entity.limbSwing, entity.limbSwingAmount, entity.ageInTicks, entity.netHeadYaw, entity.headPitch, entity.scale);
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);

        GlStateManager.color(1, 1, 1, alpha / 1.6F);
        entity.renderer.getMainModel().render(entity.parent, entity.limbSwing, entity.limbSwingAmount, entity.ageInTicks, entity.netHeadYaw, entity.headPitch, entity.scale);

        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
}
