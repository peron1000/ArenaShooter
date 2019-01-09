#version 150

//In
in vec3 color;

//Out
out vec4 FragmentColor;

void main() {
    FragmentColor = vec4(color, 1.0);
}
