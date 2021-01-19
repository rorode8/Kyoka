package Kyoka.items;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;

public class Egg {
	
	private int hatch;
	int totalWeight;
	String name;
	JSONArray drops;
	
	Random r = new Random();
	List<String> names;
	List<Integer> odds;
	private static DecimalFormat df2 = new DecimalFormat("#.##");
	int itemID;
	
	public Egg(JSONObject jo, String id) {
		id=String.valueOf(id);
		hatch = jo.getInt("hatch");
		name = jo.getString("name");
		
		this.drops = jo.getJSONArray("drops");
		totalWeight=0;
		names= new ArrayList<String>();
		odds= new ArrayList<Integer>();
		for(Object ob : drops) {
			JSONObject json = (JSONObject) ob;
			totalWeight+=json.getInt("weight");
			names.add(json.getString("name"));
			odds.add(json.getInt("weight"));
		}
	}
	
	public Document getDoc(int itemID) {
		Document header = new Document("id", itemID);
		header.append("name", name);		
		header.append("hatchTime",hatch);
		header.append("firstDate", new Date());
		header.append("petID", pullID());
				
		return header;
	}
	
	private int pullID() {
		int id=0;
		int luck= r.nextInt(totalWeight)+1;
		int i=0;
		JSONObject ob;
		while(luck>0) {
			System.out.println(luck);
			ob = (JSONObject)drops.get(i);
			id = ob.getInt("petID");
			luck=luck - ob.getInt("weight");
			i++;
		}				
		return id;
	}
	
	public String getDrops() {
		String info = "```\n";
		info+=String.format("%-20s %-10s %n", "name","drop rate");
		info+=("-----------------------------------\n");
		for(int i=0;i<odds.size();i++) {
			info+=String.format("%-20s %-10s %n", names.get(i),String.valueOf(df2.format((float)100*odds.get(i)/totalWeight)+"%"));
		}
		info+="```";
		return info;
	}
	
	//pull
	//update

}
