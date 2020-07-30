package fr.entasia.antibot.tools;

import fr.entasia.antibot.tasks.EvalTask;
import fr.entasia.antibot.tasks.PingTask;
import fr.entasia.antibot.utils.AntibotLevel;
import fr.entasia.antibot.utils.AntibotMode;
import fr.entasia.apis.other.ChatComponent;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class Listeners implements Listener {

	public static ChatComponent stabilising = new ChatComponent(
			"§cAntiBot :",
			"§cUne attaque est en cours !",
			"§cL'antibot est en train de trouver le bon niveau de défense, entre le pisolet à eau et le lance-flamme",
			"§cTu devrais pouvoir te connecter dans ~15 secondes");

	@EventHandler(priority = -128)
	public void a(PreLoginEvent e) {
		if(AntibotMode.isActive()) {
			BaseComponent[] bc = AntibotLevel.check(e.getConnection());
			if (bc==null) {
				if(AntibotMode.current==AntibotMode.STABILISING) {
					e.setCancelled(true);
					e.setCancelReason(stabilising.create());
					EvalTask.wouldConnect++;
				}
			}else{
				e.setCancelled(true);
				e.setCancelReason(bc);
			}
		}
	}

	// "Si tu veux te connecter, va sur §bhttps://enta§7sia.fr/captcha.php"

	@EventHandler(priority = 127)
	public void a(PostLoginEvent e) {
		EvalTask.connects.add(e.getPlayer().getPendingConnection());
	}

	@EventHandler(priority = -128)
	public void a(ProxyPingEvent e) {
		System.out.println("ping");
		if (AntibotLevel.current == AntibotLevel.PING || AntibotLevel.current == AntibotLevel.NAME_LEN) {
			PingTask.pings.put(e.getConnection().getAddress().getAddress().getAddress(), System.currentTimeMillis());
		}
	}
}
