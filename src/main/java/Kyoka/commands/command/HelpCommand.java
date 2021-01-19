package Kyoka.commands.command;

import java.util.Arrays;
import java.util.List;

import Kyoka.CommandManager;
import Kyoka.commands.CommandContext;
import Kyoka.commands.ICommand;
import net.dv8tion.jda.api.entities.TextChannel;

public class HelpCommand implements ICommand{
	
	private final CommandManager manager;
	
	
	public HelpCommand(CommandManager manager) {
		super();
		this.manager = manager;
	}

	@Override
	public void handle(CommandContext ctx) {
		

		
		List<String> args = ctx.getArgs();
		TextChannel channel = ctx.getChannel();
		
		if(args.isEmpty()) {
			StringBuilder builder = new StringBuilder();
			builder.append("List of commands: \n");
			
			manager.getUserCommands().stream().map(ICommand::getName).forEach(
					(it) -> builder.append('`').append(Kyoka.utils.DataLoader.getString("prefix")).append(it).append("`\n")
			);
			
			channel.sendMessage(builder.toString()).queue();
						
			return;
		}
		
		String search = args.get(0);
		ICommand command = manager.getCommand(search);
		
		if(command==null) {
			channel.sendMessage("No matches for "+search).queue();
			return;
		}
		if(!Kyoka.utils.DataLoader.admin.contains(ctx.getAuthor().getIdLong()) && command.isSecret()) {
			ctx.getChannel().sendMessage("You don't have enough permissions to do this").queue();
			return;
		}	
		
		channel.sendMessage(command.getHelp()).queue();
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "help";
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return "Shows the list with commands in the bot\n" + "Usage: `" + Kyoka.utils.DataLoader.getString("prefix") + "help [command]`";
	}
	
	@Override
	public List<String> getAliases(){
		return Arrays.asList("commands","cmds","commandlist");
	}

}
