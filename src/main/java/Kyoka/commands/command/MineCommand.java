package Kyoka.commands.command;

import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import org.bson.Document;

import Kyoka.commands.CommandContext;
import Kyoka.commands.ICommand;
import Kyoka.items.ItemPuller;
import Kyoka.utils.MongoDB;

public class MineCommand implements ICommand {
	//private final EventWaiter waiter = new EventWaiter();
	private static Hashtable<Long, Long> timeout = new Hashtable<Long, Long>();
	private static Timer timer = new Timer();
	public static ItemPuller mine = new ItemPuller("minedata.csv", "mine").init();

	@Override
	public void handle(CommandContext ctx) {

		// TODO Auto-generated method stub
		if (!MongoDB.userExists(ctx.getAuthor().getIdLong())) {
			MongoDB.createDocument(ctx.getAuthor().getIdLong());
		}

		if (timeout.containsKey(ctx.getAuthor().getIdLong())) {
			// wait message
			ctx.getChannel().sendMessage("you may mine once every 60 seconds").queue();
		} else {
			timeout.put(ctx.getAuthor().getIdLong(), ctx.getAuthor().getIdLong());
			ctx.getChannel().sendMessage("mining...").queue((message) -> {

				//Random r = new Random();
				Document minePull = mine.pull();
				if (minePull.getInteger("id")!=9) {
					
					
					
					if(MongoDB.countItems(ctx.getAuthor().getIdLong(), "bag")<30){
						message.editMessage(ctx.getAuthor().getAsMention()+" congrats, you found " + minePull.getString("name")).queueAfter(2,
								TimeUnit.SECONDS);
						MongoDB.pushIntoArray("bag", ctx.getAuthor().getIdLong(), minePull);
						
					}else {
						MongoDB.updateDB(ctx.getAuthor().getIdLong(), Double.valueOf( minePull.getInteger("sell value")));
						message.editMessage(ctx.getAuthor().getAsMention()+"  you found a " + minePull.getString("name") +" but your inventory is full so it was sold"
								+ " for: `"+minePull.getInteger("sell value")+"`").queueAfter(2,
								TimeUnit.SECONDS);						
					}
					
					
				} else {
					message.editMessage("nothing was found, the 5G waves are too strong here for there to be any shungite").queueAfter(2, TimeUnit.SECONDS);
				}

			});
			untime(ctx.getAuthor().getIdLong());
		}
		


	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "mine";
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return "to use this command register first \n"
				+ mine.toString();
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