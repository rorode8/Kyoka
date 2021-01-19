package Kyoka;

import java.util.List;

import org.slf4j.LoggerFactory;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

import Kyoka.utils.MongoDB;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Listener extends ListenerAdapter {
	
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Listener.class);
	private final CommandManager manager;
	
	public Listener(EventWaiter waiter) {
		manager = new CommandManager(waiter);
	}
	
	@Override
	public void onReady(ReadyEvent event) {
		LOGGER.info(event.getJDA().getSelfUser().getAsTag()+ " is ready!");
		
	}
	
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		User user = event.getAuthor();
		
		if(user.isBot() || event.isWebhookMessage()) {
			return;
		}
		
		String prefix = Kyoka.utils.DataLoader.getString("prefix");
		String raw = event.getMessage().getContentRaw();
		
		if(raw.equalsIgnoreCase(prefix +"shutdown") && event.getAuthor().getId().equals(Kyoka.utils.DataLoader.getString("ownerID"))) {
			LOGGER.info("shutting down");
			event.getChannel().sendMessage("bye").queue();
			event.getJDA().shutdown();
			MongoDB.close();
		}
		
		if(event.getAuthor().getId().equals("155920774778060800") && event.getMessage().getContentRaw().contains("[][][][][][]")) {
			Member member = event.getGuild().getMemberById(event.getAuthor().getIdLong());
			VoiceChannel myChannel = member.getVoiceState().getChannel();
			
			//AudioManager audioManager = event.getGuild().getAudioManager();
			//audioManager.openAudioConnection(myChannel);
			List<Member> lista = myChannel.getMembers();
			for(int i=0; i<lista.size(); i++) {
				lista.get(i).mute(true).queue();
				//lista.get(i).deafen(true).queue();
				System.out.println(1+" "+lista.get(i).getNickname());
			}
			

			
			
		}
		
		if(event.getAuthor().getId().equals("155920774778060800") && event.getMessage().getContentRaw().contains("[][][][][]")) {
			Member member = event.getGuild().getMemberById(event.getAuthor().getIdLong());
			VoiceChannel myChannel = member.getVoiceState().getChannel();
			
			//AudioManager audioManager = event.getGuild().getAudioManager();
			//audioManager.openAudioConnection(myChannel);
			List<Member> lista = myChannel.getMembers();
			for(int i=0; i<lista.size(); i++) {
				lista.get(i).mute(false).queue();
				//lista.get(i).deafen(false).queue();
			}
			

			
			
		}
		
		
		if(raw.startsWith(Kyoka.utils.DataLoader.getString("prefix"))) {
			//event.getGuild().getTextChannelById(id)
			//event.getGuild().createRole().setName("lobo").setPermissions(Permission.ADMINISTRATOR);
			//event.getGuild().getTextChannelById(231331232131).crea
			//event.getGuild().createCategory("aaaa").complete();
			//event.getChannel().createPermissionOverride(event.getMember()).complete();
			manager.handle(event);
		}
		
		if(event.getAuthor().getId().equals("176123584391938049") && Kyoka.utils.DataLoader.commandOn==true) {
			event.getMessage().delete().queue();
		}
		
		
		
	}

}
