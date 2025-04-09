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

public class MessageStructureShrinkerSettings implements IMessage {

    public BlockPos pos;
	public BlockPos area;
	public boolean showBox;

	public MessageStructureShrinkerSettings() {
	}

    public MessageStructureShrinkerSettings(BlockPos pos, BlockPos area, boolean showBox) {
        this.pos = pos;
        this.area = area;
        this.showBox = showBox;
    }

    @Override
	public void fromBytes(ByteBuf buf) {
        this.pos = NBTUtil.getPosFromTag(ByteBufUtils.readTag(buf));
		this.area = NBTUtil.getPosFromTag(ByteBufUtils.readTag(buf));
		this.showBox = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, NBTUtil.createPosTag(this.pos));
        ByteBufUtils.writeTag(buf, NBTUtil.createPosTag(this.area));
        buf.writeBoolean(this.showBox);
	}

	public static class Handler extends AbstractServerMessageHandler<MessageStructureShrinkerSettings> {

		@Override
		public IMessage handleServerMessage(EntityPlayer player, MessageStructureShrinkerSettings message, MessageContext ctx) {
			LucraftCore.proxy.getThreadFromContext(ctx).addScheduledTask(() -> {
                TileEntity tileEntity = player.world.getTileEntity(message.pos);

                if(tileEntity != null && tileEntity instanceof TileEntityStructureShrinker) {
                    ((TileEntityStructureShrinker)tileEntity).changeSettings(message.area, message.showBox);
                }
			});
			return null;
		}

	}

}
