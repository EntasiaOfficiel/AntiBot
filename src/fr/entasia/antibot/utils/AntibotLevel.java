package fr.entasia.antibot.utils;

import fr.entasia.antibot.Utils;

public enum AntibotLevel {
	PING(1), NAME_LEN(3), SAFELIST(5), HARD_SAFELIST(7);;

	public int id;
	public AntibotLevel[] exclusions;

	AntibotLevel(int id){
		this.id = id;
	}

	public static boolean isActive(){
		return Utils.currentLevel !=null;
	}

	public static boolean activeMode(AntibotLevel lvl){
		if(lvl.id <= Utils.currentLevel.id)return true;
		else return false;
	}
}
