#version 150

//In
in vec2 texCoord;

//Uniforms
uniform sampler2D baseColor;

uniform float colorMod;

//Out
out vec4 FragmentColor;

void main() {
    //Wobble effect
    float uvOffset = sin( (colorMod+texCoord.t)*8 )-.5;
    vec2 texCoordFinal = texCoord;
    texCoordFinal.s = texCoord.s + uvOffset*.01;
    
    //Sample texture
    vec4 texureSample = texture(baseColor, texCoordFinal);

    //Alpha masking
    //if( texureSample.a < 0.8 ) discard;

    //Color effect
    float colorShift = texCoord.s+(texCoord.t*2.2);
    vec4 color = vec4( abs(sin( colorMod*3.0+colorShift )), abs(sin( colorMod*3.0+colorShift+0.7 )), abs(sin( colorMod*3.0+colorShift+1.6 )), 1.0 );


    FragmentColor = texureSample*color;
}
