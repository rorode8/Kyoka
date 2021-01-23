package Kyoka.commands.command;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import org.bson.Document;

import Kyoka.commands.CommandContext;
import Kyoka.commands.ICommand;
import Kyoka.items.ItemPuller;
import Kyoka.utils.MongoDB;
import net.dv8tion.jda.api.EmbedBuilder;

public class FishCommand implements ICommand {
	//private final EventWaiter waiter = new EventWaiter();
	private static Hashtable<Long, Long> timeout = new Hashtable<Long, Long>();
	private static Timer timer = new Timer();
	public static ItemPuller fish = new ItemPuller("fishdata.csv", "fish").init();

	@Override
	public void handle(CommandContext ctx) {

		// TODO Auto-generated method stub
		if (!MongoDB.userExists(ctx.getAuthor().getIdLong())) {
			MongoDB.createDocument(ctx.getAuthor().getIdLong());
		}

		if (timeout.containsKey(ctx.getAuthor().getIdLong())) {
			// wait message
			ctx.getChannel().sendMessage("you may fish once every 60 seconds").queue();
		} else {
			timeout.put(ctx.getAuthor().getIdLong(), ctx.getAuthor().getIdLong());
			ctx.getChannel().sendMessage("fishing...").queue((message) -> {

				//Random r = new Random();
				Document fishPull = fish.pull();
				
				if (fishPull.getInteger("id")!=9) {
					
					
					
					if(MongoDB.countItems(ctx.getAuthor().getIdLong(), "bag")<30){
						message.editMessage(ctx.getAuthor().getAsMention()+" congrats, you caught a " + fishPull.getString("name")).queueAfter(2,
								TimeUnit.SECONDS);
						MongoDB.pushIntoArray("bag", ctx.getAuthor().getIdLong(), fishPull);
						
					}else {
						MongoDB.updateDB(ctx.getAuthor().getIdLong(), Double.valueOf( fishPull.getInteger("sell value")));
						message.editMessage(ctx.getAuthor().getAsMention()+"  you caught a " + fishPull.getString("name") +" but your inventory is full so it was sold"
								+ " for: `"+fishPull.getInteger("sell value")+"`").queueAfter(2,
								TimeUnit.SECONDS);
						
					}
					
					
				} else {
					message.editMessage("nothing is biting").queueAfter(2, TimeUnit.SECONDS);
				}

			});
			untime(ctx.getAuthor().getIdLong());
		}
		
		if(ctx.getAuthor().getId().equals(Kyoka.utils.DataLoader.getString("ownerID"))) {
			
			System.out.println("gay1");
			ctx.getChannel().sendMessage("testing").queueAfter(5l, TimeUnit.SECONDS, (message)->{
				System.out.println("gay2");
			});
			
		}

	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "fish";
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return "to use this command register first \n"
				+ fish.toString();
	}
	
	public List<String> getAliases(){
		return Arrays.asList("cast","kkona");
	}

	private void untime(long id) {
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				timeout.remove(id, id);
			}
		};
		timer.schedule(task, 60000l);
		

	}

}
