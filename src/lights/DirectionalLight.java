package lights;

import bibliomaths.Couleur;
import bibliomaths.Vector;

public class DirectionalLight extends Light {

    private Vector vecteur;

    /**
     * Constructeur d'une directional light
     * @param v correspond à la directeur de la lumière
     * @param c correspond à la couleur de la lumière
     */
    public DirectionalLight(Vector v, Couleur c) {
        super(c);
        this.vecteur = v;
    }

    /**
     * Accesseur de l'attribut vecteur
     * @return l'attribut vecteur
     */
    public Vector getVecteur() {
        return vecteur;
    }
}
