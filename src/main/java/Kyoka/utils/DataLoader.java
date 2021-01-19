package Kyoka.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject; 
  

  

public class DataLoader {
	
	Object obj;
	private static JSONArray ja;
	public static JSONObject jo;
	public static ArrayList<Long> admin = new ArrayList<Long>();
	final static File dir = new File("tmp\\");
	public static ArrayList<File> files = new ArrayList<File>();
	public static boolean commandOn=false;
	
	public static void turnOn() {
		commandOn = true;
	}
	
	public static void turnOff() {
		commandOn = false;
	}
	
	public static void Start(String path) {
		 try {
			 File[] temp = dir.listFiles();
			 for(int i=0;i<temp.length;i++) {
				 files.add(temp[i]);
			 }
			 jo = new JSONObject(new String(Files.readAllBytes(Paths.get(path))));
			 
			 ja = (JSONArray) jo.get("admin");
			 Iterator<Object> iter = ja.iterator();
			 
			 while(iter.hasNext()) {
				 long x  =(long) iter.next();
				 System.out.println(x);
				 admin.add(x);
			 }
			 
			 
		 }catch(Exception e) {
			 e.printStackTrace();
		 }
	}
	
	public static String getString(String key) {
		String ans="";
		try {
			ans = jo.getString(key);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return ans;
	}
	
	public static void recordAdmin(long id) {
		admin.add(id);
		JSONArray jarr = new JSONArray();
		for(long x:admin) {
			jarr.put(x);
		}
		jo.put("admin", jarr);
		PrintWriter pw;
		try {
			pw = new PrintWriter("settings.json");
			pw.write(jo.toString()); 
	          
	        pw.flush(); 
	        pw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        
		
	}
	
	

}
