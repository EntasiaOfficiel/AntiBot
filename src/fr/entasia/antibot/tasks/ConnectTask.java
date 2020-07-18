package fr.entasia.antibot.tasks;

import fr.entasia.antibot.utils.AntibotLevel;
import fr.entasia.antibot.utils.AntibotMode;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.PendingConnection;

import java.util.ArrayList;

public class ConnectTask implements Runnable {

	public static ArrayList<PendingConnection> connects = new ArrayList<>();
	public static int reminder = 0;

	@Override
	public void run() {
		if(connects.size()>8){
			reminder = 20;
		}else if(connects.size()>=5){
			reminder+=10;
		}else if(AntibotMode.isActive()){
			reminder-=3;
		}else return;

		System.out.println("reminder at "+reminder);

		if(connects.size()>30){
			if(AntibotMode.current==AntibotMode.ON)AntibotMode.set(AntibotMode.STRICT);
		}else if(AntibotMode.current==AntibotMode.STRICT)AntibotMode.set(AntibotMode.ON);

		connects.clear();

		if(reminder>=20) upgrade();
		else if(reminder<=0) degrade(); // TODO tout désactiver plutôt ?
		else return;
		reminder = 10;


	}

	private static void degrade(){
		System.out.println("a");
		if(AntibotMode.current==AntibotMode.STRICT)return;
		System.out.println(AntibotLevel.current);
		if(AntibotLevel.current==AntibotLevel.IPS_BASIS){
			System.out.println("DEGRADING");
			AntibotLevel.current = null;
			AntibotMode.set(AntibotMode.OFF);
		}else{
			AntibotLevel.set(AntibotLevel.getByID(AntibotLevel.current.id-1));
		}
	}

	private static void upgrade(){
		System.out.println("upgrade...");
		if(AntibotMode.current==AntibotMode.OFF){
			AntibotMode.set(AntibotMode.ON);
			AntibotLevel.set(AntibotLevel.IPS_BASIS);
			BaseComponent[] bc;
			for(PendingConnection c : connects){
				if(c.isConnected()){
					bc = AntibotLevel.IPS_BASIS.verify(c);
					if(bc!=null)c.disconnect(bc);
				}
			}
		}else{
			if(AntibotLevel.current==AntibotLevel.HARD_SAFELIST)return;
			AntibotLevel.set(AntibotLevel.getByID(AntibotLevel.current.id+1));
		}
		System.out.println("upgrading to "+AntibotLevel.current);
	}
}
