package rayon;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import bibliomaths.Couleur;
import bibliomaths.Point;
import bibliomaths.Vector;
import camera.Camera;
import forme.Sphere;
import forme.Triangle;
import forme.Forme;
import forme.Plan;
import sceneparser.SceneParser;
import lights.*;

public class LanceurRayon {

    private static final Logger LOGGER = Logger.getLogger("bug");
    private String fichierParse;
    private BufferedImage imgOutput;
    private Forme lastForme = null;
    private Plan plane;
    private ArrayList<Sphere> spheres;
    private ArrayList<Triangle> triangles;

    /**
     * Constructeur du lanceur de rayon
     * 
     * @param fichierParse correspond au fichier à parser
     */
    public LanceurRayon(String fichierParse) {
        this.fichierParse = fichierParse;
    }

    /**
     * Cette methode permet de parser le fichier
     * 
     * @return une variable SceneParser possédant toutes les récupérer après le
     *         parse
     */
    private SceneParser loadScene() {
        SceneParser s = new SceneParser(this.fichierParse);
        try {
            s.parse();
        } catch (IOException e) {
            LOGGER.log(java.util.logging.Level.SEVERE, "context", e);
        }

        return s;
    }

    /**
     * PCette méthode permet de savoir quelle est le point le plus proche entre les
     * différentes formes
     * 
     * @param res1 le point le plus proche correspondant à la première forme
     * @param res2 le point le plus proche correspondant à la deuxième forme
     * @param f1   correspond à la première forme
     * @param f2   correspond à la deuxième forme
     * @param eye  correspond à la position de l'oeil
     * @return le point le plus proche
     */
    private Point distanceMinEntre2Points(Point res1, Point res2, Forme f1, Forme f2, Point eye) {
        if (res1 == null) {
            if (res2 == null) {
                return null;
            } else {
                lastForme = f2;
                return res2;
            }
        } else {
            if (res2 == null) {
                lastForme = f1;
                return res1;
            } else {
                double dis1 = Math.sqrt(Math.pow(eye.getX() - res1.getX(), 2) + Math.pow(eye.getY() - res1.getY(), 2)
                        + Math.pow(eye.getZ() - res1.getZ(), 2));
                double dis2 = Math.sqrt(Math.pow(eye.getX() - res2.getX(), 2) + Math.pow(eye.getY() - res2.getY(), 2)
                        + Math.pow(eye.getZ() - res2.getZ(), 2));

                if (dis1 > dis2) {
                    lastForme = f2;
                    return res2;
                } else {
                    lastForme = f1;
                    return res1;
                }
            }
        }
    }

    /**
     * Permet de calculer le vecteur direction d⃗ pour un pixel (i,j)
     * 
     * @param c           correspond à la camera
     * @param i           correspond au numéro de la ligne ou est présent le pixel
     * @param j           correspond au numéro de la colonne ou est présent le pixel
     * @param w           correspond à l'axe passant par l'oeil (lookFrom) et le
     *                    point regardé (lookAt).
     * @param u           correspond à une normale au plan formé par les vecteurs
     *                    up⃗​ et w⃗
     * @param v           correspond au calcul à partir des vecteur u et w
     * @param fovr        correspond à l'angle de vue de la caméra en radians
     * @param pixelheight correspond à la hauteur d'un pixel
     * @param pixelwidth  correspond à la largeur d'un pixel
     * @return le vecteur direction d⃗
     */
    private Vector calcVecUnitaire(int i, int j, Vector w, Vector u, Vector v,
            double pixelheight, double pixelwidth) {
        double a = ((pixelwidth * (i - ((double) imgOutput.getWidth() / 2) + 0.5))
                / ((double) imgOutput.getWidth() / 2));
        double b = ((pixelheight * (j - ((double) imgOutput.getHeight() / 2) + 0.5))
                / ((double) imgOutput.getHeight() / 2));

        return u.mul(a).add(v.mul(b)).sub(w).hat();
    }

    /**
     * Permet de calculer les solutions du discriminant
     * 
     * @param discriminant correspond au résultat du calcul du discriminant
     * @param a
     * @param b
     * @return une des solutions du discriminant si elle existe
     */
    public double calculMiniInterSphere(double discriminant, double a, double b) {
        if (discriminant == 0) {
            return (-b) / (2 * a);
        } else if (discriminant > 0) {
            double t1 = ((-b) + Math.sqrt(discriminant)) / (2 * a);
            double t2 = ((-b) - Math.sqrt(discriminant)) / (2 * a);
            if (t2 > 0) {
                return t2;
            } else if (t1 > 0) {
                return t1;
            } else {
                return Double.POSITIVE_INFINITY;
            }
        }
        return Double.POSITIVE_INFINITY;
    }

    /**
     * Permet de calculer le point le plus proche
     * 
     * @param d   correspond au vecteur direction d⃗
     * @param cam correspond à la caméra
     * @return le point le plus proche
     */
    public Point rechercherPointProche(Vector d, Camera cam) {
        double a = 1;
        double b;
        double c;
        double discriminant;
        double t = Double.POSITIVE_INFINITY;
        double tmp;
        // Point d'inter avec plan
        Point res1 = null;

        // Point d'inter avec sphere
        Point res2 = null;

        // Point d'inter avec triangle
        Point res3 = null;
        Forme f = null;
        Forme f2 = null;
        Point eye = cam.getLookFrom();

        // Duplication de code ici
        if (plane != null) {
            double denominateur = d.dot(plane.getNormal());
            if (denominateur != 0) {
                double numerateur = plane.getCoord().sub(eye).dot(plane.getNormal());
                double tPlane = numerateur / denominateur;
                res1 = d.mul(tPlane).add(eye);
            }
        }
        // Duplication de code ici
        for (Triangle tr : triangles) {
            Point p = null;
            double denominateur = d.dot(tr.getNormal());
            if (denominateur != 0) {
                double numerateur = tr.getX().sub(eye).dot(tr.getNormal());
                double tTri = numerateur / denominateur;
                p = d.mul(tTri).add(eye);
                if (calculDesNormalesTriangle(tr, p)) {
                    res3 = p;
                    f2 = tr;
                }

            }
        }

        for (Sphere s : spheres) {
            b = eye.sub(s.getCentre()).mul(2).dot(d);
            c = eye.sub(s.getCentre()).dot(eye.sub(s.getCentre())) - (s.getRayon() * s.getRayon());
            discriminant = (b * b) - (4 * a * c);
            tmp = calculMiniInterSphere(discriminant, a, b);
            if (tmp != Double.POSITIVE_INFINITY && tmp < t) {
                t = tmp;
                f = s;
            }
        }

        if (t != Double.POSITIVE_INFINITY) {
            res2 = d.mul(t).add(eye);
        }

        return distanceMinEntre2Points(distanceMinEntre2Points(res1, res2, plane, f, eye), res3, lastForme, f2, eye);

    }

    /**
     * Permet de calculer les normales d'un triangle
     * 
     * @param tr correspond à un triangle
     * @param p  correspond au point le plus proche
     * @return si le calcul des normales est différent de 0 alors return true sinon
     *         false
     */
    private boolean calculDesNormalesTriangle(Triangle tr, Point p) {
        if (tr.getY().sub(tr.getX()).cross(p.sub(tr.getX())).dot(tr.getNormal()) < 0) {
            return false;
        }

        if (tr.getZ().sub(tr.getY()).cross(p.sub(tr.getY())).dot(tr.getNormal()) < 0) {
            return false;
        }

        return tr.getX().sub(tr.getZ()).cross(p.sub(tr.getZ())).dot(tr.getNormal()) >= 0;
    }

    /**
     * Permet de calculer la normale selon la une forme
     * 
     * @param p correspond au point le plus proche
     * @return la normale
     */
    public Vector calcN(Point p) {
        Vector n = new Vector(0, 0, 0);
        if (lastForme != null) {
            if (lastForme instanceof Sphere) {
                Sphere s = (Sphere) lastForme;
                n = p.sub(s.getCentre()).hat();
            } else if (lastForme instanceof Plan) {
                n = plane.getNormal();
            } else {
                Triangle tr = (Triangle) lastForme;
                n = tr.getNormal();
            }
        }
        return n;
    }

    /**
     * Permet de calculer tout les vecteurs ldir pour chaque lumière
     * 
     * @param plights correspond à la liste des points lights
     * @param dlights correspond à la liste des directionals lights
     * @param p       correspond au point le plus proche
     * @return une liste avec tout les vecteurs ldir
     */
    public List<Vector> calcLdir(List<LocalLight> plights, List<DirectionalLight> dlights, Point p) {
        ArrayList<Vector> ldir = new ArrayList<>();
        for (DirectionalLight l : dlights) {
            ldir.add(l.getVecteur().hat());
        }
        for (LocalLight l : plights) {
            if (p == null) {
                ldir.add(new Vector(0, 0, 0));
            } else {
                ldir.add(l.getPoint().sub(p).hat());
            }
        }

        return ldir;
    }

    /**
     * Permet de calculer la couleur des pixels lorsque des lights sont présents
     * 
     * @param ambient
     * @param ldirs   correspond à la liste de tout les vecteurs ldir
     * @param plights correspond à la liste des points lights
     * @param dlights correspond à la liste des directionals lights
     * @param n       correspond au vecteur normal calculer dans la méthode calcN
     * @return une couleur correspond à la couleur d'un pixel
     */
    private Couleur calculCouleurFinale(Couleur ambient, ArrayList<Vector> ldirs, ArrayList<LocalLight> plights,
            ArrayList<DirectionalLight> dlights, Vector n, boolean hasShadow, Point p) {
        int nombreDeDLumieres = dlights.size();
        int countLdirs = ldirs.size();
        int cpt = 0;
        Couleur dif = lastForme.getDiffuse();
        Couleur maxi = new Couleur(0, 0, 0);

        while (cpt < nombreDeDLumieres) {
            if (hasShadow) {
                boolean estUnPointOmbre = testPointOmbre(dlights.get(cpt).getVecteur(), p, new Point(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
                if (!estUnPointOmbre) {
                    maxi = maxi.add(dlights.get(cpt).getCouleur().mul(Math.max(n.dot(ldirs.get(cpt)), 0)));
                }
            } else {
                maxi = maxi.add(dlights.get(cpt).getCouleur().mul(Math.max(n.dot(ldirs.get(cpt)), 0)));
            }
            cpt++;
        }

        while (cpt < countLdirs) {
            if (hasShadow) {
                boolean estUnPointOmbre = testPointOmbre(new Vector(
                        plights.get(cpt - nombreDeDLumieres).getPoint().getX() - p.getX(),
                        plights.get(cpt - nombreDeDLumieres).getPoint().getY() - p.getY(),
                        plights.get(cpt - nombreDeDLumieres).getPoint().getZ() - p.getZ()).hat(), p,
                        plights.get(cpt - nombreDeDLumieres).getPoint());
                if (!estUnPointOmbre) {
                    maxi = maxi.add(
                            plights.get(cpt - nombreDeDLumieres).getCouleur().mul(Math.max(n.dot(ldirs.get(cpt)), 0)));
                }
            } else {
                maxi = maxi
                        .add(plights.get(cpt - nombreDeDLumieres).getCouleur().mul(Math.max(n.dot(ldirs.get(cpt)), 0)));
            }
            cpt++;
        }
        return maxi.times(dif).add(ambient);
    }

    /**
     * Permet de tester si le point eye donné est un point d'ombre d'une source de lumiere donnée
     * @param d vecteur vers la source de lumiere
     * @param eye le point initial d'intersection
     * @param lumiere la lumiere
     * @return si oui ou non eye est un point d'ombre
     */
    private boolean testPointOmbre(Vector d, Point eye, Point lumiere) {
        double a;
        double b;
        double c;
        double discriminant;
        double tmp;
        double epsilon = 0.0001;
        double distanceEyeLumiere = lumiere.sub(eye).len();
        for (Triangle tr : triangles) {
            Point p = null;
            double denominateur = d.dot(tr.getNormal());
            if (denominateur != 0) {
                double numerateur = tr.getX().sub(eye).dot(tr.getNormal());
                double tPlane = numerateur / denominateur;
                p = d.mul(tPlane).add(eye);
                if (calculDesNormalesTriangle(tr, p) && testEpsilon(epsilon, distanceEyeLumiere, eye, p)) {
                    return true;
                }
            }
        }

        for (Sphere s : spheres) {
            a = d.dot(d);
            b = eye.sub(s.getCentre()).mul(2).dot(d);
            c = eye.sub(s.getCentre()).dot(eye.sub(s.getCentre())) - (s.getRayon() * s.getRayon());
            discriminant = (b * b) - (4 * a * c);
            tmp = calculMiniInterSphere(discriminant, a, b);
            if (tmp != Double.POSITIVE_INFINITY) {
                Point testDistanceSphere = d.mul(tmp).add(eye);
                if(testEpsilon(epsilon, distanceEyeLumiere, eye, testDistanceSphere)){
                    return true;
                }
            }
        }

        return false;
    }

    private boolean testEpsilon(double epsilon, double distanceEyeLumiere, Point eye, Point p) {
        double distanceEyePointInter = Math.sqrt(Math.pow(eye.getX() - p.getX(), 2)
        + Math.pow(eye.getY() - p.getY(), 2) + Math.pow(eye.getZ() - p.getZ(), 2));
        return (distanceEyePointInter > epsilon && distanceEyePointInter < distanceEyeLumiere);
    }

    /**
     * Cette méthode est la méthode global permettant créer les images 2D, 3D ainsi
     * que les plans et les triangles
     * C'est ici que le calcule du point le plus proche est fait
     * Et caluler la couleur pour chaque pixels
     */
    public void process() {
        SceneParser s = loadScene();
        Camera c = s.getCamera();
        ArrayList<LocalLight> plights = (ArrayList<LocalLight>) s.getPlights();
        ArrayList<DirectionalLight> dlights = (ArrayList<DirectionalLight>) s.getDlights();
        imgOutput = new BufferedImage(s.getSize()[0], s.getSize()[1], BufferedImage.TYPE_INT_RGB);
        Vector w = (c.getLookFrom().sub(c.getLookAt())).hat();
        Vector u = (c.getUpDirection().cross(w)).hat();
        Vector v = (w.cross(u)).hat();
        double fovr = (c.getFov() * Math.PI) / 180d;
        double pixelheight = Math.tan(fovr / 2);
        double pixelwidth = pixelheight * ((double) imgOutput.getWidth() / (double) imgOutput.getHeight());
        Vector d;
        Point p;
        

        spheres = (ArrayList<Sphere>) s.getSpheres();
        plane = s.getPlan();
        triangles = (ArrayList<Triangle>) s.getTriangles();

        for (int i = 0; i < imgOutput.getWidth(); i++) {
            for (int j = 0; j < imgOutput.getHeight(); j++) {
                d = calcVecUnitaire(i, j, w, u, v, pixelheight, pixelwidth);
                p = rechercherPointProche(d, c);
                if (plights.size() + dlights.size() == 0) {
                    ajouteCouleurPixelSansLight(p, i, j, s);
                } else {
                    ajouteCouleurPixelAvecLight(p, i, j, s, plights, dlights);
                }
            }
        }

        try {
            ImageIO.write(imgOutput, "png", new File("./" + s.getOutputName()));
        } catch (IOException e) {
            System.out.println("Impossible de creer l'image");
        }

    }


    /**
     * 
     * Ajoute une couleur a un pixel donné quand il y a des lumieres dans la scene
     * 
     * @param p point d'intersection
     * @param i coordonnée x du pixel
     * @param j coordonnée x du pixel
     * @param s la scene
     * @param plights les lumieres ponctuelles
     * @param dlights les lumieres directionnelles
     */
    private void ajouteCouleurPixelAvecLight(Point p, int i, int j, SceneParser s, ArrayList<LocalLight> plights, ArrayList<DirectionalLight> dlights) {
        Vector n;
        ArrayList<Vector> ldirs;
        Couleur couleurFinale;
        if (p == null) {
            imgOutput.setRGB(i, (imgOutput.getHeight() - 1 - j), 0);
        } else {
            n = calcN(p);
            ldirs = (ArrayList<Vector>) calcLdir(plights, dlights, p);
            couleurFinale = calculCouleurFinale(s.getAmbient(), ldirs, plights, dlights, n, s.hasShadow(),
                    p);
            imgOutput.setRGB(i, (imgOutput.getHeight() - 1 - j), couleurFinale.getRGB());
            lastForme = null;
        }
    }

    /**
     * Ajoute une couleur a un pixel donné quand il n'y a pas de lumieres dans la scene
     * @param p point d'intersection
     * @param i coordonnée x du pixel
     * @param j coordonnée x du pixel
     * @param s la scene
     */
    private void ajouteCouleurPixelSansLight(Point p, int i, int j, SceneParser s) {
        if (p == null) {
            imgOutput.setRGB(i, (imgOutput.getHeight() - 1 - j), 0);
        } else {
            imgOutput.setRGB(i, (imgOutput.getHeight() - 1 - j), s.getAmbient().getRGB());
        }
    }

}