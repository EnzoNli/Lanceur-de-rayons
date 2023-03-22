package forme;

import bibliomaths.Couleur;
import bibliomaths.Point;
import bibliomaths.Vector;

public class Triangle extends Forme {

    private Point x;
    private Point y;
    private Point z;

    /**
     * Constructeur d'un triangle
     * @param x correspond à un point du triangle
     * @param y correspond à un point du triangle
     * @param z correspond à un point du triangle
     * @param c correspond à la couleur du triangle
     */
    public Triangle(Point x, Point y, Point z, Couleur c) {
        super(c);
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Accesseur de l'attribut X
     * @return l'attribut X
     */
    public Point getX() {
        return x;
    }


    /**
     * Accesseur de l'attribut Y
     * @return l'attribut Y
     */
    public Point getY() {
        return y;
    }


    /**
     * Accesseur de l'attribut Z
     * @return l'attribut Z
     */
    public Point getZ() {
        return z;
    }


    /**
     * Accesseur de l'attribut normal
     * @return l'attribut normal
     */
    public Vector getNormal() {
        return y.sub(x).cross(z.sub(x)).hat();
    }

}
