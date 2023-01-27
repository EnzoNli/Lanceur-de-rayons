public abstract class Triplet {
    private double x;
    private double y;
    private double z;

    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public String toString(){
        StringBuffer s = new StringBuffer();
        s.append(this.getX() + " " + this.getY() + " " + this.getZ());
        
        return s.toString();
    }
}