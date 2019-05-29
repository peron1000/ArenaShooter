package arenashooter.entities.spatials;

import java.io.File;
import java.net.PortUnreachableException;

import arenashooter.engine.animation.Animation;
import arenashooter.engine.animation.AnimationData;
import arenashooter.engine.audio.SoundSourceMulti;
import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.physic.CollisionFlags;
import arenashooter.engine.physic.bodies.RigidBody;
import arenashooter.engine.physic.shapes.ShapeDisk;
import arenashooter.entities.Arena;
import arenashooter.entities.Timer;
import arenashooter.entities.spatials.items.Usable;
import arenashooter.game.CharacterInfo;
import arenashooter.game.Main;

public class CharacterSprite extends Spatial {

	String folder;
	private Sprite body, head, footL, footR, handL, handR;
	private Sprite punchSprite = new Sprite(new Vec2f());

	private static SoundSourceMulti sndStep, sndPunch;

	private double lookAngle = 0;
	private float moveSpeed = 0;
	private boolean wasOnGround = false, isOnGround = false;
	private boolean lookRight = true;

	private boolean handLOnWeap = false, handROnWeap = false;
	
	private AnimationData punchAnim1 = AnimationData.loadAnim("data/animations/animPunch_1.xml");
	private AnimationData punchAnim2 = AnimationData.loadAnim("data/animations/animPunch_2.xml");
	private AnimationData punchAnim3 = AnimationData.loadAnim("data/animations/animPunch_3.xml");
	private Animation currentPunchAnim = null;

	private Timer stepTimer = new Timer(.25); // TODO: Improve step detection

	private double movementTime = 0;

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
		body.size = new Vec2f(body.getTexture().getWidth() * .052, body.getTexture().getHeight() * .052);
		body.getTexture().setFilter(false);
		body.attachToParent(this, "body");
		
		f = new File(folder + "/head.png");
		if(f.exists() && !f.isDirectory()) { 
			head = new Sprite(position, folder + "/head.png");
		}else if(new File(folder + "/head_tr.png").exists()) {
			head = new Sprite(position, folder + "/head_tr.png");
		}			
		head.size = new Vec2f(head.getTexture().getWidth() * .052, head.getTexture().getHeight() * .052);
		head.getTexture().setFilter(false);
		head.zIndex = 1;
		head.rotationFromParent = false;
		head.attachToParent(this, "head");
		
		//Feet
		f = new File(folder + "/foot.png");
		if(f.exists() && !f.isDirectory()) { 
			footL = new Sprite(position, folder + "/foot.png");
			footR = new Sprite(position, folder + "/foot.png");
		} else if(new File(folder + "/foot_tr.png").exists()) {
			footL = new Sprite(position, folder + "/foot_tr.png");
			footR = new Sprite(position, folder + "/foot_tr.png");
		}			
		footL.size = new Vec2f(footL.getTexture().getWidth() * .052, footL.getTexture().getHeight() * .052);
		footL.getTexture().setFilter(false);
		footL.attachToParent(this, "footL");
		footL.zIndex = 1;
		footR.size = new Vec2f(footR.getTexture().getWidth() * .052, footR.getTexture().getHeight() * .052);
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
		handL.size = new Vec2f(handL.getTexture().getWidth() * .052, handL.getTexture().getHeight() * .052);
		handL.getTexture().setFilter(false);
		handL.rotationFromParent = false;
		handL.attachToParent(this, "handL");
		handR.size = new Vec2f(handR.getTexture().getWidth() * .052, handR.getTexture().getHeight() * .052);
		handR.getTexture().setFilter(false);
		handR.rotationFromParent = false;
		handR.attachToParent(this, "handR");
		
		
		punchSprite.attachToParent(this, "Swoosh");
		punchSprite.size.set(0, 0);
	}

	public void punch(int swoosh, double direction) {
		Vec2f.rotate(new Vec2f(1.5, 0), direction, handR.localPosition);
		handR.rotation = direction;
		sndPunch.play(parentPosition);
		switch(swoosh) {
		case 1:
			currentPunchAnim = new Animation(punchAnim1);
			break;
		case 2 :
			currentPunchAnim = new Animation(punchAnim2);
			break;
		case 3 :
			currentPunchAnim = new Animation(punchAnim3);
			break;
		default :
		break;
		}
		currentPunchAnim.play();
	}

	public void setLookRight(boolean lookRight) {
		this.lookRight = lookRight;
	}

	private void land() {

	}
	
	public void explode(Vec2f impulse) {
		//Head
		RigidBody rb = new RigidBody(new ShapeDisk(.45), head.getWorldPos(), head.getWorldRot(), CollisionFlags.CORSPE, .9f, .9f);
		RigidBodyContainer rbc = new RigidBodyContainer(head.getWorldPos(), rb);
		rbc.attachToParent(getMap(), rbc.genName());
		rbc.setLinearVelocity( Vec2f.rotate(impulse, Math.random()-.5) );
		head.attachToParent(rbc, "head");
		head.localPosition.set(-.1, .19);
		head.rotationFromParent = true;

		//Body
		rb = new RigidBody(new ShapeDisk(.45), body.getWorldPos(), body.getWorldRot(), CollisionFlags.CORSPE, .9f, .9f);
		rbc = new RigidBodyContainer(body.getWorldPos(), rb);
		rbc.attachToParent(getMap(), rbc.genName());
		rbc.setLinearVelocity( Vec2f.rotate(impulse, Math.random()-.5) );
		body.attachToParent(rbc, "body");
		body.localPosition.set(-.1, -.6);
		body.rotationFromParent = true;
		
		//HandL
		rb = new RigidBody(new ShapeDisk(.15), handL.getWorldPos(), handL.getWorldRot(), CollisionFlags.CORSPE, .9f, .9f);
		rbc = new RigidBodyContainer(handL.getWorldPos(), rb);
		rbc.attachToParent(getMap(), rbc.genName());
		rbc.setLinearVelocity( Vec2f.rotate(impulse, Math.random()-.5) );
		handL.attachToParent(rbc, "handL");
		handL.localPosition.set(0, 0);
		handL.rotationFromParent = true;
		
		//HandR
		rb = new RigidBody(new ShapeDisk(.15), handR.getWorldPos(), handR.getWorldRot(), CollisionFlags.CORSPE, .9f, .9f);
		rbc = new RigidBodyContainer(handR.getWorldPos(), rb);
		rbc.attachToParent(getMap(), rbc.genName());
		rbc.setLinearVelocity( Vec2f.rotate(impulse, Math.random()-.5) );
		handR.attachToParent(rbc, "handR");
		handR.localPosition.set(0, 0);
		handR.rotationFromParent = true;
	}

	@Override
	public void step(double d) {
		super.step(d);

		if(currentPunchAnim != null) {
			currentPunchAnim.step(d);
			if(currentPunchAnim.isPlaying()) {
				punchSprite.flipY = !lookRight;
				punchSprite.setTexture(currentPunchAnim.getTrackTex("AnimTrackPunch1"));
				punchSprite.size.set(2, 2);
				punchSprite.localPosition.set(1.5, 0.3);
				Vec2f.rotate(punchSprite.localPosition, lookAngle);
				punchSprite.rotation = lookAngle;
				punchSprite.getTexture().setFilter(false);
			}else
				punchSprite.size.set(0, 0);
		}
		
		wasOnGround = isOnGround;

		if (getParent() instanceof Character) {
			lookAngle = ((Character)getParent()).aimInput;
			isOnGround = ((Character) getParent()).isOnGround;
			moveSpeed = ((Character) getParent()).getLinearVelocity().x;
		} else if (getParent() instanceof Arena) {
			lookAngle = 0;
			isOnGround = true;
			moveSpeed = 10;
		}

		if (isOnGround && !wasOnGround)
			land();

		movementTime += d * Math.abs(moveSpeed);

		body.flipX = !lookRight;
		head.flipX = !lookRight;
		footL.flipX = !lookRight;
		footR.flipX = !lookRight;

		// Feet
		double footSin = Math.sin(movementTime * 2);
		double footCos = Math.cos(movementTime * 2);

		footSin = Utils.lerpD(1, footSin, Math.min(Math.abs(moveSpeed) / 5, 1));
		footCos = Utils.lerpD(1, footCos, Math.min(Math.abs(moveSpeed) / 5, 1));

		stepTimer.step(d * Math.abs(moveSpeed) / 5);
		if (!(getParent() instanceof Arena)) { // TODO: Temp stuff for loading screen anim
			if (isOnGround && stepTimer.isOver()) {
				sndStep.play(parentPosition);
				stepTimer.restart();
			}
		}

		if (moveSpeed > 0) {
			footL.localPosition.x = (float) (-.3 + footCos * .08);
			footL.localPosition.y = (float) (.65 + footSin * .16);

			footR.localPosition.x = (float) (.3 - footSin * .08);
			footR.localPosition.y = (float) (.65 + footCos * .16);

			if (lookRight) {
				footL.zIndex = 1;
				footR.zIndex = -1;
			} else {
				footL.zIndex = -1;
				footR.zIndex = 1;
			}
		} else {
			footL.localPosition.x = (float) (.3 - footCos * .08);
			footL.localPosition.y = (float) (.65 + footSin * .16);

			footR.localPosition.x = (float) (-.3 + footSin * .08);
			footR.localPosition.y = (float) (.65 + footCos * .16);

			if (lookRight) {
				footL.zIndex = -1;
				footR.zIndex = 1;
			} else {
				footL.zIndex = 1;
				footR.zIndex = -1;
			}
		}

		// Body
		double bodyH = Utils.lerpD(0, Math.sin(movementTime * 1d), Math.min(Math.abs(moveSpeed) / 3, 1));
		body.localPosition.y = (float) (-.2 + bodyH * .032);

		// Head
		double headH = Utils.lerpD(0, Math.cos(movementTime * 3d), Math.min(Math.abs(moveSpeed) / 3, 1));
		head.localPosition.y = (float) (-.2 + headH * .035);
		
		// Hands
		if (getParent() instanceof Character) {
			Usable weap = ((Character) getParent()).getWeapon();

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
				Vec2f.rotate(handPos, weap.rotation, handL.localPosition);
				handL.rotation = weap.rotation;
				handL.flipX = false;
				handL.flipY = lookRight;
			} else {
				if (handLOnWeap) { //Left hand not on weapon
					handL.attachToParent(this, "handL");
					handL.zIndex = 0;
					handLOnWeap = false;
				}

				handL.localPosition.x = Utils.lerpF(handL.localPosition.x, .15f, Math.min(1, d * 9));
				handL.localPosition.y = Utils.lerpF(handL.localPosition.y, .15f, Math.min(1, d * 9));
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
				Vec2f.rotate(handPos, weap.rotation, handR.localPosition);
				handR.rotation = weap.rotation;
				handR.flipX = false;
				handR.flipY = lookRight;
			} else {
				if (handROnWeap) { //Right hand not on weapon
					handR.attachToParent(this, "handR");
					handR.zIndex = 0;
					handROnWeap = false;
				}

				handR.localPosition.x = Utils.lerpF(handR.localPosition.x, .15f, Math.min(1, d * 9));
				handR.localPosition.y = Utils.lerpF(handR.localPosition.y, .15f, Math.min(1, d * 9));
				handR.rotation = 0;
				handR.flipX = !lookRight;
				handR.flipY = false;
			}

		} else {
			//Attach hands back to this
			handL.attachToParent(this, "handL");
			handR.attachToParent(this, "handR");
			
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
