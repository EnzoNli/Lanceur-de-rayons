package forme;

import bibliomaths.Couleur;
import bibliomaths.Point;

public class Sphere extends Forme {

    private Point centre;
    private Double rayon;

    /**
     * Constructeur d'une sphère
     * @param centre correspond au centre de la sphere
     * @param rayon correspond au rayon de la sphere
     * @param c correspond à la couleur de la sphere
     */
    public Sphere(Point centre, Double rayon, Couleur c) {
        super(c);
        this.centre = centre;
        this.rayon = rayon;
    }

    /**
     * Accesseur de l'attribut centre
     * @return l'attribut centre
     */
    public Point getCentre() {
        return centre;
    }

    /**
     * Accesseur de l'attribut rayon
     * @return l'attribut rayon
     */
    public Double getRayon() {
        return rayon;
    }


}