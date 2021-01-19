package Kyoka.commands.race;


import java.util.ArrayList;
import java.util.List;

import Kyoka.utils.MongoDB;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class RaceListener extends ListenerAdapter {
	
	 private final long channelId, guildId; // id because keeping the entity would risk cache to become outdated
	 private RaceBuilder rb;
	 private TextChannel channel;
	 
	 private List<Double>bets;
	 private List<String>tags;
	 private List<Long>ids;
	 private List<Character>chooses;
	 
	    public RaceListener(TextChannel channel, long guildId) {
	    	this.channel=channel;
	        this.channelId = channel.getIdLong();
	        this.guildId = guildId;
	        rb= new RaceBuilder(channel);
	        bets=new ArrayList<Double>();
	        tags= new ArrayList<String>();
	        ids = new ArrayList<Long>();
	        chooses = new ArrayList<Character>();
	    }

	    @Override
	    public void onMessageReceived(MessageReceivedEvent event) {
	    	System.out.println("hello");
	    	char choose;
	    	if (event.getAuthor().isBot()) return;
	        if (event.getChannel().getIdLong() != channelId) return;
	        if (event.getGuild().getIdLong() != guildId) return;
	        String content[] = event.getMessage().getContentRaw().split(" ");
	        System.out.println(content[0]+"I!kirby");
	        if(!content[0].equalsIgnoreCase("I!kirby") || content.length<3) return;
	        if(!MongoDB.userExists(event.getAuthor().getIdLong())) return;
	        switch(content[1]) {
	        	case "blue":
	        		System.out.println("hello1");
	        		choose='b';
	        		break;
	        	case "purple":
	        		choose='p';
	        		break;
	        	case "normal":
	        		choose = 'n';
	        		break;
	        	case "green":
	        		choose = 'g';
	        		break;
	        	default:
	        		event.getChannel().sendMessage("invalid arguments").queue();
	        		return;
	        }
	        double bet ;
	        try {
	        	bet = (double)Integer.parseInt(content[2]);
	        	if(bet<1) {
	        		event.getChannel().sendMessage("you must bet a positive number").queue();
	        		return;
	        	}
	        	
	        }catch(Exception e) {
	        	event.getChannel().sendMessage("invalid arguments").queue();
	        	return;
	        }
	        
	        if(MongoDB.getDoc(event.getAuthor().getIdLong()).getDouble("money")<bet) {
	        	event.getChannel().sendMessage("you're too poor to bet "+bet).queue();
	        	return;
	        }
	        if(ids.contains(event.getAuthor().getIdLong())) {
	        	event.getChannel().sendMessage("you may only place one bet").queue();
	        	return;
	        }
	        if(!bets.isEmpty() && bet<bets.get(bets.size()-1)) {
	        	event.getChannel().sendMessage("you must bet the same or more as the last person").queue();
	        	return;
	        }
	        
	        rb.addBet(bet, event.getAuthor().getAsMention(), event.getAuthor().getIdLong(), choose);
	        bets.add(bet);
	        tags.add(event.getAuthor().getAsMention());
	        ids.add(event.getAuthor().getIdLong());
	        chooses.add(choose);
	        
	        
	        event.getChannel().sendMessage(event.getAuthor().getAsMention()+" your bet has been placed").queue();
	        
	        
	    }
	    
	    public void go() {
	    	
	    	if(rb.getSizeHorses()<2) {
	    		channel.sendMessage("not enough bets were made, the race has been cancelled").queue();
	    		
	    	}else {
	    		for(int i=0; i<bets.size();i++) {
	    			MongoDB.substract(ids.get(i), bets.get(i));
	    	        //rb.addBet(bets.get(i), tags.get(i), ids.get(i), chooses.get(i));
		    	}
	    		rb.play();
	    	}
	    	
	    	channel.getJDA().removeEventListener(this);
	    	
	    	
	    	
	    	
	    }
	

}
