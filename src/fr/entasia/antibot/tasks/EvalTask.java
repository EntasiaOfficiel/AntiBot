package fr.entasia.antibot.tasks;

import fr.entasia.antibot.AntibotAPI;
import fr.entasia.antibot.tools.Listeners;
import fr.entasia.antibot.utils.AntibotLevel;
import fr.entasia.antibot.utils.AntibotMode;
import fr.entasia.apis.other.ChatComponent;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.HashSet;

public class EvalTask implements Runnable {


	public static ArrayList<String> ips = new ArrayList<>();
	public static ArrayList<ProxiedPlayer> connectAfter = new ArrayList<>();
	public static HashSet<String> safeList = new HashSet<>();
	public static int connectBefore;
	public static int reminder;

	/*
	6 : sur d'une attaque
	< 4 : sûr qu'il n'y a pas d'attaque
	entre les deux : zone floue
	 */

	@Override
	public void run() {

		// update reminder
		if (connectAfter.size() >= 6) {
			reminder += 25;
		}else if(connectBefore < 4 && AntibotMode.current == AntibotMode.ON){
			reminder-=3;
		}else{
			reminder = 0;
		}

		System.out.println("reminder at "+reminder);

		// take reminder actions
		if(reminder>=20){
			upgrade();
			reminder = 0;
		}else{
			if(reminder<=-30){
				AntibotLevel.current = null;
				AntibotMode.set(AntibotMode.OFF);
				reminder = 0;
			}else{
				if(AntibotMode.current==AntibotMode.STABILISING&&connectAfter.size()<6){
					AntibotMode.set(AntibotMode.ON);
				}
			}
		}

		// others
		if(!AntibotAPI.isActive()&&connectBefore<4){
			addToSafelist();
		}

		connectBefore = 0;
		connectAfter.clear();
	}

	public static void addToSafelist(){
		for (ProxiedPlayer p : connectAfter) {
			if (safeList.contains(p.getName())) continue;
			safeList.add(p.getName());
		}
	}

	private static void upgrade(){
		if(AntibotMode.current==AntibotMode.OFF){
			AntibotMode.set(AntibotMode.STABILISING);
			AntibotLevel.set(AntibotLevel.lowest());
//			BaseComponent[] bc;
			for(ProxiedPlayer p : connectAfter){
				if(p.isConnected()){
					p.addGroups("bot");
					p.disconnect(Listeners.stabilising);
//					bc = AntibotLevel.IPS_BASIS.verify(c);
//					if(bc!=null)c.disconnect(bc);
				}
			}
		}else{
			if(AntibotLevel.current==AntibotLevel.highest())return;
			AntibotLevel.set(AntibotLevel.getByID(AntibotLevel.current.id+1));
		}
	}
}
