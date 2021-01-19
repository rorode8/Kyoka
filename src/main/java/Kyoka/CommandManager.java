package Kyoka;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

import Kyoka.commands.CommandContext;
import Kyoka.commands.ICommand;
import Kyoka.commands.command.AddAdminCommand;
import Kyoka.commands.command.CoinFlipCommand;
import Kyoka.commands.command.FishCommand;
import Kyoka.commands.command.HelpCommand;
import Kyoka.commands.command.ImageCommand;
import Kyoka.commands.command.InventoryCommand;
import Kyoka.commands.command.KickCommand;
import Kyoka.commands.command.MineCommand;
import Kyoka.commands.command.PingCommand;
import Kyoka.commands.command.RaceCommand;
import Kyoka.commands.command.RegisterCommand;
import Kyoka.commands.command.STFU4plyCommand;
import Kyoka.commands.command.SellCommand;
import Kyoka.commands.command.ShopCommand;
import Kyoka.commands.command.StorageCommand;
import Kyoka.commands.command.StoreCommand;
import Kyoka.commands.command.TransferCommand;
import Kyoka.commands.command.UploadCommand;
import Kyoka.commands.command.balanceCommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class CommandManager {
	private final List<ICommand> commands = new ArrayList<>();
	private static int admin =0;
	
	public CommandManager(EventWaiter waiter) {
		addCommand(new PingCommand());
		addCommand(new HelpCommand(this));
		
		addCommand(new ImageCommand());
		addCommand(new CoinFlipCommand());
		addCommand(new STFU4plyCommand());
		addCommand(new RegisterCommand());
		addCommand(new FishCommand());
		addCommand(new SellCommand());
		addCommand(new balanceCommand());
		addCommand(new RaceCommand());
		addCommand(new InventoryCommand(waiter));
		addCommand(new MineCommand());
		addCommand(new TransferCommand());
		addCommand(new StoreCommand());
		addCommand(new StorageCommand());
		addCommand(new ShopCommand());
		
		//ensure secret commands are below
		addCommand(new KickCommand());
		addCommand(new UploadCommand());
		addCommand(new AddAdminCommand());
	}
	
	private void addCommand(ICommand cmd) {
		boolean nameFound = this.commands.parallelStream().anyMatch((it) -> it.getName().equalsIgnoreCase(cmd.getName()));
		
		if(nameFound) {
			throw new IllegalArgumentException("A command with this name is already present");
		}
		if(cmd.isSecret()) {
			admin++;
		}
		commands.add(cmd);
	}
	
	
	public List<ICommand> getCommands(){
		return commands;
	}
	
	public List<ICommand> getUserCommands(){
		System.out.println(admin);
		return commands.subList(0, commands.size()-admin);
	}
	
	@Nullable
	public ICommand getCommand(String search) {
		String searchLower = search.toLowerCase();
		
		for(ICommand cmd : this.commands) {
			if(cmd.getName().equals(searchLower) || cmd.getAliases().contains(searchLower)) {
				return cmd;
			}
		}
		return null;
	}
	
	void handle(GuildMessageReceivedEvent event) {
		String[] split = event.getMessage().getContentRaw()
				.replaceFirst("(?i)" + Pattern.quote(Kyoka.utils.DataLoader.getString("prefix")), "")
				.split("\\s+");
		
		String invoke = split[0].toLowerCase();
		ICommand cmd = this.getCommand(invoke);
		
		if(cmd != null) {
			event.getChannel().sendTyping().queue();
			List<String> args = Arrays.asList(split).subList(1,split.length);
			
			CommandContext ctx = new CommandContext(event, args);
			
			cmd.handle(ctx);
			
			
			
		}
	}

}
