package sceneparser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import bibliomaths.Couleur;
import camera.Camera;

public class SceneParser {
    private String nomFichierAParser;

    private int[] size;
    private String outputName;
    private Camera camera;
    private Couleur ambient;
    private ArrayList<Couleur> diffuses;
    private ArrayList<Couleur> speculars;
    private ArrayList<Integer> shininess;

    public SceneParser(String nomFichierAParser) {
        this.nomFichierAParser = nomFichierAParser;
    }

    public void parse() throws IOException, FileNotFoundException {
        File fichier = new File(this.nomFichierAParser);
        FileReader f_reader = new FileReader(fichier);
        ArrayList<Integer> cam = new ArrayList<Integer>();
        boolean passeSize = false;
        boolean passeOutput = false;
        boolean passeCamera = false;

        try (BufferedReader br = new BufferedReader(f_reader)) {
            String ligne;
            String[] parse;

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
                            cam.add(Integer.parseInt(parse[i + 1]));
                        }
                        this.camera = new Camera(cam.get(0), cam.get(1), cam.get(2), cam.get(3), cam.get(4), cam.get(5),
                                cam.get(6), cam.get(7), cam.get(8), cam.get(9));
                        passeCamera = true;
                    }

                }
            }

            if (!(passeCamera))
                throw new IllegalArgumentException("Camera introuvable");

            if (!(passeSize))
                throw new IllegalArgumentException("Taille de l'image introuvable");

            if (!(passeOutput)) {
                outputName = "output.png";
                passeOutput = true;
            }

            // A partir d'ici, tout les autres elements sont optionnels
            // On commence par les couleurs de l'image
            double[] couleursImage;
            couleursImage = lumieresAmbients();
            if (couleursImage == null) {
                this.ambient = new Couleur(0, 0, 0);
            } else {
                this.ambient = new Couleur(couleursImage[0], couleursImage[1], couleursImage[2]);
            }
            f_reader.close();
            br.close();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        this.diffuses = findDiffuses();
        for (Couleur c : diffuses) {
            if (!(c.isValid()) || !(this.ambient.add(c).isValid())) {
                throw new IllegalArgumentException("Un diffuse ne peut pas être supérieure à 1");
            }
        }

        this.speculars = findSpeculars();
        for (Couleur c : speculars) {
            if (!(c.isValid())) {
                throw new IllegalArgumentException("Un specular ne peut pas être supérieure à 1");
            }
        }
        // ici

        this.shininess = findShininess();
    }

    private ArrayList<Integer> findShininess() {
        String ligne;
        ArrayList<Integer> toutesShininess = new ArrayList<Integer>();
        try {
            BufferedReader f = new BufferedReader(new FileReader(new File(this.nomFichierAParser)));
            while ((ligne = f.readLine()) != null) {
                if (ligne.startsWith("shininess")) {
                    String[] datas = ligne.split(" ");
                    if (Integer.parseInt(datas[1]) < 0) {
                        f.close();
                        throw new IllegalArgumentException("Valeur négative de shininess");
                    }
                    toutesShininess.add(Integer.valueOf(datas[1]));
                }
            }
            f.close();
            return toutesShininess;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return toutesShininess;
    }

    private ArrayList<Couleur> findSpeculars() {
        String ligne;
        double[] tmp = new double[3];
        ArrayList<Couleur> toutesSpeculars = new ArrayList<>();

        try {
            BufferedReader f = new BufferedReader(new FileReader(new File(this.nomFichierAParser)));
            while ((ligne = f.readLine()) != null) {
                if (ligne.startsWith("speculars")) {
                    String[] datas = ligne.split(" ");
                    for (int i = 0; i < 3; i++) {
                        tmp[i] = Double.parseDouble(datas[i + 1]);
                    }
                    toutesSpeculars.add(new Couleur(tmp[0], tmp[1], tmp[2]));
                }
            }
            f.close();
            return toutesSpeculars;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return toutesSpeculars;

    }

    private ArrayList<Couleur> findDiffuses() {
        String ligne;
        double[] tmp = new double[3];
        ArrayList<Couleur> toutesDiffuses = new ArrayList<>();

        try {
            BufferedReader f = new BufferedReader(new FileReader(new File(this.nomFichierAParser)));
            while ((ligne = f.readLine()) != null) {
                if (ligne.startsWith("diffuse")) {
                    String[] datas = ligne.split(" ");
                    for (int i = 0; i < 3; i++) {
                        tmp[i] = Double.parseDouble(datas[i + 1]);
                    }
                    toutesDiffuses.add(new Couleur(tmp[0], tmp[1], tmp[2]));
                }
            }
            f.close();
            return toutesDiffuses;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return toutesDiffuses;

    }

    private double[] lumieresAmbients() {
        String ligne;
        double[] data = new double[3];
        try {
            BufferedReader f = new BufferedReader(new FileReader(new File(this.nomFichierAParser)));
            while ((ligne = f.readLine()) != null) {
                if (ligne.startsWith("ambient")) {
                    String[] split = ligne.split(" ");
                    for (int i = 0; i < 2; i++) {
                        data[i] = Double.parseDouble(split[i + 1]);
                    }
                    f.close();
                    return data;
                }
            }
            f.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
