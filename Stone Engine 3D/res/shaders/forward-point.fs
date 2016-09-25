#version 120

#include "shaders\lighting.glh"

uniform PointLight R_CURRENTLIGHT_pointLight;

vec4 calcLightingEffect(vec3 normal, vec3 worldPos)
{
	return calcPointLight(R_CURRENTLIGHT_pointLight, normal, worldPos);
}

#include "shaders\lightingMain.glh"