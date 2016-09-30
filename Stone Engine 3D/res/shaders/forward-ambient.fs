#version 120
varying vec2 texCoord0;
varying vec3 worldPos0;
varying mat3 tbnMatrix0;

uniform vec3 R_ambient;
uniform vec3 C_eyePos;
uniform sampler2D diffuse;
uniform sampler2D dispMap;

uniform float dispMapScale;

#uniset R_ambient rendering:ambient:vec3
#uniset diffuse material:diffuse:texture
#uniset C_eyePos camera:position
#uniset dispMap material:dispMap:texture
#uniset dispMapScale material:dispMapScale:float

#include "shaders/sampling.glh"

void main()
{
	vec3 directionToEye = normalize(C_eyePos - worldPos0);
	vec2 texCoords = CalcParallaxTexCoords(dispMap, tbnMatrix0, directionToEye, texCoord0, dispMapScale, -dispMapScale/2);
	
	gl_FragColor = texture2D(diffuse, texCoords) * vec4(R_ambient, 1);
}