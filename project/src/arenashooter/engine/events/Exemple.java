package arenashooter.engine.events;

public class Exemple {
	public static void main(String[] args) {
		Observable<Integer> obs = new Observable<Integer>(5);
		EventListener<BasicEvent> lis = new EventListener<BasicEvent>() {
			
			@Override
			public void action(BasicEvent e) {
				System.out.println(e.name);
			}
		};
		obs.listener.add(lis);
		
		obs.setValue(2);
		obs.setValue(8);
	}
}
