#version 400 core

in vec2 pass_textureCoords;
in float vis;

out vec4 out_Color;

uniform sampler2D textureSampler;
uniform int brightness;
//uniform vec3 skyColor;

const vec3 skyColor = vec3(0.737, 0.823f, 0.956);

void main(void){
    float level = float(brightness + 1) / 16;
    out_Color = vec4(level, level, level, 0) * texture(textureSampler, pass_textureCoords);
    out_Color = mix(vec4(skyColor, 1.0), out_Color, vis);
}