#version 150

//In
in vec3 position;
in vec2 uv;

//Uniforms
uniform mat4 model;
uniform mat4 projection;

//Out
out vec2 texCoord;

void main() {
    mat4 mvp = projection * model;
    gl_Position = mvp * vec4(position, 1.0);
    gl_Position.z = 0.5;
    texCoord = uv;
}
