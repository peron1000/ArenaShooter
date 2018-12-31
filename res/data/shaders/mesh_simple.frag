#version 150

//In
in vec2 texCoord;
in vec3 normalCamSpace;
in vec3 lightDirection;

//Uniforms
uniform sampler2D baseColor;
uniform vec4 baseColorMod = vec4(1.0, 1.0, 1.0, 1.0);

//Out
out vec4 FragmentColor;

void main() {
    vec4 textureSample = texture(baseColor, texCoord);
    
    if(textureSample.a <= 0) discard;

	float lightAmount = (dot(normalCamSpace, lightDirection)+1.0)/2.0;
	
	lightAmount = mix( 0.25, 1.3, lightAmount );
	
    //FragmentColor = textureSample*baseColorMod;
    FragmentColor = vec4( vec3(0.929, 0.906, 0.753)*lightAmount, 1.0 );
}
