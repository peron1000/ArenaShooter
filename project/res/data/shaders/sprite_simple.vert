#version 150

//In
in vec3 position;
in vec2 uv;

//Uniforms
uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;
uniform float fogDistance = 3000;

//Out
out vec2 texCoord;
out float fogAmount;

void main() {
    mat4 mvp = projection * view * model;
    gl_Position = mvp * vec4(position, 1.0);
    texCoord = uv;
    
    //Fog
    fogAmount = clamp( gl_Position.z/fogDistance, 0.0, 0.9 );
    fogAmount = fogAmount*fogAmount;
}
