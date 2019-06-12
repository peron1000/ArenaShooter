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
out vec3 normalCamSpaceIn;

void main() {
    mat4 modelView = view * model;
    mat4 mvp = projection * modelView;
    gl_Position = mvp * vec4(position, 1.0);
    texCoord = uv;
    normalCamSpaceIn = normalize( transpose(inverse(mat3(modelView))) * normal );
}
