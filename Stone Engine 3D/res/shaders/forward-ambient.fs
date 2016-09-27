#version 120

varying vec2 texCoord0;

uniform vec3 ambient;
uniform sampler2D diffuse;

#uniset ambient rendering:ambient:vec3
#uniset diffuse material:diffuse:texture

void main()
{
	gl_FragColor = texture2D(diffuse, texCoord0.xy) * vec4(ambient, 1);
}
