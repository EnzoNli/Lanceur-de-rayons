package bibliomaths;
public class Point extends Triplet {

    
    public Point(double x, double y, double z){
        this.setX(x);
        this.setY(y);
        this.setZ(z);
    }
    
    public Point add(Vector v) {
        return new Point(this.getX()+v.getX(), this.getY()+v.getY(), this.getZ()+v.getZ());
    }
    
    public Vector sub(Point p) {
        return new Vector(this.getX()-p.getX(), this.getY()-p.getY(), this.getZ()-p.getZ());
    }

    public Point mul(double d) {
        return new Point(this.getX()*d, getY()*d, getZ()*d);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("P " + super.toString());
        return s.toString();
    }
}
