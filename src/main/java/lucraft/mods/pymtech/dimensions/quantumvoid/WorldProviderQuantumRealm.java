package lucraft.mods.pymtech.dimensions.quantumvoid;

import lucraft.mods.pymtech.dimensions.PTDimensions;
import lucraft.mods.pymtech.dimensions.quantumvoid.dimension.biomes.BiomeProviderCustom;
import lucraft.mods.pymtech.dimensions.quantumvoid.dimension.madness.MadnessGenTemplate;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class WorldProviderQuantumRealm extends WorldProvider
{
    @Override
    public void init()
    {
        this.biomeProvider = new BiomeProviderCustom(this.world.getSeed());
    }

    public DimensionType getDimensionType()
    {
        return PTDimensions.QUANTUM_REALM;
    }

    @Override
    public IChunkGenerator createChunkGenerator()
    {
        return new MadnessGenTemplate(this.world, this.world.getSeed());
    }

    @Override
    public boolean isSurfaceWorld()
    {
        return true;
    }

    @Override
    public boolean canRespawnHere()
    {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean doesXZShowFog(int x, int z)
    {
        return true;
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
    public float getCloudHeight()
    {
        return 255.0f;
    }

    @Override
    public boolean shouldMapSpin(String entity, double x, double z, double rotation)
    {
        return true;
    }

    @Override
    public double getMovementFactor()
    {
        return 16.0f;
    }

    @Override
    protected void generateLightBrightnessTable()
    {
        float f = 0.5F;
        for (int i = 0; i <= 15; ++i)
        {
            float f1 = 1.0F - (float) i / 15.0F;
            this.lightBrightnessTable[i] = (1.0F - f1) / (f1 * 3.0F + 1.0F) * (1.0F - f) + f;
        }
    }
}
