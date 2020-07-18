package fr.entasia.antibot.utils;

import fr.entasia.antibot.Utils;
import fr.entasia.antibot.tasks.PingTask;
import fr.entasia.apis.other.ChatComponent;
import fr.entasia.apis.utils.ServerUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.PendingConnection;

public enum AntibotLevel {

	IPS_BASIS(1, new ChatComponent(
			"§cAntiBot :",
			"§cUne attaque est en cours !",
			"§cTon adresse IP à été détectée comme invalide (proxy/VPN ?)",
			"§cS'il s'agit d'un faux positif, attend la fin de l'attaque pour te connecter (habituellement 2 minutes)")){
		@Override
		public BaseComponent[] verify(PendingConnection c) {
			return null;
		}
	},
	PING(2, new ChatComponent(
			"§cAntiBot :",
			"§cUne attaque est en cours !",
			"§cLes connexions directes au serveur (connexion rapide) ont été temporairement suspendues",
			"§cAjoute le serveur à ta liste de serveurs pour te connecter")){
		@Override
		public BaseComponent[] verify(PendingConnection c) {
			if(PingTask.pings.remove(c.getAddress().getAddress().getAddress())==null) {
				return msg;
			}else return null;
		}
	},
	NAME_LEN(3, new ChatComponent(
			"§cAntiBot :",
			"§cUne attaque est en cours !",
			"§cTu as été détecté comme bot par le serveur",
			"§cS'il s'agit d'un faux positif, attend la fin de l'attaque pour te connecter (habituellement 2 minutes)")){
		@Override
		public BaseComponent[] verify(PendingConnection c) {
			String name = c.getName();
			if (name.length() == 16) {
				return msg;
			}else return null;
		}
	},
	SAFELIST(4, new ChatComponent(
			"§cAntiBot :",
			"Une attaque est en cours !",
			"Toutes les connexions au serveur ont été désactivées")){
		@Override
		public BaseComponent[] verify(PendingConnection c) {
			if(Utils.safeList.contains(c.getName()))return null;
			else return msg;
		}
	},
	HARD_SAFELIST(5, new ChatComponent(
			"§cAntiBot :",
			"Une attaque est en cours !",
			"Les nouvelles connexions au serveur ont été temporairement suspendues")){
		@Override
		public BaseComponent[] verify(PendingConnection c) {
			return msg;
		}
	},

	;

	public static AntibotLevel current;
	public int id;
	public BaseComponent[] msg;

	AntibotLevel(int id, ChatComponent msg){
		this.id = id;
		this.msg = msg.create();
	}

	public abstract BaseComponent[] verify(PendingConnection c);

	public static void set(AntibotLevel level){
		AntibotLevel.current = level;
		ServerUtils.permMsg("antibot.infos", "§4§lAntibot : §cPassage au niveau "+level+" !");
	}

	public static AntibotLevel getByID(int id){
		for(AntibotLevel lvl : values()){
			if(lvl.id==id)return lvl;
		}
		return null;
	}

	/*

	- name len check
	- ping check
	- ip check

	- safelists checks
	 */

	public static BaseComponent[] check(PendingConnection c){

		if(AntibotLevel.current == SAFELIST||AntibotLevel.current == AntibotLevel.HARD_SAFELIST){
			return AntibotLevel.current.verify(c);
		}else{
			BaseComponent[] cc;
			System.out.println("active="+AntibotMode.isActive());
			System.out.println("current="+current);
			switch(current){
				case NAME_LEN:{
					cc = NAME_LEN.verify(c);
					if(cc!=null)return cc;
				}
				case PING:{
					cc = PING.verify(c);
					if(cc!=null)return cc;
				}
				case IPS_BASIS: {
					cc = IPS_BASIS.verify(c);
					if(cc!=null)return cc;
				}
			}
			return null;
		}
	}
}
