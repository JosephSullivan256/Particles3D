#version 330 core
out vec4 FragColor;

in vec2 angles;

uniform vec3[200] positions;
uniform mat4 transform;

const float PI = 3.1415926535897932364;

vec3 hsv2rgb(vec3 c)
{
	vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);
	vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);
	return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);
}

float arctanApprox(float x)
{
	if(x>0) return -(PI/2.0)/(x+1)+(PI/2.0);
	return (PI/2.0)/(-x+1)-(PI/2.0);
	//return atan(x);
}

float brightnessFunction(float x)
{
	return x;
}

void main()
{
	const float PI = 3.1415926535897932384626;
	
	float count = 200;
	float brightness = 0.0;
	
	float xsin = sin(angles.x); //azimuth
	float xcos = cos(angles.x);
	float ysin = sin(angles.y); //zenith
	float ycos = cos(angles.y);
	
	vec3 direction = vec3(xsin*ycos, ysin, xcos*ycos); //must be normalized :D
	
	float a = 0.03;
	float a2 = dot(a,a);
	
	//float d2 = dot(direction,direction);
	
	for(int i = 0; i < count; i++){
		vec3 p = (transform*vec4(positions[i],1.0)).xyz;
		//vec3 p = positions[i]+vec3(0,0,2);
		float p2 = dot(p,p);
		float dp = dot(direction,p);
		//float k = d2*sqrt(((p2+a2)/d2)-(dp*dp/(d2*d2)));
		float k = sqrt((p2+a2)-(dp*dp));
		float fx = arctanApprox(dp/(k*k));
		brightness+=(a2/k)*(PI/2.0 + fx);
	}
	
	brightness*=12;
	
	//if(brightness < 1) brightness = 0;
	
	FragColor = vec4(hsv2rgb(vec3(brightness,1.0,1.0)), 1.0);
    //FragColor = vec4(vec3(1.0,0.2,0.1)*brightness, 1.0);
    //FragColor = vec4(vec3(1.0,0.2,0.1)*brightnessFunction(brightness), 1.0);
}