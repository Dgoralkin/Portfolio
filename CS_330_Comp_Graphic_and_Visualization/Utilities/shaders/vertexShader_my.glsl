#version 330 core

// Inputs from VBO
layout(location = 0) in vec3 aPos;
layout(location = 1) in vec3 aNormal;
layout(location = 2) in vec2 aTexCoord;

// Outputs to fragment shader
out vec3 FragPos;
out vec3 Normal;
out vec2 TexCoord;

// Uniforms
uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main()
{
    // Calculate vertex position in world space
    vec4 worldPos = model * vec4(aPos, 1.0);
    FragPos = vec3(worldPos);

    // Transform the normal correctly (especially if model is non-uniformly scaled)
    Normal = mat3(transpose(inverse(model))) * aNormal;

    // Pass through texture coordinate
    TexCoord = aTexCoord;

    // Final position in clip space
    gl_Position = projection * view * worldPos;
}
