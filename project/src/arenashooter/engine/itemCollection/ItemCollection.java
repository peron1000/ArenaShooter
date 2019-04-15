package arenashooter.engine.itemCollection;

import java.util.ArrayList;

public class ItemCollection<T extends ItemConcept> {
	
	private ArrayList<T> list = new ArrayList<>();
	private double probaTotal = 0;
	
	public ItemCollection() {
		// TODO Auto-generated constructor stub
	}
	
	public void add(T it) {
		probaTotal += it.getProba();
		list.add(it);
	}
	
	public ItemConcept get() throws Exception {
		double random = Math.random() * probaTotal;
		double counter = 0;
		ItemConcept winner = null;
		for (ItemConcept ItemConcept : list) {
			counter += ItemConcept.getProba();
			if(winner == null && counter >= random) {
				winner = ItemConcept;
			}
		}
		if(winner != null) {
			return winner;
		} else {
			throw new Exception("No items in this map");
		}
	}
	
	public static void main(String[] args) {
//		ItemCollection<ItemConcept> ic = new ItemCollection<ItemConcept>();
//		ItemConcept it1 = new ItemConcept("type1",new Vec2f() , 0.5);
//		ItemConcept it2 = new ItemConcept("type2", new Vec2f() , 10.5);
//		
//		ic.add(it1);
//		ic.add(it2);
//		
//		for (int i = 0; i < 100; i++) {
//			System.out.println(ic.get().getType());
//		}
	}
}
