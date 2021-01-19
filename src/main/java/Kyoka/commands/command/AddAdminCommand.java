package Kyoka.commands.command;

import java.util.List;

import Kyoka.commands.CommandContext;
import Kyoka.commands.ICommand;
import net.dv8tion.jda.api.entities.User;

public class AddAdminCommand implements ICommand{
	
	public boolean isSecret() {
		return true;
	}

	@Override
	public void handle(CommandContext ctx) {
		//List<String> args = ctx.getArgs();
		System.out.println("in");
		List<User> user = ctx.getMessage().getMentionedUsers();
		if(user.size()!=1) {
			ctx.getChannel().sendMessage("please mention only one user").queue();
			return;
		}
		Long id = user.get(0).getIdLong();
		try {
			Kyoka.utils.DataLoader.recordAdmin(id);
			ctx.getChannel().sendMessage("user has been added").queue();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "admin";
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return "adds global admin for bot";
	}

}
