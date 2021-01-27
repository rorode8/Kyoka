package Kyoka.commands.command;

import Kyoka.commands.CommandContext;
import Kyoka.commands.ICommand;
import Kyoka.utils.DataLoader;
import Kyoka.utils.MongoDB;

public class TransferCommand implements ICommand{

	@Override
	public void handle(CommandContext ctx) {
		// TODO Auto-generated method stub
		if(ctx.getMessage().getMentionedUsers().size()!=1) {
			ctx.getChannel().sendMessage("invalid user").queue();
			return;
		}
		long r = ctx.getMessage().getMentionedUsers().get(0).getIdLong();
		
		if (!MongoDB.userExists(ctx.getAuthor().getIdLong())) {
			MongoDB.createDocument(ctx.getAuthor().getIdLong());
		}
		if (!MongoDB.userExists(r)) {
			MongoDB.createDocument(r);
		}
		double amount =0;
		try {
			amount = (double)Integer.parseInt(ctx.getArgs().get(1));
		}catch(Exception e) {
			ctx.getChannel().sendMessage("invalid amount").queue();
			return;
		}
		
		
		if(amount<=0 || MongoDB.getDoc(ctx.getAuthor().getIdLong()).getDouble("money")<amount || r == ctx.getAuthor().getIdLong()) {
			ctx.getChannel().sendMessage("invalid amount").queue();
			return;
		}
		
		MongoDB.substract(ctx.getAuthor().getIdLong(), amount);
		MongoDB.updateDB(r, amount);
		
		ctx.getChannel().sendMessage(ctx.getAuthor().getAsMention()+" just transfered `"+amount+"` to "+ctx.getMessage().getMentionedUsers().get(0).getAsMention()).queue();
		
		
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "transfer";
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return "usage is `"+DataLoader.getString("prefix")+"transfer [user] [amount]"+"`";
	}

}
