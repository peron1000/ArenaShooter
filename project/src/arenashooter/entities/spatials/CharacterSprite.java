package arenashooter.entities.spatials;

import java.io.File;

import arenashooter.engine.audio.SoundSourceMulti;
import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;
import arenashooter.entities.Map;
import arenashooter.entities.Timer;
import arenashooter.entities.spatials.items.Usable;
import arenashooter.game.CharacterInfo;

public class CharacterSprite extends Spatial {

	String folder;
	private Sprite body, head, footL, footR, handL, handR;

	private static SoundSourceMulti sndStep, sndPunch;

	private float lookAngle = 0;
	private float moveSpeed = 0;
	private boolean wasOnGround = false, isOnGround = false;
	private boolean lookRight = true;

	private boolean handLOnWeap = false, handROnWeap = false;

	private Timer stepTimer = new Timer(.25); // TODO: Improve step detection

	private double time = Math.random() * Math.PI, movementTime = 0;

	static {
		sndStep = new SoundSourceMulti("data/sound/step_01.ogg", 5, .8f, 1.2f, true);
		sndStep.setVolume(.2f);
		sndPunch = new SoundSourceMulti("data/sound/woosh_01.ogg", 5, .8f, 1.2f, true);
		sndPunch.setVolume(.7f);
	}

	public CharacterSprite(Vec2f position, CharacterInfo charInfo) {
		super(position);
		folder = "data/sprites/characters/"+charInfo.getSkin();

		File f = new File(folder + "/body.png");
		if(f.exists() && !f.isDirectory()) { 
			body = new Sprite(position, folder + "/body.png");
		}else if(new File(folder + "/body_tr.png").exists()) {
			body = new Sprite(position, folder + "/body_tr.png");
		}
		body.size = new Vec2f(body.getTexture().getWidth() * 3, body.getTexture().getHeight() * 3);
		body.getTexture().setFilter(false);
		body.attachToParent(this, "body");
		
		f = new File(folder + "/head.png");
		if(f.exists() && !f.isDirectory()) { 
			head = new Sprite(position, folder + "/head.png");
		}else if(new File(folder + "/head_tr.png").exists()) {
			head = new Sprite(position, folder + "/head_tr.png");
		}			
		head.size = new Vec2f(head.getTexture().getWidth() * 3, head.getTexture().getHeight() * 3);
		head.getTexture().setFilter(false);
		head.zIndex = 1;
		head.attachToParent(this, "head");
		
		//Feet
		f = new File(folder + "/foot.png");
		if(f.exists() && !f.isDirectory()) { 
			footL = new Sprite(position, folder + "/foot.png");
			footR = new Sprite(position, folder + "/foot.png");
		}else if(new File(folder + "/foot_tr.png").exists()) {
			footL = new Sprite(position, folder + "/foot_tr.png");
			footR = new Sprite(position, folder + "/foot_tr.png");
		}			
		footL.size = new Vec2f(footL.getTexture().getWidth() * 3, footL.getTexture().getHeight() * 3);
		footL.getTexture().setFilter(false);
		footL.attachToParent(this, "footL");
		footL.zIndex = 1;
		footR.size = new Vec2f(footR.getTexture().getWidth() * 3, footR.getTexture().getHeight() * 3);
		footR.getTexture().setFilter(false);
		footR.attachToParent(this, "footR");
		footR.zIndex = -1;
		
		//Hands
		f = new File(folder + "/foot.png");
		if(f.exists() && !f.isDirectory()) { 
			handL = new Sprite(position, folder + "/hand.png");
			handR = new Sprite(position, folder + "/hand.png");
		}else if(new File(folder + "/foot_tr.png").exists()) {
			handL = new Sprite(position, folder + "/hand_tr.png");
			handR = new Sprite(position, folder + "/hand_tr.png");
		}		
		handL.size = new Vec2f(handL.getTexture().getWidth() * 3, handL.getTexture().getHeight() * 3);
		handL.getTexture().setFilter(false);
		handL.attachToParent(this, "handL");
		handR.size = new Vec2f(handR.getTexture().getWidth() * 3, handR.getTexture().getHeight() * 3);
		handR.getTexture().setFilter(false);
		handR.attachToParent(this, "handR");
	}

	public void punch() {
		handR.localPosition.x = lookRight ? 150 : -150;
		sndPunch.play(parentPosition);
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
//			lookAngle = ((Character)getParent()). //TODO: get aiming direction
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

		// Feet
		double footSin = Math.sin(movementTime * .02);
		double footCos = Math.cos(movementTime * .02);

		footSin = Utils.lerpD(1, footSin, Math.min(Math.abs(moveSpeed) / 500, 1));
		footCos = Utils.lerpD(1, footCos, Math.min(Math.abs(moveSpeed) / 500, 1));

		stepTimer.step(d * Math.abs(moveSpeed) / 500);
		if (!(getParent() instanceof Map)) { // TODO: Temp stuff for loading screen anim
			if (isOnGround && stepTimer.isOver()) {
				sndStep.play(parentPosition);
				stepTimer.restart();
			}
		}

		if (moveSpeed > 0) {
			footL.localPosition.x = (float) (-20 + footCos * 4);
			footL.localPosition.y = (float) (37 + footSin * 10);

			footR.localPosition.x = (float) (20 - footSin * 4);
			footR.localPosition.y = (float) (37 + footCos * 10);

			if (lookRight) {
				footL.zIndex = 1;
				footR.zIndex = -1;
			} else {
				footL.zIndex = -1;
				footR.zIndex = 1;
			}
		} else {
			footL.localPosition.x = (float) (20 - footCos * 4);
			footL.localPosition.y = (float) (37 + footSin * 10);

			footR.localPosition.x = (float) (-20 + footSin * 4);
			footR.localPosition.y = (float) (37 + footCos * 10);

			if (lookRight) {
				footL.zIndex = -1;
				footR.zIndex = 1;
			} else {
				footL.zIndex = 1;
				footR.zIndex = -1;
			}
		}

		// Body
		double bodyH = Utils.lerpD(0, Math.sin(movementTime * .01d), Math.min(Math.abs(moveSpeed) / 300, 1));
		body.localPosition.y = (float) (-17 + bodyH * 1.9);

		// Head
		double headH = Utils.lerpD(0, Math.cos(movementTime * .03d), Math.min(Math.abs(moveSpeed) / 300, 1));
		head.localPosition.y = (float) (-17 + headH * 2.5);

		// Hands
		if (parent instanceof Character) {
			Usable weap = ((Character) parent).getWeapon();

			if (weap != null && weap.handPosL != null) {
				if (!handLOnWeap) { //Left hand on weapon
					handL.attachToParent(weap, "handL");
					handL.zIndex = 1;
					handLOnWeap = true;
				}

				Vec2f handPos = weap.handPosL.clone();
				if (!lookRight)
					handPos.y *= -1;
				// handL.localPosition.set(Vec2f.add(weap.localPosition, Vec2f.rotate(handPos,
				// weap.rotation)));
				handL.localPosition.set(Vec2f.rotate(handPos, weap.rotation));
				handL.rotation = weap.rotation;
				handL.flipX = false;
				handL.flipY = lookRight;
			} else {
				if (handLOnWeap) { //Left hand not on weapon
					handL.attachToParent(this, "handL");
					handL.zIndex = 0;
					handLOnWeap = false;
				}

				handL.localPosition.x = Utils.lerpF(handL.localPosition.x, 0, Math.min(1, d * 9));
				handL.localPosition.y = Utils.lerpF(handL.localPosition.y, 0, Math.min(1, d * 9));
				handL.rotation = 0;
				handL.flipX = !lookRight;
				handL.flipY = false;
			}

			if (weap != null && weap.handPosR != null) {
				if (!handROnWeap) { //Right hand on weapon
					handR.attachToParent(weap, "handR");
					handR.zIndex = 1;
					handROnWeap = true;
				}

				Vec2f handPos = weap.handPosR.clone();
				if (!lookRight)
					handPos.y *= -1;
				// handR.localPosition.set(Vec2f.add(weap.localPosition, Vec2f.rotate(handPos,
				// weap.rotation)));
				handR.localPosition.set(Vec2f.rotate(handPos, weap.rotation));
				handR.rotation = weap.rotation;
				handR.flipX = false;
				handR.flipY = lookRight;
			} else {
				if (handROnWeap) { //Right hand not on weapon
					handR.attachToParent(this, "handR");
					handR.zIndex = 0;
					handROnWeap = false;
				}

				handR.localPosition.x = Utils.lerpF(handR.localPosition.x, 0, Math.min(1, d * 9));
				handR.localPosition.y = Utils.lerpF(handR.localPosition.y, 0, Math.min(1, d * 9));
				handR.rotation = 0;
				handR.flipX = !lookRight;
				handR.flipY = false;
			}

		} else {
			// Lerp to 0 for punch anim
			handL.localPosition.x = Utils.lerpF(handL.localPosition.x, 0, Math.min(1, d * 8));
			handR.localPosition.x = Utils.lerpF(handR.localPosition.x, 0, Math.min(1, d * 8));

			handL.flipX = !lookRight;
			handR.flipX = !lookRight;
			handL.flipY = false;
			handR.flipY = false;
		}
	}

}
