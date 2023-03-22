package forme;

import bibliomaths.Couleur;

public abstract class Forme {
    private Couleur diffuse;

    /**
     * Constructeur d'une forme quelconque sans paramètre
     * ici nous déclarons la couleur de l'objet à zéro, ce qui correspond à la couleur noir.
     */
    public Forme() {
        diffuse = new Couleur(0,0,0);
    }

    /**
     * Constructeur d'une forme quelconque avec une couleur particulière
     * @param diffuse correspond à la couleur de l'objet
     */
    public Forme(Couleur diffuse) {
        this.diffuse = diffuse;
    }
    
    /**
     * Accesseur de l'attribut diffuse
     * @return l'attribut diffuse
     */
    public Couleur getDiffuse() {
        return diffuse;
    }
    
    /**
     * Permet de mettre à jour l'attribut diffuse
     * @param diffuse correspond à la nouvelle couleur de l'objet
     */
    public void setDiffuse(Couleur nouv){
        diffuse = nouv;
    }

}
