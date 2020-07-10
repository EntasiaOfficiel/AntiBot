package fr.entasia.antibot.utils;

import fr.entasia.antibot.Utils;

public enum AntibotLevel {
	PING(1), NAME_LEN(3), SAFELIST(5, NAME_LEN), SAFELIST(5, NAME_LEN);;

	public int id;
	public AntibotLevel[] exclusions;

	AntibotLevel(int id, AntibotLevel... exclusions){
		this.id = id;
		this.exclusions = exclusions;
	}

	public static boolean isActive(){
		return Utils.currentLevel !=null;
	}

	public static boolean activeMode(AntibotLevel lvl){
		if(lvl.id == Utils.currentLevel.id)return true;
		else if(lvl.id < Utils.currentLevel.id){
			return Utils.currentLevel.includes(lvl);
		}else return false;
	}

	public boolean includes(AntibotLevel lvl){
		for(AntibotLevel l : exclusions){
			if(l==lvl)return false;
		}
		return true;
	}
}
