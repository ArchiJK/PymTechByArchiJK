package lucraft.mods.pymtech.entities;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityShrunkenStructureTrail extends Entity {

    public EntityShrunkenStructure parent;
    public int lifeTime;
    public float scale;

    public EntityShrunkenStructureTrail(World worldIn) {
        super(worldIn);
        noClip = true;
        ignoreFrustumCheck = true;
        this.setSize(0.1F, 0.1F);
    }

    public EntityShrunkenStructureTrail(World worldIn, EntityShrunkenStructure parent, int lifeTime) {
        this(worldIn);
        this.parent = parent;
        this.scale = parent.getScale();
        this.lifeTime = lifeTime;
        this.setPosition(parent.posX, parent.posY, parent.posZ);
    }

    @Override
    public void onUpdate() {
        if (this.ticksExisted >= lifeTime) {
            this.setDead();
        }
    }

    @Override
    protected void entityInit() {

    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {

    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {

    }
}
