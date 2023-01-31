public class Check {
    public static void main(String[] args) {
        String[] data = args[0].split(",");
        String operation = data[1];
        Object o1 = buildObject(data[0]); // à concevoir
        Object o2 = buildObject(data[2]); // à concevoir
        try {
            Class<?> clazz2 = (o2.getClass() == Double.class) ? double.class : o2.getClass();
            Object o3 = o1.getClass().getMethod(operation,clazz2).invoke(o1,o2);
            System.out.println(display(o3)); // à concevoir
        } catch (Exception e) {
            System.out.println("Interdit");
        }
    }

    private static Object buildObject(String s) {
        String[] tmp;
        if(s.contains(" ")){
            tmp = s.split(" ");
            
            double x1 = Double.valueOf(tmp[1]);
    
            double y1 = Double.valueOf(tmp[2]);
    
            double z1 = Double.valueOf(tmp[3]);
    
            switch(tmp[0]){
                case "V":
                    return new Vector(x1, y1, z1);
                case "P":
                    return new Point(x1, y1, z1);
                case "C":
                    return new Couleur(x1, y1, z1);
            }
        }

        double d = Double.valueOf(s);
        return d;
    }
    
    private static String display(Object o3) {
        if(o3.getClass() == Vector.class){
            Vector v = (Vector) o3;
            return v.toString();
        }

        if(o3.getClass() == Point.class){
            Point p = (Point) o3;
            return p.toString();
        }

        if(o3.getClass() == Couleur.class){
            Couleur c = (Couleur) o3;
            return c.toString();
        }

        Double d = (Double) o3;
        return d.toString();
    }

}
