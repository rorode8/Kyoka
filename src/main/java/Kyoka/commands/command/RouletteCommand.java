package Kyoka.commands.command;

import java.util.Hashtable;
import java.util.Timer;

import Kyoka.commands.CommandContext;
import Kyoka.commands.ICommand;
import Kyoka.commands.roulette.RouletteListener;
import Kyoka.utils.DataLoader;
import net.dv8tion.jda.api.EmbedBuilder;

public class RouletteCommand implements ICommand{
	
	public static Hashtable<Long, Long> active = new Hashtable<Long, Long>();
	

	@Override
	public void handle(CommandContext ctx) {
		// TODO Auto-generated method stub
		if(active.containsKey(ctx.getGuild().getIdLong())) {
			ctx.getChannel().sendMessage("there's already a game in this guild").queue();
			return;
		}		
		
		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle("**THE ISABELLE CASINO IS OPENING**");
		eb.setImage("https://i.imgur.com/MPuUMqG.png");
		eb.setColor(0xff238);
		String content = "in order to play use "+DataLoader.getString("prefix")+"bet `choose` `amount` \n";
		content+="`choose` can be one of the following two word formmats: \n";
		content+="`column [n]` where n = 1,2,3 for example `I!bet column 2 50` which would bet 50 in the second column \n";
		content+="other valid formats are `numbers [numbers separeted with commas(,)]` `even` `odd` `red` `black` \n";
		content+="to see more detailed info use the help command \n";
		eb.addField("Instructions", content, true);
		ctx.getChannel().sendMessage(eb.build()).queue();
		
		ctx.getChannel().getJDA().addEventListener(new RouletteListener(ctx.getChannel(), ctx.getGuild().getIdLong()));
		active.put(ctx.getGuild().getIdLong(), ctx.getGuild().getIdLong());
		
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "roulette";
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return "it's an european roulette, suck it up kid";
	}
	

}
