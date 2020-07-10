package fr.entasia.antibot.listeners;

import fr.entasia.antibot.utils.AntibotLevel;
import fr.entasia.apis.other.ChatComponent;
import fr.entasia.corebungee.Main;
import fr.entasia.antibot.Utils;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Base implements Listener {

	public static HashMap<String, Short> ips;
	public static short i=0;

	public static short nextValue(){
		if(i==Short.MAX_VALUE)i=0;
		else i++;
		return i;
	}

	@EventHandler(priority = -128)
	public void a(PreLoginEvent e) {

		if(AntibotLevel.isActive()){
			if(AntibotLevel.activeMode(AntibotLevel.PING)){
				String ip = e.getConnection().getAddress().getAddress().getHostAddress();
				if(ips.get(ip)==null) {
					e.setCancelled(true);
					e.setCancelReason(new ChatComponent(
							"§cAntiBot :",
							"Une attaque est en cours !",
							"Les connexions directes au serveur (connexion rapide) ont été temporairement suspendues",
							"Ajoute le serveur à ta liste de serveurs pour vous connecter")
					.create());
					return;
				}else ips.remove(ip);
			}

			if(AntibotLevel.activeMode(AntibotLevel.NAME_LEN)){
				String name = e.getConnection().getName();
				if(name.length()==16){
					e.setCancelReason(new ChatComponent(
							"§cAntiBot :",
							"Une attaque est en cours !",
							"Tu as été détecté comme bot par le serveur",
							"S'il s'agit d'un faux positif, attend la fin de l'attaque pour te connecter (habituellement 2 minutes)")
							.create());
				}

			}else if(AntibotLevel.activeMode(AntibotLevel.SAFELIST)){
				String name = e.getConnection().getName();
				if(!Utils.safeList.contains(name)){
					e.setCancelled(true);
					e.setCancelReason(new ChatComponent(
							"§cAntiBot :",
							"Une attaque est en cours !",
							"Les nouvelles connexions au serveur ont été temporairement suspendues")
					.create());
				}
			}else if(AntibotLevel.activeMode(AntibotLevel.HARD_SAFELIST)){
				String name = e.getConnection().getName();
				if(!Utils.safeList.contains(name)){
					e.setCancelled(true);
					e.setCancelReason(new ChatComponent(
							"§cAntiBot :",
							"Une attaque est en cours !",
							"Toutes les connexions au serveur ont été désactivées")
					.create());
				}
			}
		}
	}

	// "Si tu veux te connecter, va sur §bhttps://enta§7sia.fr/captcha.php"


	@EventHandler(priority = -128)
	public void a(ProxyPingEvent e) {
		if(AntibotLevel.activeMode(AntibotLevel.PING)){
			String ip = e.getConnection().getAddress().getAddress().getHostAddress();
			short a = nextValue();
			ips.put(ip, a);


			ProxyServer.getInstance().getScheduler().schedule(Main.main, () -> {
				if(ips.get(ip)==a)ips.remove(ip);
			}, 7, TimeUnit.SECONDS);


		}
	}
}
