#version 150

//In
in vec2 texCoord;

//Uniforms
uniform vec4 colorA = vec4(1.0, 1.0, 1.0, 1.0);
uniform vec4 colorB = vec4(0.0, 0.0, 0.0, 1.0);

//Out
out vec4 FragmentColor;

void main() {
    FragmentColor = mix(colorA, colorB, texCoord.x);
}
