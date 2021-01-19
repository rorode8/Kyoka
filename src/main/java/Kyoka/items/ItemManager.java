package Kyoka.items;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.stream.Stream;

import org.bson.Document;
import org.json.JSONObject;


import Kyoka.utils.MongoDB;
import net.dv8tion.jda.api.entities.TextChannel;

public class ItemManager {

	private static JSONObject jo;
	private static HashMap<String,UsableItem> items;
	
	public static void init(String path) {
		
		items = new HashMap<String,UsableItem>();
		
		 try {
			jo = new JSONObject(readLineByLineJava8(path));		
		} catch (Exception e) {
			e.printStackTrace();
		} 
		 
		addType(new EggItem(jo)); 
		 
		
	}
	
	private static void addType(UsableItem item) {
		items.put(item.getType(), item);
	}
	/*
	 * normalizes reference
	 */
	public static boolean use(TextChannel channel, long authorid, int ref) {
		int i = ref-1;
		if(i>=MongoDB.countItems(authorid, "bag") || i<0) {
			return false;
		}
		Document item = MongoDB.getDoc(authorid).getList("bag", Document.class).get(i);
		if(!item.containsKey("type") || !jo.has(item.getString("type")) || !jo.getJSONObject(item.getString("type")).has(String.valueOf(item.get("id")))) {
			return false;
		}
		items.get(item.getString("type")).use(channel, authorid, i);
		
		
		return true;
	}
	public static String getInfo(int itemID, String type) {
		return items.get(type).getInfo(itemID);
	}
	
    private static String readLineByLineJava8(String filePath) 
    {
        StringBuilder contentBuilder = new StringBuilder();
 
        try (Stream<String> stream = Files.lines( Paths.get(filePath), StandardCharsets.UTF_8)) 
        {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        }
        catch (IOException e) 
        {
            e.printStackTrace();
        }
 
        return contentBuilder.toString();
    }
	
}
