package bibliomaths;

public class Operation {
    private String raw_data;

    public Operation(String raw_data){
        this.raw_data = raw_data;
    }

    public void exec(){
        String[] data = raw_data.split(",");
        String operation = data[1];
        Object o1 = buildObject(data[0]); 
        Object o2 = buildObject(data[2]);
        try {
            Class<?> clazz2 = (o2.getClass() == Double.class) ? double.class : o2.getClass();
            Object o3 = o1.getClass().getMethod(operation,clazz2).invoke(o1,o2);
            System.out.println(display(o3));
        } catch (Exception e) {
            System.out.println("Interdit");
        }
    }

    private static Object buildObject(String s) {
        String[] tmp;
        if(s.contains(" ")){
            tmp = s.split(" ");
            
            double x1 = Double.parseDouble(tmp[1]);
    
            double y1 = Double.parseDouble(tmp[2]);
    
            double z1 = Double.parseDouble(tmp[3]);
    
            switch(tmp[0]){
                case "V":
                    return new Vector(x1, y1, z1);
                case "P":
                    return new Point(x1, y1, z1);
                case "C":
                    return new Couleur(x1, y1, z1);
                default:
                    return new String("Erreur");
            }
        }
        return Double.parseDouble(s);
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
