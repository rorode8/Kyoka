package Kyoka.commands.command;

import java.awt.Color;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.bson.Document;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

import Kyoka.commands.CommandContext;
import Kyoka.commands.ICommand;
import Kyoka.items.ItemManager;
import Kyoka.utils.MongoDB;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

public class InventoryCommand implements ICommand {
	private final EventWaiter waiter;
	private static final String FORWARD = "U+25B6";
	private static final String BACKWARDS = "U+25C0";

	public InventoryCommand(EventWaiter waiter) {
		this.waiter = waiter;
	}

	@Override
	public void handle(CommandContext ctx) {
		// TODO Auto-generated method stub
		if (!MongoDB.userExists(ctx.getAuthor().getIdLong())) {
			ctx.getChannel().sendMessage("you don't have any items").queue();
		}

		final TextChannel channel = ctx.getChannel();
		long id = ctx.getAuthor().getIdLong();

		if (ctx.getArgs().size() == 0) {
			EmbedBuilder eb = new EmbedBuilder();
			eb.setTitle("Inventory");
			eb.setColor(new Color(0xF40C0C));
			eb.setDescription("Now displaying the first page of your inventory");
			String info = "```\n" + String.format("%-8s %-25s %-8s %-10s %n", "ref", "name", "ItemID", "value");
			List<Document> items = MongoDB.retrieveDocs(ctx.getAuthor().getIdLong(), "bag");

			// int i =1;

			for (int i = 0; i < Math.min(items.size(), 10); i++) {
				info += String.format("%-8s %-25s %-8d %-10d %n", String.valueOf(i + 1), items.get(i).getString("name"),
						items.get(i).getInteger("id"), items.get(i).getInteger("sell value"));

			}
			info += "```";

			eb.addField("items: ", info, false);
			eb.setThumbnail("https://i.imgur.com/WFTZq1m.png");
			// ctx.getChannel().sendMessage(eb.build()).queue();

			channel.sendMessage(eb.build())

					.queue((message) -> {
						// message.addReaction(BACKWARDS).queue();
						if (items.size() > 10) {
							message.addReaction(FORWARD).queue();
							this.waiter.waitForEvent(GuildMessageReactionAddEvent.class,
									(e) -> e.getMessageIdLong() == message.getIdLong() && !e.getUser().isBot(), (e) -> {
										//
										if (e.getReactionEmote().getAsCodepoints().equalsIgnoreCase(FORWARD)) {
											change(channel, id, message.getIdLong(), 10);

										}
										message.clearReactions().queue();

									}, 10L, TimeUnit.SECONDS, null);

						}

					});
			
		}else if(ctx.getArgs().size()==2) {
			if(ctx.getArgs().get(1).equals("use")) {
				
				//implement use
				int index=0;
				try {
					index=Integer.parseInt(ctx.getArgs().get(0));
				}catch(Exception e) {
					ctx.getChannel().sendMessage("invalid item id").queue();
					return;
				}
				if(index<1 || index>MongoDB.countItems(id, "bag")) {
					ctx.getChannel().sendMessage("invalid item").queue();
					return;
				}
				//use
				boolean canUse = ItemManager.use(ctx.getChannel(), ctx.getAuthor().getIdLong(), index);
				if(!canUse) {
					ctx.getChannel().sendMessage("that item can't be used for now").queue();
					return;
				}
				
				
			}else if(ctx.getArgs().get(1).equals("sell")) {
				//sedll
			}
			
			
		}

	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "inventory";
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return "shows your inventory";
	}

	private void change(TextChannel channel, long authorId, long messageId, int j) {

		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle("Inventory");
		eb.setColor(new Color(0xF40C0C));
		eb.setDescription("Now displaying the first page of your inventory");
		String info = "```\n" + String.format("%-8s %-25s %-8s %-10s %n", "ref", "name", "ItemID", "value");
		List<Document> items = MongoDB.retrieveDocs(authorId, "bag");

		// int i =1;

		for (int i = j; i < Math.min(items.size(), j + 10); i++) {
			info += String.format("%-8s %-25s %-8d %-10d %n", String.valueOf(i + 1), items.get(i).getString("name"),
					items.get(i).getInteger("id"), items.get(i).getInteger("sell value"));

		}

		info += "```";

		eb.addField("items: ", info, false);
		eb.setThumbnail("https://i.imgur.com/WFTZq1m.png");

		channel.editMessageById(messageId, eb.build())

				.queue((message) -> {
					if (j >= 10) {
						message.addReaction(BACKWARDS).queue();
					}
					if (items.size() > j + 10) {
						message.addReaction(FORWARD).queue();
					}

					this.waiter.waitForEvent(GuildMessageReactionAddEvent.class,
							(e) -> e.getMessageIdLong() == message.getIdLong() && !e.getUser().isBot(), (e) -> {
								//
								if (e.getReactionEmote().getAsCodepoints().equalsIgnoreCase(FORWARD)
										&& items.size() > j + 10) {
									change(channel, authorId, messageId, j + 10);

								} else if (e.getReactionEmote().getAsCodepoints().equalsIgnoreCase(BACKWARDS)
										&& j >= 10) {
									change(channel, authorId, messageId, j - 10);
								}

								message.clearReactions().queue();
							}, 10L, TimeUnit.SECONDS, null);

				});

	}

}
