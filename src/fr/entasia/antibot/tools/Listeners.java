package fr.entasia.antibot.tools;

import fr.entasia.antibot.Utils;
import fr.entasia.antibot.tasks.ConnectTask;
import fr.entasia.antibot.tasks.PingTask;
import fr.entasia.antibot.utils.AntibotLevel;
import fr.entasia.antibot.utils.AntibotMode;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class Listeners implements Listener {

	@EventHandler(priority = -128)
	public void a(PreLoginEvent e) {

		if(AntibotMode.isActive()) {
			BaseComponent[] bc = AntibotLevel.check(e.getConnection());
			if (bc != null) {
				e.setCancelled(true);
				e.setCancelReason(bc);
			}
		}
	}

	// "Si tu veux te connecter, va sur §bhttps://enta§7sia.fr/captcha.php"

	@EventHandler(priority = 127)
	public void a(PostLoginEvent e) {
		ConnectTask.connects.add(e.getPlayer().getPendingConnection());
		if(ConnectTask.connects.size()<5){
			String n = e.getPlayer().getName();
			if(Utils.safeList.contains(n))return;
			Utils.safeList.add(n);
		}
	}

	@EventHandler(priority = -128)
	public void a(ProxyPingEvent e) {
		if(AntibotLevel.current==AntibotLevel.PING||AntibotLevel.current==AntibotLevel.NAME_LEN){
			PingTask.pings.put(e.getConnection().getAddress().getAddress().getAddress(), System.currentTimeMillis());
		}
	}
}
