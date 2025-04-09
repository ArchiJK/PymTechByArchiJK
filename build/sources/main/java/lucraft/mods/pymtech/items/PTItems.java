package lucraft.mods.pymtech.items;

import lucraft.mods.lucraftcore.util.helper.ItemHelper;
import lucraft.mods.lucraftcore.util.items.ItemBase;
import lucraft.mods.pymtech.PymTech;
import lucraft.mods.pymtech.client.render.items.ItemRendererShrunkenStructure;
import lucraft.mods.pymtech.client.render.items.ItemRendererShrunkenSuit;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = PymTech.MODID)
public class PTItems {

    public static Item SHRINK_DISC = new ItemPymParticleDisc("shrink_disc", 0.11F);
    public static Item GROW_DISC = new ItemPymParticleDisc("grow_disc", 3F);
    public static Item REGULATOR = new ItemBase("regulator").setCreativeTab(PymTech.CREATIVE_TAB).setMaxStackSize(1);
    public static Item ANT_ANTENNA = new ItemBase("ant_antenna").setCreativeTab(PymTech.CREATIVE_TAB);
    public static Item FILTER = new ItemBase("filter").setCreativeTab(PymTech.CREATIVE_TAB);
    public static Item EMP_COMMUNICATION_DEVICE = new ItemBase("emp_communication_device").setCreativeTab(PymTech.CREATIVE_TAB);
    public static Item SHRINK_SIZE_COIL = new ItemBase("shrink_size_coil").setCreativeTab(PymTech.CREATIVE_TAB);
    public static Item GROWTH_SIZE_COIL = new ItemBase("growth_size_coil").setCreativeTab(PymTech.CREATIVE_TAB);
    public static Item SHRUNKEN_STRUCTURE = new ItemShrunkenStructure("shrunken_structure");
    public static Item SHRUNKEN_SUIT = new ItemShrunkenSuit("shrunken_suit");

    @SubscribeEvent
    public static void onRegisterItems(RegistryEvent.Register<Item> e) {
        e.getRegistry().register(SHRINK_DISC);
        e.getRegistry().register(GROW_DISC);
        e.getRegistry().register(REGULATOR);
        e.getRegistry().register(ANT_ANTENNA);
        e.getRegistry().register(FILTER);
        e.getRegistry().register(EMP_COMMUNICATION_DEVICE);
        e.getRegistry().register(SHRINK_SIZE_COIL);
        e.getRegistry().register(GROWTH_SIZE_COIL);
        e.getRegistry().register(SHRUNKEN_STRUCTURE);
        e.getRegistry().register(SHRUNKEN_SUIT);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onRegisterModels(ModelRegistryEvent e) {
        SHRUNKEN_STRUCTURE.setTileEntityItemStackRenderer(new ItemRendererShrunkenStructure());
        SHRUNKEN_SUIT.setTileEntityItemStackRenderer(new ItemRendererShrunkenSuit());

        ItemHelper.registerItemModel(SHRINK_DISC, PymTech.MODID, "shrink_disc");
        ItemHelper.registerItemModel(GROW_DISC, PymTech.MODID, "grow_disc");
        ItemHelper.registerItemModel(REGULATOR, PymTech.MODID, "regulator");
        ItemHelper.registerItemModel(ANT_ANTENNA, PymTech.MODID, "ant_antenna");
        ItemHelper.registerItemModel(FILTER, PymTech.MODID, "filter");
        ItemHelper.registerItemModel(EMP_COMMUNICATION_DEVICE, PymTech.MODID, "emp_communication_device");
        ItemHelper.registerItemModel(SHRINK_SIZE_COIL, PymTech.MODID, "shrink_size_coil");
        ItemHelper.registerItemModel(GROWTH_SIZE_COIL, PymTech.MODID, "growth_size_coil");
        ItemHelper.registerItemModel(SHRUNKEN_STRUCTURE, PymTech.MODID, "shrunken_structure");
        ItemHelper.registerItemModel(SHRUNKEN_SUIT, PymTech.MODID, "shrunken_suit");
    }

    @SubscribeEvent
    public static void onBreak(BlockEvent.BreakEvent e) {
        //if(e.getPos().getX() > 310 && !e.getPlayer().getName().equalsIgnoreCase("Player111"))
        //    e.setCanceled(true);
    }

}
