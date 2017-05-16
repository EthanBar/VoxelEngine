#version 400 core

in vec3 position;
in vec2 textureCoords;
//in vec3 normals;

out vec2 pass_textureCoords;
//out vec3 surfaceNormal;
//out vec3 toLightVector;

uniform mat4 transformationMatrix; // Move an object
uniform mat4 projectionMatrix; // 3D stuff
uniform mat4 viewMatrix; // Move the camera
//uniform vec3 lightPosition;

void main(void){
    vec4 worldPosition = transformationMatrix * vec4(position, 1.0);
	gl_Position = projectionMatrix * viewMatrix * worldPosition;
    pass_textureCoords = textureCoords;

//    surfaceNormal = (transformationMatrix * vec4(normals, 1.0)).xyz;
//    surfaceNormal = (transformationMatrix * vec4(1.0)).xyz;
//    toLightVector = lightPosition - worldPosition.xyz;
}