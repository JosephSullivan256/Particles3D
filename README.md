# Particles 3D

## Physics
Particles in 3D space attracted by a gravity like force (1/r^2) and repelled by short distance force (1/r^4).

## Rendering
This project builds off of my [particles](https://github.com/JosephSullivan256/Particles) project, whereby I render a list of particles by assigning the color of each pixel to a function of distances to each particle. In other words, for a given pixel (x,y), I assign a color with intensity due to the sum of (1/((r_i/a)^2 + 1), where r_i denotes the distance from (x,y) to the particle.

Then, generalizing to 3D, I assign every point in 3D space to the sum of (1/((r_i/a)^2 + 1). To render the 3D scalar field, I used ray tracing. Each ray returns the value of the integral of the space over the ray (from t=0 starting at the camera to t=infinity). The value is then represented as the color hsv(x,1,1).

![Example 1](https://github.com/josephsullivan256/Particles3D/raw/master/example1.jpg)
![Example 2](https://github.com/josephsullivan256/Particles3D/raw/master/example2.gif)
