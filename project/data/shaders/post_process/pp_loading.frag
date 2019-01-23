#version 330

//In
in vec2 screenCoord;

//Uniforms
uniform sampler2D sceneColor;

//Out
layout(location = 0) out vec3 FragmentColor;

void main() {

	vec3 sample = texture(sceneColor, screenCoord).rgb;
	
	float average = (sample.r+sample.g+sample.b)/3;

	if(average > 0.3)
		FragmentColor = vec3(1.0);
	else
		FragmentColor = vec3(0.0, 0.0, 0.0);
	
}
