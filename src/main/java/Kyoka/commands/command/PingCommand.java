package Kyoka.commands.command;

import Kyoka.commands.CommandContext;
import Kyoka.commands.ICommand;
import net.dv8tion.jda.api.JDA;

public class PingCommand implements ICommand {

	@Override
	public void handle(CommandContext ctx) {
		// TODO Auto-generated method stub
		JDA jda = ctx.getJDA();
		
		jda.getRestPing().queue(
				(ping)->ctx.getChannel()
				.sendMessageFormat("Reset ping: %sms\nWS ping: %sms", ping, jda.getGatewayPing()).queue()
				);
	}
	

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "ping";
	}


	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return "Shows the current ping from the bot to the discord servers";
	}
}
