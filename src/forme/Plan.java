package forme;

import bibliomaths.Couleur;
import bibliomaths.Point;
import bibliomaths.Vector;

public class Plan extends Forme{
    
    private Point coord;
    private Vector normal;

    /**
     * Constructeur d'un Plan
     * @param coord correspond au point par lequel passe le plan
     * @param normal correspond à la normale du plan
     * @param c correspond à la couleur du plan
     */
    public Plan(Point coord, Vector normal, Couleur c) {
        super(c);
        this.coord = coord;
        this.normal = normal;
    }

    /**
     * Accesseur de l'attribut coord
     * @return l'attribut coord
     */
    public Point getCoord() {
        return coord;
    }

    /**
     * Accesseur de l'attribut normale
     * @return l'attribut normale
     */
    public Vector getNormal() {
        return normal;
    }
}
