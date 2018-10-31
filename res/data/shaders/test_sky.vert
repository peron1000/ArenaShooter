#version 150

//In
in vec3 position;
in vec2 uv;

//Uniforms
uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

//Out
out vec3 color;

void main() {
    mat4 mvp = projection * view * model;
    gl_Position = mvp * vec4(position, 1.0);

    if( uv.t >= 1.0 ) {
        color = vec3( 0.34901960784, 0.13725490196, 0.48235294118 );
    } else {
        color = vec3( 0.996, 0.9098, 0.003922 );
    }
}
