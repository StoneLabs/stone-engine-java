#version 120

#include "shaders\lighting.glh"

uniform SpotLight R_CURRENTLIGHT_SPOTLIGHT;

vec4 calcLightingEffect(vec3 normal, vec3 worldPos)
{
	return calcSpotLight(R_CURRENTLIGHT_SPOTLIGHT, normal, worldPos);
}

#include "shaders\lightingMain.glh"