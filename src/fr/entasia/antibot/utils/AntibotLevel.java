package fr.entasia.antibot.utils;

import fr.entasia.antibot.Utils;
import fr.entasia.antibot.tasks.PingsTask;
import fr.entasia.apis.other.ChatComponent;
import net.md_5.bungee.api.connection.PendingConnection;

public enum AntibotLevel {

	BLACKLIST_IPS(1){
		@Override
		public ChatComponent apply(PendingConnection c) {
			return null;
		}
	},
	PING(3){
		@Override
		public ChatComponent apply(PendingConnection c) {
			if(PingsTask.pings.remove(c.getAddress().getAddress().getAddress())==null) {
				return new ChatComponent(
						"§cAntiBot :",
						"Une attaque est en cours !",
						"Les connexions directes au serveur (connexion rapide) ont été temporairement suspendues",
						"Ajoute le serveur à ta liste de serveurs pour vous connecter");
			}else return null;
		}
	},
	NAME_LEN(5){
		@Override
		public ChatComponent apply(PendingConnection c) {
			String name = c.getName();
			if (name.length() == 16) {
				return new ChatComponent(
						"§cAntiBot :",
						"Une attaque est en cours !",
						"Tu as été détecté comme bot par le serveur",
						"S'il s'agit d'un faux positif, attend la fin de l'attaque pour te connecter (habituellement 2 minutes)");
			}else return null;
		}
	},
	SAFELIST(7){
		@Override
		public ChatComponent apply(PendingConnection c) {
			if(Utils.safeList.contains(c.getName()))return null;
			else{
				return new ChatComponent(
						"§cAntiBot :",
						"Une attaque est en cours !",
						"Toutes les connexions au serveur ont été désactivées");
			}
		}
	},
	HARD_SAFELIST(9){
		@Override
		public ChatComponent apply(PendingConnection c) {
			return new ChatComponent(
					"§cAntiBot :",
					"Une attaque est en cours !",
					"Les nouvelles connexions au serveur ont été temporairement suspendues");
		}
	},

	;

	public static AntibotLevel current;
	public int id;

	AntibotLevel(int id){
		this.id = id;
	}

	public abstract ChatComponent apply(PendingConnection c);

	public static boolean isActive(){
		return current !=null;
	}

	/*

	- name len check
	- ping check
	- ip check

	- safelists checks
	 */

	public static ChatComponent check(PendingConnection c){

		if(AntibotLevel.current == SAFELIST||AntibotLevel.current == AntibotLevel.HARD_SAFELIST){
			return AntibotLevel.current.apply(c);
		}else{
			ChatComponent cc;
			switch(current){
				case NAME_LEN:{
					cc = NAME_LEN.apply(c);
					if(cc!=null)return cc;
				}
				case PING:{
					cc = PING.apply(c);
					if(cc!=null)return cc;
				}
				case BLACKLIST_IPS: {
					cc = BLACKLIST_IPS.apply(c);
					if(cc!=null)return cc;
				}
			}
			return null;
		}
	}
}
