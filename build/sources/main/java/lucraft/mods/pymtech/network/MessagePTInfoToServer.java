package lucraft.mods.pymtech.network;

import io.netty.buffer.ByteBuf;
import lucraft.mods.lucraftcore.LucraftCore;
import lucraft.mods.lucraftcore.network.AbstractServerMessageHandler;
import lucraft.mods.pymtech.dimensions.PTDimensions;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessagePTInfoToServer implements IMessage {
    public InfoType type;
    public int data = 0;

    public MessagePTInfoToServer() {
    }

    public MessagePTInfoToServer(InfoType type) {
        this.type = type;
    }

    public MessagePTInfoToServer(InfoType type, int data) {
        this.type = type;
        this.data = data;
    }

    public void fromBytes(ByteBuf buf) {
        this.type = MessagePTInfoToServer.InfoType.values()[buf.readInt()];
        this.data = buf.readInt();
    }

    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.type.ordinal());
        buf.writeInt(this.data);
    }

    public static enum InfoType {
        ENTER_QUANTUM_DIMENSION;

        private InfoType() {
        }
    }

    public static class Handler extends AbstractServerMessageHandler<MessagePTInfoToServer> {
        public Handler() {
        }

        public IMessage handleServerMessage(EntityPlayer player, MessagePTInfoToServer message, MessageContext ctx) {
            LucraftCore.proxy.getThreadFromContext(ctx).addScheduledTask(() -> {
                InfoType type = message.type;
                if (type != null) {
                    if (type == MessagePTInfoToServer.InfoType.ENTER_QUANTUM_DIMENSION) {
                        int progress = message.data;
                        if (progress == 0) {
                            return;
                        }

                        PTDimensions.transferBetweenDims(player, progress < 60 ? PTDimensions.MICROSCOPIC.getId() : PTDimensions.QUANTUM_REALM.getId());
                    }

                }
            });
            return null;
        }
    }
}
