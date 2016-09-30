#version 120

attribute vec3 position;
attribute vec2 texCoord;
attribute vec3 normal;
attribute vec3 tangent;

varying vec2 texCoord0;
varying vec3 worldPos0;
varying mat3 tbnMatrix0;

uniform mat4 T_WORLDMATRIX; //ALIAS MODEL MATRIX
#uniset T_WORLDMATRIX transform:world_matrix

uniform mat4 T_PROJECTEDMATRIX; //ALIAS PROJECTED MATRIX
#uniset T_PROJECTEDMATRIX transform:projected_matrix

void main()
{
	gl_Position = T_PROJECTEDMATRIX * vec4(position, 1.0);
	texCoord0 = texCoord;
	worldPos0 = (T_WORLDMATRIX * vec4(position, 1.0)).xyz;
	 
	vec3 n = normalize((T_WORLDMATRIX * vec4(normal, 0.0)).xyz);
	vec3 t = normalize((T_WORLDMATRIX * vec4(tangent, 0.0)).xyz);
	t = normalize(t - dot(t, n) * n);
	    
	vec3 biTangent = cross(t, n);
	tbnMatrix0 = mat3(t, biTangent, n);
}