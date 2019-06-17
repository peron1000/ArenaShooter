#version 150

//In
in vec2 texCoord;
in vec3 worldPosition;

//Out
out vec4 FragmentColor;

void main() {
    float opacity = 1-(length( vec2(0.5, 0.5)-texCoord )*2.0);

    float gridValue = 0.0;

    if( fract(worldPosition.x) < .1 || fract(worldPosition.y) < .1 ) //1
        gridValue = 0.4;
    if( fract(worldPosition.x*0.1) < .015 || fract(worldPosition.y*0.1) < .015 ) //10
        gridValue = 0.7;

    opacity *= gridValue;

    vec3 color = mix( vec3(1.0, 0.0, 0.0), vec3(1.0, 1.0, 1.0), min(1, length(worldPosition*0.1)) );

    FragmentColor = vec4(color, opacity);
}
