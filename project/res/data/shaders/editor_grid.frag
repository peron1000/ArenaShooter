#version 150

//In
in vec2 texCoord;
in vec3 worldPosition;

//Uniforms
uniform float lineThickness = 0.1;

//Out
out vec4 FragmentColor;

void main() {
    float opacity = 1-(length( vec2(0.5, 0.5)-texCoord )*2.0);

    float gridValue = 0.0;

    if( fract(worldPosition.x) < lineThickness || fract(worldPosition.y) < lineThickness ) //1
        gridValue = 0.4;
    if( fract(worldPosition.x*0.1) < lineThickness*.17 || fract(worldPosition.y*0.1) < lineThickness*.17 ) //10
        gridValue = 0.75;

    opacity *= gridValue;

    vec3 color = mix( vec3(1.0, 1.0, 0.0), vec3(1.0, 1.0, 1.0), min(1, length(worldPosition*0.1)) );
    
    if(worldPosition.y >= 0 && worldPosition.y*0.1 < lineThickness*.17)
    	color = vec3(1.0, 0.0, 0.0);
    else if(worldPosition.x >= 0 && worldPosition.x*0.1 < lineThickness*.17)
    	color = vec3(0.0, 1.0, 0.0);

    FragmentColor = vec4(color, opacity);
}
