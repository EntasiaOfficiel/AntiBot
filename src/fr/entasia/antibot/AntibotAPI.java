package fr.entasia.antibot;

import fr.entasia.antibot.utils.AntibotLevel;
import fr.entasia.antibot.utils.AntibotMode;

public class AntibotAPI {

	public static boolean isActive(){
		return AntibotMode.current != AntibotMode.OFF;
	}

	public static AntibotMode getMode(){
		return AntibotMode.current;
	}

	public static AntibotLevel getLevel(){
		return AntibotLevel.current;
	}
}
