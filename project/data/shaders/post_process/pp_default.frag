#version 330

//In
in vec2 screenCoord;

//Uniforms
uniform sampler2D sceneColor;

uniform float chromaAbbIntensity = 0.0;
uniform float vignetteIntensity = 1.4;

uniform float fadeToBlack = 0.0;

//Out
layout(location = 0) out vec3 FragmentColor;

void main() {
	//Distance to the center of the screen
	float distToCenter = length(screenCoord-vec2(0.5,0.5));
	
	//Chromatic aberration
	vec2 redUV = (((screenCoord*2.0)+1.0 + ( distToCenter*-chromaAbbIntensity*(screenCoord-0.5) ))-1.0)*0.5;
	vec2 greenUV = (((screenCoord*2.0)+1.0 + ( distToCenter*-(chromaAbbIntensity*0.35)*(screenCoord-0.5) ))-1.0)*0.5;
	
	float red = texture(sceneColor, redUV).r;
	float green = texture(sceneColor, greenUV).g;
	float blue = texture(sceneColor, screenCoord).b;
	
	//Vignette
	float vignette = 1.0-(distToCenter*distToCenter*distToCenter*distToCenter*vignetteIntensity);
	
	FragmentColor = vec3(red, green, blue)*vec3(vignette);
	
	FragmentColor = mix( FragmentColor, vec3(0.0, 0.0, 0.0), fadeToBlack );
}
