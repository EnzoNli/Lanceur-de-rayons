package bibliomaths;

public class Couleur extends Triplet {

    /**
     * Le constructeur de la classe couleur qui permet de créer une couleur au format rgb
     * @param x correspond à la couleur rouge
     * @param y correspond à la couleur verte
     * @param z correspond à la couleur bleu
     */
    public Couleur(double x, double y, double z){
        this.setX(x);
        this.setY(y);
        this.setZ(z);
    }
    
    /**
     * Pemet d'additionner deux couleurs.
     * @param c correspond à la couleur que l'on va additionner à l'instance courante.
     * @return un triplet correspondant à l'addition des deux couleurs.
     */
    public Couleur add(Couleur c) {
        return new Couleur(this.getX()+c.getX(), this.getY()+c.getY(), this.getZ()+c.getZ());
    }

    /**
     * Pemet de multiplier l'instance courante avec un double.
     * @param c correspond au double que l'on va multiplier avec l'instance courante.
     * @return un triplet correspondant à la multiplication de l'instance courante et du double.
     */
    public Couleur mul(double d) {
        return new Couleur(this.getX()*d, getY()*d, getZ()*d);
    }

    /**
     * Permet de multiplier deux couleurs.
     * @param c correspodn à la couleur que l'on va multipler à l'instance courante.
     * @return un triplet correspondant à la multiplication entre l'instance courante et une couleur.
     */
    public Couleur times(Couleur c) {
        return new Couleur(this.getX()*c.getX(), this.getY()*c.getY(), this.getZ()*c.getZ());
    }

    /**
     * Cette méthode permet d'afficher une couleur.
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("C " + super.toString());
        return s.toString();
    }
}
