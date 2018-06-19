# Particles 3D

Particles in 3D space attracted by gravity (1/r^2) and repelled by short distance force (1/r^4). The renderer is a ray tracer, with the color of each ray being hsv(x,1,1), where x is the integral of the 1/(1+(r^2)/(a^2)) over each ray (from t=0 to t=infinity).

![Example 1](https://github.com/josephsullivan256/Particles3D/raw/master/example1.jpg)
![Example 2](https://github.com/josephsullivan256/Particles3D/raw/master/example2.gif)
