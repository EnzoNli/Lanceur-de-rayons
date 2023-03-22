package camera;

import bibliomaths.Point;
import bibliomaths.Vector;

public class Camera {
    private Point lookFrom;
    private Point lookAt;
    private Vector upDirection;
    private int fov;

    /**
     * Constructeur de le caméra
     * @param p1 correspond à la position de l'oeil, au point de départ du rayon
     * @param p2 correspond au point regardé, le pixel sur lequel pointe le laser
     * @param v correspondent à la direction vers le haut de l'oeil
     * @param f correspond à l'angle de vue
     */
    public Camera(Point p1, Point p2, Vector v, int f) {
        this.lookFrom = p1;
        this.lookAt = p2;
        this.upDirection = v;
        this.fov = f;
    }

    /**
     * Accesseur pour l'attribut lookFrom
     * @return l'attribut lookFrom
     */
    public Point getLookFrom() {
        return this.lookFrom;
    }

    /**
     * Accesseur pour l'attribut upDirection
     * @return l'attribut upDirection
     */
    public Vector getUpDirection() {
        return this.upDirection;
    }

    /**
     * Accesseur pour l'attribut lookAt
     * @return l'attribut lookAt
     */
    public Point getLookAt() {
        return this.lookAt;
    }

    /**
     * Accesseur pour l'attribut fov
     * @return l'attribut fov
     */
    public int getFov() {
        return this.fov;
    }

}
