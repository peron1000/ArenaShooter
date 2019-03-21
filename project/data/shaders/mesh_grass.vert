#version 150

//In
in vec3 position;
in vec2 uv;
in vec3 normal;

//Uniforms
uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;
uniform int time;

//Out
out vec2 texCoord;
out vec3 normalCamSpaceIn;
out vec3 ambient;
out vec3 directionalLightDir;
out vec3 directionalLightColor;
out struct Light {
  vec3 position;
  float radius;
  vec3 color;
} light;

void main() {
    mat4 viewModel = view * model;
    mat4 mvp = projection * viewModel;
    
    //Wind
    float xOffset = 0.0;
    if(uv.y < 0.1) xOffset = sin(0.02*position.x+time*.0032+cos(time*0.001))*2.5;
    vec3 position_wind = vec3( position.x+xOffset, position.y, position.z );
    
    gl_Position = mvp * vec4(position_wind, 1.0);
    texCoord = uv;
    normalCamSpaceIn = normalize( ( viewModel * vec4(0.0, -1.0, 0.0, 0.0) ).xyz ); //Custom normal
    
    ambient = vec3(0.063, 0.078, 0.078);
    
    directionalLightDir = normalize( (view * vec4(0.45, -0.8, -0.3, 0.0)).xyz );
    directionalLightColor = vec3(0.929, 0.906, 0.753);
}
