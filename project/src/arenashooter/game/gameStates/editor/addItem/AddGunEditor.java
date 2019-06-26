package arenashooter.game.gameStates.editor.addItem;

import java.io.File;
import java.util.List;

import arenashooter.engine.FileUtils;
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
import arenashooter.entities.spatials.items.Gun;
import arenashooter.game.CharacterClass;
import arenashooter.game.CharacterInfo;
import arenashooter.game.Main;

class AddGunEditor extends UiElement implements MultiUi {

	// Demo
	private Arena arena = new Arena();
	private Character character;
	private Gun gun;

	// UI
	private TabList<UiActionable> menu = new TabList<>();
	private final ScrollerH<String> spriteScroller = getScrollerSprite();
	private UiGroup<UiElement> popup = null;
	private Label labelSensibility = new Label("Sensibility : @");
	private UiImage rectangle = null;

	// State
	private enum State {
		NOTHING, EXTENT, HAND_R, HAND_L;
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

	public AddGunEditor() {

		// Menu
		labelSensibility.bindToValuable(sensibility);
		UiImage bg = new UiImage(0.8, 0.7, 0.6, 0.5);
		bg.setScale(50, 100);

		menu.addToPosition(-40, -30);
		menu.setBackground(bg);
		menu.getBackground().setPosition(menu.getPosition().x, 0);
		menu.setBackgroundVisible(true);
		menu.addToScaleForeach(20, 0, false);

		UiListVertical<UiActionable> view = new UiListVertical<>();
		view.addElement(spriteScroller);

		view.addElements(setButtons());

		menu.addBind("View", view);
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
		character = new Character(new Vec2f(2.5, 0), ci);

		character.attachToParent(arena, "CharTest");
		gun = new Gun(spriteScroller.get());
		gun.attachToParent(character, "Item_Weapon");

		// Inputs
		input.actions.add(new EventListener<InputActionEvent>() {

			@Override
			public void launch(InputActionEvent event) {
				switch (event.getAction()) {
				case ATTACK:
					switch (event.getActionState()) {
					case JUST_PRESSED:
						character.attackStart(true);
					case JUST_RELEASED:
						character.attackStopDemo();
					case PRESSED:
						character.attackStart(false);
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

	private UiActionable[] setButtons() {
		UiActionable[] ret = {new Button("Set extent"), new Button("Set Hand Right"), new Button("Set Hand Left") };
		for (UiActionable a : ret) {
			a.setScale(30, 5);
		}

		// Triggers
		ret[0].setOnArm(new Trigger() {
			
			@Override
			public void make() {
				AddGunEditor.this.state = State.EXTENT;
				popup = getPopup("Setting extent", ": rescale th extent");
				rectangle = new UiImage(0.9, 0.2, 0.2, 0.5);
				rectangle.setScale(gun.extent);
				rectangle.setPosition(Vec2f.worldToScreen(gun.getWorldPos()));
			}
		});
		ret[1].setOnArm(new Trigger() {

			@Override
			public void make() {
				AddGunEditor.this.state = State.HAND_R;
				popup = getPopup("Setting the right hand position", ": move the hand position");
			}
		});
		ret[2].setOnArm(new Trigger() {

			@Override
			public void make() {
				AddGunEditor.this.state = State.HAND_L;
				popup = getPopup("Setting the left hand position", ": move the hand position");
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
	
	private UiGroup<UiElement> getPopup(String title ,String instr){
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
		ret.addElements(border, bg, titleLabel, moveLeft, moveRight, moveDown , moveUp, instruction1, instruction2, labelSensibility);
		ret.setPosition(40, -30);
		return ret;
	}
	
	private void stopSetting() {
		state = State.NOTHING;
		popup = null;
		rectangle = null;
	}
	
	@Override
	public boolean continueAction() {
		if (state != State.NOTHING) {
			stopSetting();
			return true;
		} else {
			return menu.continueAction();
		}
	}
	
	@Override
	public boolean selectAction() {
		if (state != State.NOTHING) {
			stopSetting();
			return true;
		} else {
			return menu.selectAction();
		}
	}

	@Override
	public boolean backAction() {
		if (state != State.NOTHING) {
			stopSetting();
			return true;
		} else {
			return menu.backAction();
		}
	}

	@Override
	public boolean downAction() {
		Vec2f add = new Vec2f(0,sensibility.getValue());
		switch (state) {
		case HAND_L:
			gun.handPosL.add(add);
			return true;
		case HAND_R:
			gun.handPosR.add(add);
			return true;
		case EXTENT:
			gun.getExtent().add(add);
			rectangle.setScale(gun.extent);
			return true;
		case NOTHING:
		default:
			return menu.downAction();
		}
	}

	@Override
	public boolean upAction() {
		Vec2f add = new Vec2f(0,-sensibility.getValue());
		switch (state) {
		case HAND_L:
			gun.handPosL.add(add);
			return true;
		case HAND_R:
			gun.handPosR.add(add);
			return true;
		case EXTENT:
			gun.getExtent().add(add);
			rectangle.setScale(gun.extent);
			return true;
		case NOTHING:
		default:
			return menu.upAction();
		}
	}

	@Override
	public boolean leftAction() {
		Vec2f add = new Vec2f(-sensibility.getValue(), 0);
		switch (state) {
		case HAND_L:
			gun.handPosL.add(add);
			return true;
		case HAND_R:
			gun.handPosR.add(add);
			return true;
		case EXTENT:
			gun.getExtent().add(add);
			rectangle.setScale(gun.extent);
			return true;
		case NOTHING:
		default:
			return menu.leftAction();
		}
	}

	@Override
	public boolean rightAction() {
		Vec2f add = new Vec2f(sensibility.getValue() ,0);
		switch (state) {
		case HAND_L:
			gun.handPosL.add(add);
			return true;
		case HAND_R:
			gun.handPosR.add(add);
			return true;
		case EXTENT:
			gun.getExtent().add(add);
			rectangle.setScale(gun.extent);
			return true;
		case NOTHING:
		default:
			return menu.rightAction();
		}
	}

	@Override
	public void update(double delta) {
		input.step(delta);
		menu.update(delta);
		arena.step(delta);
		super.update(delta);
	}

	@Override
	public void draw() {
		menu.draw();
		if (popup != null) {
			popup.draw();
		}
		if(rectangle != null) {
			rectangle.draw();
		}
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
	}

	@Override
	public UiElement getTarget() {
		return menu.getTarget();
	}

}
