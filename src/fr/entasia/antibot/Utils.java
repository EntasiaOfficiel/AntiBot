package fr.entasia.antibot;

import fr.entasia.antibot.utils.AntibotLevel;
import fr.entasia.antibot.utils.AntibotMode;
import fr.entasia.corebungee.Main;
import fr.entasia.corebungee.utils.BungeePlayer;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Utils {

	public static AntibotLevel currentLevel;
	public static AntibotMode mode;

	public static ArrayList<String> safeList = new ArrayList<>();
	public static ScheduledTask update;


	public static void changeLevel(AntibotLevel level){
		currentLevel = level;
	}

}
