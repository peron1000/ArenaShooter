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
out vec3 worldPosition;
out vec3 worldNormalIn;

void main() {
    mat4 viewModel = view * model;
    mat4 mvp = projection * viewModel;
    
    //Wind
    float xOffset = 0.0;
    if(uv.y < 0.1) xOffset = sin(0.02*position.x+time*.0032+cos(time*0.001))*2.5;
    vec3 position_wind = vec3( position.x+xOffset, position.y, position.z );
    
    gl_Position = mvp * vec4(position_wind, 1.0);
    texCoord = uv;
    worldNormalIn = normalize( (inverse(model) * vec4(0.0, -1.0, 0.0, 0.0)).xyz );
    normalCamSpaceIn = normalize( ( viewModel * vec4(0.0, -1.0, 0.0, 0.0) ).xyz ); //Custom normal
    worldPosition = (model * vec4(position, 1.0)).xyz;
}
