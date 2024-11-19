package cs3318.raytracing.model;

import cs3318.raytracing.utils.Point3D;
import cs3318.raytracing.utils.Vector3D;

public abstract class Light {

    public float rIntensity, gIntensity, bIntensity;    // intensity of the light source

    public Light(float r, float g, float b) {
        rIntensity = r;
        gIntensity = g;
        bIntensity = b;
    }

    public Vector3D calculateLightVector (Point3D point) {return null;}
}





