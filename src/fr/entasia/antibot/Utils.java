package fr.entasia.antibot;

import fr.entasia.antibot.utils.AntibotLevel;
import fr.entasia.antibot.utils.AntibotMode;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.util.ArrayList;

public class Utils {

	public static AntibotMode mode;

	public static ArrayList<String> safeList = new ArrayList<>();
	public static ScheduledTask update;


	public static void changeLevel(AntibotLevel level){
		AntibotLevel.current = level;
	}

}
