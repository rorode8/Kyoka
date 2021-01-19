package Kyoka.commands.command;

import Kyoka.commands.CommandContext;
import Kyoka.commands.ICommand;
import Kyoka.utils.MongoDB;

public class balanceCommand implements ICommand{

	@Override
	public void handle(CommandContext ctx) {
		// TODO Auto-generated method stub
		ctx.getChannel().sendMessage("You have a balance of: "+"`"+MongoDB.getDoc(ctx.getAuthor().getIdLong()).getDouble("money")+"`").queue();
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "balance";
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return "this command tells you the amount of money you own";
	}

}
