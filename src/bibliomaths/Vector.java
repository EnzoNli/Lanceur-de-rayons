package bibliomaths;
public class Vector extends Triplet {
    /**
     * Le constructeur la classe Vector permettant de créer un vecteur.
     * @param x
     * @param y
     * @param z
     */
    public Vector(double x, double y, double z){
        this.setX(x);
        this.setY(y);
        this.setZ(z);
    }

    /**
     * Permet d'additionner un vecteur avec un point.
     * @param p correspond au point que l'on va additionner avec l'instance courante.
     * @return un point car l'addition d'un point et d'un vecteur donne un point.
     */
    public Point add(Point p) {
        return new Point(this.getX() + p.getX(), this.getY() + p.getY(), this.getZ() + p.getZ());
    }

    /**
     * Permet d'additionner deux vecteurs.
     * @param p correspond au vecteur que l'on va addtionner avec l'instance courannte
     * @return un vecteur correspondant à l"addition entre l'instance courante et le vecteur.
     */
    public Vector add(Vector p) {
        return new Vector(this.getX() + p.getX(), this.getY() + p.getY(), this.getZ() + p.getZ());
    }

    /**
     * Permet de soustraire deux vecteurs.
     * @param v correspondant au vecteur que l'on va soustraire avec l'instance courante.
     * @return un vecteur correspondant à la soustraction entre l'instance courante et le vecteur.
     */
    public Vector sub(Vector v) {
        return new Vector(this.getX()-v.getX(), this.getY()-v.getY(), this.getZ()-v.getZ());
    }

    /**
     * Permet de multiplier un vecteur avec un double.
     * @param d correspondant au double que l'on va multipler avec l'instance courante.
     * @return un vecteur correspondant à la multiplication entre l'instance courante et le double.
     */
    public Vector mul(double d) {
        return new Vector(this.getX()*d, getY()*d, getZ()*d);
    }

    /**
     * Permet de multiplier deux vecteurs.
     * @param v correspondant au vecteur que l'on va multiplier avec l'instance courante.
     * @return un vecteur correspondant à la multiplication entre l'instance courante et le vecteur.
     */
    public double dot(Vector v) {
        return this.getX()*v.getX() + this.getY()*v.getY() + this.getZ()*v.getZ();
    }

    /**
     * Permet de faire le produit vectoriel.
     * @param v correspondant au vecteur que l'on va multipler avec l'instance courante.
     * @return un vecteur correspondant au produit vectoriel de l'instance courante avec le vecteur.
     */
    public Vector cross(Vector v) {
        return new Vector(this.getY() * v.getZ() - this.getZ() * v.getY(), this.getZ() * v.getX() - this.getX() * v.getZ(), this.getX() * v.getY() - this.getY() * v.getX());
    }

    /**
     * Permet de calculer la longueur d'un vecteur.
     * @return un double correspondant à la longueur de l'instance courante.
     */
    public double len() {
        return Math.sqrt(this.getX()*this.getX() + this.getY()*this.getY() + this.getZ()*this.getZ());
    }

    /**
     * Permet d'avoir la normalisation d'un vecteur.
     * @return l'instance courante après normalisation.
     */
    public Vector hat() {
        Vector v = new Vector(this.getX(), this.getY(), this.getZ());
        double res = 1 / this.len();

        return v.mul(res);
    }

    /**
     * Permet d'afficher un vecteur.
     */
    @Override
    public String toString(){
        StringBuilder s = new StringBuilder();
        s.append("V " + super.toString());

        return s.toString();
    }
}
