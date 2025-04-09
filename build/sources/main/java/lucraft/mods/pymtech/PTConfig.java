package lucraft.mods.pymtech;

import io.netty.buffer.ByteBuf;
import lucraft.mods.lucraftcore.network.AbstractClientMessageHandler;
import lucraft.mods.pymtech.network.PTPacketDispatcher;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

@Config(modid = PymTech.MODID)
public class PTConfig {

    @Config.RequiresMcRestart
    public static int QUANTUM_REALM_DIMENSION_ID = 182;

    @Config.RequiresMcRestart
    public static int MICROSCOPIC_DIMENSION_ID = 183;

    @Config.RequiresMcRestart
    public static int VORMIR_DIMENSION_ID = 184;

    @Config.RequiresWorldRestart
    public static int STRUCTURE_SHRINKER_LIMIT = 32;

    @Mod.EventBusSubscriber(modid = PymTech.MODID)
    private static class EventHandler {

        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(PymTech.MODID)) {
                ConfigManager.sync(PymTech.MODID, Config.Type.INSTANCE);
            }
        }

        @SubscribeEvent
        public static void onJoinWorld(EntityJoinWorldEvent event) {
            if (event.getEntity() instanceof EntityPlayerMP) {
                PTPacketDispatcher.sendTo(new MessageSyncConfig(), (EntityPlayerMP) event.getEntity());
            }
        }

    }

    public static class MessageSyncConfig implements IMessage {

        public MessageSyncConfig() {

        }

        @Override
        public void fromBytes(ByteBuf buf) {
            Client.STRUCTURE_SHRINKER_LIMIT = buf.readInt();
        }

        @Override
        public void toBytes(ByteBuf buf) {
            buf.writeInt(PTConfig.STRUCTURE_SHRINKER_LIMIT);
        }

        public static class Handler extends AbstractClientMessageHandler<MessageSyncConfig> {

            @Override
            public IMessage handleClientMessage(EntityPlayer player, MessageSyncConfig message, MessageContext ctx) {
                return null;
            }

        }
    }

    public static class Client {

        public static int STRUCTURE_SHRINKER_LIMIT = 32;

    }

}
