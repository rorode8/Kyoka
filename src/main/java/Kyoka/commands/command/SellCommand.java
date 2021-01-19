package Kyoka.commands.command;

import java.awt.Color;
import java.util.List;

import org.bson.Document;

import Kyoka.commands.CommandContext;
import Kyoka.commands.ICommand;
import Kyoka.utils.MongoDB;
import net.dv8tion.jda.api.EmbedBuilder;

public class SellCommand implements ICommand {

	@Override
	public void handle(CommandContext ctx) {
		// TODO Auto-generated method stub
		
		
		if(MongoDB.countItems(ctx.getAuthor().getIdLong(), "bag")==0 || !MongoDB.userExists(ctx.getAuthor().getIdLong())) {
			ctx.getChannel().sendMessage("you have no items to sell right now").queue();
			return;
		}
		if(ctx.getArgs().size()==0) {
			EmbedBuilder eb = new EmbedBuilder();
			eb.setTitle("Inventory");
			eb.setColor(new Color(0xF40C0C));
			eb.setDescription("Now displaying the first page of your inventory");
			String info="```\n"+String.format("%-8s %-25s %-8s %-10s %n", "ref", "name", "ItemID", "sell value");
			List<Document> items = MongoDB.retrieveDocs(ctx.getAuthor().getIdLong(), "bag");
			
			//int i =1;
			
			for(int i=0;i<Math.min(items.size(),10);i++) {
				info+=String.format("%-8s %-25s %-8d %-10d %n",String.valueOf(i+1), items.get(i).getString("name"), items.get(i).getInteger("id"), items.get(i).getInteger("sell value"));
				
			}
			info+="```";
			
			
			eb.addField("items: ", info, false);
			eb.setThumbnail("https://i.imgur.com/WFTZq1m.png");
			ctx.getChannel().sendMessage(eb.build()).queue();
			
			return;
		}
		
		if(ctx.getArgs().get(0).equalsIgnoreCase("all")) {
			//do
			double amount = MongoDB.sumArray(ctx.getAuthor().getIdLong());
			MongoDB.updateDB(ctx.getAuthor().getIdLong(), amount);
			MongoDB.purgeArray(ctx.getAuthor().getIdLong());
			ctx.getChannel().sendMessage("you just earned `"+amount+"`").queue();
			
		}else {
			//do 2
		}
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "sell";
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return "sell all";
	}

}
