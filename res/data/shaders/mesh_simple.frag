#version 150

//In
in vec2 texCoord;
in vec3 normalCamSpace;
in vec3 ambient;
in vec3 directionalLightDir;
in vec3 directionalLightColor;
in struct Light {
  vec3 position;
  float radius;
  vec3 color;
} light;

//Uniforms
uniform sampler2D baseColor;
uniform vec4 baseColorMod = vec4(1.0, 1.0, 1.0, 1.0);

//Out
out vec4 FragmentColor;

void main() {
    vec4 textureSample = texture(baseColor, texCoord);
    
    if(textureSample.a <= 0) discard;

	float lightAmount = (dot(normalCamSpace, directionalLightDir)+1.0)/2.0;
	
	lightAmount = mix( 0.2, 1.5, lightAmount );
	
    //FragmentColor = textureSample*baseColorMod;
    FragmentColor = vec4( (ambient+textureSample.rgb)*directionalLightColor*lightAmount, 1.0 );
}
