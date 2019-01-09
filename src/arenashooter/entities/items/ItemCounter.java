package arenashooter.entities.items;

public class ItemCounter {
	private static int number = 0;
	public static String weapon() {
		number++;
		return "Item_Arme"+number;
	}
	public static String armor() {
		number++;
		return "Item_Armure"+number;
	}

}
