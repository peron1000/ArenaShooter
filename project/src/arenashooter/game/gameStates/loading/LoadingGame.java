package arenashooter.game.gameStates.loading;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import arenashooter.engine.json.JsonTransformer;
import arenashooter.engine.xmlReaders.reader.MapXmlReader;
import arenashooter.entities.Arena;
import arenashooter.game.Main;

public class LoadingGame extends Thread {

	private String[] arenaPath;
	private int nbArenaTotal;
	private List<Arena> loadedArena;
	private boolean infini;

	public LoadingGame(int nbArena, String... strings) {
		arenaPath = strings;
		if (nbArena == -1) {
			infini = true;
			nbArenaTotal = strings.length;
		} else {
			infini = false;
			nbArenaTotal = nbArena;
		}
		loadedArena = new LinkedList<>();
	}

	public synchronized int totalArena() {
		return arenaPath.length;
	}

	public synchronized int arenaLoaded() {
		return loadedArena.size();
	}

	public synchronized boolean hasNum(int num) {
		if (infini)
			return loadedArena.size() >= nbArenaTotal;
		return loadedArena.size() >= num;
	}

	public synchronized Arena getArena(int num) {
		if(infini && loadedArena.size() >= nbArenaTotal) {
			return loadedArena.get(num%nbArenaTotal);
		}
		if (num < loadedArena.size()) {
			return loadedArena.get(num);
		} else {
			Main.log.error("The arena number " + num + " is not ready yet");
			return null;
		}

	}

	@Override
	public void run() {

		List<String> toShuffel = new LinkedList<>();
		for (String string : arenaPath) {
			toShuffel.add(string);
		}

		Collections.shuffle(toShuffel);

		Main.log.info("Begin to load " + nbArenaTotal + " Arena(s)");
		for (int i = 0; i < nbArenaTotal; i++) {
			String path = toShuffel.get(i % toShuffel.size());
			//			MapXmlReader reader = new MapXmlReader(toShuffel.get(i % toShuffel.size()));
//
//			Arena arena = new Arena();
//			reader.load(arena);
//
//			while (!reader.loadNextEntity()) {
//			}
			try {
				Arena arena = JsonTransformer.importArena(path);
				if(arena.getChildren().values().size() < 3) {
					System.err.println(arena+" not well loaded");
				}

				loadedArena.add(arena);
				Main.log.info("Arena loaded: " + toShuffel.get(i % toShuffel.size()));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		Main.log.info("All Arena loaded");

	}
}
