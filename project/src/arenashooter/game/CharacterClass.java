package arenashooter.game;

public class CharacterClass {
	public static final CharacterClass 
		bird = new CharacterClass( "Bird", new String[] {"moineau_01", "canard_01", "canard_02"} ),
		agile = new CharacterClass( "Agile", new String[] {"chat_01", "renard_01", "renard_02", "renard_03", "renard_04"} ),
		heavy = new CharacterClass( "Heavy", new String[] {"chevre_01", "vache_01"} );
	
	
	public final String name;
	public final String[] skins;
	
	private CharacterClass(String name, String[] skins) {
		this.name = name;
		this.skins = skins;
	}
}
