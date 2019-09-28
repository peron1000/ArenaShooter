#version 150

//In
in vec2 texCoord;

//Uniforms
uniform sampler2D image;
uniform sampler2D shadowMask;

//Out
out vec4 FragmentColor;

void main() {
    FragmentColor = texture(image, texCoord) * texture(shadowMask, texCoord);
}
