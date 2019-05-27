#version 150

//In
in vec2 texCoord;

//Uniforms
uniform sampler2D distanceField;
uniform vec4 baseColor = vec4(1.0, 1.0, 1.0, 1.0);
uniform float thickness = 0.25;
uniform vec4 shadowColor = vec4(0.0, 0.0, 0.0, 1.0);
uniform float shadowThickness = 0.01;

//Out
out vec4 FragmentColor;

void main() {
    vec4 textureSample = texture(distanceField, texCoord);

    //Shadow
    vec4 color = shadowColor;
    color.a = shadowColor.a * (1-smoothstep(0.0, shadowThickness, 1-textureSample.r));

    
    //Solid color
    if(textureSample.r > 1-thickness) color = baseColor;

    FragmentColor = color;
}
