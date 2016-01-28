package io.github.deviouscraker.hqmreset;

import java.io.File;
import java.nio.file.Files;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class HQMDataReset extends JavaPlugin {
	
	private Config config;
	private File mainFile;
	
	@Override
	public void onEnable() {
		this.reloadConfig();
		this.config = new Config(this);
		
		this.getCommand("hqmdatareset").setExecutor(new CommandExec(this));
		
		reloadMain();
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				if (mainFile.exists()) { // if it doesn't exist there will be an issue with getting the length
					if (mainFile.length() >= config.getMaxSize()) {
						resetMainFile(getServer().getConsoleSender());
					}
				}
			}
		}.runTaskTimer(this, 0L, config.getWaitTime()); // checks to see if there is a file every x seconds
								// where x = the value of waitTime in the config.
	}
	
	public void reloadMain() {
		mainFile = new File("world" + File.separator + "HardcoreQuesting" + File.separator + "players.dat");
	}
	
	public File getMainFile() {
		return mainFile;
	}
	
	// It takes in a CommandSender argument to be able to send a message back to the caller
	public void resetMainFile(CommandSender sender) {
		try {
			if (mainFile.exists()) { //  if it does exist, delete it
				Files.delete(mainFile.toPath());
			}
			this.getServer().dispatchCommand(this.getServer().getConsoleSender(), "hqm quest");
			sender.sendMessage(ChatColor.GOLD + "Reset Successful!");
		} catch (Exception e) {
			getLogger().log(Level.SEVERE, "Reset Failed!", e);
			sender.sendMessage(ChatColor.DARK_RED + "Reset Failed!");
		}
	}
}
