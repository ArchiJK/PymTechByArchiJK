package lucraft.mods.pymtech.dimensions.microscopic;

import lucraft.mods.pymtech.dimensions.PTDimensions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WorldProviderMicroscopic extends WorldProvider {

    @Override
    public DimensionType getDimensionType() {
        return PTDimensions.MICROSCOPIC;
    }

    @Override
    protected void init() {
        super.init();
        this.hasSkyLight = true;
        this.biomeProvider = new BiomeProviderSingle(BiomeMicroscopic.MICROSCOPIC_BIOME);
    }

    @Override
    public void onWorldUpdateEntities() {
        for(Entity entity : this.world.loadedEntityList) {
            if(entity instanceof EntityLivingBase)
                entity.motionY = ((EntityLivingBase)entity).moveForward != 0F ? -entity.rotationPitch / 1000D : 0;
            entity.fallDistance = 0;

            if(entity.posY <= 0)
                entity.setPositionAndUpdate(entity.posX, this.getActualHeight() - 5, entity.posY);
            else if(entity.posY >= this.getActualHeight())
                entity.setPositionAndUpdate(entity.posX, 5, entity.posY);
        }
    }

    @Override
    public IChunkGenerator createChunkGenerator() {
        return new ChunkGeneratorMicroscopic(this.world);
    }

    @Override
    public float calculateCelestialAngle(long worldTime, float partialTicks) {
        return 0F;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IRenderHandler getSkyRenderer() {
        return new SkyRendererMicroscopic();
    }

    @Override
    public Vec3d getSkyColor(Entity cameraEntity, float partialTicks) {
        return new Vec3d(0.5F, 0.22F, 0F);
    }

    @Override
    public Vec3d getFogColor(float p_76562_1_, float p_76562_2_) {
        int i = 10518688;
        float f = MathHelper.cos(p_76562_1_ * (float) Math.PI * 2.0F) * 2.0F + 0.5F;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        float f1 = (float) (i >> 16 & 255) / 255.0F;
        float f2 = (float) (i >> 8 & 255) / 255.0F;
        float f3 = (float) (i & 255) / 255.0F;
        f1 = f1 * (f * 0.0F + 0.15F);
        f2 = f2 * (f * 0.0F + 0.15F);
        f3 = f3 * (f * 0.0F + 0.15F);
        return new Vec3d((double) f1, (double) f2, (double) f3);
    }

    @Override
    public boolean isSkyColored() {
        return true;
    }

    @Override
    public boolean canRespawnHere() {
        return false;
    }

    @Override
    public boolean isSurfaceWorld() {
        return true;
    }

    @Override
    public float getCloudHeight() {
        return Float.MAX_VALUE;
    }

    @Override
    public boolean doesXZShowFog(int x, int z) {
        return false;
    }
}
