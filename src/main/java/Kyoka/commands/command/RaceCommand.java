package Kyoka.commands.command;

import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;

import Kyoka.commands.CommandContext;
import Kyoka.commands.ICommand;
import Kyoka.commands.race.RaceListener;
import net.dv8tion.jda.api.EmbedBuilder;

public class RaceCommand implements ICommand {
	
	private static Hashtable<Long, Long> timeout = new Hashtable<Long, Long>();
	private static Timer timer = new Timer();

	@Override
	public void handle(CommandContext ctx) {
		// TODO Auto-generated method stub
		if (timeout.containsKey(ctx.getGuild().getIdLong())) {
			// wait message
			ctx.getChannel().sendMessage("you must wait before playing again").queue();
			return;			
		}
		
		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle("Choose a racer!!!");		
		eb.setColor(0xF40C0C);
		eb.setDescription("use the commands above to participate");
		eb.addField("example commad: ", "I!kirby [color] [amount]", false);
		eb.addField("available  colors", "blue, normal, purple, green", false);
		
		ctx.getChannel().sendMessage(eb.build()).queue();
		
		//(TextChannel channel, long guildId)
		RaceListener rl = new RaceListener(ctx.getChannel(), ctx.getGuild().getIdLong());
		ctx.getChannel().getJDA().addEventListener(rl);
		
		timeout.put(ctx.getGuild().getIdLong(), ctx.getGuild().getIdLong());
		untime(ctx.getGuild().getIdLong(), rl);
		
		
		

	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "race";
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return "bet on a kirby";
	}
	
	private void untime(long id, RaceListener rl) {
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				timeout.remove(id, id);
			}
		};
		TimerTask task1 = new TimerTask() {
			@Override
			public void run() {
				rl.go();
			}
		};
		
		timer.schedule(task, 60000l);
		timer.schedule(task1, 25000l);
		// timer.schedule(task, 30000l);

	}

}
