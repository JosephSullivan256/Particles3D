#version 330 core
layout (location = 0) in vec2 inPos;

out vec2 angles;

void main()
{
	const float PI = 3.1415926535897932384626;
	gl_Position = vec4(inPos,0.0,1.0);
	float fov = (75.0/360.0)*(2.0*PI);
	angles = vec2(inPos.x*fov/2.0,inPos.y*fov/2.0);
}