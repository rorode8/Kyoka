package Kyoka.commands.command;

import java.util.List;

import Kyoka.commands.CommandContext;
import Kyoka.commands.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class KickCommand implements ICommand{

	@Override
	public void handle(CommandContext ctx) {
		TextChannel channel = ctx.getChannel();
		Message message = ctx.getMessage();
		Member member = ctx.getMember();
		final List<String> args = ctx.getArgs();
		
		if(args.size()<2 || message.getMentionedMembers().isEmpty()) {
			channel.sendMessage("Missing arguments").queue();
			return;
		}
		
		final Member target = message.getMentionedMembers().get(0);
		
		if(!member.canInteract(target) || !member.hasPermission(Permission.KICK_MEMBERS)) {
			channel.sendMessage("You are missing permission to kick this member").queue();
			return;
		}
		
		final Member selfMember  = ctx.getGuild().getSelfMember();
		
		if(!selfMember.canInteract(target) || !selfMember.hasPermission(Permission.KICK_MEMBERS)) {
			channel.sendMessage("I am missing permissions to kick that member").queue();
			return;
		}
		
		final String reason = String.join(" ", args.subList(1, args.size()));
		
		ctx.getGuild().kick(target, reason).reason(reason).queue(
				(__) -> channel.sendMessage("the user has been kicked out of the server").queue(),
				(error) -> channel.sendMessage("Could not kick: "+error.getMessage()).queue()
				);
				
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "kick";
	}

	@Override
	public String getHelp() {
		// TODO Auto-generated method stub
		return "Kick a member off the server.\n" +
				"Usage: `"+Kyoka.utils.DataLoader.getString("prefix")+" <@user> <reason>`";
	}
	
	@Override
	public boolean isSecret() {
		return true;
	}

}
