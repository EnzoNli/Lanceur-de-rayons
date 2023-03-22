package lights;

import bibliomaths.Couleur;

public abstract class Light {
    
    private Couleur couleur;

    /**
     * Constructeur d'une lumière quelconque
     * @param c correspond à la couleur de la lumière
     */
    public Light(Couleur c) {
        this.couleur = c;
    }

    /**
     * Accesseur de l'attribut couleur
     * @return l'attribut couleur
     */
    public Couleur getCouleur() {
        return couleur;
    }
}
