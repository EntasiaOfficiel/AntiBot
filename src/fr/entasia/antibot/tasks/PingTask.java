package fr.entasia.antibot.tasks;

import java.util.HashMap;

public class PingTask implements Runnable {
	public static HashMap<byte[], Long> pings = new HashMap<>();

	@Override
	public void run() {
		long l = System.currentTimeMillis();
		pings.entrySet().removeIf(e->{
			return l-e.getValue()>10000;//10s cache
		});
	}

}
