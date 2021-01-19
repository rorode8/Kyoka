package Kyoka;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;

import Kyoka.items.ItemManager;
import Kyoka.utils.MongoDB;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class Bot {
	
	private Bot() {
		//prep
		WebUtils.setUserAgent("");
		ItemManager.init("usableItems.json");
		Kyoka.utils.DataLoader.Start("settings.JSON");
		MongoDB.connect();		
		EmbedUtils.setEmbedBuilder(
				() -> new EmbedBuilder()
				.setColor(0xe0102c)
				.setFooter("Kyoka")
				
				);
		
		EventWaiter waiter = new EventWaiter();
		
		
		try {
		JDA jda  = JDABuilder.createDefault(Kyoka.utils.DataLoader.getString("token"))
				.addEventListeners(new Listener(waiter), waiter)
				.setActivity(Activity.listening("x"))
				.build();
		
		
		}catch(Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		new Bot();
	}

}
