package cs3318.raytracing.utils;

public class Point3D {
    public float x, y, z;

    public Point3D( ) {
    }

    public Point3D(float x, float y, float z) {
        this.x = x; this.y = y; this.z = z;
    }

    public Point3D(Vector3D v) {
        x = v.x;
        y = v.y;
        z = v.z;
    }

    public Point3D(Point3D p) {
        x = p.x;
        y = p.y;
        z = p.z;
    }
}
