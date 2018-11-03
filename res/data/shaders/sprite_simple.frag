#version 150

//In
in vec2 texCoord;

//Uniforms
uniform sampler2D baseColor;

//Out
out vec4 FragmentColor;

void main() {
    FragmentColor = texture(baseColor, texCoord);
}
