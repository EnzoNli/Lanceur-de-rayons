package rayon;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

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
                }
            }
        }

        if(t == Double.POSITIVE_INFINITY){
            return null;
        }
        
        return d.mul(t).add(cam.getLookFrom());
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
                p = rechercherPointProche(d, c, s.getSpheres());
                if(p == null){
                    imgOutput.setRGB(i, (imgOutput.getHeight()-1 - j), 0);
                }else{
                    imgOutput.setRGB(i, (imgOutput.getHeight()-1 - j), s.getAmbient().getRGB());
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
