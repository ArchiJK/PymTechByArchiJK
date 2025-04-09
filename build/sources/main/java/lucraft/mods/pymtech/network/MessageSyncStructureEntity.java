package lucraft.mods.pymtech.network;

import io.netty.buffer.ByteBuf;
import lucraft.mods.lucraftcore.LucraftCore;
import lucraft.mods.lucraftcore.network.AbstractClientMessageHandler;
import lucraft.mods.pymtech.entities.EntityShrunkenStructure;
import lucraft.mods.pymtech.items.ItemShrunkenStructure;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class MessageSyncStructureEntity implements IMessage {

    public int entityId;
    public float scale;
    public ItemShrunkenStructure.ShrunkenStructure shrunkenStructure;

    public MessageSyncStructureEntity() {

    }

    public MessageSyncStructureEntity(EntityShrunkenStructure entity) {
        this.entityId = entity.getEntityId();
        this.scale = entity.scale;
        this.shrunkenStructure = entity.shrunkenStructure;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.entityId = buf.readInt();
        this.scale = buf.readFloat();
        BlockPos size = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        int listSize = buf.readInt();
        ItemShrunkenStructure.BlockData[][][] data = new ItemShrunkenStructure.BlockData[size.getX()][size.getY()][size.getZ()];

        for (int i = 0; i < listSize; i++) {
            Block block = Block.REGISTRY.getObject(new ResourceLocation(ByteBufUtils.readUTF8String(buf)));
            byte meta = buf.readByte();
            NBTTagCompound tileEntity = null;
            if (buf.readBoolean())
                tileEntity = ByteBufUtils.readTag(buf);
            BlockPos pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
            data[pos.getX()][pos.getY()][pos.getZ()] = new ItemShrunkenStructure.BlockData(block.getStateFromMeta(meta), tileEntity);
        }

        this.shrunkenStructure = new ItemShrunkenStructure.ShrunkenStructure(data, size);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeFloat(this.scale);
        buf.writeInt(this.shrunkenStructure.getSize().getX());
        buf.writeInt(this.shrunkenStructure.getSize().getY());
        buf.writeInt(this.shrunkenStructure.getSize().getZ());

        List<Pair<BlockPos, ItemShrunkenStructure.BlockData>> list = new ArrayList<>();
        for (int x = 0; x < shrunkenStructure.getSize().getX(); x++) {
            for (int y = 0; y < shrunkenStructure.getSize().getY(); y++) {
                for (int z = 0; z < shrunkenStructure.getSize().getZ(); z++) {
                    ItemShrunkenStructure.BlockData d = shrunkenStructure.getData()[x][y][z];

                    if (d != null) {
                        list.add(Pair.of(new BlockPos(x, y, z), d));
                    }
                }
            }
        }
        buf.writeInt(list.size());

        for (Pair<BlockPos, ItemShrunkenStructure.BlockData> pair : list) {
            ByteBufUtils.writeUTF8String(buf, Block.REGISTRY.getNameForObject(pair.getRight().getBlock().getBlock()).toString());
            buf.writeByte(pair.getRight().getBlock().getBlock().getMetaFromState(pair.getRight().getBlock()));
            buf.writeBoolean(pair.getRight().hasTileEntity());
            if (pair.getRight().hasTileEntity())
                ByteBufUtils.writeTag(buf, pair.getRight().getTileEntityData());
            buf.writeInt(pair.getLeft().getX());
            buf.writeInt(pair.getLeft().getY());
            buf.writeInt(pair.getLeft().getZ());
        }
    }

    public static class Handler extends AbstractClientMessageHandler<MessageSyncStructureEntity> {

        @Override
        public IMessage handleClientMessage(EntityPlayer player, MessageSyncStructureEntity message, MessageContext ctx) {

            LucraftCore.proxy.getThreadFromContext(ctx).addScheduledTask(() -> {
                if (message != null && ctx != null) {
                    Entity entity = LucraftCore.proxy.getPlayerEntity(ctx).world.getEntityByID(message.entityId);

                    if (entity != null && entity instanceof EntityShrunkenStructure) {
                        ((EntityShrunkenStructure) entity).scale = message.scale;
                        ((EntityShrunkenStructure) entity).shrunkenStructure = message.shrunkenStructure;
                    }
                }
            });

            return null;
        }

    }

}
