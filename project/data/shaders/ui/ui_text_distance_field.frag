#version 150

//In
in vec2 texCoord;

//Uniforms
uniform sampler2D distanceField;
uniform vec4 baseColor = vec4(1.0, 1.0, 1.0, 1.0);
uniform float thickness = .25;

//Out
out vec4 FragmentColor;

void main() {
    vec4 textureSample = texture(distanceField, texCoord);
    
    if(textureSample.r <= 1-thickness) discard;

    FragmentColor = baseColor;
}
