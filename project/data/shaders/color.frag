#version 150

//Uniforms
uniform vec4 baseColor = vec4(1.0, 1.0, 1.0, 1.0);

//Out
out vec4 FragmentColor;

void main() {
    FragmentColor = baseColor;
}

