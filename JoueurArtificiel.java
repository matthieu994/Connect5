package Connect5Game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.Map;
import java.util.AbstractMap.SimpleEntry;

import Connect5Game.Grille;
import Connect5Game.Joueur;
import Connect5Game.Position;
import Connect5Game.Noeud;
import Connect5Game.Groupe;
import Connect5Game.Etat;

public class JoueurArtificiel implements Joueur {

    private final Random random = new Random();

    /**
     * Voici la fonction à modifier. Évidemment, vous pouvez ajouter d'autres
     * fonctions dans JoueurArtificiel. Vous pouvez aussi ajouter d'autres classes,
     * mais elles doivent être ajoutées dans le package planeteH_2.ia. Vous ne
     * pouvez pas modifier les fichiers directement dans planeteH_2., car ils seront
     * écrasés.
     * 
     * @param grille Grille reçu (état courant). Il faut ajouter le prochain coup.
     * @param delais Délais de rélexion en temps réel.
     * @return Retourne le meilleur coup calculé.
     */
    @Override
    public Position getProchainCoup(Grille grille, int delais) {
        ArrayList<Integer> casesvides = new ArrayList<Integer>();
        int count = 0;
        int nbligne = grille.getData().length;
        int nbcol = grille.getData()[0].length;
        for (int l = 0; l < nbligne; l++)
            for (int c = 0; c < nbcol; c++) {
                if (grille.getData()[l][c] == 1 || grille.getData()[l][c] == 2)
                    count++;
                if (grille.getData()[l][c] == 0)
                    casesvides.add(l * nbcol + c);
            }

        int colonne = 0, ligne = 0;

        Etat etatInitial = new Etat(grille.getData(), (count % 2) + 1);
        etatInitial.genererEtat();

        // displayGroupes(etatInitial.listeGroupes);
        // displayGroupes(etatInitial.listeGroupesAdversaire);

        // System.out.println("Evaluation: " + etatInitial.evalFunction());

        minimax(etatInitial, casesvides);

        int choix = random.nextInt(casesvides.size());
        choix = casesvides.get(choix);
        return new Position(choix / nbcol, choix % nbcol);
    }

    private void minimax(Etat initial, ArrayList<Integer> casesvides) {
        ArrayList<Etat> listeEtats = new ArrayList<>();
        int nbcol = initial.grille[0].length;

        for (int position : casesvides) {
            Etat courant = new Etat(initial);
            courant.genererSuccesseur(position / nbcol, position % nbcol);
            // displayGroupes(courant.listeGroupes);
            listeEtats.add(courant);
        }

        Collections.sort(listeEtats);

        // System.out.println(listeEtats.size());
        System.out.println("best: " + listeEtats.get(0).evalFunction());
    }

    private void displayGroupes(ArrayList<Groupe> listeGroupes) {
        if (listeGroupes == null)
            return;

        for (Groupe groupe : listeGroupes) {
            System.out.print("Groupe " + groupe.type + ":");
            System.out.print("{ ");
            for (Map.Entry<Integer, Integer> caseEntry : groupe.cases) {
                System.out.print("[" + caseEntry.getKey() + "," + caseEntry.getValue() + "]");
            }
            System.out.println(" }");
        }
        System.out.println("-------------------------");
    }
}
