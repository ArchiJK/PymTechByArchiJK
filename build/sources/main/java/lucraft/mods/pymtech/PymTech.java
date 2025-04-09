package lucraft.mods.pymtech;

import lucraft.mods.pymtech.client.gui.PTGuiHandler;
import lucraft.mods.pymtech.commands.CommandQuantumRealm;
import lucraft.mods.pymtech.dimensions.PTDimensions;
import lucraft.mods.pymtech.dimensions.quantumvoid.dimension.biomes.BiomeInit;
import lucraft.mods.pymtech.fluids.PTFluids;
import lucraft.mods.pymtech.network.*;
import lucraft.mods.pymtech.proxies.PTCommonProxy;
import lucraft.mods.pymtech.suitsets.PTSuitSet;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Logger;

@Mod(modid = PymTech.MODID, name = PymTech.NAME, version = PymTech.VERSION)
public class PymTech {

    public static final String MODID = "pymtech";
    public static final String NAME = "PymTech";
    public static final String VERSION = "1.12.2-1.0.3";

    @SidedProxy(clientSide = "lucraft.mods.pymtech.proxies.PTClientProxy", serverSide = "lucraft.mods.pymtech.proxies.PTCommonProxy")
    public static PTCommonProxy proxy;

    @Mod.Instance(value = PymTech.MODID)
    public static PymTech INSTANCE;

    public static Logger LOGGER;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
        LOGGER = event.getModLog();
        //SupporterHandler.enableSupporterCheck();

        // Packets
        int id = 0;
        PTPacketDispatcher.registerMessage(PTConfig.MessageSyncConfig.Handler.class, PTConfig.MessageSyncConfig.class, Side.CLIENT, id++);
        PTPacketDispatcher.registerMessage(MessagePTInfoToServer.Handler.class, MessagePTInfoToServer.class, Side.SERVER, id++);
        PTPacketDispatcher.registerMessage(MessageStructureShrinkerSettings.Handler.class, MessageStructureShrinkerSettings.class, Side.SERVER, id++);
        PTPacketDispatcher.registerMessage(MessageStructureShrinkerShrink.Handler.class, MessageStructureShrinkerShrink.class, Side.SERVER, id++);
        PTPacketDispatcher.registerMessage(MessageSyncStructureEntity.Handler.class, MessageSyncStructureEntity.class, Side.CLIENT, id++);
        PTPacketDispatcher.registerMessage(MessageSetRegulatedSize.Handler.class, MessageSetRegulatedSize.class, Side.SERVER, id++);
        PTPacketDispatcher.registerMessage(MessageOpenQRGui.Handler.class, MessageOpenQRGui.class, Side.CLIENT, id++);

        // GuiHandler
        NetworkRegistry.INSTANCE.registerGuiHandler(INSTANCE, new PTGuiHandler());


        // World 1.1.0
        PTDimensions.init();


        BiomeInit.registerBiomes();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @EventHandler
    public void init(FMLServerStartingEvent e) {
        e.registerServerCommand(new CommandQuantumRealm());

    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    static {
        FluidRegistry.enableUniversalBucket();
    }

    public static CreativeTabs CREATIVE_TAB = new CreativeTabs("tabPymTech") {

        @Override
        public ItemStack createIcon() {
            return new ItemStack(PTSuitSet.ANT_MAN.getHelmet());
        }

        @Override
        public void displayAllRelevantItems(NonNullList<ItemStack> items) {
            super.displayAllRelevantItems(items);
            {
                ItemStack bucket = new ItemStack(Item.REGISTRY.getObject(new ResourceLocation("forge:bucketfilled")));
                NBTTagCompound nbt = new NBTTagCompound();
                nbt.setString("FluidName", PTFluids.SHRINK_PYM_PARTICLES.getName());
                nbt.setInteger("Amount", 1000);
                bucket.setTagCompound(nbt);
                items.add(bucket);
            }
            {
                ItemStack bucket = new ItemStack(Item.REGISTRY.getObject(new ResourceLocation("forge:bucketfilled")));
                NBTTagCompound nbt = new NBTTagCompound();
                nbt.setString("FluidName", PTFluids.GROW_PYM_PARTICLES.getName());
                nbt.setInteger("Amount", 1000);
                bucket.setTagCompound(nbt);
                items.add(bucket);
            }
        }
    };

}
