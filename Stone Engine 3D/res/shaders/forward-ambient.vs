#version 120

attribute vec3 position;
attribute vec2 texCoord;

varying vec2 texCoord0;

uniform mat4 projectedMatrix;

#uniset projectedMatrix transform:projected_matrix

void main()
{
    gl_Position = projectedMatrix * vec4(position, 1.0);
    texCoord0 = texCoord;
}