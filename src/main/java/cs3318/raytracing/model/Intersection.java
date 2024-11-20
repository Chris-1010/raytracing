package cs3318.raytracing.model;

import cs3318.raytracing.utils.Point3D;
import cs3318.raytracing.utils.Vector3D;

public class Intersection {

    public Renderable object;
    public Point3D point;
    public Vector3D unitVecToRay;
    public Vector3D surfaceNormal;

    Intersection(Ray ray, Renderable object, float intersectionDistance) {
        this.object = object;
        point = calculateIntersectionPoint(intersectionDistance, ray);
        unitVecToRay = ray.unitToOrigin();
        surfaceNormal = object.surfaceNormal(point);
    }

    private Point3D calculateIntersectionPoint(float intersectionDistance, Ray ray) {
        float px = ray.origin.x + intersectionDistance * ray.direction.x;
        float py = ray.origin.y + intersectionDistance * ray.direction.y;
        float pz = ray.origin.z + intersectionDistance * ray.direction.z;

        return new Point3D(px, py, pz);
    }

    public Vector3D calculateReflect() {
        float t = unitVecToRay.dot(surfaceNormal);
        if (t > 0) {
            t *= 2;
            return new Vector3D(t * surfaceNormal.x - unitVecToRay.x, t * surfaceNormal.y - unitVecToRay.y,
                    t * surfaceNormal.z - unitVecToRay.z);
        }
        return null;
    }

    @Override
    public String toString() {
        return point.toString() + object.toString();
    }
}
