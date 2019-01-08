#version 150

//In
in vec3 position;
in vec2 uv;

//Uniforms
uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

//Out
out vec2 texCoord;

void main() {
    mat4 mvp = projection * view * model;
    gl_Position = mvp * vec4(position, 1.0);
    texCoord = uv;
}
