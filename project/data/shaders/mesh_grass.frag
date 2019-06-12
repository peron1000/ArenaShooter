#version 150

struct lightStruct {
  vec3 position;
  float radius;
  vec3 color;
};

//In
in vec2 texCoord;
in vec3 normalCamSpaceIn;

//Uniforms
uniform float editorFilter = 0.0;
uniform sampler2D baseColor;
uniform vec4 baseColorMod = vec4(1.0, 1.0, 1.0, 1.0);

uniform vec3 ambient = vec3(0.063, 0.078, 0.078);
uniform int activeLights = 0;
uniform lightStruct lights[16];

//Out
out vec4 FragmentColor;

vec3 computeLight(lightStruct light) {
    return vec3(1, 1, 1);
}

void main() {
	//Two-sided lighting
	vec3 normalCamSpace = normalCamSpaceIn;
	if(!gl_FrontFacing)
		normalCamSpace *= -1.0;

    vec4 textureSample = texture(baseColor, texCoord);
    
    if(textureSample.a <= 0) discard;

    //Lighting
    vec3 lightColor = vec3(0, 0, 0);
    for(int i=0; i<activeLights; i++) {
        lightColor += computeLight(lights[i]);
    }
    
    //Apply ambient lighting
    lightColor = max(lightColor, ambient);
    
    //Apply lighting
    FragmentColor = textureSample * vec4(lightColor, 1.0);
    
    //Fog
    //vec4 fogColor = vec4(directionalLightColor, 1.0);
	//float fogAmount = clamp( ( gl_FragCoord.z/gl_FragCoord.w )/3000, 0.0, 0.9 );
	//fogAmount = mix(fogAmount, fogAmount*fogAmount, 0.5);
    
    //FragmentColor = mix(FragmentColor, fogColor, fogAmount);
    
    //FragmentColor = vec4( (normalCamSpace+1.0)/2.0, 1.0 ); //Normal viewer
    
    //Editor filter
    FragmentColor = mix(FragmentColor, vec4(vec3(1.0, 0.964, .839), FragmentColor.a), editorFilter );
}

