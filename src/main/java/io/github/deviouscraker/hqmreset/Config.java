package io.github.deviouscraker.hqmreset;

public class Config {
	
	// the size(in bytes) of the file when it should be reset.
	private long maxSize = 0;
	
	// how long (in ticks) to wait before checking the size again
	private long waitTime = 0;

	public Config(HQMDataReset instance) {
		instance.getConfig().options().copyDefaults(true);
		instance.saveDefaultConfig();
		
		maxSize = Long.parseLong(instance.getConfig().getString("maxSize"));
		
		// since we ask for it in kb, we need to convert it to bytes.
		maxSize *= 1024;
		
		waitTime = Long.parseLong(instance.getConfig().getString("waitTime"));
		
		// converts into ticks
		waitTime *= 20;
	}
	
	public long getMaxSize() {
		return maxSize;
	}
	
	public long getWaitTime() {
		return waitTime;
	}
}
