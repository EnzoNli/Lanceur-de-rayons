package lights;

import bibliomaths.Couleur;

public abstract class Light {
    
    private Couleur couleur;

    public Light(Couleur c) {
        this.couleur = c;
    }

    public Couleur getCouleur() {
        return couleur;
    }
}
