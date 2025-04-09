package lucraft.mods.pymtech.suitsets;

import lucraft.mods.lucraftcore.sizechanging.capabilities.CapabilitySizeChanging;
import lucraft.mods.lucraftcore.superpowers.abilities.Ability;
import lucraft.mods.lucraftcore.superpowers.abilities.AbilityEnergyBlast;
import lucraft.mods.lucraftcore.superpowers.abilities.AbilityFlight;
import lucraft.mods.lucraftcore.superpowers.abilities.predicates.AbilityCondition;
import lucraft.mods.lucraftcore.superpowers.effects.EffectVibrating;
import lucraft.mods.lucraftcore.superpowers.suitsets.ItemSuitSetArmor;
import lucraft.mods.lucraftcore.superpowers.suitsets.SuitSet;
import lucraft.mods.lucraftcore.util.helper.LCRenderHelper;
import lucraft.mods.lucraftcore.util.helper.PlayerHelper;
import lucraft.mods.lucraftcore.util.helper.StringHelper;
import lucraft.mods.lucraftcore.util.render.ModelCache;
import lucraft.mods.pymtech.abilities.AbilityPymParticleSize;
import lucraft.mods.pymtech.abilities.AbilityRegulator;
import lucraft.mods.pymtech.abilities.AbilityShrinkSuit;
import lucraft.mods.pymtech.client.models.ModelAntManHelmet;
import lucraft.mods.pymtech.client.models.ModelWaspChestplate;
import lucraft.mods.pymtech.items.ItemPymParticleArmor;
import lucraft.mods.pymtech.sounds.PTSoundEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SuitSetPymParticles extends PTSuitSet {

    public static final int ANIMATION_TIME = 20;

    protected String name;
    protected String desc;
    protected boolean helmetAnimation;
    protected NBTTagCompound nbt;

    public SuitSetPymParticles(String name, String desc, boolean helmetAnimation) {
        super(name);
        this.name = name;
        this.desc = desc;
        this.helmetAnimation = helmetAnimation;
        this.nbt = new NBTTagCompound();
        this.nbt.setBoolean("protect_from_suffocating", true);
    }

    @Override
    public NBTTagCompound getData() {
        return this.nbt;
    }

    @Override
    public ItemArmor.ArmorMaterial getArmorMaterial(EntityEquipmentSlot slot) {
        if (this == PTSuitSet.ANT_MAN_2)
            return ItemArmor.ArmorMaterial.DIAMOND;
        return super.getArmorMaterial(slot);
    }

    @Override
    public ItemSuitSetArmor createItem(SuitSet suitSet, EntityEquipmentSlot slot) {
        return (ItemSuitSetArmor) new ItemPymParticleArmor(suitSet, slot).setRegistryName(suitSet.getRegistryName().getNamespace(), suitSet.getRegistryName().getPath() + (slot == EntityEquipmentSlot.HEAD ? "_helmet" : slot == EntityEquipmentSlot.CHEST ? "_chest" : slot == EntityEquipmentSlot.LEGS ? "_legs" : "_boots"));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModel(Item item, EntityEquipmentSlot slot) {
        if (slot == EntityEquipmentSlot.CHEST)
            ModelLoader.setCustomModelResourceLocation(getChestplate(), 0, new ModelResourceLocation(this.getRegistryName().getNamespace() + ":" + getUnlocalizedName() + "_chest"));
        else
            super.registerModel(item, slot);
    }

    @Override
    public boolean canOpenArmor(EntityEquipmentSlot slot) {
        return slot == EntityEquipmentSlot.HEAD;
    }

    @Override
    public boolean hasGlowyThings(EntityLivingBase entity, EntityEquipmentSlot slot) {
        if (this == PTSuitSet.ANT_MAN_3 || this == PTSuitSet.WASP)
            return slot == EntityEquipmentSlot.CHEST || slot == EntityEquipmentSlot.HEAD;
        return slot == EntityEquipmentSlot.CHEST;
    }

    @Override
    public List<String> getExtraDescription(ItemStack stack) {
        return Arrays.asList(StringHelper.translateToLocal(desc));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ModelBiped getArmorModel(SuitSet suitSet, ItemStack stack, Entity entity, EntityEquipmentSlot slot, boolean light, boolean smallArms, boolean open) {
        String key = suitSet.getRegistryName().toString() + "_" + suitSet.getArmorModelScale(slot) + "_" + suitSet.getArmorTexturePath(stack, entity, slot, false, smallArms, open) + "_" + suitSet.getArmorTexturePath(stack, entity, slot, true, smallArms, open) + "_" + slot.toString() + "_" + smallArms + "_" + EffectVibrating.isVibrating(entity);
        ModelBase model = ModelCache.getModel(key);
        if (model instanceof ModelBiped)
            return (ModelBiped) model;
        else
            return (ModelBiped) ModelCache.storeModel(key, slot == EntityEquipmentSlot.HEAD ? new ModelAntManHelmet(suitSet.getArmorModelScale(slot), suitSet.getArmorTexturePath(stack, entity, slot, false, smallArms, open), suitSet.getArmorTexturePath(stack, entity, slot, true, smallArms, open), suitSet, slot, smallArms, EffectVibrating.isVibrating(entity)) : (slot == EntityEquipmentSlot.CHEST && suitSet == PTSuitSet.WASP ? new ModelWaspChestplate(suitSet.getArmorModelScale(slot), suitSet.getArmorTexturePath(stack, entity, slot, false, smallArms, open), suitSet.getArmorTexturePath(stack, entity, slot, true, smallArms, open), suitSet, slot, smallArms, EffectVibrating.isVibrating(entity)) : super.getArmorModel(suitSet, stack, entity, slot, light, smallArms, open)));
    }

    @Override
    public String getArmorTexturePath(ItemStack stack, Entity entity, EntityEquipmentSlot slot, boolean light, boolean smallArms, boolean open) {
        if (slot == EntityEquipmentSlot.HEAD && helmetAnimation) {
            return getModId() + ":textures/models/armor/" + this.getRegistryName().getPath() + "/helmet" + (light ? "_lights" : "") + ".png";
        }
        return super.getArmorTexturePath(stack, entity, slot, light, smallArms, open);
    }

    public static Map<String, Object> SUIT_TEXTURES = new HashMap<>();

    @Override
    public void onArmorToggled(Entity entity, ItemStack stack, EntityEquipmentSlot slot, boolean open) {
        PlayerHelper.playSoundToAll(entity.world, entity.posX, entity.posY, entity.posZ, 50, open ? PTSoundEvents.HELMET_OPENING : PTSoundEvents.HELMET_CLOSING, SoundCategory.PLAYERS);
    }

    @Override
    public Ability.AbilityMap addDefaultAbilities(EntityLivingBase entity, Ability.AbilityMap abilities, Ability.EnumAbilityContext context) {
        abilities.put("pym_particle_size_shrink", new AbilityPymParticleSize(entity).setDataValue(AbilityPymParticleSize.SIZE, 0.1F).setDataValue(Ability.TITLE, new TextComponentTranslation("pymtech.info.shrink")));
        abilities.put("pym_particle_size_grow", new AbilityPymParticleSize(entity).setDataValue(AbilityPymParticleSize.SIZE, this == PTSuitSet.ANT_MAN || this == PTSuitSet.WASP ? 1F : 8F).setDataValue(Ability.TITLE, new TextComponentTranslation("pymtech.info.grow")));
        abilities.put("regulator", new AbilityRegulator(entity).setDataValue(AbilityRegulator.ADVANCED, this == PTSuitSet.ANT_MAN_2));
        abilities.put("shrink_suit", new AbilityShrinkSuit(entity).setDataValue(AbilityShrinkSuit.NEEDS_PYM_PARTICLES, true));
        if (this == PTSuitSet.WASP) {
            abilities.put("energy_blast", new AbilityEnergyBlast(entity).setDataValue(AbilityEnergyBlast.DAMAGE, 5F).setDataValue(AbilityEnergyBlast.COLOR, new Color(0.34F, 0.3F, 0.05F)).setMaxCooldown(40));
            // TODO size condition
            abilities.put("flight", new AbilityFlight(entity).setDataValue(AbilityFlight.SPEED, 3F).setDataValue(AbilityFlight.SPRINT_SPEED, 10F).setDataValue(AbilityFlight.ROTATE_ARMS, false).addCondition(new AbilityCondition((a) -> a.getEntity().getCapability(CapabilitySizeChanging.SIZE_CHANGING_CAP, null).getSize() <= 0.5F, new TextComponentString("SMALL"))));
        }
        return super.addDefaultAbilities(entity, abilities, context);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void bindArmorTexture(SuitSet suitSet, Entity entity, float f, float f1, float f2, float f3, float f4, float f5, ResourceLocation normalTex, ResourceLocation glowTex, boolean glow, EntityEquipmentSlot slot, boolean smallArms) {
        if (slot == EntityEquipmentSlot.HEAD && helmetAnimation && entity instanceof EntityLivingBase) {
            ItemStack stack = ((EntityLivingBase) entity).getItemStackFromSlot(slot);
            int timer = ANIMATION_TIME - (stack.hasTagCompound() ? stack.getTagCompound().getInteger("OpenTimer") : 0);
            // 0 = zu || ANIMATION_TIME = offen
            String key = suitSet.getRegistryName().toString() + (glow ? "glow" : "normal") + "_" + timer;

            if (timer == ANIMATION_TIME) {
                super.bindArmorTexture(suitSet, entity, f, f1, f2, f3, f4, f5, normalTex, glowTex, glow, slot, smallArms);
                return;
            }

            try {
                DynamicTexture t = null;
                if (SUIT_TEXTURES.containsKey(key))
                    t = (DynamicTexture) SUIT_TEXTURES.get(key);
                else {
                    BufferedImage image = TextureUtil.readBufferedImage(Minecraft.getMinecraft().getResourceManager().getResource(glow ? glowTex : normalTex).getInputStream());

                    float progress1 = MathHelper.clamp(timer - (ANIMATION_TIME / 3F), 0, ANIMATION_TIME * (2F / 3F)) / (ANIMATION_TIME * (2F / 3F));
                    LCRenderHelper.animY(image, 8, 0, 16, 16, progress1);
                    float progress2 = MathHelper.clamp(timer, 0, ANIMATION_TIME / 3F) / (ANIMATION_TIME / 3F);
                    LCRenderHelper.animY(image, 24, 14, 32, 8, progress2);

                    if (timer < ANIMATION_TIME - 1) {
                        LCRenderHelper.setOpacity(image, 0, 51, 8, 55, 00);
                        LCRenderHelper.setOpacity(image, 0, 42, 12, 44, 00);
                    }

                    if (timer != ANIMATION_TIME)
                        LCRenderHelper.setOpacity(image, 16, 4, 24, 8, 00);

                    if (timer < ANIMATION_TIME * 0.35F)
                        LCRenderHelper.setOpacity(image, 11, 45, 21, 50, 00);

                    if (timer < ANIMATION_TIME / 2)
                        LCRenderHelper.setOpacity(image, 0, 32, 12, 38, 00);

                    if (timer < ANIMATION_TIME * 0.65F)
                        LCRenderHelper.setOpacity(image, 0, 45, 10, 50, 00);

                    int length = MathHelper.clamp(timer, 0, (int) (ANIMATION_TIME * 0.35F));
                    for (int x = 0; x < 8; x++) {
                        for (int y = 8; y < 16; y++) {
                            int y_ = y - 8;
                            if (x + y_ > length + 1 && timer - (ANIMATION_TIME * 0.3F) < x) {
                                image.setRGB(x, 16 - y_ - 1, 0);
                                image.setRGB(23 - x, 16 - y_ - 1, 0);
                            }
                        }
                    }

                    t = new DynamicTexture(image);
                    SUIT_TEXTURES.put(key, t);
                }
                GlStateManager.bindTexture(t.getGlTextureId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else
            super.bindArmorTexture(suitSet, entity, f, f1, f2, f3, f4, f5, normalTex, glowTex, glow, slot, smallArms);
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        ItemSuitSetArmor item = (ItemSuitSetArmor) itemStack.getItem();
        if (helmetAnimation && item.armorType == EntityEquipmentSlot.HEAD) {
            if (itemStack.hasTagCompound()) {
                NBTTagCompound nbt = itemStack.getTagCompound();
                int timer = nbt.getInteger("OpenTimer");
                boolean b = false;
                if (timer < ANIMATION_TIME && item.isArmorOpen(player, itemStack)) {
                    timer++;
                    b = true;
                } else if (timer > 0 && !item.isArmorOpen(player, itemStack)) {
                    timer--;
                    b = true;
                }

                if (b) {
                    nbt.setInteger("OpenTimer", timer);
                    itemStack.setTagCompound(nbt);
                }
            }
        }
    }
}
