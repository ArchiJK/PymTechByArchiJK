package net.minecraft.client.renderer.entity;

import lucraft.mods.lucraftcore.sizechanging.capabilities.ISizeChanging;
import lucraft.mods.pymtech.dimensions.PTDimensions;
import net.minecraft.entity.EntityLivingBase;

public class SizeChangerPymParticlesSubatomic extends SizeChangerPymParticles {
    public SizeChangerPymParticlesSubatomic() {
    }

    public void end(EntityLivingBase entity, ISizeChanging data, float size) {
        PTDimensions.sendIntoQuantumRealm(entity);
        data.changeSizeChanger(SizeChangerPymParticles.PYM_PARTICLES);
    }
}
