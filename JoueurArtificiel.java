package Connect5Game;

import java.util.ArrayList;
import java.util.Random;

import Connect5Game.Grille;
import Connect5Game.Joueur;
import Connect5Game.Position;

public class JoueurArtificiel implements Joueur {

    private final Random random = new Random();

    /**
     * Voici la fonction à modifier. Évidemment, vous pouvez ajouter d'autres
     * fonctions dans JoueurArtificiel. Vous pouvez aussi ajouter d'autres classes,
     * mais elles doivent être ajoutées dans le package planeteH_2.ia. Vous ne
     * pouvez pas modifier les fichiers directement dans planeteH_2., car ils seront
     * écrasés.
     * 
     * @param grille Grille reçu (état courrant). Il faut ajouter le prochain coup.
     * @param delais Délais de rélexion en temps réel.
     * @return Retourne le meilleur coup calculé.
     */

    @Override
    public Position getProchainCoup(Grille grille, int delais) {
        ArrayList<Integer> casesvides = new ArrayList<Integer>();
        int nbcol = grille.getData()[0].length;
        for (int l = 0; l < grille.getData().length; l++)
            for (int c = 0; c < nbcol; c++)
                if (grille.getData()[l][c] == 0)
                    casesvides.add(l * nbcol + c);
        // int choix = random.nextInt(casesvides.size());
        // choix = casesvides.get(choix);
        // return new Position(choix / nbcol, choix % nbcol);
        int colonne = 0, ligne = 0;

        return new Position(colonne, ligne);
    }

}
