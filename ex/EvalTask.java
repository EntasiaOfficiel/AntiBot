package fr.entasia.antibot.tasks;

import fr.entasia.antibot.Utils;
import fr.entasia.antibot.tools.Listeners;
import fr.entasia.antibot.utils.AntibotLevel;
import fr.entasia.antibot.utils.AntibotMode;
import net.md_5.bungee.api.connection.PendingConnection;

import java.util.ArrayList;

public class EvalTask implements Runnable {

	public static ArrayList<PendingConnection> connectAfter = new ArrayList<>();
	public static int connectBefore;
	public static int reminder = 0;

	@Override
	public void run() {
		int connect;
		if(AntibotMode.current==AntibotMode.STABILISING){
			connect = connectAfter.size();
		}else{
			connect = connectBefore;
		}

		if (connect > 8) {
			reminder += 25;
		} else if (connect >= 5) {
			reminder += 10;
		} else{
			if (AntibotMode.isActive()) {
				reminder -= 3;
			} else {
				for (PendingConnection pd : connectAfter) {
					if (Utils.safeList.contains(pd.getName())) continue;
					Utils.safeList.add(pd.getName());
				}
				connectAfter.clear();
				return;
			}
		}

		System.out.println("reminder at "+reminder);

		connectAfter.clear();

		if(reminder>=20) upgrade();
		else if(reminder<=-30){
			AntibotLevel.current = null;
			AntibotMode.set(AntibotMode.OFF);
		}else{
			if(AntibotMode.current==AntibotMode.STABILISING&&connect<6){

			}
			return;
		}
		reminder = 0;
	}

//	private static void degrade(){
//		System.out.println("degrade...");
//		System.out.println(AntibotLevel.current);
//		if(AntibotLevel.current==AntibotLevel.IPS_BASIS){
//			System.out.println("DEGRADING");
//			AntibotLevel.current = null;
//			AntibotMode.set(AntibotMode.OFF);
//		}else{
//			AntibotLevel.set(AntibotLevel.getByID(AntibotLevel.current.id-1));
//		}
//	}

	private static void upgrade(){
		System.out.println("upgrade...");
		if(AntibotMode.current==AntibotMode.OFF){
			AntibotMode.set(AntibotMode.STABILISING);
			AntibotLevel.set(AntibotLevel.IPS_BASIS);
//			BaseComponent[] bc;
			for(PendingConnection c : connectAfter){
				if(c.isConnected()){
					c.disconnect(Listeners.stabilising.create());
//					bc = AntibotLevel.IPS_BASIS.verify(c);
//					if(bc!=null)c.disconnect(bc);
				}
			}
		}else{
			if(AntibotLevel.current==AntibotLevel.HARD_SAFELIST)return;
			AntibotLevel.set(AntibotLevel.getByID(AntibotLevel.current.id+1));
		}
		System.out.println("upgrading to "+AntibotLevel.current);
	}
}
