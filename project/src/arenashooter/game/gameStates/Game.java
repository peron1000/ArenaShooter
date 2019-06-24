package arenashooter.game.gameStates;

import java.util.ArrayList;
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
import arenashooter.game.ControllerPlayer;
import arenashooter.game.Main;
import arenashooter.game.gameStates.engineParam.GameMode;
import arenashooter.game.gameStates.engineParam.GameParam;
import arenashooter.game.gameStates.loading.LoadingGame;
import arenashooter.game.gameStates.loading.LoadingInterRound;

public class Game extends GameState {
	private int nbPlayers = Main.getGameMaster().controllers.size();
	private List<Character> players = new ArrayList<>(nbPlayers);

	int currentRound = 1;
	private final GameMode gameMode = GameParam.getGameMode();
	private final int nbRounds = GameParam.getRound();
	private InputListener inputs = new InputListener();

	private static SoundSource bgm;
	private static String bgmPath;
	private static final String bgmPathDefault = "data/music/Super_blep_serious_fight.ogg";

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
	private Animation endCounter = null;
	private AnimationData counterAnimData = AnimationData.loadAnim("data/animations/anim_StartCounter.xml");
	private AnimationData endAnimData = AnimationData.loadAnim("data/animations/anim_EndCounter.xml");
	private boolean canPlay = true;

	private MenuPause menuPause = new MenuPause(this);
	
	private LoadingInterRound loading;
	private boolean readyForNextRound = false, firstUpdate = true;
	
	public Game(String[] strings) {
		counterImage.setPosition(0, -25);
		LoadingGame loadingGame = new LoadingGame(GameParam.getRound(), strings);
		loading = new LoadingInterRound(this, loadingGame);
		loadingGame.start();
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
		loading.init();
		
		if (bgm != null) {
			bgm.destroy();
			bgm = null;
		}
		bgmPath = bgmPathDefault;
		bgm = Audio.createSource("data/music/Super_blep_serious_fight.ogg", AudioChannel.MUSIC, .1f, 1);
		bgm.setLooping(true);
		
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
	
	public int getCurrentRound() {
		return currentRound;
	}

	private void newRound() {
		current = loading.getArenaForNewRound();
		

		if (!current.musicPath.isEmpty()) { // Arena has custom music
			if (!bgmPath.equals(current.musicPath)) { // Only restart music if its path changed
				bgmPath = current.musicPath;
				bgm.destroy();
				bgm = Audio.createSource(bgmPath, AudioChannel.MUSIC, .1f, current.musicPitch);
				bgm.setLooping(true);
				bgm.play();
			}
		} else if (!bgmPath.equals(bgmPathDefault)) { // Switch to default music
			bgmPath = bgmPathDefault;
			bgm.destroy();
			bgm = Audio.createSource(bgmPath, AudioChannel.MUSIC, .1f, 1);
			bgm.setLooping(true);
			bgm.play();
		}
		bgm.setVolume(.1f);

		startCounter = new Animation(counterAnimData);
		startCounter.play();
		Window.postProcess.fadeToBlack = 1;
		endRound.reset();// TODO: remove
		endCounter = null;
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
		
		for(ControllerPlayer c : Main.getGameMaster().getPlayerControllers()) {
			if(c.getDevice() == Device.KEYBOARD)
				Window.setCurorVisibility(true);
		}
	}

	@Override
	public void update(double d) {
		readyForNextRound = loading.isReady();
		if(!readyForNextRound) {
			loading.update(d);
			return;
		}
		if(firstUpdate) {
			newRound();
			firstUpdate = false;
		}
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
										Audio.playSound("data/sound/Winner_01.ogg", AudioChannel.UI, 5f, 1);
									} else if (rand > 0.666) {
										Audio.playSound("data/sound/Winner_03.ogg", AudioChannel.UI, 5f, 1);
									} else {
										Audio.playSound("data/sound/Winner_02.ogg", AudioChannel.UI, 5f, 1);
									}
									counterImage.getMaterial().setParamTex("image",
											Texture.loadTexture("data/sprites/interface/Winner_Chroma2.png"));
									((CharacterSprite) winner.getCharacter().getChild("skeleton")).rainConfetti();

								} else {
									if (rand < 0.333) {
										Audio.playSound("data/sound/Draw_01.ogg", AudioChannel.UI, 5f, 1);
									} else if (rand > 0.666) {
										Audio.playSound("data/sound/Draw_03.ogg", AudioChannel.UI, 5f, 1);
									} else {
										Audio.playSound("data/sound/Draw_02.ogg", AudioChannel.UI, 5f, 1);
									}
									counterImage.getMaterial().setParamTex("image",
											Texture.loadTexture("data/sprites/interface/Draw_Chroma2.png"));
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
					counterImage.setScale(counterTexture.getSize().x * size, counterTexture.getSize().y * size);
					counterImage.getMaterial().getParamTex("image").setFilter(false);
					endCounter.step(d);
					
					if (!endCounter.isPlaying()) {
						endCounter = null;
						if (currentRound < nbRounds || nbRounds == -1) {
							currentRound++;
							for (Controller player : Main.getGameMaster().controllers) {
								if (player.getCharacter() != null) {
									player.getCharacter().takeDamage(
											new DamageInfo(0, DamageType.MISC_ONE_SHOT, new Vec2f(), 0, null));
								}
							}
							newRound();
						} else {
							bgm.stop();
							Main.getGameMaster().requestNextState(new Score(), "data/mapXML/menu_empty.xml");
						}
					}
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

				Window.postProcess.fadeToBlack = (float) startCounter.getTrackD("fadeToBlack");

				Texture counterTexture = startCounter.getTrackTex("CounterSprite");
				double size = startCounter.getTrackD("SizeOfCounterSprite");
				counterImage.getMaterial().setParamTex("image", counterTexture);
				counterImage.setScale(counterTexture.getSize().x * size, counterTexture.getSize().y * size);
				counterImage.getMaterial().getParamTex("image").setFilter(false);
				startCounter.step(d);
			}

		}
		inputs.step(d);
	}

	@Override
	public void draw() {
		if(!readyForNextRound) {
			loading.draw();
			return;
		}
		super.draw();
		Window.beginUi();
		counterImage.draw();
		menuPause.draw();
		Window.endUi();
	}
}