package arenashooter.entities;

import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import arenashooter.engine.graphics.Model;
import arenashooter.engine.graphics.Shader;
import arenashooter.engine.graphics.Texture;
import arenashooter.engine.graphics.Window;
import arenashooter.engine.math.Mat4f;
import arenashooter.engine.math.Quat;
import arenashooter.engine.math.Vec2d;
import arenashooter.engine.math.Vec3f;
import arenashooter.engine.math.Vec4f;

public class Sprite extends Spatial {
	public Texture tex;
	private static Shader shader;
	private static Model model;
	public Vec4f colorMod = new Vec4f(1,1,1,1);;
	public Vec2d extent = new Vec2d(1, 1);
	
	public Sprite() {
		if(shader == null) shader = new Shader("data/shaders/sprite_simple");
		if(model == null) model = Model.loadQuad();
	}
	
	@Override
	public void draw() {
		super.draw();
		
		shader.bind();
		
		//Create matrices
		Vec3f pos = new Vec3f( (float)position.x, (float)position.y, -1 );
		Quat rot = Quat.fromAngle(rotation);
		Vec3f scale = new Vec3f( (float)extent.x, (float)extent.y, 1 );
		Mat4f modelM = Mat4f.transform(pos, rot, scale);
		shader.setUniformM4("model", modelM);
		shader.setUniformM4("view", Mat4f.identity()); //TODO: Get view matrix from camera
		shader.setUniformM4("projection", Window.proj); //TODO: Get projection matrix properly
		
		model.bindToShader(shader);
		
		//Bind texture
		glActiveTexture(GL_TEXTURE0);
		tex.bind();
		shader.setUniformI("baseColor", 0);
		
		//Color change
		shader.setUniformV4("baseColorMod", colorMod);
		
		model.bind();
		model.draw();
		
		Model.unbind();
		Shader.unbind();
		Texture.unbind();
	}
}
