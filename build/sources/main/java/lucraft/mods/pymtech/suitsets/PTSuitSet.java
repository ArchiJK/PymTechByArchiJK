package lucraft.mods.pymtech.suitsets;

import lucraft.mods.lucraftcore.superpowers.suitsets.RegisterSuitSetEvent;
import lucraft.mods.lucraftcore.superpowers.suitsets.SuitSet;
import lucraft.mods.pymtech.PymTech;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = PymTech.MODID)
public class PTSuitSet extends SuitSet {

	public static PTSuitSet ANT_MAN = new SuitSetPymParticles("ant_man", "pymtech.suit.ant_man.desc", false);
	public static PTSuitSet ANT_MAN_2 = new SuitSetPymParticles("ant_man_2", "pymtech.suit.ant_man_2.desc", false);
	public static PTSuitSet ANT_MAN_3 = new SuitSetPymParticles("ant_man_3", "pymtech.suit.ant_man_3.desc", true);
	public static PTSuitSet WASP = new SuitSetPymParticles("wasp", "pymtech.suit.wasp.desc", true);

	@SubscribeEvent
	public static void onRegisterItems(RegisterSuitSetEvent e) {
		e.register(ANT_MAN);
		e.register(ANT_MAN_2);
		e.register(ANT_MAN_3);
		e.register(WASP);
	}

	@SubscribeEvent
	public static void onRegisterItems(RegistryEvent.Register<Item> e) {
		for (ResourceLocation sets : SuitSet.REGISTRY.getKeys()) {
			if (SuitSet.REGISTRY.getObject(sets) instanceof PTSuitSet) {
				SuitSet.REGISTRY.getObject(sets).registerItems(e);
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void onRegisterModels(ModelRegistryEvent e) {
		for (ResourceLocation sets : SuitSet.REGISTRY.getKeys()) {
			if (SuitSet.REGISTRY.getObject(sets) instanceof PTSuitSet) {
				SuitSet.REGISTRY.getObject(sets).registerModels();
			}
		}
	}

	// ---------------------------------------------------------------------------------

	public PTSuitSet(String name) {
		super(name);
		this.setRegistryName(PymTech.MODID, name);
	}

	@Override
	public String getModId() {
		return PymTech.MODID;
	}

	@Override
	public CreativeTabs getCreativeTab() {
		return PymTech.CREATIVE_TAB;
	}

}
