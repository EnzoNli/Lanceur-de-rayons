package forme;

import bibliomaths.Couleur;
import bibliomaths.Point;

public class Sphere extends Forme {

    private Point centre;
    private Double rayon;

    public Sphere(Point centre, Double rayon, Couleur c) {
        super(c);
        this.centre = centre;
        this.rayon = rayon;
    }


    public Point getCentre() {
        return centre;
    }


    public Double getRayon() {
        return rayon;
    }

}