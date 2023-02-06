package sceneparser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class SceneParser {
    private String nomFichierAParser;

    private int[] size;
    private String outputName;
    private int[] cam;
    // private double[] ambient;
    // private double[] diffuse;
    // private int[] specular;

    public SceneParser(String nomFichierAParser) {
        this.nomFichierAParser = nomFichierAParser;
    }

    public void parse() throws IOException, FileNotFoundException {
        File fichier = new File(this.nomFichierAParser);
        FileReader f_reader = new FileReader(fichier);
        BufferedReader br = new BufferedReader(f_reader);

        String ligne;
        String[] parse;

        boolean passeSize = false;
        boolean passeOutput = false;
        boolean passeCamera = false;

        while ((ligne = br.readLine()) != null) {
            parse = ligne.trim().split(" ");
            if (!(parse[0].equals("#"))) {
                // Recupération de la taille de l'image
                if (!(passeSize) && parse[0].equals("size")) {
                    if (parse.length < 3) {
                        throw new IllegalArgumentException("Pas assez d'arguments pour le size");
                    }
                    try {
                        this.size[0] = Integer.parseInt(parse[1]);
                        this.size[1] = Integer.parseInt(parse[2]);
                    } catch (NumberFormatException e) {
                        System.out.println("Un des deux arguments de size n'est pas un entier");
                    }
                    passeSize = true;
                }
                // Recupération du nom de la sortie
                if (!(passeOutput) && parse[0].equals("output")) {
                    if (parse.length < 2) {
                        throw new IllegalArgumentException("Pas assez d'arguments pour l'output");
                    }
                    this.outputName = parse[1];
                    passeOutput = true;
                }

                // Recupération de la camera
                if (!(passeCamera) && parse[0].equals("camera")) {
                    for (int i = 0; i < 10; i++) {
                        this.cam[i] = Integer.parseInt(parse[i + 1]);
                    }
                    passeCamera = true;
                }

                // A partir d'ici, tout les autres elements sont optionnels
                /*
                 * switch (parse[0]) {
                 * case "ambient":
                 * if (parse.length < 4) {
                 * throw new
                 * IllegalArgumentException("Pas assez d'arguments pour le paramètre ambient");
                 * }
                 * for (int i = 0; i < 3; i++) {
                 * ambient[i] = Double.parseDouble(parse[i + 1]);
                 * if (ambient[i] > 1) {
                 * throw new
                 * IllegalArgumentException("Un des paramètres de ambient est supérieur à 1");
                 * }
                 * }
                 * break;
                 * 
                 * case "diffuse":
                 * if (parse.length < 4) {
                 * throw new
                 * IllegalArgumentException("Pas assez d'arguments pour le paramètre diffuse");
                 * }
                 * for (int i = 0; i < 3; i++) {
                 * diffuse[i] = Double.parseDouble(parse[i + 1]);
                 * if (diffuse[i] > 1) {
                 * throw new
                 * IllegalArgumentException("Un des paramètres de diffuse est supérieur à 1");
                 * }
                 * }
                 * break;
                 * 
                 * case "specular":
                 * if (parse.length < 4) {
                 * throw new
                 * IllegalArgumentException("Pas assez d'arguments pour le paramètre specular");
                 * }
                 * for (int i = 0; i < 3; i++) {
                 * specular[i] = Integer.parseInt(parse[i + 1]);
                 * }
                 * break;
                 * 
                 * default:
                 * break;
                 * }
                 */

            }
        }

        if (!(passeOutput)) {
            outputName = "output.png";
            passeOutput = true;
        }

        /*
         * if (ambient.length != 0 && diffuse.length != 0) {
         * for (int i = 0; i < 3; i++) {
         * if (ambient[i] + diffuse[i] > 1) {
         * throw new IllegalArgumentException(
         * "La somme d'un des paramètres de diffuse et ambient est supérieur à 1");
         * }
         * }
         * }
         */

        // img = new BufferedImage(Integer.parseInt(parse[1]),
        // Integer.parseInt(parse[2]), BufferedImage.TYPE_INT_RGB);
        // file = new File(parse[1]);
        f_reader.close();
        br.close();

    }
}
