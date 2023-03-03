package forme;

import bibliomaths.Couleur;

public abstract class Forme {
    private Couleur diffuse;

    public Forme() {
        diffuse = new Couleur(0,0,0);
    }

    public Forme(Couleur diffuse) {
        this.diffuse = diffuse;
    }

    public Couleur getDiffuse() {
        return diffuse;
    }

}
