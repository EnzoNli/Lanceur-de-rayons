import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ComparateurImages{
    public static void main(String[] args){
        try {
            BufferedImage image1 = ImageIO.read(new File(args[0]));
            BufferedImage image2 = ImageIO.read(new File(args[1]));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}