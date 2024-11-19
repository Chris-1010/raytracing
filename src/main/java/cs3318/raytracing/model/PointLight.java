package cs3318.raytracing.model;

import cs3318.raytracing.utils.Point3D;
import cs3318.raytracing.utils.Vector3D;

public class PointLight extends Light {

    public Point3D lightPoint;

    public PointLight(float r, float g, float b, Point3D point) {
        super(r, g, b);
        lightPoint = point;
    }

    public Vector3D calculateLightVector(Point3D p) {
        Vector3D l = new Vector3D(lightPoint.x - p.x, lightPoint.y - p.y, lightPoint.z - p.z);
        l.normalize();
        return l;
    }

}


