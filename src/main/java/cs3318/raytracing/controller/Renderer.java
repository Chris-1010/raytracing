package cs3318.raytracing.controller;

import cs3318.raytracing.model.*;
import cs3318.raytracing.utils.Point3D;
import cs3318.raytracing.utils.Vector3D;

import java.awt.Color;

public class Renderer {

    private static final float TINY = 0.001f;
    private Color background;

    Renderer () {}

    Renderer (Color background) {
        setBackground(background);
    }

    public void setBackground(Color background) {
        this.background = background;
    }

    public Color renderPixel(int i, int j, Scene scene, Camera camera){
        Ray ray = new Ray(camera.eye, camera.calculateDirection(i, j));
        Intersection intersection = ray.trace(scene.getObjects());
        if (intersection != null) {
            return shadePixel(scene, intersection);
        }
        return background;
    }

    public Color shadePixel(Scene scene, Intersection intersection){
        float r = 0;
        float g = 0;
        float b = 0;
        Surface surface = intersection.object.getSurface();
        for (Light light : scene.getLights()) {
            if (light instanceof AmbientLight) {
                r += surface.phong.ambientReflectionCoefficient*surface.rIntrinsic*light.rIntensity;
                g += surface.phong.ambientReflectionCoefficient*surface.gIntrinsic*light.gIntensity;
                b += surface.phong.ambientReflectionCoefficient*surface.bIntrinsic*light.bIntensity;
            }
            else {
                Vector3D lightVector = light.calculateLightVector(intersection.point);

                // Check if the surface point is in shadow
                Point3D poffset = new Point3D(intersection.point.x + TINY*lightVector.x, intersection.point.y + TINY*lightVector.y, intersection.point.z + TINY*lightVector.z);
                Ray shadowRay = new Ray(poffset, lightVector);
                if (shadowRay.trace(scene.getObjects()) != null)
                    break;

                float lambert = Vector3D.dot(intersection.surfaceNormal,lightVector);
                if (lambert > 0) {
                    if (surface.phong.diffuseReflectionCoefficient > 0) {
                        float diffuse = surface.phong.diffuseReflectionCoefficient*lambert;
                        r += diffuse*surface.rIntrinsic*light.rIntensity;
                        g += diffuse*surface.gIntrinsic*light.gIntensity;
                        b += diffuse*surface.bIntrinsic*light.bIntensity;
                    }
                    if (surface.phong.specularReflectionCoefficient > 0) {
                        lambert *= 2;
                        float spec = intersection.unitVecToRay.dot(lambert*intersection.surfaceNormal.x - lightVector.x, lambert*intersection.surfaceNormal.y - lightVector.y, lambert*intersection.surfaceNormal.z - lightVector.z);
                        if (spec > 0) {
                            spec = surface.phong.specularReflectionCoefficient*((float) Math.pow(spec, surface.phong.exponent));
                            r += spec*light.rIntensity;
                            g += spec*light.gIntensity;
                            b += spec*light.bIntensity;
                        }
                    }
                }
            }
        }

        // Compute illumination due to reflection
        if (surface.phong.reflectanceCoefficient > 0) {
            Vector3D reflect = intersection.calculateReflect();
            if (reflect != null) {
//                t *= 2;
//                Vector3D reflect = new Vector3D(t*n.x - v.x, t*n.y - v.y, t*n.z - v.z);
                Point3D poffset = new Point3D(intersection.point.x + TINY*reflect.x, intersection.point.y + TINY*reflect.y, intersection.point.z + TINY*reflect.z);
                Ray reflectedRay = new Ray(poffset, reflect);
                Intersection reflectedIntersection = reflectedRay.trace(scene.getObjects());
                if (reflectedIntersection != null) {
                    Color rcolor = shadePixel(scene, reflectedIntersection);
                    r += surface.phong.reflectanceCoefficient*rcolor.getRed();
                    g += surface.phong.reflectanceCoefficient*rcolor.getGreen();
                    b += surface.phong.reflectanceCoefficient*rcolor.getBlue();
                } else {
                    r += surface.phong.reflectanceCoefficient*background.getRed();
                    g += surface.phong.reflectanceCoefficient*background.getGreen();
                    b += surface.phong.reflectanceCoefficient*background.getBlue();
                }
            }
        }

        // Add code for refraction here

        r = Math.min(r, 1f);
        g = Math.min(g, 1f);
        b = Math.min(b, 1f);

        r = (r < 0) ? 0 : r;
        g = (g < 0) ? 0 : g;
        b = (b < 0) ? 0 : b;

        return new Color(r, g, b);
    }

    // get intersect to return the intersection point and the distance to it

    //   1. the point of intersection (p) - intersection method which returns info to trace method of ray
    //   2. a unit-length surface normal (n) - method of the intersected object
    //   3. a unit-length vector towards the ray's origin (v) - can get that just from the ray itself


}
