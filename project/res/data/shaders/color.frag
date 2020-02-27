#version 150

//Uniforms
uniform float editorFilter = 0.0;
uniform vec4 baseColor = vec4(1.0, 1.0, 1.0, 1.0);

//Out
out vec4 FragmentColor;

void main() {
    FragmentColor = baseColor;
    
    //Editor filter
    FragmentColor = mix(FragmentColor, vec4(vec3(1.0, 0.964, .839), FragmentColor.a), editorFilter );
}

