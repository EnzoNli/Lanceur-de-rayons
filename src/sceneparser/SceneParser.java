package sceneparser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import bibliomaths.Couleur;
import bibliomaths.Point;
import bibliomaths.Vector;
import camera.Camera;
import forme.*;
import lights.*;
import forme.Plan;

public class SceneParser {

    private static final String DIFFU_STRING = "diffuse";
    private static final Logger LOGGER = Logger.getLogger("bug");
    private String nomFichierAParser;

    private int[] size = new int[2];
    private String outputName;
    private Camera camera;
    private Couleur ambient;
    private Couleur speculars;
    private ArrayList<Integer> shininess = new ArrayList<>();
    private ArrayList<LocalLight> plights = new ArrayList<>();
    private ArrayList<DirectionalLight> dlights = new ArrayList<>();
    private ArrayList<Sphere> spheres = new ArrayList<>();
    private ArrayList<Triangle> triangles = new ArrayList<>();
    private Plan plan;
    private boolean shadow = false;
    private boolean passeSize = false;
    private boolean passeOutput = false;
    private boolean passeCamera = false;

    public SceneParser(String nomFichierAParser) {
        this.nomFichierAParser = nomFichierAParser;
    }

    /**
     * La fonction principale de parse d'un fichier
     * 
     * @throws IOException
     */
    public void parse() throws IOException {
        File fichier = new File(this.nomFichierAParser);
        FileReader fReader = new FileReader(fichier);

        try (BufferedReader f = new BufferedReader(fReader)) {
            premierPassageParse(f);
            testPassage(passeCamera, passeSize, passeOutput);

            // A partir d'ici, tout les autres elements sont optionnels
            // On commence par les couleurs de l'image
            fReader.close();
            this.ambient = findColors("ambient");
        } catch (NumberFormatException e) {
            LOGGER.log(java.util.logging.Level.SEVERE, e.toString());
        }

        this.speculars = findColors("speculars");
        if (!(speculars.isValid())) {
            throw new IllegalArgumentException("Un specular ne peut pas être supérieure à 1");
        }

        this.shininess = findShininess();
        findLights();
        findSphere();
        findTriangle();
        findPlane();

    }

    /**
     * Le premier passage de parse recupere si les ombres sont activés, la taille de
     * l'image, le nom de l'image et la position de la camera
     * 
     * @param f le BuffererReader
     * @throws NumberFormatException
     * @throws IOException
     */
    private void premierPassageParse(BufferedReader f) throws NumberFormatException, IOException {
        String ligne;
        String[] parse;
        ArrayList<Double> cam = new ArrayList<>();

        while ((ligne = f.readLine()) != null) {
            parse = ligne.trim().split(" ");
            // Ombres activées ou non ?
            if (parse[0].equals("shadow")) {
                this.shadow = Boolean.parseBoolean(parse[1]);
            }

            // Recupération de la taille de l'image
            if (!(passeSize) && parse[0].equals("size")) {
                if (parse.length < 3) {
                    throw new IllegalArgumentException("Pas assez d\'arguments pour le size");
                }
                tryParseInt(parse[1], parse[2]);
                passeSize = true;
            }
            // Recupération du nom de la sortie
            if (!(passeOutput) && parse[0].equals("output")) {
                if (parse.length < 2) {
                    throw new IllegalArgumentException("Pas assez d\'arguments pour l\'output");
                }
                this.outputName = parse[1];
                passeOutput = true;
            }

            // Recupération de la camera
            if (!(passeCamera) && parse[0].equals("camera")) {
                for (int i = 0; i < 9; i++) {
                    cam.add(Double.parseDouble(parse[i + 1]));
                }
                this.camera = new Camera(new Point(cam.get(0), cam.get(1), cam.get(2)),
                        new Point(cam.get(3), cam.get(4), cam.get(5)),
                        new Vector(cam.get(6), cam.get(7), cam.get(8)), Integer.parseInt(parse[10]));
                passeCamera = true;
            }
        }
    }

    /**
     * Permet la gestion d'erreur, si il n'y a pas de camera, de size ou de nom
     * d'image
     * 
     * @param passeCamera passe à true si la caméra existe
     * @param passeSize   passe à true si la size existe
     * @param passeOutput passe à true si l'Output existe
     */
    private void testPassage(boolean passeCamera, boolean passeSize, boolean passeOutput) {
        if (!(passeCamera))
            throw new IllegalArgumentException("Camera introuvable");

        if (!(passeSize))
            throw new IllegalArgumentException("Taille de l\'image introuvable");

        if (!(passeOutput)) {
            outputName = "output.png";
        }
    }

    /**
     * permet de transformer les chaines de caractères destinées a la taille de
     * l'image en entier
     * 
     * @param s1 chaine 1
     * @param s2 chaine 2
     */
    private void tryParseInt(String s1, String s2) {
        try {
            this.size[0] = Integer.parseInt(s1);
            this.size[1] = Integer.parseInt(s2);
        } catch (NumberFormatException e) {
            LOGGER.log(java.util.logging.Level.SEVERE, "Argument de size non entier");
        }
    }

    /**
     * Permet de chercher les triangles dans le fichier et leur appliquer leur
     * couleur diffuse
     * 
     * @throws IOException
     */
    private void findTriangle() throws IOException {
        String ligne;
        ArrayList<Point> vertex = new ArrayList<>();
        int maxverts = 0;
        Couleur diffuse = new Couleur(0, 0, 0);
        try (BufferedReader f = new BufferedReader(new FileReader(new File(this.nomFichierAParser)))) {

            while ((ligne = f.readLine()) != null) {
                if (ligne.startsWith("maxverts") && maxverts == 0) {
                    String[] datas = ligne.split(" ");
                    if (datas.length == 2) {
                        vertex = findVertex();
                        maxverts = Integer.parseInt(datas[1]);
                        if (vertex.isEmpty()) {
                            throw new IllegalArgumentException("Il n\'y a pas de vertex");
                        } else if (vertex.size() > maxverts) {
                            throw new IllegalArgumentException("Il y a plus de vertex que de maxverts");
                        }
                    } else {
                        throw new IllegalArgumentException("Il n\'y a pas le bon nombre d\'argument dans maxverts");
                    }
                } else if (ligne.startsWith("tri") && maxverts > 0) {
                    String[] datas = ligne.split(" ");
                    if (datas.length == 4) {
                        for (int i = 1; i < datas.length; i++) {
                            if (Integer.parseInt(datas[i]) >= maxverts) {
                                throw new IllegalArgumentException("Le vertex n'existe pas");
                            }
                        }
                        this.triangles.add(new Triangle(vertex.get(Integer.parseInt(datas[1])),
                                vertex.get(Integer.parseInt(datas[2])),
                                vertex.get(Integer.parseInt(datas[3])), diffuse));
                    }
                } else if (ligne.startsWith(DIFFU_STRING)) {
                    String[] datas = ligne.split(" ");
                    diffuse = new Couleur(Double.parseDouble(datas[1]), Double.parseDouble(datas[2]),
                            Double.parseDouble(datas[3]));
                }
            }
        } catch (IOException e) {
            LOGGER.log(java.util.logging.Level.SEVERE, e.toString());
        }
    }

    /**
     * Permet de chercher les vertex dans le fichier
     * 
     * @throws IOException
     */
    private ArrayList<Point> findVertex() throws IOException {
        ArrayList<Point> vertex = new ArrayList<>();
        String ligne;
        try (BufferedReader f2 = new BufferedReader(new FileReader(new File(this.nomFichierAParser)))) {

            while ((ligne = f2.readLine()) != null) {
                if (ligne.startsWith("vertex")) {
                    String[] datas = ligne.split(" ");
                    Point p = new Point(Double.parseDouble(datas[1]), Double.parseDouble(datas[2]),
                            Double.parseDouble(datas[3]));
                    vertex.add(p);
                }
            }
        } catch (IOException e) {
            LOGGER.log(java.util.logging.Level.SEVERE, e.toString());
        }
        return vertex;
    }

    /**
     * Permet de chercher le plan dans le fichier et lui appliquer sa couleur
     * diffuse
     * 
     * @throws IOException
     */
    private void findPlane() throws IOException {
        String ligne;
        Couleur diffuse = new Couleur(0, 0, 0);
        try (BufferedReader f2 = new BufferedReader(new FileReader(new File(this.nomFichierAParser)))) {
            while ((ligne = f2.readLine()) != null) {
                if (ligne.startsWith("plane")) {
                    String[] datas = ligne.split(" ");
                    plan = new Plan(
                            new Point(Double.parseDouble(datas[1]), Double.parseDouble(datas[2]),
                                    Double.parseDouble(datas[3])),
                            new Vector(Double.parseDouble(datas[4]), Double.parseDouble(datas[5]),
                                    Double.parseDouble(datas[6])),
                            diffuse);
                } else if (ligne.startsWith(DIFFU_STRING)) {
                    String[] datas = ligne.split(" ");
                    diffuse = new Couleur(Double.parseDouble(datas[1]), Double.parseDouble(datas[2]),
                            Double.parseDouble(datas[3]));
                }
            }
        } catch (IOException e) {
            LOGGER.log(java.util.logging.Level.SEVERE, e.toString());
        }
    }

    /**
     * Permet de chercher les spheres dans le fichier et leur appliquer leur couleur
     * diffuse
     * 
     * @throws IOException
     */
    private void findSphere() throws IOException {
        String ligne;
        Couleur diffuse = new Couleur(0, 0, 0);
        try (BufferedReader f = new BufferedReader(new FileReader(new File(this.nomFichierAParser)))) {

            while ((ligne = f.readLine()) != null) {
                if (ligne.startsWith("sphere")) {
                    String[] datas = ligne.split(" ");
                    if (datas.length == 5) {
                        this.spheres
                                .add(new Sphere(new Point(Double.parseDouble(datas[1]), Double.parseDouble(datas[2]),
                                        Double.parseDouble(datas[3])), Double.parseDouble(datas[4]), diffuse));
                    }
                } else if (ligne.startsWith(DIFFU_STRING)) {
                    String[] datas = ligne.split(" ");
                    diffuse = new Couleur(Double.parseDouble(datas[1]), Double.parseDouble(datas[2]),
                            Double.parseDouble(datas[3]));
                }
            }
        } catch (IOException e) {
            LOGGER.log(java.util.logging.Level.SEVERE, e.toString());
        }
    }

    /**
     * Permet de chercher les lumieres dans le fichier
     * 
     * @throws IOException
     */
    private void findLights() throws IOException {
        String ligne;
        try (BufferedReader f = new BufferedReader(new FileReader(new File(this.nomFichierAParser)))) {
            while ((ligne = f.readLine()) != null) {
                if (ligne.startsWith("point")) {
                    String[] datas = ligne.split(" ");
                    Point p = new Point(Double.parseDouble(datas[1]), Double.parseDouble(datas[2]),
                            Double.parseDouble(datas[3]));
                    Couleur c = new Couleur(Double.parseDouble(datas[4]), Double.parseDouble(datas[5]),
                            Double.parseDouble(datas[6]));
                    this.plights.add(new LocalLight(p, c));
                } else if (ligne.startsWith("directional")) {
                    String[] datas = ligne.split(" ");
                    Vector v = new Vector(Double.parseDouble(datas[1]), Double.parseDouble(datas[2]),
                            Double.parseDouble(datas[3]));
                    Couleur c = new Couleur(Double.parseDouble(datas[4]), Double.parseDouble(datas[5]),
                            Double.parseDouble(datas[6]));
                    this.dlights.add(new DirectionalLight(v, c));
                }
            }
            if (!checkLights(plights, dlights)) {
                throw new IllegalArgumentException("La somme des composantes d\'une des lumières dépasse 1");
            }
        } catch (IOException e) {
            LOGGER.log(java.util.logging.Level.SEVERE, e.toString());
        }
    }

    /**
     * Permet de tester si les lumieres sont bonnes
     * 
     * @param plights Lumières ponctuelles
     * @param dlights Lumières directionnelles
     * @return true si les lumière sont bonnes, false sinon
     */
    private boolean checkLights(ArrayList<LocalLight> plights, ArrayList<DirectionalLight> dlights) {
        Couleur sommeP = new Couleur(0.0, 0.0, 0.0);
        Couleur sommeD = new Couleur(0.0, 0.0, 0.0);
        Couleur sommeFinal;
        for (Light c : plights) {
            sommeP.add(c.getCouleur());
        }
        for (Light c : dlights) {
            sommeD.add(c.getCouleur());
        }
        sommeFinal = sommeD.add(sommeP);
        return sommeFinal.isValid();
    }

    /**
     * Permet de trouver les shininess
     * 
     * @throws IOException
     */
    private ArrayList<Integer> findShininess() throws IOException {
        String ligne;
        ArrayList<Integer> toutesShininess = new ArrayList<>();
        try (BufferedReader f = new BufferedReader(new FileReader(new File(this.nomFichierAParser)))) {

            while ((ligne = f.readLine()) != null) {
                if (ligne.startsWith("shininess")) {
                    String[] datas = ligne.split(" ");
                    if (Integer.parseInt(datas[1]) < 0) {
                        throw new IllegalArgumentException("Valeur négative de shininess");
                    }
                    toutesShininess.add(Integer.valueOf(datas[1]));
                }
            }
            return toutesShininess;
        } catch (IOException e) {
            LOGGER.log(java.util.logging.Level.SEVERE, e.toString());
        }
        return toutesShininess;
    }

    /**
     * Permet de trouver les differentes couleures (ambient, speculars)
     * 
     * @param firstWord ambient ou speculars
     * @return la couleur trouvée
     * @throws IOException
     */
    private Couleur findColors(String firstWord) throws IOException {
        String ligne;
        double[] tmp = new double[3];
        Couleur colors = new Couleur(0, 0, 0);

        try (BufferedReader f = new BufferedReader(new FileReader(new File(this.nomFichierAParser)))) {
            while ((ligne = f.readLine()) != null) {
                if (ligne.startsWith(firstWord)) {
                    String[] datas = ligne.split(" ");
                    for (int i = 0; i < 3; i++) {
                        tmp[i] = Double.parseDouble(datas[i + 1]);
                    }
                    colors = new Couleur(tmp[0], tmp[1], tmp[2]);
                }
            }
            return colors;
        } catch (IOException e) {
            LOGGER.log(java.util.logging.Level.SEVERE, e.toString());
        }
        return colors;

    }

    /**
     * accesseur pour la taille de l'image
     * 
     * @return la taille de l'image
     */
    public int[] getSize() {
        return size;
    }

    /**
     * accesseur pour la camera
     * 
     * @return la camera
     */
    public Camera getCamera() {
        return camera;
    }

    /**
     * accesseur pour savoir si les ombres sont activées
     * 
     * @return ombres activées ou non
     */
    public boolean hasShadow() {
        return shadow;
    }

    /**
     * accesseur pour les shininess
     * 
     * @return les shininess
     */
    public List<Integer> getShininess() {
        return shininess;
    }

    /**
     * accesseur pour le specular
     * 
     * @return le specular
     */
    public Couleur getSpeculars() {
        return speculars;
    }

    /**
     * accesseur pour les lumières directionnelles
     * 
     * @return les lumières directionnelles
     */
    public List<DirectionalLight> getDlights() {
        return dlights;
    }

    /**
     * accesseur pour les lumières ponctuelles
     * 
     * @return les lumières ponctuelles
     */
    public List<LocalLight> getPlights() {
        return plights;
    }

    /**
     * accesseur pour les triangles
     * 
     * @return les triangles
     */
    public List<Triangle> getTriangles() {
        return triangles;
    }

    /**
     * accesseur pour les spheres
     * 
     * @return les spheres
     */
    public List<Sphere> getSpheres() {
        return spheres;
    }

    /**
     * accesseur pour la lumiere ambiante
     * 
     * @return la lumiere ambiante
     */
    public Couleur getAmbient() {
        return ambient;
    }

    /**
     * accesseur pour le nom de l'image
     * 
     * @return le nom de l'image
     */
    public String getOutputName() {
        return outputName;
    }

    /**
     * accesseur pour le plan
     * 
     * @return le plan
     */
    public Plan getPlan() {
        return plan;
    }

    /**
     * la méthode toString pour afficher le resultat du parsage
     * 
     * @return le resultat du parsage
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(this.outputName);
        s.append("\n");
        s.append(this.size[0] * this.size[1]);
        s.append("\n");
        s.append(this.triangles.size() + this.spheres.size());
        s.append("\n");
        s.append(this.plights.size() + this.dlights.size());
        s.append("\n");

        return s.toString();
    }
}
