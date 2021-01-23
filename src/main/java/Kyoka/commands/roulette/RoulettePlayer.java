package Kyoka.commands.roulette;

import java.util.List;

import Kyoka.utils.MongoDB;

public class RoulettePlayer {
	
	private long id;
	private String name;
	private double wage;
	private List<Integer> choose;
	private int multiplier;
	
	public RoulettePlayer(long id, String name, double wage, List<Integer> bet, int mult) {
		super();
		this.id = id;
		this.name = name;
		this.wage = wage;
		this.choose = bet;
		this.multiplier = mult;
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
	
	public List<Integer> getNumber() {
		return choose;
	}
	
	public String pay() {
		double amount = wage * multiplier;
		MongoDB.updateDB(id, amount);
		return name+ " just earned `"+amount+"`";
	}
	


}
