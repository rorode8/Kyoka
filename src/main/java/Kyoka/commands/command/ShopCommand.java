package Kyoka.commands.command;

import java.awt.Color;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import Kyoka.commands.CommandContext;
import Kyoka.commands.ICommand;
import Kyoka.items.CsvMapper;
import Kyoka.items.ItemManager;
import Kyoka.utils.MongoDB;
import net.dv8tion.jda.api.EmbedBuilder;

public class ShopCommand implements ICommand{
	
	private static CsvMapper data= new CsvMapper("shopItems.csv");
	private static final int OFFSET = Integer.parseInt(data.getData().get(0).get("id"));
	private static List<Map<String, String>> items = data.getData();

	@Override
	public void handle(CommandContext ctx) {
		// TODO Auto-generated method stub
		if(ctx.getArgs().size()==0) {
			String info ="```\n";
			
			
			info+=String.format("%-5s %-15s %-10s %-10s %n", "ID", "name", "type", "price");
			int i=1;
			for(Map<String,String> row : items) {
				info+=String.format("%-5d %-15s %-10s %-10s %n", i, row.get("name"), row.get("type"), row.get("price"));
				i++;
			}
			info+="```";
			EmbedBuilder eb = new EmbedBuilder();
			eb.setTitle("Welcome to Da TEM SHOP!!!");
			eb.setThumbnail("https://i.imgur.com/gYWjfdy.png");
			eb.addField("items", info, false);
			eb.setColor(new Color(0xeb6c8c));
			ctx.getChannel().sendMessage(eb.build()).queue();
			return;
		}else if(ctx.getArgs().size()<=2) {
			
			int id=0;
			try {
				id=Integer.parseInt(ctx.getArgs().get(0));
			}catch(Exception e) {
				ctx.getChannel().sendMessage("invalid item id").queue();
				return;
			}
			if(id>items.size() || id<1) {
				ctx.getChannel().sendMessage("invalid item id").queue();
				return;
			}
			//normalized position
			id=id-1;
			
			int itemid = Integer.parseInt(items.get(id).get("id"));
			String type = items.get(id).get("type");
			
			if(ctx.getArgs().size()==2 && ctx.getArgs().get(1).equalsIgnoreCase("buy")) {
				double amount = Double.valueOf(items.get(id).get("price"));
				if(MongoDB.getDoc(ctx.getAuthor().getIdLong()).getDouble("money")<amount) {
					ctx.getChannel().sendMessage("you can't afford that").queue();
					return;
				}
				Document shopItem = new Document("id", Integer.valueOf(items.get(id).get("id")))
						.append("name", items.get(id).get("name"))
						.append("type", type)
						.append("sell value",  Integer.valueOf(items.get(id).get("sell price")));
				
				MongoDB.substract(ctx.getAuthor().getIdLong(), amount);
				MongoDB.pushIntoArray("bag", ctx.getAuthor().getIdLong(), shopItem);
				
				ctx.getChannel().sendMessage("purchased made succesfully "+ctx.getAuthor().getAsMention()).queue();
					
			}else {
				EmbedBuilder eb = new EmbedBuilder();
				eb.setTitle(items.get(id).get("name"));
				String info = ItemManager.getInfo(itemid, type);
				eb.addField("description", items.get(id).get("description")+"\n"+info, false);
				eb.setThumbnail(items.get(id).get("source"));
				eb.setColor(new Color(0xeb6c8c));
				ctx.getChannel().sendMessage(eb.build()).queue();
			}
			
			
		}
		
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "shop";
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return "fuck you";
	}

}
