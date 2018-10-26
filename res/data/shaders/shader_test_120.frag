#version 120

uniform sampler2D baseColor;

uniform float colorMod;

void main() {
    gl_FragColor = texture2D(baseColor, gl_TexCoord[0].st)*vec4(1.0, colorMod, colorMod, 1.0);
}
