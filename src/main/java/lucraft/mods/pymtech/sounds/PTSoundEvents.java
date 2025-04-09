package lucraft.mods.pymtech.sounds;

import lucraft.mods.pymtech.PymTech;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = PymTech.MODID)
public class PTSoundEvents {

    public static SoundEvent HELMET_OPENING;
    public static SoundEvent HELMET_CLOSING;
    public static SoundEvent SHRINK;
    public static SoundEvent GROW;
    public static SoundEvent BUTTON;
    public static SoundEvent QUANTUM_REALM_ENTER;
    public static SoundEvent QUANTUM_REALM_LEAVE;

    @SubscribeEvent
    public static void onRegisterSounds(RegistryEvent.Register<SoundEvent> e) {
        e.getRegistry().register(HELMET_OPENING = new SoundEvent(new ResourceLocation(PymTech.MODID, "helmet_opening")).setRegistryName(new ResourceLocation(PymTech.MODID, "helmet_opening")));
        e.getRegistry().register(HELMET_CLOSING = new SoundEvent(new ResourceLocation(PymTech.MODID, "helmet_closing")).setRegistryName(new ResourceLocation(PymTech.MODID, "helmet_closing")));
        e.getRegistry().register(SHRINK = new SoundEvent(new ResourceLocation(PymTech.MODID, "shrink")).setRegistryName(new ResourceLocation(PymTech.MODID, "shrink")));
        e.getRegistry().register(GROW = new SoundEvent(new ResourceLocation(PymTech.MODID, "grow")).setRegistryName(new ResourceLocation(PymTech.MODID, "grow")));
        e.getRegistry().register(BUTTON = new SoundEvent(new ResourceLocation(PymTech.MODID, "button")).setRegistryName(new ResourceLocation(PymTech.MODID, "button")));
        e.getRegistry().register(QUANTUM_REALM_ENTER = new SoundEvent(new ResourceLocation(PymTech.MODID, "quantum_realm_enter")).setRegistryName(new ResourceLocation(PymTech.MODID, "quantum_realm_enter")));
        e.getRegistry().register(QUANTUM_REALM_LEAVE = new SoundEvent(new ResourceLocation(PymTech.MODID, "quantum_realm_leave")).setRegistryName(new ResourceLocation(PymTech.MODID, "quantum_realm_leave")));
    }

}
