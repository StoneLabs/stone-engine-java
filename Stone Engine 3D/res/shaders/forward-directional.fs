#version 120

#include "shaders\lighting.glh"

uniform DirectionalLight R_CURRENTLIGHT_directionalLight;

vec4 calcLightingEffect(vec3 normal, vec3 worldPos)
{
	return calcDirectionalLight(R_CURRENTLIGHT_directionalLight, normal, worldPos);
}

#include "shaders\lightingMain.glh"