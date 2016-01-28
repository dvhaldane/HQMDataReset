package io.github.deviouscraker.hqmreset;

import java.io.File;
import java.nio.file.Files;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class HQMReset extends JavaPlugin {
	
	private Config config;
	private File mainFile;
	
	private boolean checkFile;
	
	@Override
	public void onEnable() {
		this.reloadConfig();
		this.config = new Config(this);
		
		this.getCommand("hqmreset").setExecutor(new CommandExec(this));
		
		new File(this.getDataFolder() + File.separator + "Backup").mkdir();
		reloadMain();
		
		checkFile = mainFile.exists();
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				if (checkFile) {
					if (mainFile.length() >= config.getMaxSize()) {
						resetMainFile(getServer().getConsoleSender());
					}
				}
			}
		}.runTaskTimer(this, 0L, config.getWaitTime());
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
			if (!mainFile.exists()) {
				this.getServer().dispatchCommand(this.getServer().getConsoleSender(), "hqm quest");
				sender.sendMessage(ChatColor.GOLD + "Reset Successful!");			
			} else {
				Files.delete(mainFile.toPath());
				this.getServer().dispatchCommand(this.getServer().getConsoleSender(), "hqm quest");
				sender.sendMessage(ChatColor.GOLD + "Reset Successful!");
			}
		} catch (Exception e) {
			getLogger().log(Level.SEVERE, "Reset Failed!", e);
			sender.sendMessage(ChatColor.DARK_RED + "Reset Failed!");
		}
	}
}
