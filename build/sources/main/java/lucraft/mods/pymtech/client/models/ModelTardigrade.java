package lucraft.mods.pymtech.client.models;

import lucraft.mods.lucraftcore.util.helper.LCRenderHelper;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

/**
 * Tardigrade - Neon Created using Tabula 4.1.1
 */
public class ModelTardigrade extends ModelBase {

	public ModelRenderer bodyFront;
	public ModelRenderer leg4L;
	public ModelRenderer leg4R;
	public ModelRenderer bodyMiddle;
	public ModelRenderer head;
	public ModelRenderer leg2L;
	public ModelRenderer leg3L;
	public ModelRenderer leg2R;
	public ModelRenderer leg3R;
	public ModelRenderer bodyBack;
	public ModelRenderer leg1L;
	public ModelRenderer leg1R;
	public ModelRenderer mouth;
	public ModelRenderer mouth2;

	public ModelTardigrade() {
		this.textureWidth = 64;
		this.textureHeight = 32;
		this.leg1L = new ModelRenderer(this, 52, 0);
		this.leg1L.setRotationPoint(-3.0F, 2.0F, 2.5F);
		this.leg1L.addBox(-4.0F, -1.0F, -1.0F, 4, 2, 2, 0.0F);
		this.setRotateAngle(leg1L, 0.0F, 0.0F, -0.7853981633974483F);
		this.leg3R = new ModelRenderer(this, 52, 0);
		this.leg3R.mirror = true;
		this.leg3R.setRotationPoint(3.5F, 2.0F, 2.5F);
		this.leg3R.addBox(0.0F, -1.0F, -1.0F, 4, 2, 2, 0.0F);
		this.setRotateAngle(leg3R, 0.0F, 0.0F, 0.7853981633974483F);
		this.leg2L = new ModelRenderer(this, 52, 0);
		this.leg2L.setRotationPoint(-3.5F, 2.0F, 7.5F);
		this.leg2L.addBox(-4.0F, -1.0F, -1.0F, 4, 2, 2, 0.0F);
		this.setRotateAngle(leg2L, 0.0F, 0.0F, -0.7853981633974483F);
		this.bodyBack = new ModelRenderer(this, 0, 0);
		this.bodyBack.setRotationPoint(0.0F, 0.0F, 9.5F);
		this.bodyBack.addBox(-4.0F, -4.0F, 0.0F, 8, 8, 4, 0.0F);
		this.setRotateAngle(bodyBack, -0.091106186954104F, 0.0F, 0.0F);
		this.leg4R = new ModelRenderer(this, 52, 0);
		this.leg4R.mirror = true;
		this.leg4R.setRotationPoint(3.0F, 2.0F, 1.5F);
		this.leg4R.addBox(0.0F, -1.0F, -1.0F, 4, 2, 2, 0.0F);
		this.setRotateAngle(leg4R, 0.0F, 0.0F, 0.7853981633974483F);
		this.leg2R = new ModelRenderer(this, 52, 0);
		this.leg2R.mirror = true;
		this.leg2R.setRotationPoint(3.5F, 2.0F, 7.5F);
		this.leg2R.addBox(0.0F, -1.0F, -1.0F, 4, 2, 2, 0.0F);
		this.setRotateAngle(leg2R, 0.0F, 0.0F, 0.7853981633974483F);
		this.bodyFront = new ModelRenderer(this, 0, 0);
		this.bodyFront.setRotationPoint(0.0F, 0.5F, -9.5F);
		this.bodyFront.addBox(-4.0F, -4.0F, 0.0F, 8, 8, 4, 0.0F);
		this.setRotateAngle(bodyFront, 0.091106186954104F, 0.0F, 0.0F);
		this.bodyMiddle = new ModelRenderer(this, 0, 13);
		this.bodyMiddle.setRotationPoint(0.0F, 0.0F, 3.5F);
		this.bodyMiddle.addBox(-4.5F, -4.5F, 0.0F, 9, 9, 10, 0.0F);
		this.setRotateAngle(bodyMiddle, -0.091106186954104F, 0.0F, 0.0F);
		this.leg4L = new ModelRenderer(this, 52, 0);
		this.leg4L.setRotationPoint(-3.0F, 2.0F, 1.5F);
		this.leg4L.addBox(-4.0F, -1.0F, -1.0F, 4, 2, 2, 0.0F);
		this.setRotateAngle(leg4L, 0.0F, 0.0F, -0.7853981633974483F);
		this.leg3L = new ModelRenderer(this, 52, 0);
		this.leg3L.setRotationPoint(-3.5F, 2.0F, 2.5F);
		this.leg3L.addBox(-4.0F, -1.0F, -1.0F, 4, 2, 2, 0.0F);
		this.setRotateAngle(leg3L, 0.0F, 0.0F, -0.7853981633974483F);
		this.head = new ModelRenderer(this, 46, 13);
		this.head.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.head.addBox(-3.5F, -3.5F, -2.0F, 7, 7, 2, 0.0F);
		this.leg1R = new ModelRenderer(this, 52, 0);
		this.leg1R.mirror = true;
		this.leg1R.setRotationPoint(3.0F, 2.0F, 2.5F);
		this.leg1R.addBox(0.0F, -1.0F, -1.0F, 4, 2, 2, 0.0F);
		this.setRotateAngle(leg1R, 0.0F, 0.0F, 0.7853981633974483F);
		this.mouth2 = new ModelRenderer(this, 31, 0);
		this.mouth2.setRotationPoint(0.0F, 0.0F, -3.5F);
		this.mouth2.addBox(-1.5F, -1.5F, 0.0F, 3, 3, 2, 0.0F);
		this.mouth = new ModelRenderer(this, 30, 9);
		this.mouth.setRotationPoint(0.0F, 0.0F, -1.5F);
		this.mouth.addBox(-2.0F, -2.0F, -2.0F, 4, 4, 2, 0.0F);
		this.bodyBack.addChild(this.leg1L);
		this.bodyMiddle.addChild(this.leg3R);
		this.bodyMiddle.addChild(this.leg2L);
		this.bodyMiddle.addChild(this.bodyBack);
		this.bodyFront.addChild(this.leg4R);
		this.bodyMiddle.addChild(this.leg2R);
		this.bodyFront.addChild(this.bodyMiddle);
		this.bodyFront.addChild(this.leg4L);
		this.bodyMiddle.addChild(this.leg3L);
		this.bodyFront.addChild(this.head);
		this.bodyBack.addChild(this.leg1R);
		this.mouth.addChild(this.mouth2);
		this.head.addChild(this.mouth);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		this.bodyFront.render(f5);
	}

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
		double pi = Math.PI;
		float f = (entityIn.ticksExisted + LCRenderHelper.renderTick) / 25F;

		this.head.rotateAngleX = MathHelper.cos((float) (f + (pi / 4F))) / 10F;
		this.mouth.rotateAngleX = -this.head.rotateAngleX;

		this.bodyFront.rotateAngleX = MathHelper.cos((float) (f + (pi / 2F))) / 10F;

		this.bodyMiddle.rotateAngleX = MathHelper.cos((float) (f + (pi * 0.75F))) / 10F;

		this.bodyBack.rotateAngleX = MathHelper.cos((float) (f + pi)) / 10F;

		this.leg1R.rotateAngleZ = 0.8F - MathHelper.sin((entityIn.ticksExisted + LCRenderHelper.renderTick) / 20F) / 5F;
		this.leg1L.rotateAngleZ = -this.leg1R.rotateAngleZ;

		this.leg2R.rotateAngleZ = 0.8F - MathHelper.sin((entityIn.ticksExisted + 100 + LCRenderHelper.renderTick) / 20F) / 5F;
		this.leg2L.rotateAngleZ = -this.leg2R.rotateAngleZ;

		this.leg3R.rotateAngleZ = 0.8F - MathHelper.sin((entityIn.ticksExisted + 200 + LCRenderHelper.renderTick) / 20F) / 5F;
		this.leg3L.rotateAngleZ = -this.leg3R.rotateAngleZ;

		this.leg4R.rotateAngleZ = 0.8F - MathHelper.sin((entityIn.ticksExisted + 300 + LCRenderHelper.renderTick) / 20F) / 5F;
		this.leg4L.rotateAngleZ = -this.leg4R.rotateAngleZ;
    }

    /**
	 * This is a helper function from Tabula to set the rotation of model parts
	 */
	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}
