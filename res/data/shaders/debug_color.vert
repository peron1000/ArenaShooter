#version 150

//In
in vec3 position;
in vec2 uv;
in vec3 normal;

//Uniforms
uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

//Out

void main() {
    mat4 viewModel = view * model;
    mat4 mvp = projection * viewModel;
    gl_Position = mvp * vec4(position, 1.0);
    gl_Position.z = 0.0;
}
