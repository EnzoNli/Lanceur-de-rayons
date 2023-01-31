package comparateur;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

public class ComparateurImage{

    private static final Logger LOGGER = Logger.getLogger("bug");

    private String nomImg1;
    private String nomImg2;
    private BufferedImage image1 = null;
    private BufferedImage image2 = null;
    private int compteurPixels = 0;


    public ComparateurImage(String nomImg1, String nomImg2){
        this.nomImg1 = nomImg1;
        this.nomImg2 = nomImg2;
    }

    private void testImage(BufferedImage diff) {
        Color couleur1 = null;
        Color couleur2 = null;
        Color couleurdiff = null;
        for(int i = 0; i < this.image1.getWidth(); i++) {
            for(int j = 0; j < this.image1.getHeight(); j++) {
                if(this.image1.getRGB(i, j) != this.image2.getRGB(i, j)){
                    this.compteurPixels++;
                    couleur1 = new Color(this.image1.getRGB(i, j));
                    couleur2 = new Color(this.image2.getRGB(i, j));

                    couleurdiff = new Color(Math.abs(couleur1.getRed() - couleur2.getRed()), Math.abs(couleur1.getGreen() - couleur2.getGreen()), Math.abs(couleur1.getBlue() - couleur2.getBlue()));
                    diff.setRGB(i, j, couleurdiff.getRGB());
                }else{
                    diff.setRGB(i, j, 0);
                }
            }
        }
    }

    private void creerDiff(BufferedImage diff){
        try{
            ImageIO.write(diff, "png", new File("./diff.png"));
        }catch (IOException e){
            LOGGER.log(java.util.logging.Level.SEVERE, "Impossible de créer diff.png", e);
        }
    }


    public void compare(){
        BufferedImage diff = null;
        try {
            this.image1 = ImageIO.read(new File(this.nomImg1));
            this.image2 = ImageIO.read(new File(this.nomImg2));
            if((this.image1.getWidth() == this.image2.getWidth()) && (this.image1.getHeight() == this.image2.getHeight())){
                diff = new BufferedImage(this.image1.getWidth(), this.image1.getHeight(), BufferedImage.TYPE_INT_RGB);
                testImage(diff);
                if(this.compteurPixels >= 1){
                    creerDiff(diff);
                }
                if(this.compteurPixels <= 1000) {
                    System.out.println("OK");
                } else {
                    System.out.println("KO");
                }
                System.out.println(this.compteurPixels);
            }
        } catch (IOException e) {
            LOGGER.log(java.util.logging.Level.SEVERE, "Impossible de créer une des images", e);
        }
    }
}