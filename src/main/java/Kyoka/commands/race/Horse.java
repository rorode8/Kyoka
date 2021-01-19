package Kyoka.commands.race;

import java.util.Random;

public class Horse {

	private double amount;
	private int horseLuck;
	private String name;
	public int x,y;
	private Random r = new Random();
	private String sprite;
	private char horseType;
	
	public Horse(char horse, int x0, int y0) {
		horseType = horse;
		switch(horseType) {
		case 'n':										//normal
			name="Kirby";
			horseLuck=r.nextInt(9)+4;
			System.out.println("luck normal: "+horseLuck);
			sprite="1";
			break;
		case 'b':										//blue
			name="blue Kirby";
			horseLuck=8;
			sprite="3";
			break;
		case 'p':										//purple
			name="purple Kirby";
			horseLuck=r.nextInt(17);
			System.out.println("luck purple: "+horseLuck);
			sprite="0";
			break;
		case 'g':										//green
			name="green Kirby";
			System.out.println("luck green: "+horseLuck);
			horseLuck=r.nextInt(9)+4;
			sprite="2";
			break;
		default:
			name="Kirby";
			horseLuck=3;
			System.out.println("error horse creating");
			
		}
		amount=0;
		x=x0;
		y=y0;
		
	}
	
	public void placeBet(double bet) {
		amount+=bet;
	}
	
	public void Stride() {
		x=x+ r.nextInt(30 - horseLuck) + horseLuck;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public String getName() {
		return name;
	}
	
	public double getAmount() {
		return amount;
	}

	public String getSprite() {
		return sprite;
	}
	
	public char getType() {
		return horseType;
	}
	
	
}
