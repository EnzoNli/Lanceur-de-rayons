package rayon;

import bibliomaths.Point;
import bibliomaths.Vector;

public class Plan {
    
    private Point coord;
    public Vector normal;

    public Plan(Point coord, Vector normal) {
        this.coord = coord;
        this.normal = normal;
    }

    public Point getCoord() {
        return coord;
    }

    public Vector getNormal() {
        return normal;
    }
}
