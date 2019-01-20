#version 330

//In
in vec2 screenCoord;

//Uniforms
uniform sampler2D sceneColor;

//Out
layout(location = 0) out vec4 FragmentColor;

void main() {

	vec3 sample = texture(sceneColor, screenCoord).rgb;
	
	float average = (sample.r+sample.g+sample.b)/3;

	if(average > 0.3)
		FragmentColor = vec4(1.0);
	else
		FragmentColor = vec4(0.0, 0.0, 0.0, 1.0);
	
}
