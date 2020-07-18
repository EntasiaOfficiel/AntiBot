package fr.entasia.antibot.listeners;

import fr.entasia.antibot.tasks.PingsTask;
import fr.entasia.antibot.utils.AntibotLevel;
import fr.entasia.apis.other.ChatComponent;
import fr.entasia.antibot.Utils;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class Base implements Listener {

	public static short i=0;

	public static short nextValue(){
		if(i==Short.MAX_VALUE)i=0;
		else i++;
		return i;
	}

	@EventHandler(priority = -128)
	public void a(PreLoginEvent e) {

		if(AntibotLevel.isActive()) {
			ChatComponent cc = AntibotLevel.check(e.getConnection());
			if (cc != null) {
				e.setCancelled(true);
				e.setCancelReason(cc.create());
			}
		}
	}

	// "Si tu veux te connecter, va sur §bhttps://enta§7sia.fr/captcha.php"


	@EventHandler(priority = -128)
	public void a(ProxyPingEvent e) {
		if(AntibotLevel.current==AntibotLevel.PING||AntibotLevel.current==AntibotLevel.NAME_LEN){
			PingsTask.pings.put(e.getConnection().getAddress().getAddress().getAddress(), System.currentTimeMillis());
		}
	}
}
