package arenashooter.engine.events;

public class Exemple {
	public static void main(String[] args) {
		Observable<Integer> obs = new Observable<Integer>(5);
		EventListener<NewValueEvent<Integer>> lis = new EventListener<NewValueEvent<Integer>>() {
			
			@Override
			public void action(NewValueEvent<Integer> event) {
				System.out.println("Old value : "+event.getOldValue()+" -> new value : "+event.getNewValue());
			}
		};
		obs.listener.add(lis);
		
		obs.setValue(2);
		obs.setValue(8);
	}
}
