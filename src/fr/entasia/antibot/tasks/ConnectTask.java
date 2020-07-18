package fr.entasia.antibot.tasks;

import fr.entasia.antibot.utils.AntibotLevel;
import fr.entasia.antibot.utils.AntibotMode;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.PendingConnection;

import java.util.ArrayList;

public class ConnectTask implements Runnable {

	public static ArrayList<PendingConnection> connects = new ArrayList<>();
	public static boolean reminder;

	@Override
	public void run() {
		System.out.println(connects.size());
		if(connects.size()>10){
			upgrade();

		}else if(connects.size()>=5){
			if(reminder) upgrade();
			else reminder = true;
		}else{
			reminder = false;
			degrade();
		}


		if(connects.size()>30){
			if(AntibotMode.current==AntibotMode.ON)AntibotMode.set(AntibotMode.STRICT);
		}else if(AntibotMode.current==AntibotMode.STRICT)AntibotMode.set(AntibotMode.ON);


		connects.clear();
	}

	private static void degrade(){
		if(AntibotMode.current==AntibotMode.STRICT)return;
		if(AntibotLevel.current==AntibotLevel.IPS_BASIS){
			AntibotLevel.current = null;
			AntibotMode.set(AntibotMode.OFF);
		}
	}

	private static void upgrade(){
		System.out.println("upgrade");
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
	}
}
