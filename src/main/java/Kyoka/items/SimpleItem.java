package Kyoka.items;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bson.Document;

public class SimpleItem implements dropItem {
	
	private int luckWeight;
	private int minWeight;
	private int maxWeight;
	private String name;
	private int id;
	private int kgprice;
	private Random r = new Random();
	

	public SimpleItem(String line) {
		super();
		List<String> items = new ArrayList<String>(Arrays.asList(line.split(",")));
		
		this.luckWeight = Integer.parseInt(items.get(5));
		this.maxWeight = Integer.parseInt(items.get(4));
		this.minWeight = Integer.parseInt(items.get(3));
		this.kgprice = Integer.parseInt(items.get(2));
		this.name = items.get(1);
		this.id = Integer.parseInt(items.get(0));
		
		
	}

	@Override
	public int getWeight() {
		return luckWeight;
				
	}

	@Override
	public Document getDoc() {
		int size = r.nextInt(maxWeight-minWeight +1 ) + minWeight;
		Document doc = new Document("id",id)
				.append("name", name)
				.append("weight",size)
				.append("sell value", size * kgprice);
				
				
				
		return doc;
	}

	@Override
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}



}
