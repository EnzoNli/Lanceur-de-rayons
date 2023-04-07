# Projet Lanceur de rayon réalisé par Enzo Nulli et Ugo Courché
# Quelques explications: Nous avons fait le projet avec l'aide de l'extension Live Share sur VSCode.
# C'est pour cette raison que les push son souvent fait par Enzo.

# Comparateur d'Images

- Permet de comparer deux images pixels par pixels.
- Permet aussi la création d'une nouvelle image diff et lorsqu'un pixel est différent, 
  on calcul la couleur du pixel, en faisant la soustraction de la couleur du pixel de l'image1 avec ma couleur du pixel de l'image2 
  et on le place dans la nouvelle image diff.

# Bibiliothèque Mathématique

- Création de la classe abtraite Triplet permettant de créer un triplet quelconque et 
  contenant les getters et les setters des différents éléments d'un triplet ainsi que la méthode toString.
- Par la suite, nous avons créé trois classes représentant des Triplets, les classes Couleur, Point et Vecteur
  Comportant chacune les opérations qui leurs sont propres et qui sont utiles pour le projet.
- Et enfin, il y a la méthode Opération qui permet de parser les fiches d'opérations et d'utiliser les calculs correpondant au bon triplet.

# Lecteur de Scènes

- Permet récupérer les principaux éléments de la scène à partir d'un fichier texte afin de créé une image
- Pour que ce soit plus pratique, on utilise la fonction startWith afin de savoir par quoi commence la ligne afin de pouvoir gérer en conséquences
- De plus tout les méthodes find"ElementRecherché" mettent à jour des attributs que l'on peut récupérer dans d'autres classe grâce à des getters
- Nous avons la méthode parse() qui est la méthode qui va parser tout le fichiers,
  
  Parser le fichier se fait en plusieurs temps :
- Tout d'abord nous faisons appelle à la méthode premierPassageParse() afin de répcupérer les éléments nécessaire
  c'est à dire les ombres sont activés, la taille de l'image, le nom de l'image et la position de la camera.
- Ensuite nous faisons appelle à la méthode testPassage afin de gérer les potentielles erreurs si si il n'y a pas de camera, de size ou de nom d'image.
- À partir d'ici, les éléments sont optionnels, on doit donc vérifier indépendemment chaque éléments
- on vérifie si les couleurs ambient, speculars et shininess existent sinon on gère l'erreur
- Pour les sphères, comme il peut y en avoir plusieurs, on utilise un ArrayList afin de pouvoir les stocker et comme chaque sphère prend comme couleur
  la dernière couleur diffuse déclarer juste avant on cherche on même une ligne commençant par le mot diffuse.
  c'est la méthode findSphere() qui nous permet de faire ceci.
- même principe pour le triangle, comme il peut y en avoir plusieurs, on les stockes dans un ArrayList et on cherche leur couleurs diffuse en mếme temps
  on a cependant besoin de vérifier qu'il y ai des vertex puisqu'ils permettent de créer des triangles ainsi que toute autres formes géométriques.
  c'est la méthode findTriangle() qui nous permet de faire ceci et la méthode findVertex() est utilisé dedans.
- Pour le plan comme il ne peut y avoir qu'un seul plan, pas besoin d'ArrayList, juste d'une variable et on cherche aussi ça couleur diffuse en temps
  c'est la méthode findPlane() qui nous permet de faire ceci.
- Et enfin pour les lights, comme il y a deux types de lights, nous avons décider de stocker les point lights dans l'ArrayList plight et
  les directionnals lights dans l'ArrayList dlight afin de pouvoir faire plus facilement les calculs par la suite.
  c'est la méthode findLights() qui nous permet de faire ceci


# les différentes classes d'objets

Avant de passer à l'Image2D, nous avons créer plusieurs classes :


