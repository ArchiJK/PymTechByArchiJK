package lucraft.mods.pymtech.network;

import io.netty.buffer.ByteBuf;
import lucraft.mods.lucraftcore.LucraftCore;
import lucraft.mods.lucraftcore.network.AbstractServerMessageHandler;
import lucraft.mods.lucraftcore.util.helper.LCMathHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageSetRegulatedSize implements IMessage {

    public float size;

    public MessageSetRegulatedSize() {
    }

    public MessageSetRegulatedSize(float size) {
        this.size = size;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.size = buf.readFloat();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeFloat(this.size);
    }

    public static class Handler extends AbstractServerMessageHandler<MessageSetRegulatedSize> {

        @Override
        public IMessage handleServerMessage(EntityPlayer player, MessageSetRegulatedSize message, MessageContext ctx) {
            LucraftCore.proxy.getThreadFromContext(ctx).addScheduledTask(() -> {
                ItemStack chest = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);

                if (!chest.isEmpty()) {
                    NBTTagCompound nbt = chest.hasTagCompound() ? chest.getTagCompound() : new NBTTagCompound();
                    nbt.setFloat("RegulatedSize", (float) LCMathHelper.round(message.size, 2));
                    chest.setTagCompound(nbt);
                }
            });

            return null;
        }
    }

    public enum InfoType {

        ENTER_QUANTUM_DIMENSION;

    }

}