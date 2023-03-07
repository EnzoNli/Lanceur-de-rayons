package lights;

import bibliomaths.Couleur;
import bibliomaths.Point;
import bibliomaths.Vector;

public abstract class Light {
    
    private Couleur couleur;
    private Point point;
    private Vector vecteur;

    public Light(Couleur c) {
        this.couleur = c;
    }

    public Couleur getCouleur() {
        return couleur;
    }

    public Point getPoint() {
        return point;
    }

    public Vector getVecteur() {
        return vecteur;
    }

}
