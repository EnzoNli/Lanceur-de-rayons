package rayon;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import bibliomaths.Point;
import bibliomaths.Vector;
import camera.Camera;
import forme.Sphere;
import sceneparser.SceneParser;

public class LanceurRayon {

    private String fichierParse;
    private BufferedImage imgOutput;

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
        double a = ((pixelwidth * (i - (imgOutput.getWidth()/2) + 0.5))/((imgOutput.getWidth()/2)));
        double b = ((pixelheight * (j - (imgOutput.getHeight()/2) + 0.5))/((imgOutput.getHeight()/2)));
        
        return u.mul(a).add(v.mul(b)).sub(w).hat();
    }

    public Point rechercherPointProche(Vector d, Camera cam, ArrayList<Sphere> spheres){
        Point p = null;
        double a = 1;
        double b;
        double c;
        double discriminant;


        for(Sphere s : spheres){
            if(d.dot(d) == (d.len() * d.len())*Math.cos(0) && (d.len() * d.len())*Math.cos(0) == 1) {
                a = d.dot(d);
            }
            b = s.getCentre().sub(cam.getLookFrom()).mul(2).dot(d);
            c = s.getCentre().sub(cam.getLookFrom()).dot(s.getCentre().sub(cam.getLookFrom())) - s.getRayon()*s.getRayon(); // c'est pas bon
            discriminant = (b*b)-4*a*c;
        }
        return p;
    }
    
    public void process(){
        SceneParser s = loadScene();
        Camera c = s.getCamera();
        imgOutput = new BufferedImage(s.getSize()[0], s.getSize()[1], BufferedImage.TYPE_INT_RGB);
        Vector w = c.getLookFrom().sub(c.getLookAt()).hat();
        Vector u = c.getUpDirection().cross(w).hat();
        Vector v = w.cross(u).hat();
        double fovr = (c.getFov() * Math.PI)/180;
        double pixelheight = Math.tan(fovr/2);
        double pixelwidth = pixelheight * (imgOutput.getWidth()/imgOutput.getHeight());
        Vector d;
        Point p;
        
        for (int i = 0; i < imgOutput.getWidth(); i++) {
            for (int j = 0; j < imgOutput.getHeight(); j++) {
                d = calcVecUnitaire(c, i, j, w, u, v, fovr, pixelheight, pixelwidth);
                p = rechercherPointProche(d, c);
            }
        }
    }
}
