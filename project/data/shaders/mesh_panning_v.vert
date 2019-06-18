#version 150

// In
in vec3 position;
in vec2 uv;
in vec3 normal;

// Uniforms
uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;
uniform float fogDistance = 3000;
uniform int time = 0;

// Out
out vec2 texCoord;
out vec3 normalCamSpaceIn;
out vec3 worldPosition;
out vec3 worldNormalIn;
out float fogAmount;

void main() {
    mat4 modelView = view * model;
    mat4 mvp = projection * modelView;
    gl_Position = mvp * vec4(position, 1.0);

    // Panning
    texCoord = vec2(texCoord.x, texCoord.y-(time*0.0002));

    // worldNormalIn = normalize( (inverse(mat3(model))) * normal );
    worldNormalIn = normalize( (model * vec4(normal, 0.0)).xyz );
    //normalCamSpaceIn = normalize( (inverse(mat3(modelView))) * normal );
    normalCamSpaceIn = normalize( (modelView * vec4(normal, 0.0)).xyz );
    worldPosition = (model * vec4(position, 1.0)).xyz;
    
    // Fog
    fogAmount = clamp( gl_Position.z/fogDistance, 0.0, 0.9 );
    fogAmount = fogAmount*fogAmount;
}
