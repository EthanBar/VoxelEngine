#version 400 core

in vec2 pass_textureCoords;

out vec4 out_Color;

uniform sampler2D textureSampler;
uniform int brightness;

void main(void){
    float level = float(brightness + 1) / 16;
    out_Color = vec4(level, level, level, 0) * texture(textureSampler, pass_textureCoords);
}