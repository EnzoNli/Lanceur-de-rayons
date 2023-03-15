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
import forme.Triangle;
import forme.Forme;
import forme.Plan;
import sceneparser.SceneParser;
import lights.*;


public class LanceurRayon {
    
    private String fichierParse;
    private BufferedImage imgOutput;
    private Forme lastForme = null;
    private Plan plane;
    private ArrayList<Sphere> spheres;
    private ArrayList<Triangle> triangles;

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


    private Point distanceMinEntre2Points(Point res1, Point res2, Forme f1, Forme f2, Point eye) {
        if(res1 == null){
            if(res2 == null){
                return null;
            }else{
                lastForme = f2;
                return res2;
            }
        }else{
            if(res2 == null){
                lastForme = f1;
                return res1;
            }else{
                double dis1 = Math.sqrt(Math.pow(eye.getX() - res1.getX(), 2) + Math.pow(eye.getY() - res1.getY(), 2) + Math.pow(eye.getZ() - res1.getZ(), 2));
                double dis2 = Math.sqrt(Math.pow(eye.getX() - res2.getX(), 2) + Math.pow(eye.getY() - res2.getY(), 2) + Math.pow(eye.getZ() - res2.getZ(), 2));

                if(dis1 > dis2){
                    lastForme = f2;
                    return res2;
                }else{
                    lastForme = f1;
                    return res1;
                }
            }
        }
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

    public Point rechercherPointProche(Vector d, Camera cam){
        double a = 1;
        double b;
        double c;
        double discriminant;
        double t = Double.POSITIVE_INFINITY;
        double tmp;
        Point res1 = null;
        Point res2 = null;
        Point res3 = null;
        Forme f = null;
        Forme f2 = null;
        Point eye = cam.getLookFrom();

        // Duplication de code ici
        if(plane != null){
            double denominateur = d.dot(plane.getNormal());
            if(denominateur != 0) {
                double numerateur = plane.getCoord().sub(eye).dot(plane.getNormal());
                double t_plane = (double) numerateur / (double) denominateur;
                res1 = d.mul(t_plane).add(eye);
            }
        }
        // Duplication de code ici
        for(Triangle tr : triangles){
            Point p = null;
            double denominateur = d.dot(tr.getNormal());
            if(denominateur != 0) {
                double numerateur = tr.getX().sub(eye).dot(tr.getNormal());
                double t_plane = (double) numerateur / (double) denominateur;
                p = d.mul(t_plane).add(eye);
                if(calculDesNormalesTriangle(tr, p)){
                    res3 = p;
                    f2 = tr;
                }

            }
        }

        for(Sphere s : spheres){
            b = eye.sub(s.getCentre()).mul(2).dot(d);
            c = eye.sub(s.getCentre()).dot(eye.sub(s.getCentre())) - (s.getRayon()*s.getRayon());
            discriminant = (b*b)-(4*a*c);
            tmp = calculMiniInterSphere(discriminant, a, b);
            if(tmp != Double.POSITIVE_INFINITY){
                if(tmp < t){
                    t = tmp;
                    f = s;
                }
            }
        }

        if(t != Double.POSITIVE_INFINITY){
            res2 = d.mul(t).add(eye);
        }

        return distanceMinEntre2Points(distanceMinEntre2Points(res1, res2, plane, f, eye), res3, lastForme, f2, eye);

    }

    private boolean calculDesNormalesTriangle(Triangle tr, Point p) {
        if(tr.getY().sub(tr.getX()).cross(p.sub(tr.getX())).dot(tr.getNormal()) < 0){
            return false;
        }

        if(tr.getZ().sub(tr.getY()).cross(p.sub(tr.getY())).dot(tr.getNormal()) < 0){
            return false;
        }

        if(tr.getX().sub(tr.getZ()).cross(p.sub(tr.getZ())).dot(tr.getNormal()) < 0){
            return false;
        }
        return true;
    }

    public Vector calcN(Point p) {
        Vector n = new Vector(0, 0, 0);
        if(lastForme != null) {
            if(lastForme instanceof Sphere){
                Sphere s = (Sphere) lastForme;
                n = p.sub(s.getCentre()).hat();
            }else if(lastForme instanceof Plan){
                n = plane.getNormal();
            }else{
                Triangle tr = (Triangle) lastForme;
                n = tr.getNormal();
            }
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

    private Couleur calculCouleurFinale(Couleur ambient, ArrayList<Vector> ldirs, ArrayList<LocalLight> plights, ArrayList<DirectionalLight> dlights, Vector n) {
        int nombreDeDLumieres = dlights.size();
        int count_ldirs = ldirs.size();
        int cpt = 0;
        Couleur dif = lastForme.getDiffuse();
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
        ArrayList<Vector> ldirs;
        Vector n;
        Couleur couleurFinale;

        spheres = s.getSpheres();
        plane = s.getPlan();
        triangles = s.getTriangles();
        
        for (int i = 0; i < imgOutput.getWidth(); i++) {
            for (int j = 0; j < imgOutput.getHeight(); j++) {
                d = calcVecUnitaire(c, i, j, w, u, v, fovr, pixelheight, pixelwidth);
                p = rechercherPointProche(d, c);
                if(plights.size()+dlights.size() == 0){
                    if(p == null){
                        imgOutput.setRGB(i, (imgOutput.getHeight()-1 - j), 0);
                    }else{
                        imgOutput.setRGB(i, (imgOutput.getHeight()-1 - j), s.getAmbient().getRGB());
                    }
                }else{
                    if(p == null){
                        imgOutput.setRGB(i, (imgOutput.getHeight()-1 - j), 0);
                    }else{
                        n = calcN(p);
                        ldirs = calcLdir(plights, dlights, p);
                        couleurFinale = calculCouleurFinale(s.getAmbient(), ldirs, plights, dlights, n);
                        imgOutput.setRGB(i, (imgOutput.getHeight()-1 - j), couleurFinale.getRGB());
                        lastForme = null;
                    }
                }
            }
        }

        try{
            ImageIO.write(imgOutput, "png", new File("./" + s.getOutputName()));
        }catch (IOException e){
            System.out.println("Impossible de creer l'image");
        }

    }

}
