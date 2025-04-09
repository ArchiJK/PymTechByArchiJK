package lucraft.mods.pymtech.network;

import io.netty.buffer.ByteBuf;
import lucraft.mods.lucraftcore.LucraftCore;
import lucraft.mods.lucraftcore.network.AbstractClientMessageHandler;
import lucraft.mods.pymtech.client.gui.GuiQRTransition;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageOpenQRGui implements IMessage {
    public MessageOpenQRGui() {
    }

    public void fromBytes(ByteBuf buf) {
    }

    public void toBytes(ByteBuf buf) {
    }

    public static class Handler extends AbstractClientMessageHandler<MessageOpenQRGui> {
        public Handler() {
        }

        @SideOnly(Side.CLIENT)
        public IMessage handleClientMessage(EntityPlayer player, MessageOpenQRGui message, MessageContext ctx) {
            LucraftCore.proxy.getThreadFromContext(ctx).addScheduledTask(() -> {
                Minecraft.getMinecraft().displayGuiScreen(new GuiQRTransition());
            });
            return null;
        }
    }
}
