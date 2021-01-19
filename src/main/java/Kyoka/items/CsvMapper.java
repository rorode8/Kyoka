package Kyoka.items;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CsvMapper {
	
	private List<Map<String, String>> records;
	private List<String> header;
	
	public CsvMapper(String file) {
		records=new ArrayList<Map<String,String>>();
		try {
			BufferedReader csvReader = new BufferedReader(new FileReader(file));			
			header = new ArrayList<String>(Arrays.asList(csvReader.readLine().split(",")));
			System.out.println(header.toString());
			List<String> list;
			String row;
			while ((row = csvReader.readLine()) != null) {
				list=Arrays.asList(row.split(","));
				Map<String, String> rowHash = new HashMap<String, String>();
				for(int i=0;i<header.size();i++) {
					rowHash.put(header.get(i), list.get(i));
				}
				records.add(rowHash);
			    			    
			}
			csvReader.close();
			
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
	
	public List<String> getHeader(){
		return header;
	}
	
	public List<Map<String, String>> getData(){
		return records;
	}
	
	
	

}
