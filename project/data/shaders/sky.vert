#version 150

//In
in vec2 uv;

//Uniforms
uniform vec3 colorBot = vec3( 0.996, 0.9098, 0.003922 );
uniform vec3 colorTop = vec3( 0.34901960784, 0.13725490196, 0.48235294118 );

//Out
out vec3 color;

void main() {
    gl_Position = vec4((uv*2.0)-1.0, 1.0, 1.0);

    if( uv.t <= 0.5 ) {
        color = colorBot;
    } else {
        color = colorTop;
    }
}
