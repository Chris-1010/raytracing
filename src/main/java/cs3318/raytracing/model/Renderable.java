package cs3318.raytracing.model;

import cs3318.raytracing.utils.*;

public interface Renderable {
    Float intersect(Ray r, float intersectDistance);
    String toString();
    Vector3D surfaceNormal(Point3D intersectionPoint);
    Surface getSurface();
}

