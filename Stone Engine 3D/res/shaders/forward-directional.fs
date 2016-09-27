#version 120

#include "shaders\lighting.glh"

uniform DirectionalLight directionalLight;
#uniset directionalLight rendering:directional_light

vec4 calcLightingEffect(vec3 normal, vec3 worldPos)
{
	return calcDirectionalLight(directionalLight, normal, worldPos);
}

#include "shaders\lightingMain.glh"