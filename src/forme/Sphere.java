package forme;

import bibliomaths.Couleur;
import bibliomaths.Point;

public class Sphere extends Forme {

    private Point centre;
    private Double rayon;
    private Couleur c;

    public Sphere(Point centre, Double rayon, Couleur c) {
        this.centre = centre;
        this.rayon = rayon;
        this.c = c;
    }

}