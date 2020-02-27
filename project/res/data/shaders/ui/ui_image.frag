#version 150

//In
in vec2 texCoord;

//Uniforms
uniform sampler2D image;
uniform vec4 color = vec4(1.0, 1.0, 1.0, 1.0);

//Out
out vec4 FragmentColor;

void main() {
    vec4 textureSample = texture(image, texCoord);
    
    if(textureSample.a <= 0) discard;

    FragmentColor = textureSample*color;
}
