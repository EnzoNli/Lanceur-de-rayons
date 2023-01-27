

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class CompareImage{
    public static void main(String[] args){
        BufferedImage image1 = null;
        BufferedImage image2 = null;
        BufferedImage diff = null;
        Color couleur1 = null;
        Color couleur2 = null;
        Color couleurdiff;
        int compteur = 0;
        try {
            image1 = ImageIO.read(new File(args[0]));
            image2 = ImageIO.read(new File(args[1]));
            if((image1.getWidth() == image2.getWidth()) && (image1.getHeight() == image2.getHeight())){
                diff = new BufferedImage(image1.getWidth(), image1.getHeight(), BufferedImage.TYPE_INT_RGB);
                for(int i = 0; i < image1.getWidth(); i++) {
                    for(int j = 0; j < image1.getHeight(); j++) {
                        if(image1.getRGB(i, j) != image2.getRGB(i, j)){
                            compteur++;
                            couleur1 = new Color(image1.getRGB(i, j));
                            couleur2 = new Color(image2.getRGB(i, j));

                            couleurdiff = new Color(Math.abs(couleur1.getRed() - couleur2.getRed()), Math.abs(couleur1.getGreen() - couleur2.getGreen()), Math.abs(couleur1.getBlue() - couleur2.getBlue()));
                            diff.setRGB(i, j, couleurdiff.getRGB());
                        }else{
                            diff.setRGB(i, j, 0);
                        }
                    }
                }
                if(compteur >= 1){
                    try{
                        ImageIO.write(diff, "png", new File("./diff.png"));
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
    
                if(compteur <= 1000) {
                    System.out.println("OK");
                } else {
                    System.out.println("KO");
                }
                System.out.println(compteur);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}