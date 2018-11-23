#version 150

//In
in vec2 texCoord;

//Uniforms
uniform sampler2D baseColor;
uniform vec4 baseColorMod = vec4(1.0, 1.0, 1.0, 1.0);

//Out
out vec4 FragmentColor;

void main() {
    FragmentColor = texture(baseColor, texCoord)*baseColorMod;
}
