#version 330

//In
in vec2 uv;

//Out
out vec2 screenCoord;

void main() {
    gl_Position = vec4((uv-0.5)*2.0, 1.0, 1.0);
    screenCoord = uv;
}
