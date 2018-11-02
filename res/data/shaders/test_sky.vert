#version 150

//In
in vec2 uv;

//Out
out vec3 color;

void main() {
    gl_Position = vec4((uv*2.0)-1.0, 1.0, 1.0);

    if( uv.t >= 1.0 ) {
        color = vec3( 0.34901960784, 0.13725490196, 0.48235294118 );
    } else {
        color = vec3( 0.996, 0.9098, 0.003922 );
    }
}
