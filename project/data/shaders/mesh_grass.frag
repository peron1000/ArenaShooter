#version 150

struct dirLightStruct {
  vec3 color;
  vec3 direction;
};
struct pointLightStruct {
  vec3 color;
  vec3 position;
  float radius;
};
struct spotLightStruct {
  vec3 color;
  vec3 position;
  float radius;
  vec3 direction;
  float angle;
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
uniform int activeLightsDir = 0;
uniform dirLightStruct lightsDir[2];
uniform int activeLightsPoint = 0;
uniform pointLightStruct lightsPoint[8];
uniform int activeLightsSpot = 0;
uniform spotLightStruct lightsSpot[8];

//Out
out vec4 FragmentColor;

vec3 directionalLight(dirLightStruct light, vec3 worldNormal) {
    float dotN = dot(light.direction, worldNormal);
    
    return light.color * max(dotN, 0);
}

vec3 pointLight(pointLightStruct light, vec3 worldNormal) {
    vec3 direction = light.position - worldPosition;
    float distance = length(direction);
    
    float intensity = 1.0-min( distance/light.radius, 1.0 );
    
    intensity *= max( dot(direction, worldNormal), 0.0 );
    
    return light.color*( intensity );
}

vec3 spotLight(spotLightStruct light, vec3 worldNormal) {
    vec3 direction = light.position - worldPosition;
    float distance = length(direction);
    
    float intensity = 1.0-min( distance/light.radius, 1.0 );
    
    intensity *= max( dot(direction, worldNormal), 0.0 );
    
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
    for(int i=0; i<activeLightsDir; i++) {
        lightColor += directionalLight(lightsDir[i], worldNormal);
    }
    for(int i=0; i<activeLightsPoint; i++) {
        lightColor += pointLight(lightsPoint[i], worldNormal);
    }
    for(int i=0; i<activeLightsSpot; i++) {
        lightColor += spotLight(lightsSpot[i], worldNormal);
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

