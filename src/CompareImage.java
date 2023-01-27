import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class CompareImage{
    public static void main(String[] args){
        BufferedImage image1 = null;
        BufferedImage image2 = null;
        BufferedImage diff = null;
        int compteur = 0;
        try {
            image1 = ImageIO.read(new File(args[0]));
            image2 = ImageIO.read(new File(args[1]));
            if(image1.getWidth() == image2.getWidth() && image1.getWidth() == image2.getWidth()){
                diff = new BufferedImage(image1.getWidth(), image1.getHeight(), BufferedImage.TYPE_INT_RGB);
                for(int i = 0; i < image1.getWidth(); i++) {
                    for(int j = 0; j < image1.getHeight(); j++) {
                        if(image1.getRGB(i, j) != image2.getRGB(i, j)){
                            compteur++;
                            diff.setRGB(i, j, image1.getRGB(i, j));
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