package Kyoka.commands.roulette;

import java.util.ArrayList;

import Kyoka.commands.race.RaceBuilder;
import Kyoka.utils.MongoDB;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class RouletteListener extends ListenerAdapter{
	
	private final long channelId, guildId; // id because keeping the entity would risk cache to become outdated
	private RouletteManager rm;
	private TextChannel channel;
	
	public RouletteListener(TextChannel channel, long guildId) {
    	this.channel=channel;
        this.channelId = channel.getIdLong();
        this.guildId = guildId;
	    //initialize rm
        rm = new RouletteManager(channel);
    }
	
	@Override
    public void onMessageReceived(MessageReceivedEvent event) {
		if (event.getAuthor().isBot()) return;
        if (event.getChannel().getIdLong() != channelId) return;
        if (event.getGuild().getIdLong() != guildId) return;
        String content[] = event.getMessage().getContentRaw().split(" ");
        if(!MongoDB.userExists(event.getAuthor().getIdLong())) return;
        
        
	}
	
	@SuppressWarnings("unused")
	private int[] parseBet(String[] args) throws Exception {
		int[] ans=null;
		switch(args[1]) {
		case "numbers":
			String[] nums = args[2].split(",");
			if(36 % nums.length != 0 || nums.length>6) {
				throw new Exception("invalid single numbers");
			}
			ans = parseStringArr(nums);
			break;
		case "column":
			switch(args[2]) {
			case "1":
				//
				ans = new int[]{1,4,7,10,13,16,9,22,25,28,31,34};
				break;
			case "2":
				//
				ans = new int[]{2, 5, 8, 11, 14, 17, 20, 23, 26, 29, 32, 35};
				break;
			case "3":
				//
				ans = new int[]{ 3, 6, 9, 12, 15, 18, 21, 24, 27, 30, 33, 36};
				break;
			default:
				throw new Exception("no such column");
			}
			break;
		case "dozen":
			switch(args[2]) {
			case "1":
				//
				ans = new int[]{1,2,3,4,5,6,7,8,9,10,11,12};
				break;
			case "2":
				//
				ans = new int[]{13,14,15,16,17,18,19,20,21,22,23,24};
				break;
			case "3":
				ans = new int[]{25,26,27,28,29,30,31,32,33,34,35,36};
				break;
			default:
				throw new Exception("no such dozen");
			}
			break;
		case "odd":
			ans = new int[]{1,3,5,7,9,11,13,15,17,19,21,23,25,27,29,31,33,35};
			break;
		case "even":
			ans = new int[]{2,4,6,8,10,12,14,16,18,20,22,24,26,28,30,32,34,36};
			break;
		case "red":
			ans = new int[]{1, 3, 5, 7, 9, 12, 14, 16, 18, 19, 21, 23, 25, 27, 30, 32, 34, 36};
			break;
		case "black":
			ans = new int[]{2, 4, 6, 8, 10, 11,	13, 15, 17, 20, 22, 24,	26, 28, 29, 31, 33, 35};
			break;
		case "half":
			if(args[2].equalsIgnoreCase("1")) {
				ans = new int[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18};
			}else if(args[2].equalsIgnoreCase("2")) {
				ans = new int[]{19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36};
			}else {
				throw new Exception("gay");
			}
			break;
			
		}
		
		return ans;
	}
	
	private int[] parseStringArr(String[] numberStrs) throws Exception {
		
		int[] numbers = new int[numberStrs.length];
		for(int i = 0;i < numberStrs.length;i++)
		{
		   int x = Integer.parseInt(numberStrs[i]);
		   if(x<0 || x>36) {
			   throw new Exception("cringe number");
		   }
		   numbers[i] = x;
		}
		
		return numbers;
	}
}
