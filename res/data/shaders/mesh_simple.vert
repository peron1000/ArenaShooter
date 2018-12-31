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
out vec2 texCoord;
out vec3 normalCamSpace;
out vec3 lightDirection;

void main() {
    mat4 viewModel = view * model;
    mat4 mvp = projection * viewModel;
    gl_Position = mvp * vec4(position, 1.0);
    texCoord = uv;
    normalCamSpace = normalize( ( viewModel * vec4(normal, 0.0) ).xyz );
    lightDirection = normalize( (view * vec4(0.45, -0.8, -0.3, 0.0)).xyz );
}
