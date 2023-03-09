package lights;

import bibliomaths.Couleur;
import bibliomaths.Vector;

public class DirectionalLight extends Light {

    private Vector vecteur;

    public DirectionalLight(Vector v, Couleur c) {
        super(c);
        this.vecteur = v;
        // TODO Auto-generated constructor stub
    }

    public Vector getVecteur() {
        return vecteur;
    }
}
