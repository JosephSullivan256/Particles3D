#version 330 core
#define COUNT 200

out vec4 FragColor;

in vec2 angles;

uniform vec3[COUNT] positions;
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

float sq(float x){
	return x*x;
}

float axisVisible(vec4 ori, vec4 axis, vec3 dir){
	vec3 v1 = vec3(-axis.x/dir.x,-axis.y/dir.y,-axis.z/dir.z);
	vec3 v2 = vec3(ori.x/dir.x,ori.y/dir.y,ori.z/dir.z);
	mat2 m1 = mat2(1,1,v1.x,v1.y);
	mat2 m2 = mat2(1,1,v1.x,v1.z);
	mat2 m3 = mat2(1,1,v1.y,v1.z);
	
	vec2 u1 = inverse(m1)*v2.xy;
	vec2 u2 = inverse(m2)*v2.xz;
	vec2 u3 = inverse(m3)*v2.yz;
	
	if((u1.x+u2.x+u3.x)/3.0 < 0.1) return 0;
	
	vec2 u12 = u2-u1;
	vec2 u13 = u3-u1;
	
	//if(abs(cross(vec3(u12,0),vec3(u13,0)).z)/2.0 < 0.1 && dot(u12,u13) < 0.5) return 1;
	//return 0;
	
	float proximity = 1/(abs(cross(vec3(u12,0),vec3(u13,0)).z)/2.0);
	
	return 0.01*proximity;
	
	/*float xm = (u1.x+u2.x+u3.x)/3.0;
	float ym = (u1.y+u2.y+u3.y)/3.0;
	float xs = sq(xm-u1.x)+sq(xm-u2.x)+sq(xm-u3.x);
	float ys = sq(ym-u1.y)+sq(ym-u2.y)+sq(ym-u3.y);
	
	return 1/(ys);*/
}

void main()
{
	const float PI = 3.1415926535897932384626;
	
	float count = COUNT;
	float brightness = 0.0;
	
	float xsin = sin(angles.x); //azimuth
	float xcos = cos(angles.x);
	float ysin = sin(angles.y); //zenith
	float ycos = cos(angles.y);
	
	vec3 dir = vec3(xsin*ycos, ysin, xcos*ycos); //must be normalized :D
	
	float a = 0.03;
	float a2 = dot(a,a);
	
	//float d2 = dot(dir,dir);
	
	for(int i = 0; i < count; i++){
		vec3 p = (transform*vec4(positions[i],1.0)).xyz;
		//vec3 p = positions[i]+vec3(0,0,2);
		float p2 = dot(p,p);
		float dp = dot(dir,p);
		//float k = d2*sqrt(((p2+a2)/d2)-(dp*dp/(d2*d2)));
		float k = sqrt((p2+a2)-(dp*dp));
		float fx = arctanApprox(dp/(k*k));
		brightness+=(a2/k)*(PI/2.0 + fx);
	}
	
	/*vec4 ori = transform*vec4(0,0,0,1);
	mat4 t2 = transpose(inverse(transform));
	vec4 i = t2*vec4(1,0,0,0);
	vec4 j = t2*vec4(0,1,0,0);
	vec4 k = t2*vec4(0,0,1,0);
	
	brightness+=axisVisible(ori,i,dir);
	brightness+=axisVisible(ori,j,dir);
	brightness+=axisVisible(ori,k,dir);*/
	
	brightness*=12;
	
	//if(brightness < 1) brightness = 0;
	
	FragColor = vec4(hsv2rgb(vec3(brightness,1.0,1.0)), 1.0);
    //FragColor = vec4(vec3(1.0,0.2,0.1)*brightness, 1.0);
    //FragColor = vec4(vec3(1.0,0.2,0.1)*brightnessFunction(brightness), 1.0);
}