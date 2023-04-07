# Projet Lanceur de rayon réalisé par Enzo Nulli et Ugo Courché
# Quelques explications: Nous avons fait le projet avec l'aide de l'extension Live Share sur VSCode.
# C'est pour cette raison que les pushs sont souvent fait par Enzo.

# Triangles et plans

- Nous avons presque réussi à faire la partie Triangles et plans, 1 test sur les 22 ne fonctionne pas et nous ne savons pas pourquoi. Il concerne 2 triangles dont un qui doit etre masqué par l'autre puisqu'il est dans son ombre.

# Sonarqube

- Nous n'avons pas d'erreurs importante, nous somme passé de plus de 50 mauvaises habitudes à 8.
- Parmi ces 8 mauvaises habitudes, 6 sont des problèmes de Logger que nous n'avons pas réussi à résoudre car nous avions besoins des SystemOut pour valider les test sur gitlab.

# Compilation

Pour obtenir une image il suffit de faire un ./build et de faire un ./raytracer.sh avec le nom du fichier .test de la scène que l'on veut générer.


# Comparateur d'Images

- Permet de comparer deux images pixel par pixel.
- Permet aussi la création d'une nouvelle image diff et lorsqu'un pixel est différent, 
  on calcul la couleur du pixel, en faisant la soustraction de la couleur du pixel de l'image1 avec la couleur du pixel de l'image2 
  et on le place dans la nouvelle image diff.

# Bibiliothèque Mathématique

- Création de la classe abstraite Triplet permettant de créer un triplet quelconque et 
  contenant les getters et les setters des différents éléments d'un triplet ainsi que la méthode toString.
- Par la suite, nous avons créé trois classes représentant des Triplets, les classes Couleur, Point et Vecteur
  Comportant chacune les opérations qui leurs sont propres et qui sont utiles pour le projet.
- Et enfin, il y a la classe Opération qui permet de parser les fiches d'opérations et d'utiliser les calculs correpondant au bon triplet.

# Lecteur de Scènes

- Permet de récupérer les principaux éléments de la scène à partir d'un fichier texte afin de créer une image
- Pour que ce soit plus pratique, on utilise la fonction startWith afin de savoir par quoi commence la ligne afin de pouvoir gérer en conséquences
- De plus toutes les méthodes find"ElementRecherché" mettent à jour des attributs que l'on peut récupérer dans d'autres classe grâce à des getters
- Nous avons la méthode parse() qui est la méthode qui va parser tout le fichiers,
  
  Parser le fichier se fait en plusieurs temps :
- Tout d'abord nous faisons appelle à la méthode premierPassageParse() afin de récupérer les éléments nécessaires
  c'est à dire les ombres sont activés ou non, la taille de l'image, le nom de l'image et la position de la camera.
- Ensuite nous faisons appel à la méthode testPassage afin de gérer les potentielles erreurs si il n'y a pas de camera, de size ou de nom d'image.
- À partir d'ici, les éléments sont optionnels, on doit donc vérifier indépendemment chaque éléments
- on vérifie si les couleurs ambient, speculars et shininess existent sinon on gère l'erreur
- Pour les sphères, comme il peut y en avoir plusieurs, on utilise un ArrayList afin de pouvoir les stocker et comme chaque sphère prend comme couleur
  la dernière couleur diffuse déclarée juste avant on cherche la derniere ligne commençant par le mot diffuse avant la declaration de la sphere.
  c'est la méthode findSphere() qui nous permet de faire ceci.
- même principe pour le triangle, comme il peut y en avoir plusieurs, on les stockes dans un ArrayList et on cherche leur couleurs diffuse en mếme temps
  on a cependant besoin de vérifier qu'il y ai des vertex puisqu'ils permettent de créer des triangles.
  c'est la méthode findTriangle() qui nous permet de faire ceci et la méthode findVertex() est utilisée dedans.
- Pour le plan comme il ne peut y avoir qu'un seul plan, pas besoin d'ArrayList, juste d'une variable et on cherche aussi sa couleur diffuse
  c'est la méthode findPlane() qui nous permet de faire ceci.
- Et enfin pour les lights, comme il y a deux types de lights, nous avons décidé de stocker les points lights dans l'ArrayList plight et
  les directionals lights dans l'ArrayList dlight afin de pouvoir faire plus facilement les calculs par la suite.
  c'est la méthode findLights() qui nous permet de faire ceci


# les différentes classes d'objets

Avant de passer à l'Image2D et 3D, nous avons crée plusieurs classes :

- Nous avons crée la classe Caméra qui nous permet de créer l'objet Caméra à l'aide du Lecteur de Scène fait précédement
  La classe Caméra possède aussi des getters pour chacun des ses attributs.
- Nous avons La classe abstraite Forme représentant une forme quelconque et qui prend comme paramètre sa couleur.
  La classe permet d'avoir un getter afin de récupérer la couleur de la forme
- Nous avons crée les classes Sphère, Plan et Triangle qui implémentent chacune la classe Forme et elles permettent de créer 
  ces trois objet à l'aide du Lecteur de Scène fait précédement chacune de ses classe possèdent des getters pour récupérer leurs attributs.
- Et enfin nous avons créer la Classe abstraite Light qui permet de créer une light quelconque et qui prend en paramètre une couleur.
  Puis nous avons le classes DirectionalLight et LocalLight qui implementent toutes les deux la classe light et qui permettent respectivement
  de créer les directionals light et point light.


# Image2D, Image3D et Triangle et Plan

Pour finir, nous avons la classe LanceurRayon, qui est la classe gérant tout le processus du lanceur de rayon.

- La méthode qui gère le processus est la méthode process.
- On commence par parser la scène afin de récupérer tout les éléments grâce à la fonction méthode loadScene.
- Ensuite on effectue tout les calculs qui sont à faire une seule fois, c'est à dire les calculs des Vector u, v et w,
  du fovr, du pixelheight et du pixelwidth pour éviter de les recalculer à chaque fois.
- on récupère dans les variables toutes les éléments trouvés avec le lecteur de scène grâce au getters.
- Et enfin, nous calculons pour chaque pixels de l'image le point le plus proche afin de calculer sa couleur.

