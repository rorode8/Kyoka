package Kyoka.commands.command;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import Kyoka.commands.CommandContext;
import Kyoka.commands.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;

public class CoinFlipCommand implements ICommand {

	@Override
	public void handle(CommandContext ctx) {
		EmbedBuilder em = new EmbedBuilder();
		Random rand = new Random();
		if(rand.nextInt(2)==1) {
			em.setTitle("Heads");
			em.setImage("https://i.imgur.com/wMRX9tU.png");
			
		}else {
			em.setTitle("Tails");
			em.setImage("https://i.imgur.com/K4pkIE6.png");
		}
		ctx.getChannel().sendMessage(em.build()).queue();
	

	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "coinflip";
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return "flips a coin";
	}
	
	public List<String> getAliases(){
		return Arrays.asList("soloqueue","volado");
	}

}
