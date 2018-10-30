#version 120

//In
attribute vec3 position;
attribute vec2 uv;

//Uniforms
uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

//Out
varying vec2 texCoord;

void main() {
    mat4 mvp = projection * view * model;
    //gl_Position = mvp * vec4(position, 1.0);
    gl_Position = gl_ModelViewProjectionMatrix * vec4(position, 1.0);
    texCoord = uv;
    //texCoord = gl_MultiTexCoord0.st;
}
