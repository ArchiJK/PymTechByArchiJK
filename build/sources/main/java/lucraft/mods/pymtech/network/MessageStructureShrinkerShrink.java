package lucraft.mods.pymtech.network;

import io.netty.buffer.ByteBuf;
import lucraft.mods.lucraftcore.LucraftCore;
import lucraft.mods.lucraftcore.network.AbstractServerMessageHandler;
import lucraft.mods.pymtech.tileentities.TileEntityStructureShrinker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageStructureShrinkerShrink implements IMessage {

    public BlockPos pos;

    public MessageStructureShrinkerShrink() {
    }

    public MessageStructureShrinkerShrink(BlockPos pos) {
        this.pos = pos;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.pos = NBTUtil.getPosFromTag(ByteBufUtils.readTag(buf));
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeTag(buf, NBTUtil.createPosTag(this.pos));
    }

    public static class Handler extends AbstractServerMessageHandler<MessageStructureShrinkerShrink> {

        @Override
        public IMessage handleServerMessage(EntityPlayer player, MessageStructureShrinkerShrink message, MessageContext ctx) {
            LucraftCore.proxy.getThreadFromContext(ctx).addScheduledTask(() -> {
                TileEntity tileEntity = player.world.getTileEntity(message.pos);

                if(tileEntity != null && tileEntity instanceof TileEntityStructureShrinker) {
                    ((TileEntityStructureShrinker)tileEntity).shrink(player);
                }
            });
            return null;
        }

    }

}
