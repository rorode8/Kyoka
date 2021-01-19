package Kyoka.commands.command;

import Kyoka.commands.CommandContext;
import Kyoka.commands.ICommand;
import Kyoka.items.CsvMapper;
import Kyoka.utils.MongoDB;

public class PetsCommand implements ICommand {
	
	private static CsvMapper data= new CsvMapper("pets.csv");

	@Override
	public void handle(CommandContext ctx) {
		
		if(!MongoDB.userExists(ctx.getAuthor().getIdLong()) || MongoDB.countItems(ctx.getAuthor().getIdLong(), "pets")<=0) {
			ctx.getChannel().sendMessage("you have no pets").queue();
			return;
		}
		
		
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "pets";
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return "information about your pets";
	}

}
