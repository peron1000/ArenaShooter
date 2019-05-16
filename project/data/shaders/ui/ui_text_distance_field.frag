#version 150

//In
in vec2 texCoord;

//Uniforms
uniform sampler2D distanceField;
uniform vec4 baseColor = vec4(1.0, 1.0, 1.0, 1.0);
uniform float thickness = .25;
uniform vec4 shadowColor = vec4(0.0, 0.0, 0.0, 1.0);
uniform float shadowThickness = .5;

//Out
out vec4 FragmentColor;

void main() {
    vec4 textureSample = texture(distanceField, texCoord);

    //Shadow
    vec4 color = shadowColor;
    color.a = shadowColor.a * smoothstep(0.0, shadowThickness, textureSample.r);

    
    //Solid color
    if(textureSample.r < thickness) color = baseColor;

    FragmentColor = color;
}
