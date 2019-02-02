package arenashooter.entities.spatials;

import arenashooter.engine.audio.SoundSource;
import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;
import arenashooter.entities.Map;
import arenashooter.entities.Timer;
import arenashooter.entities.spatials.items.Weapon;

public class CharacterSprite extends Spatial {

	String folder;
	private Sprite body, head, footL, footR, handL, handR;
	
	private static SoundSource sndStep, sndPunch;

	private float lookAngle = 0;
	private float moveSpeed = 0;
	private boolean wasOnGround = false, isOnGround = false;
	private boolean lookRight = true;

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

		body = new Sprite(position, folder + "/body.png");
		body.size = new Vec2f(body.tex.getWidth() * 3, body.tex.getHeight() * 3);
		body.tex.setFilter(false);
		body.attachToParent(this, "body");
		head = new Sprite(position, folder + "/head.png");
		head.size = new Vec2f(head.tex.getWidth() * 3, head.tex.getHeight() * 3);
		head.tex.setFilter(false);
		head.attachToParent(this, "aaa_head");
		
		//Feet
		footL = new Sprite(position, folder + "/foot.png");
		footL.size = new Vec2f(footL.tex.getWidth() * 3, footL.tex.getHeight() * 3);
		footL.tex.setFilter(false);
		footL.attachToParent(this, "footL");
		footR = new Sprite(position, folder + "/foot.png");
		footR.size = new Vec2f(footR.tex.getWidth() * 3, footR.tex.getHeight() * 3);
		footR.tex.setFilter(false);
		footR.attachToParent(this, "footR");
		
		//Hands
		handL = new Sprite(position, folder + "/hand.png");
		handL.size = new Vec2f(handL.tex.getWidth() * 3, handL.tex.getHeight() * 3);
		handL.tex.setFilter(false);
		handL.attachToParent(this, "handL");
		handR = new Sprite(position, folder + "/hand.png");
		handR.size = new Vec2f(handR.tex.getWidth() * 3, handR.tex.getHeight() * 3);
		handR.tex.setFilter(false);
		handR.attachToParent(this, "handR");
	}

	public void punch() {
		handR.localPosition.x = lookRight ? 150 : -150;
		sndPunch.play(position);
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

		if( getParent() instanceof Character ) {
//			lookAngle = ((Character)getParent()). //TODO: get aiming direction
			isOnGround = ((Character)getParent()).isOnGround;
			moveSpeed = ((Character)getParent()).vel.x;
		} else if( getParent() instanceof Map ) { //TODO: Temp stuff for loading screen anim
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
		if( !(getParent() instanceof Map) ) { //TODO: Temp stuff for loading screen anim
			if (isOnGround && stepTimer.isOver()) {
				sndStep.play(position);
				stepTimer.restart();
			}
		}

		if (moveSpeed > 0) {
			footL.localPosition.x = (float) (-20 + footCos * 4);
			footL.localPosition.y = (float) (37 + footSin * 10);
			
			footR.localPosition.x = (float) (20 - footSin * 4);
			footR.localPosition.y = (float) (37 + footCos * 10);
		} else {
			footL.localPosition.x = (float) (20 - footCos * 4);
			footL.localPosition.y = (float) (37 + footSin * 10);
			
			footR.localPosition.x = (float) (-20 + footSin * 4);
			footR.localPosition.y = (float) (37 + footCos * 10);
		}

		// Body
		double bodyH = Utils.lerpD(0, Math.sin(movementTime * .01d), Math.min(Math.abs(moveSpeed) / 300, 1));
		body.localPosition.y = (float) (-17 + bodyH * 1.9);

		// Head
		double headH = Utils.lerpD(0, Math.cos(movementTime * .03d), Math.min(Math.abs(moveSpeed) / 300, 1));
		head.localPosition.y = (float) (-17 + headH * 2.5);

		// Hands
		if(parent instanceof Character) {
			Weapon weap = ((Character)parent).getWeapon();
			
			if(weap != null && weap.handPosL != null) {
				handL.localPosition.set(Vec2f.add(weap.localPosition, Vec2f.rotate(weap.handPosL, weap.rotation)));
				handL.rotation = weap.rotation;
			} else {
				handL.localPosition.x = Utils.lerpF(handL.localPosition.x, 0, Math.min(1, d*9));
				handL.localPosition.y = Utils.lerpF(handL.localPosition.y, 0, Math.min(1, d*9));
				handL.rotation = 0;
			}
			
			if(weap != null && weap.handPosR != null) {
				handR.localPosition.set(Vec2f.add(weap.localPosition, Vec2f.rotate(weap.handPosR, weap.rotation)));
				handR.rotation = weap.rotation;
			} else {
				handR.localPosition.x = Utils.lerpF(handR.localPosition.x, 0, Math.min(1, d*9));
				handR.localPosition.y = Utils.lerpF(handR.localPosition.y, 0, Math.min(1, d*9));
				handR.rotation = 0;
			}
			
		} else {
			//Lerp to 0 for punch anim
			handL.localPosition.x = Utils.lerpF(handL.localPosition.x, 0, Math.min(1, d*8));
			handR.localPosition.x = Utils.lerpF(handR.localPosition.x, 0, Math.min(1, d*8));
		}
	}

}