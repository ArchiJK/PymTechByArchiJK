package lucraft.mods.pymtech.proxies;

import lucraft.mods.pymtech.client.render.tileentities.TESRStructureShrinker;
import lucraft.mods.pymtech.tileentities.TileEntityStructureShrinker;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class PTClientProxy extends PTCommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);

        // TESR
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityStructureShrinker.class, new TESRStructureShrinker());
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }
}
