package lights;

import bibliomaths.Couleur;
import bibliomaths.Point;

public class LocalLight extends Light {

    private Point point;

    public LocalLight(Point p, Couleur c) {
        super(c);
        this.point = p;
    }

    public Point getPoint() {
        return point;
    }
}
