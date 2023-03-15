package forme;

import bibliomaths.Couleur;
import bibliomaths.Point;

public class Triangle extends Forme {

    private Point x;
    private Point y;
    private Point z;

    public Triangle(Point x, Point y, Point z, Couleur c) {
        super(c);
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point getX() {
        return x;
    }

    public Point getY() {
        return y;
    }

    public Point getZ() {
        return z;
    }

}
