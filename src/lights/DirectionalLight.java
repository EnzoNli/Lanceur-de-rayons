package lights;

import bibliomaths.Couleur;
import bibliomaths.Vector;

public class DirectionalLight extends Light {

    private Vector vecteur;

    public DirectionalLight(Vector v, Couleur c) {
        super(c);
        this.vecteur = v;
    }

    public Vector getVecteur() {
        return vecteur;
    }
}
