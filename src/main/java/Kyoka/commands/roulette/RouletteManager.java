package Kyoka.commands.roulette;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import net.dv8tion.jda.api.entities.TextChannel;

public class RouletteManager {
	
	private List<RoulettePlayer> players;
	private TextChannel channel;
	private Random r = new Random();
	private boolean betsOpen;
	
	public RouletteManager(TextChannel textChannel) {
		channel = textChannel;
		
	}
	/*
	 * initializes the players list, this must be done every round
	 */
	public void start() {
		players = new ArrayList<RoulettePlayer>();
		betsOpen=true;
	}
	
	public int sizeP() {
		return players.size();
	}
	
	public void addPlayer(long id, String name, double wage, List<Integer> bet) {
		players.add(new RoulettePlayer(id, name, wage, bet));
		channel.sendMessage(name+" your bet has been placed").queue();
		
	}
	public boolean isBetsOpen() {
		return betsOpen;
	}
	
	/*
	 * play the game, meaning pulls a number
	 */
	public void play() {
		betsOpen=false;
		int roll = r.nextInt(37);
		
		channel.sendMessage("bets are closed").addFile(new File("roulette/giphy.gif")).queue();
		channel.sendMessage("and it's a "+roll).addFile(new File("roulette/"+roll+".jpg")).queueAfter(7l, TimeUnit.SECONDS, (message) -> {
			
			String win="";
			for(int i=0;i<players.size();i++) {
				if(players.get(i).getNumber().contains(roll)) {
					win+=players.get(i).pay()+" \n";	//here we pay
				}
			}
			channel.sendMessage("**WINNERS:** \n"+
			win).queue();
			//bets are opened again
			channel.sendMessage("you may bet now").queueAfter(3l, TimeUnit.SECONDS, (message2) -> {
				this.start();			
				
			});
			
		});
		//});
		
		

	}

}
