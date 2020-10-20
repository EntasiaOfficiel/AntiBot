package fr.entasia.antibot.tools;

import fr.entasia.antibot.tasks.EvalTask;
import fr.entasia.antibot.utils.AntibotLevel;
import fr.entasia.antibot.utils.AntibotMode;
import fr.entasia.apis.other.ChatComponent;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class AntibotCmd extends Command {

	public AntibotCmd(String name) {
		super(name);
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(sender.hasPermission("antibot.cmd")) {
			if(args.length==0){
				sender.sendMessage(ChatComponent.create("§cArguments disponibles :"));
				sender.sendMessage(ChatComponent.create("§c- info"));
			}else {
				args[0] = args[0].toLowerCase();
				switch(args[0]){
					case "info":{
						sender.sendMessage(ChatComponent.create("§cInformations sur l'antibot :"));
						sender.sendMessage(ChatComponent.create("§c- Mode : "+ AntibotMode.current));
						sender.sendMessage(ChatComponent.create("§c- Level : "+ AntibotLevel.current));
						sender.sendMessage(ChatComponent.create("§c- Reminder : "+ EvalTask.reminder));
						sender.sendMessage(ChatComponent.create("§c- Taille Safelist : "+ EvalTask.safeList.size()));
						break;
					}
					default:{
						sender.sendMessage(ChatComponent.create("§cArgument invalide !"));
						break;
					}
				}
			}
		}else sender.sendMessage(ChatComponent.create("§cTu n'as pas accès à cette commande !"));

	}
}
