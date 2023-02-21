# Projet Snake

### Design pattern utilisés :

- Observateur : permet la mise à jour de l'affichage quand les données jeu est mis à jour
- État : change comment les fonctions du controller fonctionne en fonction de l'état dans lequel est celui-ci
- Fabrique : gestion de plusieurs constructeurs pour la classe Snake
- Stratégie : change comment un snake va choisir ses mouvements en fonction du mode de jeu choisi
- Patron : change les comportements d'un snake en fonction de quel item il a ramassé
  - Le design pattern Stratégie aurait aussi pu être utilisé, cependant beaucoup de fonctions étaient communes et j'ai donc trouvé plus pertinent d'utiliser le design pattern Patron pour factoriser le code

### Fonctions nouvelles ajoutées :
- Mode de jeu où un ou deux joueurs peuvent contrôler leur Snake
- Un Snake a 3 vies avant d'être définitivement éliminé de la partie
  - Affichage des vies restantes d'un joueur
  - La couleur des cœurs change en fonction de l'effet sous lequel est le Snake
    - Rouge quand il est normal
    - Violet quand il est invincible
    - Jaune quand il est malade
  - Le jeu prend fin quand tous les tours ont été joué ou quand tous les Snakes ont perdu toutes leurs vies
- Affichage d'un menu avant le lancement du jeu
  - Choix du mode de jeu entre le random et le fait de contrôler son Snake
  - Choix du nombre de tours d'une partie
  - Choix de la map de jeu en faisant une recherche de fichier