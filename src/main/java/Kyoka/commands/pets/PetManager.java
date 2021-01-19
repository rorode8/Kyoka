package Kyoka.commands.pets;

import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.bson.Document;

import Kyoka.items.CsvMapper;

public class PetManager {
	
	private static CsvMapper petData= new CsvMapper("pets.csv");
	private static Random r = new Random();
	
	private Document update(Document pet) {
		String type = pet.getString("type");
		if(type.equals("egg")) {
			Date one = pet.getDate("firstDate");			
			long diffInMillies = Math.abs(new Date().getTime() - one.getTime());
			long elapsed = TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS);
			long hatchTime = pet.getInteger("hatchTime");
			long hatchMinutes = hatchTime * 60;
			long remaining = hatchMinutes - elapsed;
			if(remaining<0) {
				//hatched
				return hatch(pet);
			}else {
				
			}
		}
		return pet;
		
	}
	
	private Document hatch(Document pet) {
		Map<String,String> petRow = petData.getData().get(pet.getInteger("petID")-1);
		int factor = Integer.valueOf(petRow.get("factor"));
		Document hatch = new Document("petID", Integer.valueOf(petRow.get("petID")))
				.append("name", petRow.get("name"))
				.append("nickname", petRow.get("name"))
				.append("speed", Integer.valueOf(petRow.get("speed")) + r.nextInt(factor))
				.append("power", Integer.valueOf(petRow.get("power")) + 2 * r.nextInt(factor))
				.append("HP", Integer.valueOf(petRow.get("HP")) + 3 * r.nextInt(factor))
				.append("hunger", 50)
				.append("lastMeal", new Date())
				.append("happiness", 40)
				.append("lastPlay", new Date());
				
		
		
		switch(petRow.get("type").charAt(0)) {
		case 'c':
			hatch.append("type", 'c');
			break;
		case 'p':
			hatch.append("type", 'p');
			if(r.nextInt(100)==69) {
				hatch.append("shiny", true);
			}else {
				hatch.append("shiny", false);
			}
			break;
		default:
			hatch.append("type", 'u');
		}
		
		
				
		return hatch;
	}
	
	

}
