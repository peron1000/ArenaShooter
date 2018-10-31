#version 150

//In
in vec2 texCoord;

//Uniforms
uniform sampler2D baseColor;

uniform float colorMod;

void main() {
    //Color effect
    float colorShift = texCoord.s+(texCoord.t*2.2);
    vec4 color = vec4( abs(sin( colorMod*3.0+colorShift )), abs(sin( colorMod*3.0+colorShift+0.7 )), abs(sin( colorMod*3.0+colorShift+1.6 )), 1.0 );

    //Wobble effect
    float uvOffset = sin( (colorMod+texCoord.t)*8 )-.5;
    vec2 texCoordFinal = texCoord;
    texCoordFinal.s = texCoord.s + uvOffset*.01;

    gl_FragColor = texture2D(baseColor, texCoordFinal)*color;
}
