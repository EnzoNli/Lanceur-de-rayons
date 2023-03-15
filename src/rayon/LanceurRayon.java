package rayon;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import bibliomaths.Couleur;
import bibliomaths.Point;
import bibliomaths.Vector;
import camera.Camera;
import forme.Sphere;
import forme.Plan;
import sceneparser.SceneParser;
import lights.*;


public class LanceurRayon {
    
    private String fichierParse;
    private BufferedImage imgOutput;
    private Sphere sphereActu = null;

    public LanceurRayon(String fichierParse){
        this.fichierParse = fichierParse;
    }

    private SceneParser loadScene() {
        SceneParser s = new SceneParser(this.fichierParse);
        try {
            s.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return s;
    }

    private Vector calcVecUnitaire(Camera c, int i, int j, Vector w, Vector u, Vector v, double fovr, double pixelheight, double pixelwidth){
        double a = ((pixelwidth * (i - ((double) imgOutput.getWidth()/2) + 0.5))/(((double) imgOutput.getWidth()/2)));
        double b = ((pixelheight * (j - ((double) imgOutput.getHeight()/2) + 0.5))/(((double) imgOutput.getHeight()/2)));
        
        return u.mul(a).add(v.mul(b)).sub(w).hat();
    }

    public double calculMiniInterSphere(double discriminant, double a, double b) {
        if(discriminant == 0) {
            return (-b)/(2*a);
        } else if ( discriminant > 0) {
            double t1 = ((-b) + Math.sqrt(discriminant))/(2*a);
            double t2 = ((-b) - Math.sqrt(discriminant))/(2*a);
            if(t2 > 0) {
                return t2;
            } else if (t1 > 0) {
                return t1;
            }else{
                return Double.POSITIVE_INFINITY;
            }
        } 
        return Double.POSITIVE_INFINITY;
    }

    public Point rechercherPointProche(Vector d, Camera cam, ArrayList<Sphere> spheres){
        double a = 1;
        double b;
        double c;
        double discriminant;
        double t = Double.POSITIVE_INFINITY;
        double tmp;


        for(Sphere s : spheres){
            b = cam.getLookFrom().sub(s.getCentre()).mul(2).dot(d);
            c = cam.getLookFrom().sub(s.getCentre()).dot(cam.getLookFrom().sub(s.getCentre())) - (s.getRayon()*s.getRayon());
            discriminant = (b*b)-(4*a*c);
            tmp = calculMiniInterSphere(discriminant, a, b);
            if(tmp != Double.POSITIVE_INFINITY){
                if(tmp < t){
                    t = tmp;
                    sphereActu = s;
                }
            }
        }

        if(t == Double.POSITIVE_INFINITY){
            return null;
        }
        
        return d.mul(t).add(cam.getLookFrom());
    }


    public Vector calcN(Point p) {
        Vector n = new Vector(0, 0, 0);
        if(sphereActu != null) {
            n = p.sub(sphereActu.getCentre()).hat();
        }
        return n;
    }


    public ArrayList<Vector> calcLdir(ArrayList<LocalLight> plights, ArrayList<DirectionalLight> dlights, Point p) {
        ArrayList<Vector> ldir = new ArrayList<>();
        for(DirectionalLight l : dlights) {
            ldir.add(l.getVecteur().hat());
        }
        for(LocalLight l : plights) {
            if(p == null){
                ldir.add(new Vector(0, 0, 0));
            }else{
                ldir.add(l.getPoint().sub(p).hat());
            }
        }

        return ldir;
    }

    private Couleur calculCouleurFinale(Couleur ambient, ArrayList<Vector> ldirs, ArrayList<LocalLight> plights, ArrayList<DirectionalLight> dlights, Vector n, Couleur dif) {
        int nombreDeDLumieres = dlights.size();
        int count_ldirs = ldirs.size();
        int cpt = 0;
        Couleur maxi = new Couleur(0, 0, 0);

        while(cpt < nombreDeDLumieres){
            maxi = maxi.add(dlights.get(cpt).getCouleur().mul(Math.max(n.dot(ldirs.get(cpt)), 0)));
            cpt++;
        }

        while(cpt < count_ldirs){
            maxi = maxi.add(plights.get(cpt-nombreDeDLumieres).getCouleur().mul(Math.max(n.dot(ldirs.get(cpt)), 0)));
            cpt++;
        }

        return maxi.times(dif).add(ambient);
    }

    
    public Point RechercheIntersectionPlan(Plan plane, Camera cam, Vector d) {
        if(plane == null){
            return null;
        }

        double denominateur = d.dot(plane.getNormal());
        if(denominateur == 0) {
            return null;
        }
        double numerateur = plane.getCoord().sub(cam.getLookFrom()).dot(plane.getNormal());
        double t = (double) numerateur / (double) denominateur;

        return d.mul(t).add(cam.getLookFrom());
    }
    


    public void process(){
        SceneParser s = loadScene();
        Camera c = s.getCamera();
        ArrayList<LocalLight> plights = s.getPlights();
        ArrayList<DirectionalLight> dlights = s.getDlights();
        imgOutput = new BufferedImage(s.getSize()[0], s.getSize()[1], BufferedImage.TYPE_INT_RGB);
        Vector w = (c.getLookFrom().sub(c.getLookAt())).hat();
        Vector u = (c.getUpDirection().cross(w)).hat();
        Vector v = (w.cross(u)).hat();
        double fovr = (c.getFov() * Math.PI) / 180d;
        double pixelheight = (double) Math.tan(fovr/2);
        double pixelwidth = pixelheight * ((double) imgOutput.getWidth()/ (double) imgOutput.getHeight());
        Vector d;
        Point p;
        ArrayList<Vector> ldirs; //= new ArrayList<>();
        Vector n;
        Couleur couleurFinale;
        Point pplane; 
        Plan plane = s.getPlan();
        
        for (int i = 0; i < imgOutput.getWidth(); i++) {
            for (int j = 0; j < imgOutput.getHeight(); j++) {
                d = calcVecUnitaire(c, i, j, w, u, v, fovr, pixelheight, pixelwidth);
                p = rechercherPointProche(d, c, s.getSpheres());
                if(plights.size()+dlights.size() == 0){
                    if(p == null){
                        imgOutput.setRGB(i, (imgOutput.getHeight()-1 - j), 0);
                    }else{
                        imgOutput.setRGB(i, (imgOutput.getHeight()-1 - j), s.getAmbient().getRGB());
                    }
                }else{
                    pplane = RechercheIntersectionPlan(plane, c, d);
                    if(p == null){
                        if(pplane != null){
                            ldirs = calcLdir(plights, dlights, pplane);
                            couleurFinale = calculCouleurFinale(s.getAmbient(), ldirs, plights, dlights, plane.getNormal(), plane.getDiffuse());
                            imgOutput.setRGB(i, (imgOutput.getHeight()-1 - j), couleurFinale.getRGB());
                        }else{
                            imgOutput.setRGB(i, (imgOutput.getHeight()-1 - j), 0);
                        }
                    }else{
                        // Si plan existe, tester si t existe
                        // si il existe comparer distance entre t et cam / p et cam pour savoir lequel est le plus proche
                        n = calcN(p);
                        ldirs = calcLdir(plights, dlights, p);
                        couleurFinale = calculCouleurFinale(s.getAmbient(), ldirs, plights, dlights, n, sphereActu.getDiffuse());
                        imgOutput.setRGB(i, (imgOutput.getHeight()-1 - j), couleurFinale.getRGB());
                        sphereActu = null;
                    }
                }
            }
        }

        try{
            ImageIO.write(imgOutput, "png", new File("./" + s.getOutputName()));
        }catch (IOException e){
            System.out.println("aled");
        }

    }

}
