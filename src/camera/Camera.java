package camera;

import bibliomaths.Point;
import bibliomaths.Vector;

public class Camera {
    private Point lookFrom;
    private Point lookAt;
    private Vector upDirection;
    private int fov;

    public Camera(Point p1, Point p2, Vector v, int f) {
        this.lookFrom = p1;
        this.lookAt = p2;
        this.upDirection = v;
        this.fov = f;
    }

    public Point getlookFrom() {
        return this.lookFrom;
    }

    public Vector getUpDirection() {
        return this.upDirection;
    }

    public Point getLookAt() {
        return this.lookAt;
    }

    public int getFov() {
        return this.fov;
    }

}
