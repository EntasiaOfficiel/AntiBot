package fr.entasia.antibot.utils;

import fr.entasia.apis.utils.ServerUtils;

public enum AntibotMode { // a voir pour ajouter SLEEP ?
	OFF,
	STABILISING,
	ON,

	;

	public static AntibotMode current = OFF;

	public static void set(AntibotMode mode){
		if(current==mode)return;
		current = mode;
		ServerUtils.permMsg("antibot.infos", "§4§lAntibot : §cPassage au mode "+mode+" !");
	}


	public static boolean isActive(){
		return current != OFF;
	}
}
