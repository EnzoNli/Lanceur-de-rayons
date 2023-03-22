package lights;

import bibliomaths.Couleur;
import bibliomaths.Point;

public class LocalLight extends Light {

    private Point point;

    /**
     * Constructeur d'une point light
     * @param p correspond au point représentant la lumière
     * @param c correspond à la couleur de la lumière
     */
    public LocalLight(Point p, Couleur c) {
        super(c);
        this.point = p;
    }

    /**
     * Accesseur de l'attribut point
     * @return l'attribut point
     */
    public Point getPoint() {
        return point;
    }
}
