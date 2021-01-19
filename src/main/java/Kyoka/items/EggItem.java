package Kyoka.items;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.bson.Document;
import org.json.JSONObject;

import Kyoka.utils.MongoDB;
import net.dv8tion.jda.api.entities.TextChannel;

public class EggItem implements UsableItem{
	
	
	private JSONObject subtypes;
	private Map<String, Egg> eggs;
	
	public EggItem(JSONObject ob) {
		eggs = new HashMap<String,Egg>();
		subtypes = ob.getJSONObject("egg");
		System.out.println(subtypes.toString());
		System.out.println(subtypes.keys());
		Iterator<String> keys = subtypes.keys();
		while(keys.hasNext()) {
		    String key = keys.next();
		    eggs.put(key,new Egg(subtypes.getJSONObject(key),key));
		}
	}
	/*
	 * ref is already normalized
	 */
	@Override
	public void use(TextChannel channel, long id, int i) {
			if(MongoDB.countItems(id, "pets")>=3) {
				channel.sendMessage("you must get rid off a pet before getting a new one").queue();
				return;
			}
			
			Document user = MongoDB.getDoc(id);
			int itemID = user.getList("bag", Document.class).get(i).getInteger("id");
			Document egg = eggs.get(String.valueOf(itemID)).getDoc(itemID);
			MongoDB.pushIntoArray("pets", id, egg);
			MongoDB.removeFromArray(i, id, "bag");
			
			channel.sendMessage("your egg will hatch in **"+egg.getInteger("hatchTime")+" hours **").queue();
			
			
	}
	
	@Override
	public String getInfo(int itemID) {
		return eggs.get(String.valueOf(itemID)).getDrops();
	}
		
	@Override
	public String getType() {		
		return "egg";
	}

		
	
	
}
