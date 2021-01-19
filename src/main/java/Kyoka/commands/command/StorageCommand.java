package Kyoka.commands.command;

import java.awt.Color;
import java.util.List;

import org.bson.Document;

import Kyoka.commands.CommandContext;
import Kyoka.commands.ICommand;
import Kyoka.utils.MongoDB;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

public class StorageCommand implements ICommand{

	@Override
	public void handle(CommandContext ctx) {
		// TODO Auto-generated method stub
		if(!MongoDB.userExists(ctx.getAuthor().getIdLong())) {
			ctx.getChannel().sendMessage("you don't have any items").queue();
		}

		final TextChannel channel = ctx.getChannel();
		long id = ctx.getAuthor().getIdLong();

		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle("Inventory");
		eb.setColor(new Color(0xF40C0C));
		eb.setDescription("Now displaying the first page of your inventory");
		String info = "```\n" + String.format("%-8s %-25s %-8s %-10s %n", "ref", "name", "ItemID", "value");
		List<Document> items = MongoDB.retrieveDocs(ctx.getAuthor().getIdLong(), "storage");

		// int i =1;

		for (int i = 0; i < Math.min(items.size(), 10); i++) {
			info += String.format("%-8s %-25s %-8d %-10d %n", String.valueOf(i + 1), items.get(i).getString("name"),
					items.get(i).getInteger("id"), items.get(i).getInteger("sell value"));

		}
		info += "```";

		eb.addField("items: ", info, false);
		eb.setThumbnail("https://i.imgur.com/npypybC.png");
		channel.sendMessage(eb.build()).queue();
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "storage";
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return "use it to see the items you've stored";
	}

}
