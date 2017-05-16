#version 400 core

in vec3 position;
in vec2 textureCoords;
in vec3 offset;
//in vec3 normals;

out vec2 pass_textureCoords;
out float vis;
//out vec3 surfaceNormal;
//out vec3 toLightVector;

uniform mat4 transformationMatrix; // Move an object
uniform mat4 projectionMatrix; // 3D stuff
uniform mat4 viewMatrix; // Move the camera
uniform float renderDistance;
//uniform vec3 lightPosition;

const float density = 1;

void main(void){
    vec4 worldPosition = transformationMatrix * vec4(position, 1.0);
    vec4 posRealativeToCamera = viewMatrix * worldPosition;
	gl_Position = projectionMatrix * posRealativeToCamera;
    pass_textureCoords = textureCoords;

    float distance = length(posRealativeToCamera.xyz);
    float gradient = 1 + (renderDistance / 16);
    vis = 1;
    if (distance > (renderDistance) - 20) {
//        vis = exp(-pow((distance * density), gradient));
        vis = ((renderDistance) - distance - 1) / 20;
        vis = clamp(vis, 0.0, 1.0);
    }

//    surfaceNormal = (transformationMatrix * vec4(normals, 1.0)).xyz;
//    surfaceNormal = (transformationMatrix * vec4(1.0)).xyz;
//    toLightVector = lightPosition - worldPosition.xyz;
}