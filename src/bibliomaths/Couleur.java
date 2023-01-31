package bibliomaths;
public class Couleur extends Triplet {

    
    public Couleur(double x, double y, double z){
        this.setX(x);
        this.setY(y);
        this.setZ(z);
    }
    
    public Couleur add(Couleur c) {
        return new Couleur(this.getX()+c.getX(), this.getY()+c.getY(), this.getZ()+c.getZ());
    }


    public Couleur mul(double d) {
        return new Couleur(this.getX()*d, getY()*d, getZ()*d);
    }


    public Couleur times(Couleur c) {
        return new Couleur(this.getX()*c.getX(), this.getY()*c.getY(), this.getZ()*c.getZ());
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("C " + super.toString());
        return s.toString();
    }
}
