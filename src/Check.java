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

    private static String display(Object o3) {
        if(o3.getClass() == Vector.class){
            Vector v = (Vector) o3;
            return v.toString();
        }

        if(o3.getClass() == Point.class){
            Point p = (Point) o3;
            return p.toString();
        }

        Double d = (Double) o3;
        return d.toString();
    }

    private static Object buildObject(String s) {
        if(s.length() != 1){
            int x = s.charAt(2);
            double x1 = x;
    
            int y = s.charAt(4);
            double y1 = y;
    
            int z = s.charAt(6);
            double z1 = z;
    
            switch(s.charAt(0)){
                case 'V':
                    return new Vector(x1, y1, z1);
                case 'P':
                    return new Point(x1, y1, z1);
            }
        }

        int d = s.charAt(0);
        double d1 = d;

        return new Double(d1);
    }
}
