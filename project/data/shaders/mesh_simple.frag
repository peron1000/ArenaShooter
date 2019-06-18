#version 150

struct lightStruct {
  vec3 position;
  float radius;
  vec3 color;
};

//In
in vec2 texCoord;
in vec3 normalCamSpaceIn;
in vec3 worldNormalIn;
in vec3 worldPosition;
in float fogAmount;

//Uniforms
uniform float editorFilter = 0.0;
uniform sampler2D baseColor;
uniform vec4 baseColorMod = vec4(1.0, 1.0, 1.0, 1.0);
uniform vec3 fogColor = vec3(0.929, 0.906, 0.753);

uniform vec3 ambient = vec3(0.063, 0.078, 0.078);
uniform int activeLights = 0;
uniform lightStruct lights[16];

//Out
out vec4 FragmentColor;

vec3 directionalLight(lightStruct light, vec3 worldNormal) {
    float dotN = dot(light.position*-1, worldNormal);
    
    return light.color * max(dotN, 0);
}

vec3 pointLight(lightStruct light, vec3 worldNormal) {
    vec3 direction = worldPosition - light.position;
    float distance = length(direction);
    
    float intensity = 1-min( distance/light.radius, 1 );
    
    intensity *= max( 1, dot(direction*-1, worldNormal) );
    
    return light.color*( intensity );
}

void main() {
	//Two-sided lighting
	vec3 normalCamSpace = normalCamSpaceIn;
	vec3 worldNormal = worldNormalIn;
	if(!gl_FrontFacing) {
		normalCamSpace *= -1.0;
		worldNormal *= -1.0;
	}

    vec4 textureSample = texture(baseColor, texCoord);
    
    if(textureSample.a <= 0) discard;

    //Lighting
    vec3 lightColor = vec3(0, 0, 0);
    for(int i=0; i<activeLights; i++) {
        if(lights[i].radius > 0)
            lightColor += pointLight(lights[i], worldNormal);
        else
            lightColor += directionalLight(lights[i], worldNormal);
    }
    
    //Apply ambient lighting
    lightColor = max(lightColor, ambient);
    
    //Apply lighting
    FragmentColor = textureSample * vec4(lightColor, 1.0);
    
    //Fog
    FragmentColor = mix(FragmentColor, vec4(fogColor, FragmentColor.a), fogAmount);

    //FragmentColor = vec4( (normalCamSpace+1.0)/2.0, 1.0 ); //Normal viewer
    
    //Editor filter
    FragmentColor = mix(FragmentColor, vec4(vec3(1.0, 0.964, .839), FragmentColor.a), editorFilter );
}

