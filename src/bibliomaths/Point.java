package bibliomaths;
public class Point extends Triplet {

    /**
     * Le constructeur de la classe Point permettant de créer un point.
     * @param x correspond à l'abscisse.
     * @param y correspond à l'ordonnée.
     * @param z correspond à la cote.
     */
    public Point(double x, double y, double z){
        this.setX(x);
        this.setY(y);
        this.setZ(z);
    }
    
    /**
     * Pemet d'additionner un Point avec un Vector.
     * @param v correspond au vecteur que l'on va additionner à l'instance courante.
     * @return un point car l'addition d'un point et d'un vecteur donne un point.
     */
    public Point add(Vector v) {
        return new Point(this.getX()+v.getX(), this.getY()+v.getY(), this.getZ()+v.getZ());
    }
    
    /**
     * Pemet de soustraire deux Points.
     * @param p correspond au point que l'on va soustraire à l'instance courante.
     * @return un vector car la soustraction de deux points donne un vecteur.
     */
    public Vector sub(Point p) {
        return new Vector(this.getX()-p.getX(), this.getY()-p.getY(), this.getZ()-p.getZ());
    }

    /**
     * Permet de multiplier un point avec un double.
     * @param d correspond au double que l'on va multiplier avec l'intance courante.
     * @return un point correspond à la multiplication entre l'instance courante et le double.
     */
    public Point mul(double d) {
        return new Point(this.getX()*d, getY()*d, getZ()*d);
    }

    /**
     * Cette méthode permet d'afficher un point.
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("P " + super.toString());
        return s.toString();
    }
}
