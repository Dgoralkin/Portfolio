#version 330 core

in vec3 FragPos;        // World-space position of the fragment
in vec3 Normal;         // Normal vector passed from vertex shader
in vec2 TexCoord;       // Optional if you use textures

out vec4 FragColor;

// Material properties
struct Material {
    vec3 ambientColor;
    vec3 diffuseColor;
    vec3 specularColor;
    float shininess;
    float ambientStrength;
};

uniform Material material;

// Light properties
struct Light {
    vec3 position;
    vec3 ambientColor;
    vec3 diffuseColor;
    vec3 specularColor;
    float specularIntensity;
};

uniform Light light;
uniform vec3 viewPos; // Camera position

void main()
{
    // Normalize input
    vec3 norm = normalize(Normal);
    vec3 lightDir = normalize(light.position - FragPos);
    vec3 viewDir = normalize(viewPos - FragPos);

    // ----- Ambient -----
    vec3 ambient = light.ambientColor * material.ambientColor * material.ambientStrength;

    // ----- Diffuse -----
    float diff = max(dot(norm, lightDir), 0.0);
    vec3 diffuse = diff * light.diffuseColor * material.diffuseColor;

    // ----- Blinn-Phong Specular -----
    vec3 halfwayDir = normalize(lightDir + viewDir);
    float spec = pow(max(dot(norm, halfwayDir), 0.0), material.shininess);
    vec3 specular = spec * light.specularColor * material.specularColor * light.specularIntensity;

    // Final color
    vec3 result = ambient + diffuse + specular;
    FragColor = vec4(result, 1.0);
}
