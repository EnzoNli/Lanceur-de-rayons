package sceneparser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import bibliomaths.Couleur;
import bibliomaths.Point;
import bibliomaths.Vector;
import camera.Camera;
import forme.*;
import lights.*;

public class SceneParser {
    private String nomFichierAParser;

    private int[] size = new int[2];
    private String outputName;
    private Camera camera;
    private Couleur ambient;
    private Couleur diffuses;
    private Couleur speculars;
    private ArrayList<Integer> shininess = new ArrayList<Integer>();
    private ArrayList<Light> lights = new ArrayList<Light>();
    private ArrayList<Forme> objects = new ArrayList<Forme>();

    public SceneParser(String nomFichierAParser) {
        this.nomFichierAParser = nomFichierAParser;
    }

    public void parse() throws IOException {
        File fichier = new File(this.nomFichierAParser);
        FileReader f_reader = new FileReader(fichier);
        ArrayList<Integer> cam = new ArrayList<Integer>();
        boolean passeSize = false;
        boolean passeOutput = false;
        boolean passeCamera = false;
        BufferedReader br = null;

        try {
            br = new BufferedReader(f_reader);
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
            this.ambient = findColors("ambient");
            f_reader.close();
            br.close();
        } catch (NumberFormatException e) {
            System.out.println(e.toString());
        } finally {
            br.close();
        }

        this.diffuses = findColors("diffuse");
        if (!(diffuses.isValid()) || !(this.ambient.isValid())) {
            throw new IllegalArgumentException("Un diffuse ne peut pas être supérieure à 1");
        }

        this.speculars = findColors("speculars");
        if (!(speculars.isValid())) {
            throw new IllegalArgumentException("Un specular ne peut pas être supérieure à 1");
        }
        // ici

        this.shininess = findShininess();
        findLights();
        findSphere();
        findTriangle();

    }

    private void findTriangle() throws IOException {
        String ligne;
        ArrayList<Point> vertex = new ArrayList<>();
        int maxverts = 0;
        BufferedReader f = new BufferedReader(null);
        try {
            f = new BufferedReader(new FileReader(new File(this.nomFichierAParser)));

            while ((ligne = f.readLine()) != null) {
                if (ligne.startsWith("maxverts") && maxverts == 0) {
                    String[] datas = ligne.split(" ");
                    if (datas.length == 2) {
                        vertex = findVertex();
                        maxverts = Integer.parseInt(datas[1]);
                        if (vertex.isEmpty()) {
                            f.close();
                            throw new IllegalArgumentException("Il n'y a pas de vertex");
                        } else if (vertex.size() > maxverts) {
                            f.close();
                            throw new IllegalArgumentException("Il y a plus de vertex que de maxverts");
                        }
                    } else {
                        f.close();
                        throw new IllegalArgumentException("Il n'y a pas le bon nombre d'argument dans maxverts");
                    }
                } else if (ligne.startsWith("tri") && maxverts > 0) {
                    String[] datas = ligne.split(" ");
                    if (datas.length == 4) {
                        for (int i = 1; i < datas.length; i++) {
                            if (Integer.parseInt(datas[i]) >= maxverts) {
                                f.close();
                                throw new IllegalArgumentException("Le vertex n'existe pas");
                            }
                        }
                        this.objects.add(new Triangle(vertex.get(Integer.parseInt(datas[1])),
                                vertex.get(Integer.parseInt(datas[2])),
                                vertex.get(Integer.parseInt(datas[3]))));
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        } finally {
            f.close();
        }
    }

    private ArrayList<Point> findVertex() throws IOException {
        ArrayList<Point> vertex = new ArrayList<>();
        String ligne;
        BufferedReader f = new BufferedReader(null);
        try {
            f = new BufferedReader(new FileReader(new File(this.nomFichierAParser)));
            while ((ligne = f.readLine()) != null) {
                if (ligne.startsWith("vertex")) {
                    String[] datas = ligne.split(" ");
                    Point p = new Point(Double.parseDouble(datas[1]), Double.parseDouble(datas[2]),
                            Double.parseDouble(datas[3]));
                    vertex.add(p);
                }
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        } finally {
            f.close();
        }
        return vertex;
    }

    private void findSphere() throws IOException {
        String ligne;
        Couleur diffuse = new Couleur(0, 0, 0);
        BufferedReader f = new BufferedReader(null);
        try {
            f = new BufferedReader(new FileReader(new File(this.nomFichierAParser)));

            while ((ligne = f.readLine()) != null) {
                if (ligne.startsWith("sphere")) {
                    String[] datas = ligne.split(" ");
                    if (datas.length == 5) {
                        this.objects
                                .add(new Sphere(new Point(Double.parseDouble(datas[1]), Double.parseDouble(datas[2]),
                                        Double.parseDouble(datas[3])), Double.parseDouble(datas[4]), diffuse));
                    }
                } else if (ligne.startsWith("diffuse")) {
                    String[] datas = ligne.split(" ");
                    diffuse = new Couleur(Double.parseDouble(datas[1]), Double.parseDouble(datas[2]),
                            Double.parseDouble(datas[3]));
                }
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        } finally {
            f.close();
        }
    }

    private void findLights() throws IOException {
        String ligne;
        BufferedReader f = new BufferedReader(null);
        try {
            f = new BufferedReader(new FileReader(new File(this.nomFichierAParser)));
            while ((ligne = f.readLine()) != null) {
                if (ligne.startsWith("point")) {
                    String[] datas = ligne.split(" ");
                    Point p = new Point(Double.parseDouble(datas[1]), Double.parseDouble(datas[2]),
                            Double.parseDouble(datas[3]));
                    Couleur c = new Couleur(Double.parseDouble(datas[4]), Double.parseDouble(datas[5]),
                            Double.parseDouble(datas[6]));
                    this.lights.add(new LocalLight(p, c));
                } else if (ligne.startsWith("directional")) {
                    String[] datas = ligne.split(" ");
                    Vector v = new Vector(Double.parseDouble(datas[1]), Double.parseDouble(datas[2]),
                            Double.parseDouble(datas[3]));
                    Couleur c = new Couleur(Double.parseDouble(datas[4]), Double.parseDouble(datas[5]),
                            Double.parseDouble(datas[6]));
                    this.lights.add(new DirectionalLight(v, c));
                }
            }
            if (!checkLights(lights)) {
                throw new IllegalArgumentException("La somme des composantes d'une des lumières dépasse 1");
            }
            f.close();
        } catch (IOException e) {
            System.out.println(e.toString());
        } finally {
            f.close();
        }
    }

    private boolean checkLights(ArrayList<Light> lights2) {
        // IMPLEMENTER ??????
        return true;
    }

    private ArrayList<Integer> findShininess() throws IOException {
        String ligne;
        ArrayList<Integer> toutesShininess = new ArrayList<Integer>();
        BufferedReader f = new BufferedReader(null);
        try {
            f = new BufferedReader(new FileReader(new File(this.nomFichierAParser)));
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
            System.out.println(e.toString());
        } finally {
            f.close();
        }
        return toutesShininess;
    }

    private Couleur findColors(String firstWord) throws IOException {
        String ligne;
        double[] tmp = new double[3];
        Couleur colors = new Couleur(0, 0, 0);
        BufferedReader f = new BufferedReader(null);

        try {
            f = new BufferedReader(new FileReader(new File(this.nomFichierAParser)));
            while ((ligne = f.readLine()) != null) {
                if (ligne.startsWith(firstWord)) {
                    String[] datas = ligne.split(" ");
                    for (int i = 0; i < 3; i++) {
                        tmp[i] = Double.parseDouble(datas[i + 1]);
                    }
                    colors = new Couleur(tmp[0], tmp[1], tmp[2]);
                }
            }
            f.close();
            return colors;
        } catch (IOException e) {
            System.out.println(e.toString());
        } finally {
            f.close();
        }
        return colors;

    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(this.outputName);
        s.append("\n");
        s.append(this.size[0] * this.size[1]);
        s.append("\n");
        s.append(this.objects.size());
        s.append("\n");
        s.append(this.lights.size());
        s.append("\n");

        return s.toString();
    }
}
