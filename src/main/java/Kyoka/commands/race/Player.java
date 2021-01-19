package Kyoka.commands.race;

public class Player {
	
	private long id;
	private String name;
	private double wage;
	public char choose;
	
	public Player(long id, String name, double wage, char bet) {
		super();
		this.id = id;
		this.name = name;
		this.wage = wage;
		this.choose = bet;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public double getWage() {
		return wage;
	}
	
	public double getPayout(double horse, double total) {
		return (double)Math.round((total/horse)*(wage));
		
	}
	


}