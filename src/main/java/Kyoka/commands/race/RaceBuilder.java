package Kyoka.commands.race;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;

import Kyoka.utils.MongoDB;
import net.dv8tion.jda.api.entities.TextChannel;

public class RaceBuilder {
	
	List<Player> players;
	List<Horse> horses;
	List<Character> types;
	List<Character> bag;
	boolean flag;
	double totalAmount;
	Random r = new Random();
	private int y;
	TextChannel channel;
	private Timer timer;
	
	public RaceBuilder(TextChannel channel) {
		players = new ArrayList<Player>();
		horses = new ArrayList<Horse>();		
		types = Arrays.asList('n','p','b','g');
		bag = new ArrayList<Character>();
		totalAmount=0;
		y=160;
		flag = false;
		this.channel=channel;
		timer  = new Timer();
	}
	
	public void addBet(double amount, String name, long id, char choose) {
		players.add(new Player(id, name, amount, choose));
		if(bag.isEmpty() || !bag.contains(choose)) {
			bag.add(choose);
			horses.add(new Horse(choose,30,y));
			
			y+=15;
		}
		horses.get(bag.indexOf(choose)).placeBet(amount);
		
		totalAmount+=amount;
	}
	
	
	
	
	public void play() {
		
		System.out.println("played !!!");
		
		BufferedImage bgImage = Kyoka.utils.ImageManager.readImage("background.png");
		List<BufferedImage> images = new ArrayList<BufferedImage>();
		List<Integer> x = new ArrayList<Integer>();
		List<Integer> y = new ArrayList<Integer>();
		BufferedImage overlayedImage;
		
		if(flag) {
			//winner 
			char win = horses.get(0).getType();
			int max = horses.get(0).x;
			for(Horse horse : horses) {
		        images.add(Kyoka.utils.ImageManager.readImage("kirby/images/kirb_"+horse.getSprite()+String.valueOf(1 + r.nextInt(9))+".png"));
		        x.add(horse.x);
		        y.add(horse.y);
		        
				if(horse.x>max) {
					max = horse.x;
					win = horse.getType();
				}
			}
			overlayedImage = Kyoka.utils.ImageManager.overlayImages(bgImage, images, x,y);
			if (overlayedImage != null){
	        	//Kyoka.utils.ImageManager.writeImage(overlayedImage, "overLayedImage.png", "PNG");
				channel.sendMessage("**"+horses.get(bag.indexOf(win)).getName()+"** is the winner!!!").queue();
	        	
	        	try {
	        		ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ImageIO.write(overlayedImage, "png", baos);
					baos.flush();
					byte[] bytes = baos.toByteArray();
		        	channel.sendFile(bytes, "image.png").queue();
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	
	            System.out.println("Overlay Completed...");
	        }else
	            System.out.println("Problem With Overlay...");
			
			System.out.println("gonna pay");
			payOut(win);
			return;
		}

		
		for(Horse horse : horses) {
			
			String url = "kirby/images/kirb_"+horse.getSprite()+String.valueOf(1+ r.nextInt(9))+".png";
			System.out.println(url);
	        images.add(Kyoka.utils.ImageManager.readImage(url));
	        x.add(horse.x);
	        y.add(horse.y);
	        horse.Stride();
	        
	        if(horse.x>=400) {
	        	flag=true;
	        }

		}
		
		overlayedImage = Kyoka.utils.ImageManager.overlayImages(bgImage, images, x,y);
		
        if (overlayedImage != null){
        	//Kyoka.utils.ImageManager.writeImage(overlayedImage, "overLayedImage.png", "PNG");
        	
        	try {
        		ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write(overlayedImage, "png", baos);
				baos.flush();
				byte[] bytes = baos.toByteArray();
	        	channel.sendFile(bytes, "image.png").queue();
	        	
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
            System.out.println("Overlay Completed...");
        }else
            System.out.println("Problem With Overlay...");
		
        TimerTask task = new TimerTask() {
			@Override
			public void run() {
				play();
			}
		};
		timer.schedule(task, 3000l);
		
		
	}
	
	private void payOut(char winner) {
		System.out.println("size: "+players.size());
		for(Player p: players) {
			System.out.println("name "+p.getName());
			if(p.choose == (winner)) {
				System.out.println("name "+p.getName());
				double amount = p.getPayout(horses.get(bag.indexOf(winner)).getAmount(), this.totalAmount);
				System.out.println("payed");
				MongoDB.updateDB(p.getId(), amount);
				channel.sendMessage(p.getName()+" just earned `"+amount+"`").queue();
			}
		}
	}
	
	public int getSizeHorses() {
		return horses.size();
	}
	
	
	
	

}
