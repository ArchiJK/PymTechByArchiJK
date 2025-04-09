package lucraft.mods.pymtech.commands;

import lucraft.mods.lucraftcore.sizechanging.capabilities.CapabilitySizeChanging;
import lucraft.mods.pymtech.dimensions.PTDimensions;
import net.minecraft.client.renderer.entity.SizeChangerPymParticles;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandQuantumRealm extends CommandBase {

	@Override
	public String getName() {
		return "quantumrealm";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.quantumrealm.usage";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length > 1)
			throw new WrongUsageException("commands.quantumrealm.usage", new Object[0]);
		else {
			if (args.length <= 0) {
				EntityPlayer player = getCommandSenderAsPlayer(sender);
                player.getCapability(CapabilitySizeChanging.SIZE_CHANGING_CAP, null).setSize(PTDimensions.isQuantumDimension(player.dimension) ? 16F : 0.00001F, SizeChangerPymParticles.PYM_PARTICLES_SUBATOMIC);
				notifyCommandListener(sender, this, "commands.quantumrealm.success.self");
			} else {
				Entity entity = getEntity(server, sender, args[0]);
				if(entity.hasCapability(CapabilitySizeChanging.SIZE_CHANGING_CAP, null)) {
                    entity.getCapability(CapabilitySizeChanging.SIZE_CHANGING_CAP, null).setSize(PTDimensions.isQuantumDimension(entity.dimension) ? 16F : 0.00001F, SizeChangerPymParticles.PYM_PARTICLES_SUBATOMIC);
                    notifyCommandListener(sender, this, "commands.quantumrealm.success.other", new Object[] { entity.getName() });
                } else {
                    sender.sendMessage(new TextComponentTranslation("commands.size.wrongentity"));
                }
			}
		}
	}

}
