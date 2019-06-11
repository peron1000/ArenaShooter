package arenashooter.game;

public enum CharacterClass {
	Bird ("moineau_01", "canard_01", "canard_02"), 
	Agile("chat_01", "renard_01", "renard_02", "renard_03", "renard_04", "ecureuil_01"),
	Heavy("chevre_01", "vache_01"),
	Aqua ("poisson_01", "manchot_01", "manchot_02");
	
	private String[] skins;
	private boolean mirrorLeftFoot = false;

	private CharacterClass (String... skins) {
		this.skins = skins;
	}
	
	public int nextSkin (int current) {
		return (current+1)%skins.length;
	}
	
	public int previousSkin (int current) {
		current--;
		if(current < 0 )
			current = skins.length-1;
		
		return current;
	}
	
	public String getSkin(int index) {
		return skins[index];
	}
	
	public CharacterClass nextClass() {
		int next = (this.ordinal()+1)%values().length;
		return values()[next];
	}
	
	public CharacterClass previousClass() {
		int next = (this.ordinal()-1)%values().length;
		if(next < 0)next = values().length-1;
		return values()[next];
	}
}
