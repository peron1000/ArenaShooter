package arenashooter.entities.spatials;

import java.io.File;
import java.util.Queue;

import arenashooter.engine.DamageInfo;
import arenashooter.engine.animation.Animation;
import arenashooter.engine.animation.AnimationData;
import arenashooter.engine.animation.animevents.AnimEvent;
import arenashooter.engine.animation.animevents.AnimEventSound;
import arenashooter.engine.audio.Audio;
import arenashooter.engine.audio.AudioChannel;
import arenashooter.engine.graphics.Texture;
import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;
import arenashooter.engine.math.Vec4f;
import arenashooter.engine.physic.CollisionFlags;
import arenashooter.engine.physic.bodies.RigidBody;
import arenashooter.engine.physic.shapes.ShapeDisk;
import arenashooter.entities.Arena;
import arenashooter.entities.Timer;
import arenashooter.entities.spatials.items.Usable;
import arenashooter.game.CharacterInfo;

public class CharacterSprite extends Spatial {

	String folder;
	private Sprite body, head, footL, footR, handL, handR;
	private Sprite punchSprite = new Sprite(new Vec2f(), "data/sprites/swooshes/swoosh_1_1.png");
	private Sprite chargeSprite = new Sprite(new Vec2f(), "data/sprites/swooshes/Tching2.png");
	private Sprite parrySprite = new Sprite(new Vec2f(), "data/sprites/swooshes/Parry_Oriented2.png");
	private Texture brokenParry = Texture.loadTexture("data/sprites/swooshes/Broken_Parry.png");
	private Texture parryShield = Texture.loadTexture("data/sprites/swooshes/Parry_Oriented2.png");
	private Sprite stunStars = new Sprite(new Vec2f(0, -0.5), "data/sprites/StunStars.png");
	private String bloodParticles = "data/particles/blood.xml";
	private String stun = "data/particles/stun.xml";

	private double lookAngle = 0;
	private float moveSpeed = 0;
	private boolean wasOnGround = false, isOnGround = false;
	private boolean lookRight = true;
	public boolean charging = false;
	public boolean charged = false;
	boolean canParry = true;
	private boolean parrying = false;
	private boolean afterDeath = false;

	private boolean mirrorLeftFoot = false;
	private boolean handLOnWeap = false, handROnWeap = false;

	private AnimationData redBlinks = AnimationData.loadAnim("data/animations/BlinkRed_1.xml");
	private AnimationData punchAnim1 = AnimationData.loadAnim("data/animations/animPunch_1.xml");
	private AnimationData punchAnim2 = AnimationData.loadAnim("data/animations/animPunch_2.xml");
	private AnimationData punchAnim3 = AnimationData.loadAnim("data/animations/animPunch_3.xml");
	private AnimationData superPunch = AnimationData.loadAnim("data/animations/animSuperPunch.xml");
	private AnimationData stunStarsT = AnimationData.loadAnim("data/animations/stun.xml");
	private AnimationData wiggleX = AnimationData.loadAnim("data/animations/brokenParryWiggle.xml");
	private Animation blinking = null;
	private Animation shieldWiggling = null;
	private Animation animStun = null;
	private Animation currentPunchAnim = null;
	private Texture charge1 = Texture.loadTexture("data/sprites/swooshes/Tching1.png");
	private Texture charge2 = Texture.loadTexture("data/sprites/swooshes/Tching2.png");

	private Timer stepTimer = new Timer(.25); // TODO: Improve step detection

	private double movementTime = 0;
	private double sinTime;// Used for rescaling chargePunch Sprite.
	public boolean stunned;
	
	public CharacterSprite(CharacterInfo charInfo) {
		super();
		folder = "data/sprites/characters/" + charInfo.getSkin();
		this.mirrorLeftFoot = charInfo.mirrorLeftFoot();

		File f = new File(folder + "/body.png");
		if (f.exists() && !f.isDirectory()) {
			body = new Sprite(new Vec2f(0, -.2), folder + "/body.png");
		} else if (new File(folder + "/body_tr.png").exists()) {
			body = new Sprite(new Vec2f(0, -.2), folder + "/body_tr.png");
		}
		body.size = new Vec2f(body.getTexture().getWidth() * .052, body.getTexture().getHeight() * .052);
		body.getTexture().setFilter(false);
		body.attachToParent(this, "body");

		f = new File(folder + "/head.png");
		if (f.exists() && !f.isDirectory()) {
			head = new Sprite(new Vec2f(0, -.2), folder + "/head.png");
		} else if (new File(folder + "/head_tr.png").exists()) {
			head = new Sprite(new Vec2f(0, -.2), folder + "/head_tr.png");
		}
		head.size = new Vec2f(head.getTexture().getWidth() * .052, head.getTexture().getHeight() * .052);
		head.getTexture().setFilter(false);
		head.zIndex = 1;
		head.attachToParent(this, "head");

		// Feet
		f = new File(folder + "/foot.png");
		if (f.exists() && !f.isDirectory()) {
			footL = new Sprite(new Vec2f((mirrorLeftFoot ? -.5 : -.3) + .08, .81), folder + "/foot.png");
			footR = new Sprite(new Vec2f(.3 - .08, .81), folder + "/foot.png");
		} else if (new File(folder + "/foot_tr.png").exists()) {
			footL = new Sprite(new Vec2f((mirrorLeftFoot ? -.5 : -.3) + .08, .81), folder + "/foot_tr.png");
			footR = new Sprite(new Vec2f(.3 - .08, .81), folder + "/foot_tr.png");
		}
		footL.size = new Vec2f(footL.getTexture().getWidth() * .052, footL.getTexture().getHeight() * .052);
		footL.getTexture().setFilter(false);
		footL.attachToParent(this, "footL");
		if (mirrorLeftFoot)
			footL.zIndex = -1;
		footL.flipX = mirrorLeftFoot;
		footR.size = new Vec2f(footR.getTexture().getWidth() * .052, footR.getTexture().getHeight() * .052);
		footR.getTexture().setFilter(false);
		footR.attachToParent(this, "footR");
		footR.zIndex = -1;

		// Hands
		f = new File(folder + "/foot.png");
		if (f.exists() && !f.isDirectory()) {
			handL = new Sprite(new Vec2f(), folder + "/hand.png");
			handR = new Sprite(new Vec2f(), folder + "/hand.png");
		} else if (new File(folder + "/foot_tr.png").exists()) {
			handL = new Sprite(new Vec2f(), folder + "/hand_tr.png");
			handR = new Sprite(new Vec2f(), folder + "/hand_tr.png");
		}
		handL.size = new Vec2f(handL.getTexture().getWidth() * .052, handL.getTexture().getHeight() * .052);
		handL.getTexture().setFilter(false);
		handL.attachRot = false;
		handL.attachToParent(this, "handL");
		handR.size = new Vec2f(handR.getTexture().getWidth() * .052, handR.getTexture().getHeight() * .052);
		handR.getTexture().setFilter(false);
		handR.attachRot = false;
		handR.attachToParent(this, "handR");

		punchSprite.attachToParent(this, "Swoosh");
		punchSprite.size.set(0, 0);

		chargeSprite.attachToParent(this, "Charge");
		chargeSprite.size.set(0, 0);
		chargeSprite.zIndex = getZIndex() + 2;

		parrySprite.attachToParent(this, "Parry");
		parrySprite.useTransparency = true;
		parrySprite.size.set(0, 0);
		parrySprite.getTexture().setFilter(false);
		parrySprite.zIndex = getZIndex() + 2;

		stunStars.attachToParent(this, "Stun_Stars");
		stunStars.size.set(0, 0);
		stunStars.getTexture().setFilter(false);
		stunStars.zIndex = getZIndex() + 2;

		shieldWiggling = new Animation(wiggleX);
		shieldWiggling.play();
	}

	public void parryStart(boolean broken) {
		parrySprite.size.set(parrySprite.getTexture().getWidth() * 0.04, parrySprite.getTexture().getHeight() * 0.04);
		parrySprite.setTexture(broken ? brokenParry : parryShield);
		if (!broken) {
			parrying = true;
			Vec2f targetPos = new Vec2f(1.5, 0);
			Vec2f.rotate(targetPos, lookAngle, targetPos);
			parrySprite.localPosition.set(targetPos);
			if (!lookRight)
				parrySprite.flipY = true;
		}
	}

	public void parryStop() {
		parrySprite.size.set(0, 0);
		parrying = false;
	}

	public void breakParry() {
		parrySprite.setTexture(brokenParry);
		canParry = false;
	}

	public void stunStart(double stunTime) {
		if (!stunned) {
			stunned = true;
			animStun = new Animation(stunStarsT);
			animStun.play();
			stunStars.size.set(stunStars.getTexture().getWidth() * 0.06, stunStars.getTexture().getWidth() * 0.06);
		}
		for (int i = 0; i < 1 + stunTime * 3; i++) {
			Particles stars = new Particles(new Vec2f(0, -0.25), stun);
			stars.zIndex = getZIndex() + 2;
			stars.selfDestruct = true;
			stars.attachToParent(this, genName());
		}
	}

	public void stunStop() {
		// TODO stunStop Effects
		stunned = false;
		animStun = null;
		stunStars.size.set(0, 0);
	}

	public void punch(int swoosh, double direction) {
		Vec2f.rotate(new Vec2f(1.5, 0), direction, handR.localPosition);
		handR.localRotation = direction;
		Audio.playSound2D("data/sound/woosh_01.ogg", AudioChannel.SFX, .7f, Utils.lerpF(.8f, 1.2f, Math.random()),
				getWorldPos());
		switch (swoosh) {
		case 1:
			currentPunchAnim = new Animation(punchAnim1);
			break;
		case 2:
			currentPunchAnim = new Animation(punchAnim2);
			break;
		case 3:
			currentPunchAnim = new Animation(punchAnim3);
			break;
		case -1:
			currentPunchAnim = new Animation(superPunch);
			break;
		default:
			break;
		}
		punchSprite.size.set(2, 2);
		currentPunchAnim.play();
	}

	public void damageEffects(DamageInfo info) {
		Particles blood = new Particles(new Vec2f(), bloodParticles);
		blood.selfDestruct = true;
		blood.attachToParent(this, blood.genName());
	}

	public void stopCharge() {
		chargeSprite.size.set(0, 0);
		charging = false;
		charged = false;
	}

	public void setLookRight(boolean lookRight) {
		this.lookRight = lookRight;
	}

	private void land() {
	}

	public void plum() {
//		Particles feathers = new Particles(new Vec2f(), "data/particles/Feathers.xml");
//		feathers.selfDestruct = true;
//		feathers.attachToParent(this, feathers.genName());
	}

	public void activateBushidoMode() {
		// TODO Auto-generated method stub
		afterDeath = true;
		blinking = new Animation(redBlinks);
		blinking.play();
		//Audio.playSound2D("data/sound/Bushido_Trigger.ogg", AudioChannel.SFX, 1, (float) (0.95 + Math.random()*0.1), localPosition);
		Audio.playSound2D("data/sound/Draw_03.ogg", AudioChannel.SFX, 5f, (float) (0.95 + Math.random()*0.1), localPosition);
		Particles rageBits = new Particles(new Vec2f(), "data/particles/Rage.xml");
		rageBits.selfDestruct = true;
		rageBits.attachToParent(this, rageBits.genName());
	}
	
	public void rainConfetti() {
		Particles confett1 = new Particles(new Vec2f(), "data/particles/Confetti1.xml");
		Particles confett2 = new Particles(new Vec2f(), "data/particles/Confetti2.xml");
		Particles confett3 = new Particles(new Vec2f(), "data/particles/Confetti3.xml");
		Particles confett4 = new Particles(new Vec2f(), "data/particles/Confetti4.xml");
		Particles confett5 = new Particles(new Vec2f(), "data/particles/Confetti5.xml");
		Particles confett6 = new Particles(new Vec2f(), "data/particles/Confetti6.xml");
		Particles confett7 = new Particles(new Vec2f(), "data/particles/Confetti7.xml");
		confett1.localPosition.set(0, -5);
		confett2.localPosition.set(0, -5);
		confett3.localPosition.set(0, -5);
		confett4.localPosition.set(0, -5);
		confett5.localPosition.set(0, -5);
		confett6.localPosition.set(0, -5);
		confett7.localPosition.set(0, -5);
		confett1.selfDestruct = true;
		confett2.selfDestruct = true;
		confett3.selfDestruct = true;
		confett4.selfDestruct = true;
		confett5.selfDestruct = true;
		confett6.selfDestruct = true;
		confett7.selfDestruct = true;
		confett1.attachToParent(this, confett1.genName());
		confett2.attachToParent(this, confett2.genName());
		confett3.attachToParent(this, confett3.genName());
		confett4.attachToParent(this, confett4.genName());
		confett5.attachToParent(this, confett5.genName());
		confett6.attachToParent(this, confett6.genName());
		confett7.attachToParent(this, confett7.genName());
	}
	public void explode(Vec2f impulse) {
		// Head
		RigidBody rb = new RigidBody(new ShapeDisk(.45), Vec2f.subtract(head.getWorldPos(), new Vec2f(0, .5)),
				head.getWorldRot(), CollisionFlags.CORPSE, 2.9f, .9f);
		RigidBodyContainer rbc = new RigidBodyContainer(rb);
		rbc.attachToParent(getArena(), rbc.genName());
		rbc.setLinearVelocity(Vec2f.rotate(impulse, Math.random() - .5));
		head.attachToParent(rbc, "head");
		head.localPosition.set(-.1, .19);
		head.attachRot = true;

		// Body
		rb = new RigidBody(new ShapeDisk(.45), Vec2f.subtract(body.getWorldPos(), new Vec2f(0, .1)), body.getWorldRot(),
				CollisionFlags.CORPSE, 2.9f, .9f);
		rbc = new RigidBodyContainer(rb);
		rbc.attachToParent(getArena(), rbc.genName());
		rbc.setLinearVelocity(Vec2f.rotate(impulse, Math.random() - .5));
		body.attachToParent(rbc, "body");
		body.localPosition.set(-.1, -.6);
		body.attachRot = true;

		// HandL
		rb = new RigidBody(new ShapeDisk(.15), handL.getWorldPos(), handL.getWorldRot(), CollisionFlags.CORPSE, 2.9f,
				.9f);
		rbc = new RigidBodyContainer(rb);
		rbc.attachToParent(getArena(), rbc.genName());
		rbc.setLinearVelocity(Vec2f.rotate(impulse, Math.random() - .5));
		handL.attachToParent(rbc, "handL");
		handL.localPosition.set(0, 0);
		handL.attachRot = true;

		// HandR
		rb = new RigidBody(new ShapeDisk(.15), handR.getWorldPos(), handR.getWorldRot(), CollisionFlags.CORPSE, 2.9f,
				.9f);
		rbc = new RigidBodyContainer(rb);
		rbc.attachToParent(getArena(), rbc.genName());
		rbc.setLinearVelocity(Vec2f.rotate(impulse, Math.random() - .5));
		handR.attachToParent(rbc, "handR");
		handR.localPosition.set(0, 0);
		handR.attachRot = true;

		Particles blood = new Particles(new Vec2f(), bloodParticles);
		blood.selfDestruct = true;
		blood.attachToParent(this, blood.genName());
	}

	@Override
	public void step(double d) {
		super.step(d);

		wasOnGround = isOnGround;

		if (getParent() instanceof Character) {
			lookAngle = ((Character) getParent()).aimInput;
			isOnGround = ((Character) getParent()).isOnGround;
			moveSpeed = ((Character) getParent()).getLinearVelocity().x;
		} else {
			lookAngle = 0;
			isOnGround = true;
			moveSpeed = 10;
		}

		if (isOnGround && !wasOnGround)
			land();

		movementTime += d * Math.abs(moveSpeed);
		sinTime += d * 16;

		if (animStun != null) {
			animStun.step(d);
			stunStars.setTexture(animStun.getTrackTex("starsTexture"));
			stunStars.getTexture().setFilter(false);
			Queue<AnimEvent> events = animStun.getEvents();
			AnimEvent current = events.peek();
			while ((current = events.poll()) != null) {
				if (current instanceof AnimEventSound) {
					AnimEventSound snd = (AnimEventSound) current;
					Audio.playSound2D(snd.path, snd.channel, snd.volume, (float) (snd.pitch + Math.random() / 4),
							getWorldPos());
				}
			}
		}

		if (charging) {
			if (charged) {
				chargeSprite.setTexture(charge2);
				chargeSprite.size.set(chargeSprite.getTexture().getHeight() * 0.07 + Math.sin(sinTime) / 3,
						chargeSprite.getTexture().getWidth() * 0.07 + Math.sin(sinTime) / 3);
			} else {
				chargeSprite.setTexture(charge1);
				chargeSprite.size.set(chargeSprite.getTexture().getHeight() * 0.07,
						chargeSprite.getTexture().getWidth() * 0.07);
			}
			chargeSprite.localPosition.set((lookRight ? 0.25 : -0.25), 0.2);
			chargeSprite.getTexture().setFilter(false);
		}

		if (!canParry) {
			if (shieldWiggling != null) {
				parrySprite.flipY = false;
				shieldWiggling.step(d);
				parrySprite.localPosition.set((float) shieldWiggling.getTrackD("BrokenShieldPosX"), 0);
				parrySprite.localRotation = 0;
			}
		} else if(parrying) {
			handL.localPosition.set(.2, -.2);
			handR.localPosition.set(.3, -.25);
			handL.localRotation = lookAngle;
			handL.localRotation = lookAngle;
			if(lookRight) {
				handL.flipY = false;
				handR.flipY = false;
			} else {
				handL.flipY = true;
				handR.flipY = true;
			}
			
			Vec2f targetParryPosition = new Vec2f(0.5, 0);
			Vec2f.rotate(targetParryPosition, lookAngle, targetParryPosition);
			parrySprite.localPosition.set(targetParryPosition);
			parrySprite.localRotation = targetParryPosition.angle();
			parrySprite.flipY = !lookRight;
		}

		if(blinking != null) {
			blinking.step(d);
			if (blinking.isPlaying()) {
				Vec4f blinkColor = new Vec4f(blinking.getTrackVec3f("BlinkColor"), 1f);
				body.material.setParamVec4f("color", blinkColor);
				head.material.setParamVec4f("color", blinkColor);
				footL.material.setParamVec4f("color", blinkColor);
				footR.material.setParamVec4f("color", blinkColor);
				handL.material.setParamVec4f("color", blinkColor);
				handR.material.setParamVec4f("color", blinkColor);
			}
		} 
		
		if (currentPunchAnim != null) {
			currentPunchAnim.step(d);
			if (currentPunchAnim.isPlaying()) {
				punchSprite.flipY = !lookRight;
				punchSprite.setTexture(currentPunchAnim.getTrackTex("AnimTrackPunch1"));
				punchSprite.localPosition.set(1.5, 0.3);
				Vec2f.rotate(punchSprite.localPosition, lookAngle, punchSprite.localPosition);
				punchSprite.localRotation = lookAngle;
				punchSprite.getTexture().setFilter(false);
			} else
				punchSprite.size.set(0, 0);
		}

		body.flipX = !lookRight;
		head.flipX = !lookRight;
		if (!mirrorLeftFoot) {
			footL.flipX = !lookRight;
			footR.flipX = !lookRight;
		} else {
			footL.flipX = true;
			footR.flipX = false;
		}

		// Feet
		double footSin = Math.sin(movementTime * 2);
		double footCos = Math.cos(movementTime * 2);

		footSin = Utils.lerpD(1, footSin, Math.min(Math.abs(moveSpeed) / 5, 1));
		footCos = Utils.lerpD(1, footCos, Math.min(Math.abs(moveSpeed) / 5, 1));

		stepTimer.step(d * Math.abs(moveSpeed) / 5);
		if (!(getParent() instanceof Arena)) { // TODO: Temp stuff for loading screen anim
			if (isOnGround && stepTimer.isOver()) {
				Audio.playSound2D("data/sound/step_01.ogg", AudioChannel.SFX, .2f,
						Utils.lerpF(.8f, 1.2f, Math.random()), getWorldPos());
				stepTimer.restart();
			}
		}

		if (moveSpeed > 0) {
			
			if(!mirrorLeftFoot) {
				footL.localPosition.set((float) (-.3 + footCos * .08), (float) (.65 + footSin * .16));
				footR.localPosition.set((float) (.3 - footSin * .08), (float) (.65 + footCos * .16));
			} else {
//				footL.flipX = true;
//				footR.flipX = false;
				footL.localPosition.set((float) (-.5 + (lookRight ? 0:.2 ) + footCos * .08),
						(float) (.65 + footSin * .16));
				footR.localPosition.set((float) (.3 + (lookRight ? 0:0.2 ) - footSin * .08), (float) (.65 + footCos * .16));
			}
			if (lookRight) {
				footL.zIndex = mirrorLeftFoot ? -1 : 1;
				footR.zIndex = -2;
			} else {
				footL.zIndex = -2;
				footR.zIndex = mirrorLeftFoot ? -1 : 1;
			}
		} else {
			if(!mirrorLeftFoot) {
				footL.localPosition.set((float) (.3 - footCos * .08), (float) (.65 + footSin * .16));
				footR.localPosition.set((float) (-.3 + footSin * .08), (float) (.65 + footCos * .16));
			} else {
//				footL.flipX = false;
//				footR.flipX = true;
				footL.localPosition.set((float) (-.5 + (lookRight ? 0:.2 ) - footCos * .08), (float) (.65 + footSin * .16));
				footR.localPosition.set((float) (0.3 + (lookRight ? 0:0.2 ) + footSin * .08), (float) (.65 + footCos * .16));
			}

			if (lookRight) {
				footL.zIndex = -2;
				footR.zIndex = mirrorLeftFoot ? -1 : 1;
			} else {
				footL.zIndex = mirrorLeftFoot ? -1 : 1;
				footR.zIndex = -2;
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
				if (!handLOnWeap) { // Left hand on weapon
					handL.attachToParent(weap, "handL");
					handL.zIndex = 1;
					handLOnWeap = true;
				}

				Vec2f handPos = weap.handPosL.clone();
				if (!lookRight)
					handPos.y *= -1;
				// handL.localPosition.set(Vec2f.add(weap.localPosition, Vec2f.rotate(handPos,
				// weap.rotation)));
				Vec2f.rotate(handPos, weap.getWorldRot(), handL.localPosition);
				handL.localRotation = weap.getWorldRot();
				handL.flipX = false;
				handL.flipY = lookRight;
			} else {
				if (handLOnWeap) { // Left hand not on weapon
					handL.attachToParent(this, "handL");
					handL.zIndex = 0;
					handLOnWeap = false;
				}

				handL.localPosition.x = Utils.lerpF(handL.localPosition.x, .15f, Math.min(1, d * 9));
				handL.localPosition.y = Utils.lerpF(handL.localPosition.y, .15f, Math.min(1, d * 9));
				handL.localRotation = 0;
				handL.flipX = !lookRight;
				handL.flipY = false;
			}

			if (weap != null && weap.handPosR != null) {
				if (!handROnWeap) { // Right hand on weapon
					handR.attachToParent(weap, "handR");
					handR.zIndex = 1;
					handROnWeap = true;
				}

				Vec2f handPos = weap.handPosR.clone();
				if (!lookRight)
					handPos.y *= -1;
				// handR.localPosition.set(Vec2f.add(weap.localPosition, Vec2f.rotate(handPos,
				// weap.rotation)));
				Vec2f.rotate(handPos, weap.getWorldRot(), handR.localPosition);
				handR.localRotation = weap.getWorldRot();
				handR.flipX = false;
				handR.flipY = lookRight;
			} else {
				if (handROnWeap) { // Right hand not on weapon
					handR.attachToParent(this, "handR");
					handR.zIndex = 0;
					handROnWeap = false;
				}

				handR.localPosition.x = Utils.lerpF(handR.localPosition.x, .15f, Math.min(1, d * 9));
				handR.localPosition.y = Utils.lerpF(handR.localPosition.y, .15f, Math.min(1, d * 9));
				handR.localRotation = 0;
				handR.flipX = !lookRight;
				handR.flipY = false;
			}

		} else {
			// Attach hands back to this
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
