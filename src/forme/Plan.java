package forme;

import bibliomaths.Couleur;
import bibliomaths.Point;
import bibliomaths.Vector;

public class Plan extends Forme{
    
    private Point coord;
    private Vector normal;


    public Plan(Point coord, Vector normal, Couleur c) {
        super(c);
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
