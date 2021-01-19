package Kyoka.items;

import org.json.JSONObject;

import net.dv8tion.jda.api.entities.TextChannel;

public interface UsableItem {

	
	public void use(TextChannel channel, long id, int ref);
	
	public String getInfo(int itemID);
	
	public String getType();
	
}
