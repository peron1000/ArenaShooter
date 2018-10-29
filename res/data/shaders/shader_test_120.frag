#version 120

//In
varying vec2 texCoord;

//Uniforms
uniform sampler2D baseColor;
uniform float colorMod;


void main() {
    //Wobble effect
    float uvOffset = sin( (colorMod+texCoord.t)*8 )-.5;
    vec2 texCoordFinal = texCoord;
    texCoordFinal.s = texCoord.s + uvOffset*.01;
    
    gl_FragColor = texture2D(baseColor, texCoordFinal)*vec4(1.0, colorMod, colorMod, 1.0);
}
