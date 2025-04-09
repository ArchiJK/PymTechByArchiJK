package lucraft.mods.pymtech.network;

import lucraft.mods.pymtech.PymTech;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PTPacketDispatcher {

	private static final SimpleNetworkWrapper dispatcher = NetworkRegistry.INSTANCE.newSimpleChannel(PymTech.MODID);

	public static final void registerMessage(Class handlerClass, Class messageClass, Side side, int id) {
		dispatcher.registerMessage(handlerClass, messageClass, id, side);
	}

	public static final void sendTo(IMessage message, EntityPlayerMP player) {
		dispatcher.sendTo(message, player);
	}

	public static final void sendToAllAround(IMessage message, NetworkRegistry.TargetPoint point) {
		dispatcher.sendToAllAround(message, point);
	}

	public static final void sendToAll(IMessage message) {
		dispatcher.sendToAll(message);
	}

	public static final void sendToAllAround(IMessage message, int dimension, double x, double y, double z, double range) {
		sendToAllAround(message, new NetworkRegistry.TargetPoint(dimension, x, y, z, range));
	}

	public static final void sendToAllAround(IMessage message, EntityPlayer player, double range) {
		sendToAllAround(message, player.world.provider.getDimension(), player.posX, player.posY, player.posZ, range);
	}

	public static final void sendToDimension(IMessage message, int dimensionId) {
		dispatcher.sendToDimension(message, dimensionId);
	}

	public static final void sendToServer(IMessage message) {
		dispatcher.sendToServer(message);
	}


}
