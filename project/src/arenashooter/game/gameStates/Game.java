package arenashooter.game.gameStates;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import arenashooter.engine.DamageInfo;
import arenashooter.engine.DamageType;
import arenashooter.engine.animation.Animation;
import arenashooter.engine.animation.AnimationData;
import arenashooter.engine.animation.animevents.AnimEvent;
import arenashooter.engine.animation.animevents.AnimEventCustom;
import arenashooter.engine.animation.animevents.AnimEventSound;
import arenashooter.engine.audio.Audio;
import arenashooter.engine.audio.AudioChannel;
import arenashooter.engine.audio.SoundSource;
import arenashooter.engine.events.input.InputListener;
import arenashooter.engine.graphics.Texture;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.math.Quat;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.ui.MenuPause;
import arenashooter.engine.ui.simpleElement.UiImage;
import arenashooter.entities.Arena;
import arenashooter.entities.Timer;
import arenashooter.entities.spatials.Character;
import arenashooter.game.Controller;
import arenashooter.game.Main;
import arenashooter.game.gameStates.engineParam.GameMode;
import arenashooter.game.gameStates.engineParam.GameParam;

public class Game extends GameState {
	private int nbPlayers = Main.getGameMaster().controllers.size();
	private List<Character> players = new ArrayList<>(nbPlayers);
	private List<Arena> mapsToShuffle = new ArrayList<Arena>();

	int currentRound = 1;
	private final GameMode gameMode = GameParam.getGameMode();
	private final int nbRounds = GameParam.getRound();
	private final boolean teams = GameParam.getTeam();
	private InputListener inputs = new InputListener();

	private static SoundSource bgm;

	// Les teams sont pour l'instant au nombre de 2. On pourra changer
	// l'implementation plus tard en faisant en sorte d'avoir autant de team que
	// voulues.
	Set<Controller> team1 = new HashSet<Controller>();
	Set<Controller> team2 = new HashSet<Controller>();
	Timer roundTimer;
	private boolean oneLeft;

	/**
	 * Time to count before deciding who wins, or if it's a draw if the last one
	 * dies before the timer expires
	 */
	public Timer chooseWinner = new Timer(3.2);

	/**
	 * Time before switching to next map. After the winner has been found
	 */
	private Timer endRound = new Timer(2);
	private UiImage counterImage = new UiImage(Texture.default_tex);
	private Animation startCounter = null;
	private AnimationData counterAnimData = AnimationData.loadAnim("data/animations/anim_StartCounter.xml");
	private boolean canPlay = true;
	
	private MenuPause menuPause = new MenuPause(this);

	/**
	 * @author Marin C Evaluates if one character or less is alive. (Trigger this
	 *         function only when a character dies)
	 */
	private void evalOneLeft() {
		int aliveChars = 0;
		Controller c = null;
		for (Controller controller : Main.getGameMaster().controllers) {
			if (!controller.hasDeadChar()) {
				aliveChars++;
				c = controller;
			}
		}
		if (aliveChars <= 1) {
			if (c != null)
				c.roundsWon++;
			oneLeft = true;
		}
	}

	public Game(int nbMap) {
		super(nbMap);
		counterImage.setPosition(0, -25);
	}

	@Override
	public void init() {
		
		if (bgm != null) {
			bgm.destroy();
			bgm = null;
		}
		bgm = Audio.createSource("data/music/Super_blep_serious_fight.ogg", AudioChannel.MUSIC, .1f, 1);
		bgm.setLooping(true);
		
		for (Arena map : maps) {
			mapsToShuffle.add(map);
		}
		Collections.shuffle(mapsToShuffle);
		current = mapsToShuffle.get(0);
		newRound();
	}

	public void characterDeath(Controller controller, Character character) {
		players.remove(character);
		evalOneLeft();
	}

	private void newRound() {
		bgm.setVolume(.1f);
		current = mapsToShuffle.get(currentRound % mapsToShuffle.size());
		startCounter = new Animation(counterAnimData);
		startCounter.play();
		Window.postProcess.fadeToBlack = 1;
		endRound.reset();
		chooseWinner.reset();
		canPlay = false;
		for (Controller controller : Main.getGameMaster().controllers) {
			// Ce n'est plus un spawn aleatoire
			Character character = controller.createNewCharacter(current.GetRandomRespawnch2());
			players.add(character);
			character.attachToParent(current, character.genName());
		}
		oneLeft = false;
		super.init();
	}

	@Override
	public void update(double d) {
		menuPause.update(d);
		if (menuPause.isPaused()) {
			return;
		} else if (canPlay) {
			if (oneLeft && !chooseWinner.inProcess) {
				chooseWinner.setProcessing(true);
			}
			chooseWinner.step(d);
			if (chooseWinner.isOver() && !endRound.inProcess) {
				endRound.setProcessing(true);
			}
			endRound.step(d);
			if (endRound.isOver()) {
				if (currentRound < nbRounds || nbRounds == -1) {
					currentRound++;
					for (Controller player : Main.getGameMaster().controllers) {
						if (player.getCharacter() != null) {
							player.getCharacter()
									.takeDamage(new DamageInfo(0, DamageType.MISC_ONE_SHOT, new Vec2f(), 0, null));
						}
					}
					newRound();
				} else {
					bgm.stop();
					Main.getGameMaster().requestNextState(new Score(), "data/mapXML/menu_empty.xml");
				}
			}

			if (getCamera() != null) {
				getCamera().center(players, current.cameraBasePos, d);
				Audio.setListener(getCamera().getWorldPos(), getCamera().getWorldRot());
			} else
				Audio.setListener(new Vec3f(), new Quat());

			// Update controllers
			for (Controller controller1 : Main.getGameMaster().controllers)
				controller1.step(d);
			super.update(d);
		}

		if (startCounter != null) {

			if (getCamera() != null) {
				getCamera().center(players, current.cameraBasePos, d);
				Audio.setListener(getCamera().getWorldPos(), getCamera().getWorldRot());
				getCamera().step(d);
			} else
				Audio.setListener(new Vec3f(), new Quat());

			if (!startCounter.isPlaying()) {
				counterImage.setScale(0, 0);
				startCounter = null;
			} else {
				Queue<AnimEvent> events = startCounter.getEvents();
				AnimEvent current = events.peek();
				while( (current = events.poll()) != null ) {
					if(current instanceof AnimEventCustom) {
						if(((AnimEventCustom)current).data.equals("CanPlayNow")) {
							canPlay = true;
							bgm.setVolume(.5f);
							if(!bgm.isPlaying()) bgm.play();
						}
					} else if(current instanceof AnimEventSound) {
						((AnimEventSound) current).play(null);
					}
				}
				
				Window.postProcess.fadeToBlack = (float) startCounter.getTrackD("fadeToBlack");
				
				Texture counterTexture = startCounter.getTrackTex("CounterSprite");
				double size = startCounter.getTrackD("SizeOfCounterSprite");
				counterImage.getMaterial().setParamTex("image", counterTexture);
				counterImage.setScale(counterTexture.getSize().x*size, counterTexture.getSize().y*size);
				counterImage.getMaterial().getParamTex("image").setFilter(false);
				startCounter.step(d);
			}

		}
		inputs.step(d);
	}

	@Override
	public void draw() {
		super.draw();
		Window.beginUi();
		counterImage.draw();
		menuPause.draw();
		Window.endUi();
	}
}