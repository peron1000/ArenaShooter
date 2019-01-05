package arenashooter.entities;

import arenashooter.engine.math.Utils;
import arenashooter.engine.math.Vec2f;

public class CharacterSprite extends Spatial {

	String folder;
	private Sprite body, head, footL, footR, handL, handR;
	
	public float lookAngle = 0;
	public float moveSpeed = 0;
	public boolean onGround = false;
	public boolean lookRight = true;
	
	private double time = Math.random()*Math.PI, movementTime = 0;
	
	public CharacterSprite(Vec2f position, String folder) {
		super(position);
		this.folder = folder;
		
		body = new Sprite(position, folder+"/body.png");
		body.size = new Vec2f(body.tex.getWidth() * 3, body.tex.getHeight() * 3);
		body.tex.setFilter(false);
		body.attachToParent(this, "body");
		head = new Sprite(position, folder+"/head.png");
		head.size = new Vec2f(head.tex.getWidth() * 3, head.tex.getHeight() * 3);
		head.tex.setFilter(false);
		head.attachToParent(this, "aaa_head");
		footL = new Sprite(position, folder+"/foot.png");
		footL.size = new Vec2f(footL.tex.getWidth() * 3, footL.tex.getHeight() * 3);
		footL.tex.setFilter(false);
		footL.attachToParent(this, "footL");
		footR = new Sprite(position, folder+"/foot.png");
		footR.size = new Vec2f(footR.tex.getWidth() * 3, footR.tex.getHeight() * 3);
		footR.tex.setFilter(false);
		footR.attachToParent(this, "footR");
		handL = new Sprite(position, folder+"/hand.png");
		handL.size = new Vec2f(handL.tex.getWidth() * 3, handL.tex.getHeight() * 3);
		handL.tex.setFilter(false);
		handL.attachToParent(this, "handL");
		handR = new Sprite(position, folder+"/hand.png");
		handR.size = new Vec2f(handR.tex.getWidth() * 3, handR.tex.getHeight() * 3);
		handR.tex.setFilter(false);
		handR.attachToParent(this, "handR");
	}
	
	@Override
	public void step(double d) {
		super.step(d);
		
		time += d;
		movementTime += d*Math.abs(moveSpeed);
		
		body.flipX = !lookRight;
		head.flipX = !lookRight;
		footL.flipX = !lookRight;
		footR.flipX = !lookRight;
		handL.flipX = !lookRight;
		handR.flipX = !lookRight;
		
		//Feet
		float footSin = (float)Math.sin(movementTime*.02d);
		float footCos = (float)Math.cos(movementTime*.02d);
		footSin = Utils.lerpF( 1, footSin, Math.min(Math.abs(moveSpeed)/500, 1) );
		footCos = Utils.lerpF( 1, footCos, Math.min(Math.abs(moveSpeed)/500, 1) );
		
		if(moveSpeed > 0) {
			footL.position.add(new Vec2f(-20+footCos*4, 44+footSin*10));
			footR.position.add(new Vec2f( 20-footSin*4, 44+footCos*10));
		} else {
			footL.position.add(new Vec2f( 20-footCos*4, 44+footSin*10));
			footR.position.add(new Vec2f(-20+footSin*4, 44+footCos*10));
		}
		
		//Body
		float bodyH = Utils.lerpF(0, (float)Math.sin(movementTime*.01d), Math.min(Math.abs(moveSpeed)/300, 1));
		body.position.add(new Vec2f( 0, -17+bodyH*1.9f ));
		
		//Head
		float headH = Utils.lerpF(0, (float)Math.cos(movementTime*.03d), Math.min(Math.abs(moveSpeed)/300, 1));
		head.position.add(new Vec2f( 0, -17+headH*1.8f ));
	}

}
