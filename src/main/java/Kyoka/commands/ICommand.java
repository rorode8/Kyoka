package Kyoka.commands;

import java.util.Arrays;
import java.util.List;

public interface ICommand {

	public void handle(CommandContext ctx);
	
	public String getName();
	
	public String getHelp();
	
	public default boolean isSecret() {
		return false;
	}
	
	
	default List<String> getAliases(){
		return Arrays.asList(getName());
	}
}
