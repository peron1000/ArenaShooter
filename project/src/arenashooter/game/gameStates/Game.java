package arenashooter.game.gameStates;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import arenashooter.engine.DamageInfo;
import arenashooter.engine.DamageType;
import arenashooter.engine.animation.Animation;
import arenashooter.engine.animation.AnimationData;
import arenashooter.engine.animation.animevents.AnimEvent;
import arenashooter.engine.animation.animevents.AnimEventCustom;
import arenashooter.engine.animation.animevents.AnimEventSound;
import arenashooter.engine.audio.AudioChannel;
import arenashooter.engine.audio.SoundSource;
import arenashooter.engine.events.input.InputListener;
import arenashooter.engine.graphics.Texture;
import arenashooter.engine.input.Device;
import arenashooter.engine.math.Quat;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.ui.MenuPause;
import arenashooter.engine.ui.simpleElement.UiImage;
import arenashooter.entities.Timer;
import arenashooter.entities.spatials.Character;
import arenashooter.entities.spatials.CharacterSprite;
import arenashooter.game.Controller;
import arenashooter.game.ControllerAi;
import arenashooter.game.ControllerPlayer;
import arenashooter.game.Main;

public class Game extends GameState {
	private int nbPlayers = Main.getGameMaster().controllers.size();
	private List<Character> players = new ArrayList<>(nbPlayers);

	// int currentRound = 1;
	// private final GameMode gameMode = GameParam.getGameMode();
	// private final int nbRounds = GameParam.getRound();
	private InputListener inputs = new InputListener();

	private static SoundSource bgm;
	private static String bgmPath;
	private static final String bgmPathDefault = "data/music/Super_blep_serious_fight.ogg";

//	// Les teams sont pour l'instant au nombre de 2. On pourra changer
//	// l'implementation plus tard en faisant en sorte d'avoir autant de team que
//	// voulues.
//	Set<Controller> team1 = new HashSet<Controller>();
//	Set<Controller> team2 = new HashSet<Controller>();
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
	private UiImage counterImage = new UiImage(Main.getRenderer().getDefaultTexture());
	private AnimationData counterAnimData = AnimationData.loadAnim("data/animations/anim_StartCounter.xml");
	private AnimationData endAnimData = AnimationData.loadAnim("data/animations/anim_EndCounter.xml");
	private Animation startCounter = null;
	private Animation endCounter = null;
	/**
	 * true when the animation counter is finish
	 */
	private boolean canPlay = false;
	
	private boolean animationCounterFinished = false;
	private MenuPause menuPause = new MenuPause(this);

	public Game(String arenaPath) {
		super(arenaPath);
		counterImage.setPosition(0, -25);
	}

	/**
	 * @author Marin C Evaluates if one character or less is alive. (Trigger this
	 *         function only when a character dies)
	 */
	public void evalOneLeft() {
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

	@Override
	public void init() {

		if (bgm != null) {
			bgm.destroy();
			bgm = null;
		}
		bgmPath = bgmPathDefault;
		bgm = Main.getAudioManager().createSource("data/music/Super_blep_serious_fight.ogg", AudioChannel.MUSIC, .1f, 1);
		bgm.setLooping(true);
		
		if (!current.musicPath.isEmpty()) { // Arena has custom music
			if (!bgmPath.equals(current.musicPath)) { // Only restart music if its path changed
				bgmPath = current.musicPath;
				bgm.destroy();
				bgm = Main.getAudioManager().createSource(bgmPath, AudioChannel.MUSIC, .1f, current.musicPitch);
				bgm.setLooping(true);
				bgm.play();
			}
		} else if (!bgmPath.equals(bgmPathDefault)) { // Switch to default music
			bgmPath = bgmPathDefault;
			bgm.destroy();
			bgm = Main.getAudioManager().createSource(bgmPath, AudioChannel.MUSIC, .1f, 1);
			bgm.setLooping(true);
			bgm.play();
		}
		bgm.setVolume(.1f);
		
		newRound();

	}

	@Override
	public void destroy() {
		super.destroy();

		if (bgm != null)
			bgm.destroy();
	}

	public void characterDeath(Controller controller, Character character) {
		players.remove(character);
		evalOneLeft();
	}

	private void newRound() {
		startCounter = new Animation(counterAnimData);
		startCounter.play();
		Main.getRenderer().getPostProcess().fadeToBlack = 1;
		endRound.reset();// TODO: remove
		endCounter = null;
		chooseWinner.reset();
		canPlay = false;
		for (Controller controller : Main.getGameMaster().controllers) {
			Character character = controller.createNewCharacter(current.GetPlayerSpawn());
			players.add(character);
			character.attachToParent(current, character.genName());
		}
		oneLeft = false;
		super.init();

		for (ControllerPlayer c : Main.getGameMaster().getPlayerControllers()) {
			if (c.getDevice() == Device.KEYBOARD) {
				Main.getRenderer().setCurorVisibility(true);
				break;
			}
		}
		
		//TODO: Remove this test ai character
		ControllerAi ai = new ControllerAi();
		Main.getGameMaster().controllers.add(ai);
		ai.createNewCharacter(new Vec2f(0, -2)).attachToParent(current, "testAiChar");
	}

	@Override
	public void update(double d) {
		menuPause.update(d);
		if (menuPause.isPaused()) {
			return;
		} else if (canPlay) {
			if (oneLeft) {
				if (endCounter == null) {
					endCounter = new Animation(endAnimData);
					endCounter.play();
					// chooseWinner.setProcessing(true);
				} else {
					double chromaAbbIntensity = 1 - endCounter.getTime() / endCounter.getLength();
					chromaAbbIntensity = .3 * (chromaAbbIntensity * chromaAbbIntensity);
					Main.getRenderer().getPostProcess().chromaAbbIntensity = (float) chromaAbbIntensity;

					Queue<AnimEvent> events = endCounter.getEvents();
					AnimEvent current = events.peek();
					while ((current = events.poll()) != null) {
						if (current instanceof AnimEventCustom) {
							if (((AnimEventCustom) current).data.equals("Choose_Winner")) {
								Controller winner = null;
								for (Controller player : Main.getGameMaster().controllers)
									if (!player.hasDeadChar())
										winner = player;// Let's suppose everything works fine -> only 1 player alive
								double rand = Math.random();
								if (winner != null) {
									if (rand < 0.333) {
										Main.getAudioManager().playSound("data/sound/Winner_01.ogg", AudioChannel.UI, 5f, 1);
									} else if (rand > 0.666) {
										Main.getAudioManager().playSound("data/sound/Winner_03.ogg", AudioChannel.UI, 5f, 1);
									} else {
										Main.getAudioManager().playSound("data/sound/Winner_02.ogg", AudioChannel.UI, 5f, 1);
									}
									counterImage.getMaterial().setParamTex("image",
											Main.getRenderer().loadTexture("data/sprites/interface/Winner_Chroma2.png"));
									((CharacterSprite) winner.getCharacter().getChild("skeleton")).rainConfetti();

								} else {
									if (rand < 0.333) {
										Main.getAudioManager().playSound("data/sound/Draw_01.ogg", AudioChannel.UI, 5f, 1);
									} else if (rand > 0.666) {
										Main.getAudioManager().playSound("data/sound/Draw_03.ogg", AudioChannel.UI, 5f, 1);
									} else {
										Main.getAudioManager().playSound("data/sound/Draw_02.ogg", AudioChannel.UI, 5f, 1);
									}
									counterImage.getMaterial().setParamTex("image",
											Main.getRenderer().loadTexture("data/sprites/interface/Draw_Chroma2.png"));
								}
							}
						} else if (current instanceof AnimEventSound) {
							((AnimEventSound) current).play(null);
						}

					}

					Texture counterTexture = counterImage.getMaterial().getParamTex("image");
					if (endCounter.getTime() < 3.2) {
						counterTexture = endCounter.getTrackTex("CounterSprite");
						counterImage.getMaterial().setParamTex("image", counterTexture);
					}
					double size = endCounter.getTrackD("SizeOfCounterSprite");
					counterImage.setScale(counterTexture.getWidth() * size, counterTexture.getHeight() * size);
					counterImage.getMaterial().getParamTex("image").setFilter(false);
					endCounter.step(d);

					if (!endCounter.isPlaying()) {
						endCounter = null;
						endGame();
					}
				}
			}

			if (getCamera() != null) {
				getCamera().center(players, current.cameraBasePos, d);
				Main.getAudioManager().setListener(getCamera().getWorldPos(), getCamera().getWorldRot());
			} else
				Main.getAudioManager().setListener(new Vec3f(), new Quat());

			// Update controllers
			for (Controller controller1 : Main.getGameMaster().controllers)
				controller1.step(d);
			super.update(d);
		}

		if (startCounter != null) {

			if (getCamera() != null) {
				getCamera().center(players, current.cameraBasePos, d);
				Main.getAudioManager().setListener(getCamera().getWorldPos(), getCamera().getWorldRot());
				getCamera().step(d);
			} else
				Main.getAudioManager().setListener(new Vec3f(), new Quat());

			if (!startCounter.isPlaying()) {
				counterImage.setScale(0, 0);
				startCounter = null;
			} else {
				Queue<AnimEvent> events = startCounter.getEvents();
				AnimEvent current = events.peek();
				while ((current = events.poll()) != null) {
					if (current instanceof AnimEventCustom) {
						if (((AnimEventCustom) current).data.equals("CanPlayNow")) {
							canPlay = true;
							if (bgmPath.equals(bgmPathDefault))
								bgm.setVolume(.5f);
							else
								bgm.setVolume(this.current.musicVolume);
							if (!bgm.isPlaying())
								bgm.play();
						}
					} else if (current instanceof AnimEventSound) {
						((AnimEventSound) current).play(null);
					}
				}

				Main.getRenderer().getPostProcess().fadeToBlack = (float) startCounter.getTrackD("fadeToBlack");

				Texture counterTexture = startCounter.getTrackTex("CounterSprite");
				double size = startCounter.getTrackD("SizeOfCounterSprite");
				counterImage.getMaterial().setParamTex("image", counterTexture);
				counterImage.setScale(counterTexture.getWidth() * size, counterTexture.getHeight() * size);
				counterImage.getMaterial().getParamTex("image").setFilter(false);
				startCounter.step(d);
			}

		}
		inputs.step(d);
	}

	private void endGame() {
		for (Controller player : Main.getGameMaster().controllers) {
			if (player.getCharacter() != null) {
				player.getCharacter().takeDamage(
						new DamageInfo(0, DamageType.MISC_ONE_SHOT, new Vec2f(), 0, null));
			}
		}
		bgm.stop();
		Main.getGameMaster().launchNextGame();
	}

	@Override
	public void draw() {
		super.draw();
		Main.getRenderer().beginUi();
		counterImage.draw();
		menuPause.draw();
		Main.getRenderer().endUi();
	}
}