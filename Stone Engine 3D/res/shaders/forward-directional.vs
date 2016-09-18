#version 120

attribute vec3 position;
attribute vec2 texCoord;
attribute vec3 normal;

varying vec2 texCoord0;
varying vec3 normal0;
varying vec3 worldPos0;

uniform mat4 T_WORLDMATRIX_MODEL;
uniform mat4 T_PROJECTEDMATRIX_MVP;

void main()
{
	gl_Position = T_PROJECTEDMATRIX_MVP * vec4(position, 1.0);
	texCoord0 = texCoord;
	normal0 = (T_WORLDMATRIX_MODEL * vec4(normal, 0.0)).xyz;
	worldPos0 = (T_WORLDMATRIX_MODEL * vec4(position, 1.0)).xyz;
}