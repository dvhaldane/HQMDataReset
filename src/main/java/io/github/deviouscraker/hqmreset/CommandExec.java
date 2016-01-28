package io.github.deviouscraker.hqmreset;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandExec implements CommandExecutor {
	
	private static final String NO_PERMISSION = ChatColor.DARK_RED + "Insufficient permissions.";
	
	private HQMReset instance;
	
	public CommandExec(HQMReset instance) {
		this.instance = instance;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equals("hqmreset")) {
			if (!sender.hasPermission("hqmreset.manage")) {
				sender.sendMessage(NO_PERMISSION);
				return false;
			}
			
			if (args.length == 0) {
				sender.sendMessage(ChatColor.GOLD + "Usage /"+label+" [reset|size|reload]");
				return false;
			}
			
			args[0] = args[0].toLowerCase();
			
			if (args[0].equals("size")) {
				instance.reloadMain();
				float size = instance.getMainFile().length() / 1024f;
				String sizeStr = Float.toString(size).substring(0, Float.toString(size).indexOf('.') + 3); // cuts it down to 2 decimal places
				sender.sendMessage(ChatColor.GOLD + "Current HQM size: " + sizeStr + "kb");
				return true;
			} else if (args[0].equals("reload")) {
				instance.getPluginLoader().disablePlugin(instance);
				instance.getPluginLoader().enablePlugin(instance);
				sender.sendMessage("HQMReset has been reloaded.");
				return true;
			} else if (args[0].equals("reset")) {
				instance.resetMainFile(sender);
				
				return true;
			}
		}
		
		
		return false;
	}

}
