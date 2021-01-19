package Kyoka.commands.command;

import Kyoka.commands.CommandContext;
import Kyoka.commands.ICommand;
import Kyoka.utils.MongoDB;

public class RegisterCommand implements ICommand {

	@Override
	public void handle(CommandContext ctx) {
		
		if(MongoDB.userExists(ctx.getAuthor().getIdLong())) {
			ctx.getChannel().sendMessage("You are already registred").queue();
			
		}else {
			MongoDB.createDocument(ctx.getAuthor().getIdLong());
			ctx.getChannel().sendMessage("You've been succesufully registed").queue();
		}

	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "register";
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return "register yourself to have an account and gain access to more features";
	}

}
