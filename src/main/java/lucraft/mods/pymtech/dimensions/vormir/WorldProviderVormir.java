package lucraft.mods.pymtech.dimensions.vormir;

import lucraft.mods.pymtech.dimensions.PTDimensions;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WorldProviderVormir extends WorldProvider {
    public WorldProviderVormir() {
    }

    protected void init() {
        super.init();
        this.biomeProvider = new BiomeProviderSingle(BiomeVormir.VORMIR_BIOME);
    }

    public IChunkGenerator createChunkGenerator() {
        return new ChunkGeneratorVormir(this.world, this.getSeed(), true, this.world.getWorldInfo().getGeneratorOptions());
    }

    public DimensionType getDimensionType() {
        return PTDimensions.VORMIR;
    }

    @SideOnly(Side.CLIENT)
    public Vec3d getSkyColor(Entity cameraEntity, float partialTicks) {
        return super.getSkyColor(cameraEntity, partialTicks).scale(0.5);
    }

    @SideOnly(Side.CLIENT)
    public boolean doesXZShowFog(int x, int z) {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public Vec3d getFogColor(float p_76562_1_, float p_76562_2_) {
        return new Vec3d(0.10000000149011612, 0.10000000149011612, 0.30000001192092896);
    }

    @SideOnly(Side.CLIENT)
    public boolean isSkyColored() {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public Vec3d getCloudColor(float partialTicks) {
        return new Vec3d(0.0, 0.0, 0.0);
    }

    public boolean canRespawnHere() {
        return false;
    }
}

