package Kyoka.commands.command;

import Kyoka.commands.CommandContext;
import Kyoka.commands.ICommand;

public class STFU4plyCommand implements ICommand {

	@Override
	public void handle(CommandContext ctx) {
		
		if(ctx.getAuthor().getId().equals(Kyoka.utils.DataLoader.getString("ownerID"))) {
			System.out.println("not cringe");
			if(Kyoka.utils.DataLoader.commandOn == true) {
				ctx.getChannel().sendMessage("4ply has been set free").queue();
				Kyoka.utils.DataLoader.turnOff();
			}else {
				ctx.getChannel().sendMessage("STFU4ply NaM").queue();
				Kyoka.utils.DataLoader.turnOn();
			}
		}else {
			System.out.println("cringe");
		}
		

	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "4ply";
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return "if casted by the right person it will fuck 4ply";
	}

}
