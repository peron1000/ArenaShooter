package arenashooter.entities.spatials;

import arenashooter.engine.audio.SoundSource;
import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;
import arenashooter.entities.Map;
import arenashooter.entities.Timer;

public class CharacterSprite extends Spatial {

	String folder;
	private Sprite body, head, footL, footR, handL, handR;

	private static SoundSource sndStep, sndPunch;

	private float lookAngle = 0;
	private float moveSpeed = 0;
	private boolean wasOnGround = false, isOnGround = false;
	private boolean lookRight = true;
	private Vec2f handRLoc = new Vec2f();

	private Timer stepTimer = new Timer(.25); // TODO: Improve step detection

	private double time = Math.random() * Math.PI, movementTime = 0;

	static {
		sndStep = new SoundSource("data/sound/step_01.ogg", 5, .8f, 1.2f, true);
		sndStep.setVolume(.2f);
		sndPunch = new SoundSource("data/sound/woosh_01.ogg", 5, .8f, 1.2f, true);
		sndPunch.setVolume(.7f);
	}

	public CharacterSprite(Vec2f position, CharacterInfo charInfo) {
		super(position);
		folder = charInfo.spriteFolder;

		body = new Sprite(pos(), folder + "/body.png");
		body.size = new Vec2f(body.tex.getWidth() * 3, body.tex.getHeight() * 3);
		body.tex.setFilter(false);
		body.attachToParent(this, "body");
		head = new Sprite(pos(), folder + "/head.png");
		head.size = new Vec2f(head.tex.getWidth() * 3, head.tex.getHeight() * 3);
		head.tex.setFilter(false);
		head.attachToParent(this, "aaa_head");
		footL = new Sprite(pos(), folder + "/foot.png");
		footL.size = new Vec2f(footL.tex.getWidth() * 3, footL.tex.getHeight() * 3);
		footL.tex.setFilter(false);
		footL.attachToParent(this, "footL");
		footR = new Sprite(pos(), folder + "/foot.png");
		footR.size = new Vec2f(footR.tex.getWidth() * 3, footR.tex.getHeight() * 3);
		footR.tex.setFilter(false);
		footR.attachToParent(this, "footR");

		if (siblings().get("weapon") != null)

		{
			// Les mains devront de placer au bons endroits sur l'arme BOUYASHAKA
		} else {

			handL = new Sprite(position, folder + "/hand.png");
			handL.size = new Vec2f(handL.tex.getWidth() * 3, handL.tex.getHeight() * 3);
			handL.tex.setFilter(false);
			handL.attachToParent(this, "handL");
			handR = new Sprite(position, folder + "/hand.png");
			handR.size = new Vec2f(handR.tex.getWidth() * 3, handR.tex.getHeight() * 3);
			handR.tex.setFilter(false);
			handR.attachToParent(this, "handR");
		}
	}

	public void punch() {
		handRLoc.x = 150;
		sndPunch.play(pos());
	}

	public void setLookRight(boolean lookRight) {
		this.lookRight = lookRight;
	}

	private void land() {

	}

	@Override
	public void step(double d) {
		super.step(d);

		wasOnGround = isOnGround;

		if (getParent() instanceof Character) {
			isOnGround = ((Character) getParent()).isOnGround;
			moveSpeed = ((Character) getParent()).vel.x;
		} else if (getParent() instanceof Map) { // TODO: Temp stuff for loading screen anim
			isOnGround = true;
			moveSpeed = 1000;
		}

		if (isOnGround && !wasOnGround)
			land();

		time += d;
		movementTime += d * Math.abs(moveSpeed);

		body.flipX = !lookRight;
		head.flipX = !lookRight;
		footL.flipX = !lookRight;
		footR.flipX = !lookRight;
		handL.flipX = !lookRight;
		handR.flipX = !lookRight;

		// Feet
		double footSin = Math.sin(movementTime * .02);
		double footCos = Math.cos(movementTime * .02);

		footSin = Utils.lerpD(1, footSin, Math.min(Math.abs(moveSpeed) / 500, 1));
		footCos = Utils.lerpD(1, footCos, Math.min(Math.abs(moveSpeed) / 500, 1));

		stepTimer.step(d * Math.abs(moveSpeed) / 500);
		if (!(getParent() instanceof Map)) { // TODO: Temp stuff for loading screen anim
			if (isOnGround && stepTimer.isOver()) {
				sndStep.play(pos());
				stepTimer.restart();
			}
		}

		if (moveSpeed > 0) {
			footL.position.add(new Vec2f(-20 + footCos * 4, 37 + footSin * 10));
			footR.position.add(new Vec2f(20 - footSin * 4, 37 + footCos * 10));
		} else {
			footL.position.add(new Vec2f(20 - footCos * 4, 37 + footSin * 10));
			footR.position.add(new Vec2f(-20 + footSin * 4, 37 + footCos * 10));
		}

		// Body
		double bodyH = Utils.lerpD(0, Math.sin(movementTime * .01d), Math.min(Math.abs(moveSpeed) / 300, 1));
		body.position.add(new Vec2f(0, -17 + bodyH * 1.9f));

		// Head
		double headH = Utils.lerpD(0, Math.cos(movementTime * .03d), Math.min(Math.abs(moveSpeed) / 300, 1));
		head.position.add(new Vec2f(0, -17 + headH * 2.5f));

		// Hands
		handRLoc.x = Utils.lerpF(handRLoc.x, 0, d * 8);
		handR.position.add(lookRight ? handRLoc : Vec2f.multiply(handRLoc, -1));
	}

}
