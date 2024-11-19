package cs3318.raytracing.model;

import cs3318.raytracing.utils.*;

import java.util.List;


public class Ray {
    public static final float MAX_T = Float.MAX_VALUE;
    public Point3D origin;
    public Vector3D direction;

    public Ray(Point3D eye, Vector3D dir) {
        origin = new Point3D(eye);
        direction = Vector3D.normalize(dir);
    }

    public Intersection trace(List<Renderable> objects) {
        float intersectDistance = MAX_T;
        Renderable intersectObject = null;
        for (Renderable object : objects) {
            Float t = object.intersect(this, intersectDistance);
            if (t != null){
                intersectDistance = Math.min(t, intersectDistance);
                intersectObject = object;
            }
        }
        Intersection intersection = null;
        if (intersectObject != null){
            intersection = new Intersection(this, intersectObject, intersectDistance);
        }

        return intersection;
    }

    public Vector3D unitToOrigin(){
        return new Vector3D(-direction.x, -direction.y, -direction.z);
    }

    public String toString() {
        return ("ray origin = "+origin+"  direction = "+direction);
    }
}


