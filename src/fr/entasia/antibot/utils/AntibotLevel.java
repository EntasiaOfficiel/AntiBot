package fr.entasia.antibot.utils;

import fr.entasia.antibot.AntibotAPI;
import fr.entasia.antibot.Main;
import fr.entasia.antibot.tasks.EvalTask;
import fr.entasia.antibot.tasks.PingTask;
import fr.entasia.apis.other.ChatComponent;
import fr.entasia.apis.utils.ServerUtils;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.PendingConnection;

import java.util.Arrays;

public enum AntibotLevel {

//	IPS_BASIS(1, new ChatComponent(
//			"§cTon adresse IP à été détectée comme invalide (proxy/VPN ?)",
//			"§cS'il s'agit d'un faux positif, attend la fin de l'attaque pour te connecter (habituellement 2 minutes)")){
//		@Override
//		public BaseComponent[] verify(PendingConnection c) {
//			return null;
//		}
//	},
	PING(1, 1, new ChatComponent(
			"§cLes connexions directes au serveur (connexion rapide) ont été temporairement suspendues (ping first)",
			"§cAjoute le serveur à ta liste de serveurs pour te connecter")){
		@Override
		public BaseComponent[] verify(PendingConnection c) {

			if(PingTask.pings.remove(Main.hashIP(c.getAddress().getAddress().getAddress()))==null) {
				return msg;
			}else{
				return null;
			}
		}
	},
	NAME_LEN(2, 1, new ChatComponent(
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
	PREMIUM(3, 1, new ChatComponent(
			"§cLes comptes craqués ont été suspendus jusqu'a la fin de l'attaque",
			"§cAttend quelques minutes pour pouvoir te connecter")){
		@Override
		public BaseComponent[] verify(PendingConnection c) { // TODO HARDCODER UN CHECK POUR VOIR S'IL EST KICK ET ENVOYER LE MESSAGE LA PROCHAINE FOIS
			c.setOnlineMode(true);
			return null;
		}
	},
	SAFELIST(4, 2, new ChatComponent(
			"Toutes les nouvelles connexions au serveur ont été temporairement suspendues")){
		@Override
		public BaseComponent[] verify(PendingConnection c) {
			if(EvalTask.safeList.contains(c.getName()))return null;
			else return msg;
		}
	},
	HARD_SAFELIST(5, 2, new ChatComponent(
			"Toutes les nouvelles connexions au serveur ont été temporairement suspendues")){
		@Override
		public BaseComponent[] verify(PendingConnection c) {
			return msg;
		}
	},

	;

	public static AntibotLevel current;
	public int id;
	public int protocol;
	public BaseComponent[] msg;

	AntibotLevel(int id, int protocol, ChatComponent msg){
		this.id = id;
		this.protocol = protocol;
		this.msg = msg.insertFirst(Main.baseMsg).create();
	}

	public abstract BaseComponent[] verify(PendingConnection c);


	public static void set(AntibotLevel level){
		if(current==level)return;
		AntibotLevel.current = level;
		Main.main.getLogger().info("Passage au niveau "+level);
		ServerUtils.permMsg("antibot.infos", "§4§lAntibot : §cPassage au niveau "+level+" !");
	}

	public static AntibotLevel lowest(){
		return PING;
	}

	public static AntibotLevel highest(){
		return HARD_SAFELIST;
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
			switch(current){
				case PREMIUM:{
					PREMIUM.verify(c);
				}
				case NAME_LEN:{
					cc = NAME_LEN.verify(c);
					if(cc!=null)return cc;
				}
				case PING:{
					cc = PING.verify(c);
					if(cc!=null)return cc;
				}
//				case IPS_BASIS: {
//					cc = IPS_BASIS.verify(c);
//					if(cc!=null)return cc;
//				}
			}
			return null;
		}
	}
}
