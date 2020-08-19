package fr.entasia.antibot;

import fr.entasia.antibot.tasks.EvalTask;
import fr.entasia.antibot.tasks.PingTask;
import fr.entasia.antibot.tools.AntibotCmd;
import fr.entasia.antibot.tools.Listeners;
import fr.entasia.apis.other.ChatComponent;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.util.concurrent.TimeUnit;

public class Main extends Plugin {

	public static Main main;

	//tasks ScheduledTask
	public static ScheduledTask pingTask;
	public static ScheduledTask connectTask;

	public static final ChatComponent baseMsg = new ChatComponent(
			"§cAntiBot :",
			"Une attaque est en cours !");


	@Override
	public void onEnable() {
		try{
			main = this;

			getProxy().getPluginManager().registerCommand(this, new AntibotCmd("antibot"));
			getProxy().getPluginManager().registerListener(this, new Listeners());

			pingTask = getProxy().getScheduler().schedule(this, new PingTask(), 0, 6, TimeUnit.SECONDS);
			connectTask = getProxy().getScheduler().schedule(this, new EvalTask(), 0, 1, TimeUnit.SECONDS);


		}catch(Throwable e){
			e.printStackTrace();
			getLogger().severe("Une erreur est survenue ! ARRET DU SERVEUR");
			getProxy().stop();
		}
	}

//	public static void main(String[] as) throws Exception {
//		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/?useSSL=false", "root", "azerty123");
//		connection.setAutoCommit();
//	}
}
