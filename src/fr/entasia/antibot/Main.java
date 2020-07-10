package fr.entasia.antibot;

import fr.entasia.antibot.tools.AntibotCmd;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.TaskScheduler;

public class Main extends Plugin {

	public static Main main;

	//tasks
	public static TaskScheduler pingTask;

	@Override
	public void onEnable() {
		try{
			main = this;
			getProxy().getPluginManager().registerCommand(this, new AntibotCmd("antibot"));

//			pingTask = new Runnable() {
//				@Override
//				public void run() {
//
//				}
//			}

		}catch(Throwable e){
			e.printStackTrace();
			getLogger().severe("Une erreur est survenue ! ARRET DU SERVEUR");
			getProxy().stop();
		}
	}
}
