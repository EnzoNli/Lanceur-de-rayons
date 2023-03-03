package rayon;

import java.awt.image.BufferedImage;
import java.io.IOException;

import bibliomaths.Vector;
import camera.Camera;
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

    public void process(){
        SceneParser s = loadScene();
        Camera c = s.getCamera();
        imgOutput = new BufferedImage(s.getSize()[0], s.getSize()[1], BufferedImage.TYPE_INT_RGB);
        Vector w = c.getlookFrom().sub(c.getLookAt()).hat();
        Vector u = c.getUpDirection().cross(w).hat();
        Vector v = w.cross(u).hat();
        Vector d;
        
        for (int i = 0; i < imgOutput.getWidth(); i++) {
            for (int j = 0; j < imgOutput.getHeight(); j++) {
            }
        }
    }

}
