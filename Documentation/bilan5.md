# Bilan [Livraison 5](https://github.com/uca-m1informatique-softeng/M1-S1-7W-vamos/milestone/6) :

- [X] Implementation du Design Pattern Strategy :   
La structure du jeu nous laisse pressentir la création d'une classe Joueur, représentant un individu acteur de la partie, affrontant d'autres joueurs.
Dans le cadre du sujet, il nous est demandé d'implémenter des "Bots", c'est à dire des joueurs ayant une sorte d'Intelligence Artificielle très basique.
Comment ces Bots sont censés jouer au jeu ? On peut par exemple penser à un joueur jouant des coups de manière aléatoire, ou bien un joueur priorisant les cartes militaires ou scientifiques par exemple.
L'implémentation du Joueur, et de ces multiples façons de jouer, pose un problème de conception. Plusieurs Design Patterns s'offrent à nous pour résoudre ce problème :
- Le Design Pattern Template, implémenté par une classe abstraite player n'ayant que les méthodes permettant de décider du coup à jouer qui seraient abstraites, dont hériteraient par exemple DumbPlayer, MilitaryPlayer, ou bien encore SciencePlayer.
- Le Design Pattern Strategy, implémenté par un attribut de type Strategy, classe abstraite définissant le coup à jouer en fonction des informations que possède le joueur, dont hériteraient alors DumbStrategy, MilitaryStrategy, ou ScienceStrategy.
Strategy nous propose plusieurs avantages par rapport à Template : Il serait possible de changer de stratégie en cours de jeu, le code serait plus simple à lire (et donc mieux maintenable), et l'instanciation d'un joueur ne diffèrerait pas en fonction de la stratégie qu'il devrait appliquer.
Ainsi, nous choisissons d'utiliser le Design Pattern Strategy pour implémenter l'IA primitive de nos joueurs dans notre soumission du projet, dans le package player.
- [X] Le client se connecte au serveur et lui envoi les statistiques .
- [X] Création de plusieurs modules
- [X] Configuration SonarQube pour un projet multi-modules 
- [X] Ajout de tout les merveilles
- [X] Ajout de quelques effets des étapes de merveilles

-----------------------------------------------
Quelques tests ont éte fait/corriger
  
-----------------------------------------------
À améliorer : 
- le client va envoyer au serveur les données brut et c'est le serveur qui traitera ces données
- faire plus de tests
-----------------------------------------------