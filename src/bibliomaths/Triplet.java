package bibliomaths;
public abstract class Triplet {
    private double x;
    private double y;
    private double z;

    /**
     * Acesseur de l'attribut x.
     * @return la valeur de l'attribut x.
     */
    public double getX() {
        return x;
    }
    
    /**
     * Acesseur de l'attribut y.
     * @return la valeur de l'attribut y.
     */
    public double getY() {
        return y;
    }

    /**
     * Acesseur de l'attribut z.
     * @return la valeur de l'attribut z.
     */
    public double getZ() {
        return z;
    }

    /**
     * Permet de modifier la valeur de l'attribut x.
     * @param x correspond à la nouvelle valeur de x.
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Permet de modifier la valeur de l'attribut y.
     * @param y correspond à la nouvelle valeur de y.
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Permet de modifier la valeur de l'attribut z.
     * @param z correspond à la nouvelle valeur de z.
     */
    public void setZ(double z) {
        this.z = z;
    }

    /**
     * Permet d'afficher un Triplet.
     */
    public String toString(){
        StringBuilder s = new StringBuilder();
        s.append(this.getX() + " " + this.getY() + " " + this.getZ());

        return s.toString();
    }
}