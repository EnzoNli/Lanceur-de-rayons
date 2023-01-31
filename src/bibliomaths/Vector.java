package bibliomaths;
public class Vector extends Triplet {
    public Vector(double x, double y, double z){
        this.setX(x);
        this.setY(y);
        this.setZ(z);
    }

    public Point add(Point p) {
        return new Point(this.getX() + p.getX(), this.getY() + p.getY(), this.getZ() + p.getZ());
    }


    public Vector add(Vector p) {
        return new Vector(this.getX() + p.getX(), this.getY() + p.getY(), this.getZ() + p.getZ());
    }


    public Vector sub(Vector v) {
        return new Vector(this.getX()-v.getX(), this.getY()-v.getY(), this.getZ()-v.getZ());
    }


    public Vector mul(double d) {
        return new Vector(this.getX()*d, getY()*d, getZ()*d);
    }


    public double dot(Vector v) {
        return this.getX()*v.getX() + this.getY()*v.getY() + this.getZ()*v.getZ();
    }


    public Vector cross(Vector v) {
        return new Vector(this.getY() * v.getZ() - this.getZ() * v.getY(), this.getZ() * v.getX() - this.getX() * v.getZ(), this.getX() * v.getY() - this.getY() * v.getX());
    }

    public double len() {
        return Math.sqrt(this.getX()*this.getX() + this.getY()*this.getY() + this.getZ()*this.getZ());
    }


    public Vector hat() {
        Vector v = new Vector(this.getX(), this.getY(), this.getZ());
        double res = 1 / this.len();

        return v.mul(res);
    }

    @Override
    public String toString(){
        StringBuilder s = new StringBuilder();
        s.append("V " + super.toString());

        return s.toString();
    }
}
