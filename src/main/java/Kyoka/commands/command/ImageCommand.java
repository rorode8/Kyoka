package Kyoka.commands.command;

import java.io.File;
import java.util.Random;

import Kyoka.commands.CommandContext;
import Kyoka.commands.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;

public class ImageCommand implements ICommand{

	@Override
	public void handle(CommandContext ctx) {
		Random rand = new Random();
		Message message = new MessageBuilder().append("My message").build();
		EmbedBuilder em = new EmbedBuilder();
		
		//em.setImage(Kyoka.utils.DataLoader.files.get(rand.nextInt(Kyoka.utils.DataLoader.files.size())).getPath());
		File meme = Kyoka.utils.DataLoader.files.get(rand.nextInt(Kyoka.utils.DataLoader.files.size()));
		ctx.getChannel().sendFile(meme, meme.getName()).queue();
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "img";
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return "sends random image";
	}

}
