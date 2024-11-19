package cs3318.raytracing.model;

import cs3318.raytracing.utils.Point3D;
import cs3318.raytracing.utils.Vector3D;

public class Sphere implements Renderable {
    public Surface surface;
    Point3D center;
    float radius;
    float radSqr;

    public Sphere(Point3D c, float r, Surface s) {
        surface = s;
        center = c;
        radius = r;
        radSqr = r*r;
    }

    public Float intersect(Ray ray, float intersectDistance) {
        float dx = center.x - ray.origin.x;
        float dy = center.y - ray.origin.y;
        float dz = center.z - ray.origin.z;
        float v = ray.direction.dot(dx, dy, dz);

        // Do the following quick check to see if there is even a chance
        // that an intersection here might be closer than a previous one
        if (v - radius > intersectDistance)
            return null;

        // Test if the ray actually intersects the sphere
        float t = radSqr + v*v - dx*dx - dy*dy - dz*dz;
        if (t < 0)
            return null;

        // Test if the intersection is in the positive
        // ray direction, and it is the closest so far
        t = v - ((float) Math.sqrt(t));
        if ((t > intersectDistance) || (t < 0))
            return null;

        return t;
    }

    public Vector3D surfaceNormal(Point3D intersectionPoint) {
        Vector3D n = new Vector3D(intersectionPoint.x - center.x, intersectionPoint.y - center.y, intersectionPoint.z - center.z);
        n.normalize();
        return n;
    }

    public String toString() {
        return ("sphere "+center+" "+radius);
    }

    public Surface getSurface(){
        return surface;
    }
}