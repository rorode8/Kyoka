package Kyoka.commands.command;

import java.util.Random;

import Kyoka.commands.CommandContext;
import Kyoka.commands.ICommand;

public class RollCommand implements ICommand {
	
	private static Random r = new Random();

	@Override
	public void handle(CommandContext ctx) {
		// TODO Auto-generated method stub
		if(ctx.getArgs().size()<1) {
			ctx.getChannel().sendMessage(ctx.getAuthor().getAsMention()+" just rolled `"+(r.nextInt(100)+1)+"`").queue();
		}else if(ctx.getArgs().size()>=1) {
			int p = 0;
			try {
				p = Integer.valueOf(ctx.getArgs().get(0));
				if(p>100 || p<1) {
					ctx.getChannel().sendMessage("invalid arguments").queue();
					return;
				}
			}catch(Exception e) {
				ctx.getChannel().sendMessage("invalid arguments").queue();
				return;
			}
			ctx.getChannel().sendMessage(ctx.getAuthor().getAsMention()+" just rolled `"+(r.nextInt(p)+1)+"`").queue();
		
		}
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "roll";
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return "rolls a number between 1 and the given parameter I!roll `50` would roll a number between 1 and 50 \n"+
				"the parameter must be less or equal than 100. If no parameter is given rolls between 1 and 100";
	}

}
