#version 120
#include "shaders\lighting.glh"

varying vec2 texCoord0;
varying vec3 normal0;
varying vec3 worldPos0;

uniform sampler2D R_SAMPLER2D_diffuse;
uniform PointLight R_CURRENTLIGHT_pointLight;

void main()
{ 	
	gl_FragColor = texture2D(R_SAMPLER2D_diffuse, texCoord0.xy) * 
		calcPointLight(R_CURRENTLIGHT_pointLight, normalize(normal0), worldPos0);
}