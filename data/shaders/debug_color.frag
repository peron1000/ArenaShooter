#version 150

//Uniforms
uniform vec4 color = vec4(1.0, 1.0, 1.0, 1.0);

//Out
out vec4 FragmentColor;

void main() {
    FragmentColor = color;
}
