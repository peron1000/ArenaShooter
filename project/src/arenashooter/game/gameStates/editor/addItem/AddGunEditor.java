package arenashooter.game.gameStates.editor.addItem;

import java.io.File;
import java.util.List;

import arenashooter.engine.FileUtils;
import arenashooter.engine.audio.Audio;
import arenashooter.engine.audio.AudioChannel;
import arenashooter.engine.events.EventListener;
import arenashooter.engine.events.input.InputActionEvent;
import arenashooter.engine.events.input.InputListener;
import arenashooter.engine.graphics.Texture;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.graphics.fonts.Text.TextAlignH;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.ui.MultiUi;
import arenashooter.engine.ui.ScrollerH;
import arenashooter.engine.ui.TabList;
import arenashooter.engine.ui.TextInput;
import arenashooter.engine.ui.Trigger;
import arenashooter.engine.ui.UiActionable;
import arenashooter.engine.ui.UiElement;
import arenashooter.engine.ui.UiGroup;
import arenashooter.engine.ui.UiListVertical;
import arenashooter.engine.ui.Valuable;
import arenashooter.engine.ui.simpleElement.Button;
import arenashooter.engine.ui.simpleElement.Label;
import arenashooter.engine.ui.simpleElement.UiImage;
import arenashooter.entities.Arena;
import arenashooter.entities.Entity;
import arenashooter.entities.spatials.Character;
import arenashooter.entities.spatials.SoundEffect;
import arenashooter.entities.spatials.items.Gun;
import arenashooter.game.CharacterClass;
import arenashooter.game.CharacterInfo;
import arenashooter.game.Main;
import arenashooter.game.gameStates.editor.ArenaEditor;
import arenashooter.game.gameStates.editor.Editor;

class AddGunEditor extends UiElement implements MultiUi {

	// Demo
	private Arena arena = new Arena();
	private Character character;
	private Gun gun;
	private BulletType bulletType = BulletType.DEFAULT;

	// UI
	private final double xScale = 30, yScale = 5;
	private TabList<UiActionable> menu = new TabList<>();
	private final ScrollerH<String> spriteScroller = getScrollerSprite();
	private ScrollerH<String> scrollersSound = scrollerSounds();
	private Trigger cancelSettingSound = new Trigger() {

		@Override
		public void make() {
			// Nothing by default
		}
	};
	private UiGroup<UiElement> popup = null;
	private Label labelSensibility = new Label("Sensibility : @");
	private TextInput textInput = new TextInput();

	// State
	private enum State {
		NOTHING, EXTENT, HAND_R, HAND_L, BULLET_START, SOUND_PICKUP, SOUND_WARMUP, SOUND_NOAMMO, SOUND_BANG, SAVING;
	}

	private State state = State.NOTHING;

	// Input
	private InputListener input = new InputListener();
	private Valuable<Float> sensibility = new Valuable<Float>() {

		private Float value = 0.1f;

		@Override
		public void setValue(Float newValue) {
			value = newValue;
			labelSensibility.setText(labelSensibility.getText());
		}

		@Override
		public Float getValue() {
			return value;
		}

		@Override
		public String getStringValue() {
			int percent = (int) (value * 1000);
			return percent + "%";
		}
	};
	private Arena arenaToConstruct;
	private Editor editor;
	private ArenaEditor arenaEditor;

	public AddGunEditor(Arena arenaToConstruct, Editor editor, ArenaEditor arenaEditor) {

		this.arenaToConstruct = arenaToConstruct;
		this.editor = editor;
		this.arenaEditor = arenaEditor;
		// Menu
		labelSensibility.bindToValuable(sensibility);
		UiImage bg = new UiImage(0.8, 0.7, 0.6, 0.5);
		bg.setScale(50, 100);

		menu.addToPosition(-40, -30);
		menu.setBackground(bg);
		menu.getBackground().setPosition(menu.getPosition().x, 0);
		menu.setBackgroundVisible(true);
		menu.addToScaleForeach(20, 0, false);
		menu.setArrowsDistance(20);
		menu.setScaleArrows(8, 8);

		UiListVertical<UiActionable> view = new UiListVertical<>();
		view.addElement(spriteScroller);
		view.addElements(getButtonsView());

		UiListVertical<UiActionable> sounds = new UiListVertical<>();
		sounds.addElements(getButtonsSounds(sounds));

		menu.addBind("View", view);
		menu.addBind("Sound", sounds);
		delegationActionsTo(menu);
		UiImage.selector.setPosition(getTarget().getPosition());

		// Character demo
		CharacterInfo ci = new CharacterInfo(CharacterClass.Agile);
		int randomIndex = (int) (Math.random() * CharacterClass.values().length);
		for (int i = 0; i < randomIndex; i++) {
			ci.nextClass();
		}
		randomIndex = (int) (Math.random() * ci.getCharClass().getNbOfSkins());
		for (int i = 0; i < randomIndex; i++) {
			ci.nextSkin();
		}
		character = new Character(new Vec2f(2.5, 0), ci, true);

		character.attachToParent(arena, "CharTest");
		gun = new Gun(spriteScroller.get());
		gun.attachToParent(character, "Item_Weapon");
		bulletType.getSprite().size = new Vec2f(bulletType.getSprite().getTexture().getWidth() * .018,
				bulletType.getSprite().getTexture().getHeight() * .018);
		bulletType.getSprite().getTexture().setFilter(false);

		// Inputs
		input.actions.add(new EventListener<InputActionEvent>() {

			@Override
			public void launch(InputActionEvent event) {
				switch (event.getAction()) {
				case ATTACK:
					switch (event.getActionState()) {
					case JUST_PRESSED:
						character.attackStartDemo(true);
						break;
					case JUST_RELEASED:
						character.attackStopDemo();
						break;
					case PRESSED:
						character.attackStart(false);
						break;
					default:
						break;
					}
					break;
				case UI_ZOOMR:
					sensibility.setValue(sensibility.getValue() * 1.05f);
					break;
				case UI_ZOOML:
					sensibility.setValue(sensibility.getValue() / 1.05f);
					break;
				default:
					break;
				}
			}

		});
	}

	private UiActionable[] getButtonsView() {
		ScrollerH<BulletType> bt = new ScrollerH<>(BulletType.values());
		bt.setBackgroundVisible(true);
		bt.setTitle("Bullet type");

		UiActionable[] ret = { new Button("Set extent"), new Button("Set Hand Right"), new Button("Set Hand Left"),
				new Button("Set bullet start position"), bt };
		for (UiActionable a : ret) {
			a.setScale(xScale, yScale);
		}

		// Triggers
		ret[0].setOnArm(new Trigger() {

			@Override
			public void make() {
				AddGunEditor.this.state = State.EXTENT;
				popup = getPopupView("Setting extent", ": rescale the extent");
				// TODO : rajouter visuel
			}
		});
		ret[1].setOnArm(new Trigger() {

			@Override
			public void make() {
				AddGunEditor.this.state = State.HAND_R;
				popup = getPopupView("Setting the right hand position", ": move the hand position");
			}
		});
		ret[2].setOnArm(new Trigger() {

			@Override
			public void make() {
				AddGunEditor.this.state = State.HAND_L;
				popup = getPopupView("Setting the left hand position", ": move the hand position");
			}
		});
		ret[3].setOnArm(new Trigger() {

			@Override
			public void make() {
				AddGunEditor.this.state = State.BULLET_START;
				popup = getPopupView("Setting bullet start position", ": move the bullet start position");
				bulletType.getSprite().attachToParent(gun, "canonLenght");
				addToCanonLenght(0);
			}
		});
		bt.setOnArm(new Trigger() {

			@Override
			public void make() {
				bulletType.getSprite().attachToParent(gun, "bulletType");
				bulletType.getSprite().localPosition.set(gun.getCannonLength(), 0);
			}
		});
		bt.setOnChange(new Trigger() {

			@Override
			public void make() {
				bulletType.getSprite().detach();
				bulletType = bt.get();
				bulletType.getSprite().attachToParent(gun, "bulletType");
				bulletType.getSprite().localPosition.set(gun.getCannonLength(), 0);
				gun.setBulletType(bulletType.getId());
			}
		});
		bt.setOnValidation(new Trigger() {

			@Override
			public void make() {
				bulletType.getSprite().detach();
				gun.setBulletType(bulletType.getId());
			}
		});

		return ret;
	}

	private Button[] getButtonsSounds(UiListVertical<UiActionable> list) {
		Button[] ret = { new Button("Set sound pickup"), new Button("Set sound warmup"),
				new Button("Set sound no ammo"), new Button("Set sound bang") };
		for (Button button : ret) {
			button.setScale(xScale, yScale);
		}

		// Triggers
		ret[0].setOnArm(new Trigger() {

			@Override
			public void make() {
				state = State.SOUND_PICKUP;
				popup = getPopupSounds("Setting sound pickup");
				list.replaceElement(scrollersSound, ret[0]);
				scrollersSound.setOnArm(new Trigger() {

					@Override
					public void make() {
						list.replaceElement(ret[0], scrollersSound);
						popup = null;
						gun.soundPickup = scrollersSound.get();
						state = State.NOTHING;
					}
				});
				cancelSettingSound = new Trigger() {

					@Override
					public void make() {
						list.replaceElement(ret[0], scrollersSound);
						popup = null;
						state = State.NOTHING;
					}
				};
			}
		});
		ret[1].setOnArm(new Trigger() {

			@Override
			public void make() {
				state = State.SOUND_WARMUP;
				popup = getPopupSounds("Setting sound warmup");
				list.replaceElement(scrollersSound, ret[1]);
				scrollersSound.setOnArm(new Trigger() {

					@Override
					public void make() {
						list.replaceElement(ret[1], scrollersSound);
						popup = null;
						gun.soundPickup = scrollersSound.get();
						SoundEffect se = new SoundEffect(new Vec2f(), scrollersSound.get(), AudioChannel.SFX, 0, 1, 1,
								true);
						gun.setSndWarmup(se);
						state = State.NOTHING;
					}
				});
				cancelSettingSound = new Trigger() {

					@Override
					public void make() {
						list.replaceElement(ret[1], scrollersSound);
						popup = null;
						state = State.NOTHING;
					}
				};
			}
		});
		ret[2].setOnArm(new Trigger() {

			@Override
			public void make() {
				state = State.SOUND_NOAMMO;
				popup = getPopupSounds("Setting sound no ammo");
				list.replaceElement(scrollersSound, ret[2]);
				scrollersSound.setOnArm(new Trigger() {

					@Override
					public void make() {
						list.replaceElement(ret[2], scrollersSound);
						popup = null;
						gun.setSoundNoAmmo(scrollersSound.get());
						state = State.NOTHING;
					}
				});
				cancelSettingSound = new Trigger() {

					@Override
					public void make() {
						list.replaceElement(ret[2], scrollersSound);
						popup = null;
						state = State.NOTHING;
					}
				};
			}
		});
		ret[3].setOnArm(new Trigger() {

			@Override
			public void make() {
				state = State.SOUND_BANG;
				popup = getPopupSounds("Setting sound bang");
				list.replaceElement(scrollersSound, ret[3]);
				scrollersSound.setOnArm(new Trigger() {

					@Override
					public void make() {
						list.replaceElement(ret[3], scrollersSound);
						popup = null;
						gun.setSoundFire(scrollersSound.get());
						state = State.NOTHING;
					}
				});
				cancelSettingSound = new Trigger() {

					@Override
					public void make() {
						list.replaceElement(ret[3], scrollersSound);
						popup = null;
						state = State.NOTHING;
					}
				};
			}
		});

		return ret;
	}

	private ScrollerH<String> getScrollerSprite() {
		ScrollerH<String> scSprite = new ScrollerH<>();
		File weapons = new File("data/weapons");
		List<File> listFile = FileUtils.listFilesByType(weapons, ".png");
		for (File file : listFile) {
			scSprite.add(file.getAbsolutePath());
			scSprite.changeValueView(file.getAbsolutePath(), file.getName());
		}
		scSprite.setValue(listFile.get(0).getAbsolutePath());
		scSprite.setScale(30, 10);
		scSprite.setOnChange(new Trigger() {

			@Override
			public void make() {
				gun.setSprite(scSprite.get());
			}
		});
		return scSprite;
	}

	private UiGroup<UiElement> getPopupView(String title, String instr) {
		UiGroup<UiElement> ret = new UiGroup<>();
		double textScale = 2.5;

		// background
		UiImage bg = new UiImage(.85, .8, .75, .7), border = new UiImage(0, 0, 0, 1);
		bg.setScale(60, 35);
		double toBorder = 1.5;
		border.setScale(bg.getScale().x + toBorder, bg.getScale().y + toBorder);

		// Title
		Label titleLabel = new Label(title);
		titleLabel.setScale(4.5);
		titleLabel.setPosition(0, -10);

		// Instructions
		UiImage moveLeft = new UiImage(Texture.loadTexture("data/sprites/interface/left.png")),
				moveRight = new UiImage(Texture.loadTexture("data/sprites/interface/right.png")),
				moveUp = new UiImage(Texture.loadTexture("data/sprites/interface/up.png")),
				moveDown = new UiImage(Texture.loadTexture("data/sprites/interface/down.png"));
		moveLeft.setScale(textScale);
		moveRight.setScale(textScale);
		moveUp.setScale(textScale);
		moveDown.setScale(textScale);

		moveLeft.setPosition(-20, -2.5);
		moveRight.setPosition(-15, -2.5);
		moveUp.setPosition(-17.5, -5);
		moveDown.setPosition(-17.5, 0);

		Label instruction1 = new Label(instr);
		instruction1.setScale(textScale);
		instruction1.setPosition(-10, -2.5);
		instruction1.setAlignH(TextAlignH.LEFT);
		Label instruction2 = new Label(": increase/decrease the sensibility");
		instruction2.setScale(textScale);
		instruction2.setPosition(-10, 4);
		instruction2.setAlignH(TextAlignH.LEFT);

		labelSensibility.setScale(textScale);
		labelSensibility.setPosition(0, 10);
		ret.addElements(border, bg, titleLabel, moveLeft, moveRight, moveDown, moveUp, instruction1, instruction2,
				labelSensibility);
		ret.setPosition(40, -30);
		return ret;
	}

	private UiGroup<UiElement> getPopupSounds(String title) {
		// background
		UiImage bg = new UiImage(.85, .8, .75, .7), border = new UiImage(0, 0, 0, 1);
		bg.setScale(60, 35);
		double toBorder = 1.5;
		border.setScale(bg.getScale().x + toBorder, bg.getScale().y + toBorder);

		// Title
		Label titleLabel = new Label(title);
		titleLabel.setScale(4.5);
		titleLabel.setPosition(0, -10);

		double textScale = 3;
		Label instr1 = new Label("Y or shift+space to listen the sound");
		instr1.setScale(textScale);
		instr1.setPosition(0, -2);
		Label instr2 = new Label("A or space to confirm your choice");
		instr2.setScale(textScale);
		instr2.setPosition(0, 4);

		UiGroup<UiElement> ret = new UiGroup<UiElement>();

		ret.addElements(border, bg, titleLabel, instr1, instr2);
		ret.setPosition(40, -30);
		return ret;
	}

	private UiGroup<UiElement> getPopupSaver() {
		// background
		UiImage bg = new UiImage(.85, .8, .75, .7), border = new UiImage(0, 0, 0, 1);
		bg.setScale(60, 35);
		double toBorder = 1.5;
		border.setScale(bg.getScale().x + toBorder, bg.getScale().y + toBorder);

		// Title
		Label titleLabel = new Label("Save your weapon ?");
		titleLabel.setScale(4.5);
		titleLabel.setPosition(0, -10);

		textInput.reset();

		UiGroup<UiElement> ret = new UiGroup<UiElement>();

		ret.addElements(border, bg, titleLabel, textInput);
		return ret;
	}

	private ScrollerH<String> scrollerSounds() {
		ScrollerH<String> sc = new ScrollerH<String>() {
			@Override
			public boolean cancelAction() {
				cancelSettingSound.make();
				return true;
			}
		};
		File sounds = new File("data/sound");
		List<File> files = FileUtils.listFilesByType(sounds, ".ogg");
		for (File file : files) {
			sc.add(file.getPath());
			sc.changeValueView(file.getPath(), file.getName());
		}
		sc.setScale(xScale, yScale);
		sc.setAlwaysScrollable(true);
		UiImage bg = new UiImage(.8, .5, .2, 1);
		bg.setScale(xScale, yScale);
		sc.setBackgroundUnselect(bg);
		sc.setBackgroundVisible(true);
		sc.setValue(sc.get(0));
		return sc;
	}

	private void addToCanonLenght(double add) {
		gun.setCannonLength(gun.getCannonLength() + add);
		bulletType.getSprite().localPosition.set(gun.getCannonLength(), 0);
	}

	private void stopSetting() {
		state = State.NOTHING;
		popup = null;
		bulletType.getSprite().detach();
	}

	private boolean setting(Vec2f add) {
		switch (state) {
		case HAND_L:
			gun.handPosL.add(add);
			return true;
		case HAND_R:
			gun.handPosR.add(add);
			return true;
		case EXTENT:
			gun.getExtent().add(add);
			return true;
		case BULLET_START:
			addToCanonLenght(add.x);
			return true;
		case NOTHING:
		default:
			return false;
		}
	}

	@Override
	public boolean continueAction() {
		switch (state) {
		case BULLET_START:
		case EXTENT:
		case HAND_L:
		case HAND_R:
			stopSetting();
			return true;
		case SOUND_BANG:
		case SOUND_NOAMMO:
		case SOUND_PICKUP:
		case SOUND_WARMUP:
			return scrollersSound.selectAction();
		case SAVING:
			gun.name = textInput.getText();
			arenaToConstruct.items.add(gun);
			editor.setCurrentMenu(arenaEditor);
			return true;
		case NOTHING:
		default:
			popup = getPopupSaver();
			state = State.SAVING;
			return true;
		}
	}

	@Override
	public boolean selectAction() {
		switch (state) {
		case BULLET_START:
		case EXTENT:
		case HAND_L:
		case HAND_R:
			stopSetting();
			return true;
		case SOUND_BANG:
		case SOUND_NOAMMO:
		case SOUND_PICKUP:
		case SOUND_WARMUP:
			return scrollersSound.selectAction();
		case SAVING:
			return textInput.selectAction();
		case NOTHING:
		default:
			return menu.selectAction();
		}
	}

	@Override
	public boolean backAction() {
		switch (state) {
		case BULLET_START:
		case EXTENT:
		case HAND_L:
		case HAND_R:
			stopSetting();
			return true;
		case SOUND_BANG:
		case SOUND_NOAMMO:
		case SOUND_PICKUP:
		case SOUND_WARMUP:
			scrollersSound.cancelAction();
			return true;
		case SAVING:
			state = State.NOTHING;
			popup = null;
			return true;
		case NOTHING:
		default:
			state = State.SAVING;
			popup = getPopupSaver();
			return true;
		}
	}

	@Override
	public boolean downAction() {
		Vec2f add = new Vec2f(0, sensibility.getValue());
		switch (state) {
		case BULLET_START:
		case EXTENT:
		case HAND_L:
		case HAND_R:
			setting(add);
			return true;
		case SOUND_BANG:
		case SOUND_NOAMMO:
		case SOUND_PICKUP:
		case SOUND_WARMUP:
			return scrollersSound.downAction();
		case SAVING:
			return textInput.downAction();
		default:
		case NOTHING:
			return menu.downAction();
		}
	}

	@Override
	public boolean upAction() {
		Vec2f add = new Vec2f(0, -sensibility.getValue());
		switch (state) {
		case BULLET_START:
		case EXTENT:
		case HAND_L:
		case HAND_R:
			setting(add);
			return true;
		case SOUND_BANG:
		case SOUND_NOAMMO:
		case SOUND_PICKUP:
		case SOUND_WARMUP:
			return scrollersSound.upAction();
		case SAVING:
			return textInput.upAction();
		default:
		case NOTHING:
			return menu.upAction();
		}
	}

	@Override
	public boolean leftAction() {
		Vec2f add = new Vec2f(-sensibility.getValue(), 0);
		switch (state) {
		case BULLET_START:
		case EXTENT:
		case HAND_L:
		case HAND_R:
			setting(add);
			return true;
		case SOUND_BANG:
		case SOUND_NOAMMO:
		case SOUND_PICKUP:
		case SOUND_WARMUP:
			return scrollersSound.leftAction();
		case SAVING:
			return textInput.leftAction();
		default:
		case NOTHING:
			return menu.leftAction();
		}
	}

	@Override
	public boolean rightAction() {
		Vec2f add = new Vec2f(sensibility.getValue(), 0);
		switch (state) {
		case BULLET_START:
		case EXTENT:
		case HAND_L:
		case HAND_R:
			setting(add);
			return true;
		case SOUND_BANG:
		case SOUND_NOAMMO:
		case SOUND_PICKUP:
		case SOUND_WARMUP:
			return scrollersSound.rightAction();
		case SAVING:
			return textInput.rightAction();
		default:
		case NOTHING:
			return menu.rightAction();
		}
	}

	@Override
	public boolean changeAction() {
		switch (state) {
		case SOUND_BANG:
		case SOUND_NOAMMO:
		case SOUND_PICKUP:
		case SOUND_WARMUP:
			Audio.playSound(scrollersSound.get(), AudioChannel.SFX, 0.5f, 1f);
			return true;
		case SAVING:
			return textInput.changeAction();
		case BULLET_START:
		case EXTENT:
		case HAND_L:
		case HAND_R:
		case NOTHING:
		default:
			return menu.changeAction();
		}
	}

	@Override
	public void update(double delta) {
		input.step(delta);
		menu.update(delta);
		arena.step(delta);
		if(state == State.SAVING) {
			textInput.update(delta);
		}
		super.update(delta);
	}

	@Override
	public void draw() {
		menu.draw();
		Window.endUi();
		arena.renderFirstPass();

		if (!Main.skipTransparency) {
			Window.beginTransparency();
			for (Entity e : arena.transparent)
				e.draw(true);
			arena.transparent.clear();
			Window.endTransparency();
		}
		Window.beginUi();
		if (popup != null) {
			popup.draw();
		}
	}

	@Override
	public UiElement getTarget() {
		return menu.getTarget();
	}

}
