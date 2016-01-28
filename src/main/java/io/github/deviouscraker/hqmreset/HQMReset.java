package io.github.deviouscraker.hqmreset;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class HQMReset extends JavaPlugin {
	
	private Config config;
	private File backupFile;
	private File mainFile;
	
	private boolean checkFile;
	
	@Override
	public void onEnable() {
		this.config = new Config(this);
		
		this.getCommand("hqmreset").setExecutor(new CommandExec(this));
		
		new File(this.getDataFolder() + File.separator + "Backup").mkdir();
		backupFile = new File(this.getDataFolder() + File.separator + "Backup" + File.separator + "players.dat");
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
	
	public File getBackupFile() {
		return backupFile;
	}
	
	// It takes in a CommandSender argument to be able to send a message back to the caller
	public void resetMainFile(CommandSender sender) {
		try {
			Files.copy(getBackupFile().toPath(), getMainFile().toPath(), StandardCopyOption.REPLACE_EXISTING);
			sender.sendMessage(ChatColor.GOLD + "Reset Successful!");
		} catch (IOException e) {
			getLogger().log(Level.SEVERE, "Reset Failed!", e);
			sender.sendMessage(ChatColor.DARK_RED + "Backup Failed!");
		}
	}
}
