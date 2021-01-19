package Kyoka.items;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bson.Document;

public class ItemPuller {
	
	private List<dropItem> items;
	private int totalWeight;
	private String filePath;
	private Random r;
	private String itemName;
	private static DecimalFormat df2 = new DecimalFormat("#.##");
	
	public ItemPuller(String filePath, String itemName) {
		this.filePath = filePath;
		totalWeight=0;
		
		 r = new Random();
		 this.itemName=itemName;
	}
	
	@SuppressWarnings("resource")
	public ItemPuller init(){
		try {
			BufferedReader csvReader = new BufferedReader(new FileReader(filePath));
			String row=csvReader.readLine();
			int i=0;
			items = new ArrayList<dropItem>();
			while ((row = csvReader.readLine()) != null) {
				System.out.println(row);
			    items.add(new SimpleItem(row));
			    totalWeight+=items.get(i).getWeight();
			    i++;
			    
			    
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

		return this;
	}
	
	public Document pull() {
		int luck = r.nextInt(totalWeight);
		int i=0;
		System.out.println("rand: "+luck+ " to tw of "+totalWeight);
		while(luck>0) {
			System.out.println("luck"+luck);
			if(luck>=items.get(i).getWeight()) {
				luck=luck-items.get(i).getWeight();
				i++;
			}else {
				luck=0;
			}
			
			System.out.println("name: "+items.get(i).getName());
			
		}
		
		return items.get(i).getDoc();
	}
	
	public String toString() {
		
		String test="```\n";
		test+=String.format("%-25s %-10s %n","name","drop rate");
		test+="-------------------------------------------------------\n";
		for(int i=0; i<items.size();i++) {
			test+=String.format("%-25s %-10s %n", items.get(i).getName(), String.valueOf(df2.format((float)100*items.get(i).getWeight()/totalWeight)+"%"));
			
		}
		test+="\n```";
		System.out.println(test);
		return test;
	}
	
	
	
	
	

}
