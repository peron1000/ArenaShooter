#version 150

//In
in vec2 texCoord;

//Uniforms
uniform sampler2D image;
uniform sampler2D shadowMask;

//Out
out vec4 FragmentColor;

void main() {
    vec4 mask = texture(shadowMask, texCoord);

    if(mask.a <= 0) discard;

    vec4 thumbnail = texture(image, texCoord);

    FragmentColor = thumbnail*mask;
}
