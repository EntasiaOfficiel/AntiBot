package fr.entasia.antibot.tasks;

import java.util.HashMap;
import java.util.Map;

public class PingsTask implements Runnable {
    public static HashMap<byte[], Long> pings;

    @Override
    public void run() {
        long l = System.currentTimeMillis();
        pings.entrySet().removeIf(e->{
            return l-e.getValue()>10000;//10s cache
        });
    }

}
