#version 150

//In
in vec2 texCoord;
in vec3 normalCamSpaceIn;
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
	//Two-sided lighting
	vec3 normalCamSpace = normalCamSpaceIn;
	if(!gl_FrontFacing)
		normalCamSpace *= -1.0;

    vec4 textureSample = texture(baseColor, texCoord);
    
    if(textureSample.a <= 0) discard;

	float lightAmount = (dot(normalCamSpace, directionalLightDir)+1.0)/2.0;
	
	lightAmount = mix( 0.2, 1.5, lightAmount );

    //FragmentColor = textureSample*baseColorMod;
    FragmentColor = vec4( (ambient+textureSample.rgb)*directionalLightColor*lightAmount, 1.0 );
    
    //Fog
    vec4 fogColor = vec4(directionalLightColor, 1.0);
	float fogAmount = clamp( ( gl_FragCoord.z/gl_FragCoord.w )/3000, 0.0, 0.9 );
	fogAmount = mix(fogAmount, fogAmount*fogAmount, 0.5);
    
    FragmentColor = mix(FragmentColor, fogColor, fogAmount);
    
    //FragmentColor = vec4( (normalCamSpace+1.0)/2.0, 1.0 ); //Normal viewer
}

