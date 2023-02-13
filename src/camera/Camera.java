package camera;

import bibliomaths.Point;
import bibliomaths.Vector;

public class Camera {
    private Point lookFrom;
    private Point lookAt;
    private Vector upDirection;
    private int fov;

    public Camera(int x, int y, int z, int u, int v, int w, int m, int n, int o, int f) {
        this.lookFrom = new Point(x, y, z);
        this.lookAt = new Point(u, v, w);
        this.upDirection = new Vector(m, n, o);
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
