package bibliomaths;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

public class Operation {

    private static final Logger LOGGER = Logger.getLogger("bug");
    private String rawData;

    /**
     * Le constructeur de la class Operation
     * @param rawData correspond à la chaine de caractère à parser.
     */
    public Operation(String rawData){
        this.rawData = rawData;

        LOGGER.setLevel(Level.ALL);
        System.setProperty("java.util.logging.SimpleFormatter.format",
              "%5$s%n");
        SimpleFormatter formatter = new SimpleFormatter();
        LOGGER.addHandler(new StreamHandler(System.out, formatter));
    }

    /**
     * Cette méthode utilise les méthodes buildObject et display
     */
    public void exec(){
        String[] data = rawData.split(",");
        String operation = data[1];
        Object o1 = buildObject(data[0]); 
        Object o2 = buildObject(data[2]);
        try {
            Class<?> clazz2 = (o2.getClass() == Double.class) ? double.class : o2.getClass();
            Object o3 = o1.getClass().getMethod(operation,clazz2).invoke(o1,o2);
            //System.out.println(display(o3));
            LOGGER.log(java.util.logging.Level.INFO, display(o3));
        } catch (Exception e) {
            LOGGER.log(java.util.logging.Level.SEVERE, "Interdit");
        }
    }

    /**
     * Cette méthode permet de parser.
     * @param s correspond à la chaine de carctère à parser
     * @return un Point, un Vector, une Couleur ou un Double.
     */
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
    
    /**
     * Cette méthode permet de déterminer si l'objet passer en paramétre est une Couleur, un Point ou un Vector.
     * @param o3 correspond à un Triplet.
     * @return la chaine de caractère correspondant à l'objet passé en paramètre.
     */
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
