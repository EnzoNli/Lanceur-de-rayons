# Projet lanceur de rayons

<a title="By Henrik (Own work) [GFDL (http://www.gnu.org/copyleft/fdl.html) or CC BY-SA 4.0-3.0-2.5-2.0-1.0 (http://creativecommons.org/licenses/by-sa/4.0-3.0-2.5-2.0-1.0)], via Wikimedia Commons" href="https://commons.wikimedia.org/wiki/File%3ARay_trace_diagram.svg"><img width="512" alt="Ray trace diagram" src="https://upload.wikimedia.org/wikipedia/commons/thumb/8/83/Ray_trace_diagram.svg/512px-Ray_trace_diagram.svg.png"/></a>

Projet lanceur de rayons.

## Étapes du projet

Le projet se décompose en cinq phases distinctes. 

```plantuml
(*) -right-> "Initiale"
"Initiale" -right-> "Basique"
"Basique" -right-> "Réaliste"
"Réaliste" -right-> "Matrices"
"Matrices" -right-> "Efficacité"
"Efficacité" --> (*)
"Matrices" --> (*)
"Réaliste" --> (*)
"Réaliste" --> "Efficacité"
```

Il existe des dépendances entre les différentes activités, représentées ci-après :

```plantuml
(*) --> "Environnement"
"Environnement" --> ===B1===
===B1=== --> "Comparateur d'image"
===B1=== --> "Bibliothèque mathématique"
===B1=== --> "Lecteur des scènes"
"Comparateur d'image" --> ===B2===
"Bibliothèque mathématique" --> ===B2===
"Lecteur des scènes" --> ===B2===
===B2=== --> "Images 2D"
"Images 2D" --> "Images 3D"
"Images 3D" --> "Triangles et plans"
"Triangles et plans" --> ===B3===
===B3=== --> "Rendu"
===B3=== --> "Damier"
"Damier" --> ===B5===
===B5=== --> "Anti-crénelage"
===B5=== --> "Textures"
===B3=== --> "Transformations simples"
"Transformations simples" --> "Transformations complexes"
"Transformations complexes" --> "Transformations contextes"
===B3=== --> "Parallélisme"
"Parallélisme" --> ===B4===
"Transformations contextes" --> ===B4===
"Anti-crénelage" --> ===B4===
"Textures" --> ===B4===
"Rendu" --> ===B4===
===B4=== --> (*)
```

+ La phase `initiale` ne nécessite aucune connaissance spécifique. Il s'agit simplement de mettre en place les différents outils nécessaires à la réalisation du projet. Les premières lignes de code sont dédiées à des programmes utilitaires.
+ Un premier lanceur de rayons est construit dans la phase `basique`. Les images auront du relief sans être réalistes. Cette phase va nécessiter la compréhension de diverses formules et concepts optiques, mais ne pose pas de difficulté de programmation. Il s'agit de mettre en place le squelette du lanceur de rayons.
+ La phase `réaliste` a pour vocation de créer des images plus spectaculaires. Elles nécessiteront plus d'efforts de programmation : toute erreur de structure de données sera significatif.
+ La phase `matrice` est très mathématique (manipulation de matrices 3x3 et 4x4). Elle peut être évitée par les étudiants réfractaires aux mathématiques. Les images produites à ce niveau sont les plus belles.
+ La phase `efficacité` a pour but de réduire le temps de calcul des images, soit en parallélisant les calculs, soit en utilisant des structures de données dédiées plus efficaces.

|Phase | Nom        | Description                                                    |
|------|------------|----------------------------------------------------------------|
|Initiale|[Environnement](https://gitlab.univ-artois.fr/lanceurrayons/sujetlanceurrayons/-/blob/master/SUJETSTP/TP1a.markdown) | Mise en place du projet sur gitlab. | 
|Initiale|[Comparateur d'image](https://gitlab.univ-artois.fr/lanceurrayons/sujetlanceurrayons/-/blob/master/SUJETSTP/TP1b.markdown) | Réaliser un comparateur d'images pixel par pixel. |
|Initiale|[Bibliothèque mathématique](https://gitlab.univ-artois.fr/lanceurrayons/sujetlanceurrayons/-/blob/master/SUJETSTP/TP1c.markdown) | Réaliser une bibliothèque mathématique pour manipuler des triplets numériques. |
|Basique|[Lecteur des scènes](https://gitlab.univ-artois.fr/lanceurrayons/sujetlanceurrayons/-/blob/master/SUJETSTP/TP2.markdown)|Lecture des fichiers textes représentant les scènes 3D|
|Basique|[Images 2D](https://gitlab.univ-artois.fr/lanceurrayons/sujetlanceurrayons/-/blob/master/SUJETSTP/TP3.markdown)|Génération d'images avec des sphères en 2D|
|Basique|[Images 3D](https://gitlab.univ-artois.fr/lanceurrayons/sujetlanceurrayons/-/blob/master/SUJETSTP/TP4.markdown)|Ajout des sources de lumière, lumière diffuse|
|Basique|[Triangles et plans](https://gitlab.univ-artois.fr/lanceurrayons/sujetlanceurrayons/-/blob/master/SUJETSTP/TP5.markdown)|Ajout des triangles et des plans, calcul des ombres|
|Réaliste|[Rendu](https://gitlab.univ-artois.fr/lanceurrayons/sujetlanceurrayons/-/blob/master/SUJETSTP/TP6.markdown)|Phong et surfaces réfléchissantes|
|Réaliste|[Damier](https://gitlab.univ-artois.fr/lanceurrayons/sujetlanceurrayons/-/blob/master/SUJETSTP/TP10.markdown)|Ajout des textures procédurales sur un plan|
|Réaliste|[Anti-crénelage](https://gitlab.univ-artois.fr/lanceurrayons/sujetlanceurrayons/-/blob/master/SUJETSTP/TP11.markdown)|Éviter les effets d'escalier sur le damier|
|Réaliste|[Textures](https://gitlab.univ-artois.fr/lanceurrayons/sujetlanceurrayons/-/blob/master/SUJETSTP/TP13.markdown)|Appliquer des textures à des objets|
|Matrices|[Transformations simples](https://gitlab.univ-artois.fr/lanceurrayons/sujetlanceurrayons/-/blob/master/SUJETSTP/TP7.markdown)|Transformation des objets de base|
|Matrices|[Transformations complexes](https://gitlab.univ-artois.fr/lanceurrayons/sujetlanceurrayons/-/blob/master/SUJETSTP/TP8.markdown)|Appliquer plusieurs transformations à un même objet|
|Matrices|[Transformations contextes](https://gitlab.univ-artois.fr/lanceurrayons/sujetlanceurrayons/-/blob/master/SUJETSTP/TP9.markdown)|Transformations avec contexte|
|Efficacité|[Parallélisme](https://gitlab.univ-artois.fr/lanceurrayons/sujetlanceurrayons/-/blob/master/SUJETSTP/TP12.markdown)|Découpage de l'image pour rendu en parallèle|
|Efficacité|Optimisation|Utilisation des bounding box, arbres, etc|
