#version 330

//In
in vec2 screenCoord;

//Uniforms
uniform sampler2D sceneColor;

uniform float chromaAbbIntensity = 0.0;
uniform float vignetteIntensity = 1.4;

//Out
layout(location = 0) out vec3 FragmentColor;

void main() {
	//Distance to the center of the screen
	float distToCenter = length(screenCoord-vec2(0.5,0.5));
	
	vec2 realUV = screenCoord;
	realUV.y += sin(realUV.x*500.0)*0.01;
	
	//Chromatic aberration
	vec2 redUV = (((realUV*2.0)+1.0 + ( distToCenter*-chromaAbbIntensity*(realUV-0.5) ))-1.0)*0.5;
	vec2 greenUV = (((realUV*2.0)+1.0 + ( distToCenter*-(chromaAbbIntensity*0.35)*(realUV-0.5) ))-1.0)*0.5;
	
	float red = texture(sceneColor, redUV).r;
	float green = texture(sceneColor, greenUV).g;
	float blue = texture(sceneColor, realUV).b;
	
	//Vignette
	float vignette = 1.0-(distToCenter*distToCenter*distToCenter*distToCenter*vignetteIntensity);
	
	FragmentColor = vec3(red, green*0.9, blue*0.3)*vec3(vignette);
}
