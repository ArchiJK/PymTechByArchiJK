package lucraft.mods.pymtech.client.models;

import com.google.common.collect.ImmutableList;
import lucraft.mods.lucraftcore.superpowers.abilities.Ability;
import lucraft.mods.lucraftcore.superpowers.abilities.AbilityFlight;
import lucraft.mods.lucraftcore.superpowers.abilities.supplier.AbilityContainer;
import lucraft.mods.lucraftcore.superpowers.models.ModelBipedSuitSet;
import lucraft.mods.lucraftcore.superpowers.suitsets.SuitSet;
import lucraft.mods.lucraftcore.util.entity.FakePlayerClient;
import lucraft.mods.lucraftcore.util.helper.LCRenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.math.MathHelper;

/**
 * WaspWings - Neon Created using Tabula 7.0.0
 */
public class ModelWaspChestplate extends ModelBipedSuitSet {

    public ModelRenderer wingHolder;
    public ModelRenderer wingbigLeft;
    public ModelRenderer wingbigRight;
    public ModelRenderer wingsmallLeft;
    public ModelRenderer wingsmallRight;
    public ModelRenderer detail;
    public ModelRenderer texture;
    public ModelRenderer texture_1;
    public ModelRenderer texture_2;
    public ModelRenderer texture_3;

    public ModelWaspChestplate(float f, String normalTex, String lightTex, SuitSet hero, EntityEquipmentSlot slot, boolean hasSmallArms, boolean vibrating) {
        this(f, normalTex, lightTex, hero, slot, hasSmallArms, vibrating, 128, 64);
    }

    public ModelWaspChestplate(float f, String normalTex, String lightTex, SuitSet hero, EntityEquipmentSlot slot, boolean hasSmallArms, boolean vibrating, int width, int height) {
        super(f, normalTex, lightTex, hero, slot, hasSmallArms, vibrating, width, height);

        this.textureWidth = 128;
        this.textureHeight = 64;
        this.texture_3 = new ModelRenderer(this, 56, 21);
        this.texture_3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.texture_3.addBox(-12.0F, 0.0F, 0.0F, 12, 4, 0, 0.0F);
        this.wingHolder = new ModelRenderer(this, 56, 0);
        this.wingHolder.setRotationPoint(0.0F, 4.5F, 1.5F);
        this.wingHolder.addBox(-2.0F, -2.0F, 0.0F, 4, 4, 1, 0.0F);
        this.detail = new ModelRenderer(this, 68, 0);
        this.detail.setRotationPoint(0.0F, 0.0F, 0.7F);
        this.detail.addBox(-1.5F, -1.5F, 0.0F, 3, 3, 1, 0.0F);
        this.texture_1 = new ModelRenderer(this, 66, 13);
        this.texture_1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.texture_1.addBox(-16.0F, 0.0F, 0.0F, 16, 6, 0, 0.0F);
        this.wingbigLeft = new ModelRenderer(this, 66, 10);
        this.wingbigLeft.setRotationPoint(1.0F, -1.0F, 1.0F);
        this.wingbigLeft.addBox(0.0F, -0.5F, -0.5F, 12, 1, 1, 0.0F);
        this.setRotateAngle(wingbigLeft, 0.0F, -0.0F, -0.7853981633974483F);
        this.texture = new ModelRenderer(this, 66, 13);
        this.texture.mirror = true;
        this.texture.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.texture.addBox(0.0F, 0.0F, 0.0F, 16, 6, 0, 0.0F);
        this.wingbigRight = new ModelRenderer(this, 66, 10);
        this.wingbigRight.mirror = true;
        this.wingbigRight.setRotationPoint(-1.0F, -1.0F, 1.0F);
        this.wingbigRight.addBox(-12.0F, -0.5F, -0.5F, 12, 1, 1, 0.0F);
        this.setRotateAngle(wingbigRight, 0.0F, 0.0F, 0.7853981633974483F);
        this.wingsmallRight = new ModelRenderer(this, 56, 6);
        this.wingsmallRight.mirror = true;
        this.wingsmallRight.setRotationPoint(-1.0F, 1.0F, 1.0F);
        this.wingsmallRight.addBox(-8.0F, -0.5F, -0.5F, 8, 1, 1, 0.0F);
        this.setRotateAngle(wingsmallRight, 0.0F, 0.0F, -0.7853981633974483F);
        this.wingsmallLeft = new ModelRenderer(this, 56, 6);
        this.wingsmallLeft.setRotationPoint(1.0F, 1.0F, 1.0F);
        this.wingsmallLeft.addBox(0.0F, -0.5F, -0.5F, 8, 1, 1, 0.0F);
        this.setRotateAngle(wingsmallLeft, 0.0F, 0.0F, 0.7853981633974483F);
        this.texture_2 = new ModelRenderer(this, 56, 21);
        this.texture_2.mirror = true;
        this.texture_2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.texture_2.addBox(0.0F, 0.0F, 0.0F, 12, 4, 0, 0.0F);

        this.bipedBody.addChild(this.wingHolder);
        this.wingsmallRight.addChild(this.texture_3);
        this.wingHolder.addChild(this.detail);
        this.wingbigRight.addChild(this.texture_1);
        this.wingHolder.addChild(this.wingbigLeft);
        this.wingbigLeft.addChild(this.texture);
        this.wingHolder.addChild(this.wingbigRight);
        this.wingHolder.addChild(this.wingsmallRight);
        this.wingHolder.addChild(this.wingsmallLeft);
        this.wingsmallLeft.addChild(this.texture_2);
    }

    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        super.render(entity, f, f1, f2, f3, f4, f5);
    }

    @Override
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entityIn) {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, entityIn);
        boolean flying = false;
        boolean still = false;

        if (entityIn instanceof FakePlayerClient && ((FakePlayerClient) entityIn).info.equalsIgnoreCase("suit_stand")) {
            flying = true;
            still = true;
        }

        if (!flying && entityIn instanceof EntityLivingBase) {
            EntityLivingBase entity = (EntityLivingBase) entityIn;
            AbilityContainer container = Ability.getAbilityContainer(Ability.EnumAbilityContext.SUIT, entity);

            if (container != null && !container.getAbilities().isEmpty()) {
                for (AbilityFlight abilityFlight : Ability.getAbilitiesFromClass(ImmutableList.copyOf(container.getAbilities()), AbilityFlight.class)) {
                    if (abilityFlight != null && abilityFlight.isUnlocked() && abilityFlight.isEnabled()) {
                        flying = true;
                        break;
                    }
                }
            }
        }

        this.wingbigRight.showModel = flying;
        this.wingbigLeft.showModel = flying;
        this.wingsmallRight.showModel = flying;
        this.wingsmallLeft.showModel = flying;

        if (flying) {
            float h = still ? -0.4F : (-0.4F + MathHelper.cos((Minecraft.getMinecraft().player.ticksExisted + LCRenderHelper.renderTick) * 3F) / 4);

            this.wingbigRight.rotateAngleY = -h;
            this.wingbigLeft.rotateAngleY = h;
            this.wingsmallRight.rotateAngleY = -h;
            this.wingsmallLeft.rotateAngleY = h;
        }
    }
}
