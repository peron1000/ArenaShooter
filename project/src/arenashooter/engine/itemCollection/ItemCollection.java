package arenashooter.engine.itemCollection;

import java.util.ArrayList;

import arenashooter.engine.math.Vec2f;
import arenashooter.entities.Collider;

public class ItemCollection {
	
	private ArrayList<ItemType> list = new ArrayList<>();
	private double probaTotal = 0;
	
	public ItemCollection() {
		// TODO Auto-generated constructor stub
	}
	
	public void add(ItemType it) {
		probaTotal += it.getProba();
		list.add(it);
	}
	
	public ItemType get() {
		double random = Math.random() * probaTotal;
		double counter = 0;
		ItemType winner = null;
		for (ItemType itemType : list) {
			counter += itemType.getProba();
			if(winner == null && counter >= random) {
				winner = itemType;
			}
		}
		if(winner != null) {
			return winner;
		} else {
			return list.get(list.size() - 1);
		}
	}
	
	public static void main(String[] args) {
		ItemCollection ic = new ItemCollection();
		ItemType it1 = new ItemType("path", "type1", new Vec2f(), 0.5);
		ItemType it2 = new ItemType("path", "type2", new Vec2f(), 10.5);
		
		ic.add(it1);
		ic.add(it2);
		
		for (int i = 0; i < 100; i++) {
			System.out.println(ic.get().getType());
		}
	}
}
