package Connect5Game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.Map;
import javafx.util.Pair;

import Connect5Game.Grille;
import Connect5Game.Joueur;
import Connect5Game.Position;
import Connect5Game.Noeud;
import Connect5Game.Groupe;
import Connect5Game.Etat;

public class JoueurArtificiel implements Joueur {

    private final Random random = new Random();

    private long start; // Début fonction
    private long delais; // Délai de reflexion max
    private int joueur;

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
        this.delais = delais;
        this.start = System.currentTimeMillis();

        int count = 0;
        for (int l = 0; l < grille.getData().length; l++)
            for (int c = 0; c < grille.getData()[0].length; c++)
                if (grille.getData()[l][c] == 1 || grille.getData()[l][c] == 2)
                    count++;

        this.joueur = (count % 2) + 1;

        Etat etatInitial = new Etat(grille.getData(), this.joueur);
        etatInitial.genererEtat();

        // displayGroupes(etatInitial.listeGroupes);
        // displayGroupes(etatInitial.listeGroupesAdversaire);

        return minimax(etatInitial, grille.getData(), Integer.MIN_VALUE, Integer.MAX_VALUE, true);

        // int choix = random.nextInt(casesvides.size());
        // choix = casesvides.get(choix);
        // return new Position(choix / nbcol, choix % nbcol);
    }

    private Position minimax(Etat initial, byte[][] grille, int alpha, int beta, boolean isJoueur) {
        ArrayList<Integer> casesvides = getCasesVides(grille);
        ArrayList<Pair<Etat, Position>> listeEtats = new ArrayList<>();
        int nbcol = initial.grille[0].length;

        for (int casevide : casesvides) {
            Etat courant = new Etat(initial, getJoueur(isJoueur));
            Position position = new Position(casevide / nbcol, casevide % nbcol);
            courant.genererSuccesseur(position.ligne, position.colonne);
            // displayGroupes(courant.listeGroupes);
            // System.out.println("score: " + courant.evalFunction());
            listeEtats.add(new Pair<>(courant, position));
        }

        Collections.sort(listeEtats, new Comparator<Pair<Etat, Position>>() {
            public int compare(Pair<Etat, Position> e1, Pair<Etat, Position> e2) {
                return e2.getKey().evalFunction() - e1.getKey().evalFunction();
            }
        });

        // System.out.println("taille: " + listeEtats.size());
        // System.out.println("best: " + listeEtats.get(0).getKey().evalFunction());

        grille[listeEtats.get(0).getValue().ligne][listeEtats.get(0).getValue().colonne] = (byte) getJoueur(isJoueur);

        return listeEtats.get(0).getValue();
        // if (getJoueur(isJoueur) == this.joueur) {
        //     if (getTime() >= delais || casesvides.size() <= 2)
        //         return listeEtats.get(0).getValue();
        //     else
        //         return minimax(listeEtats.get(0).getKey(), grille, alpha, beta, !isJoueur);
        // } else
        //     return minimax(listeEtats.get(0).getKey(), grille, alpha, beta, !isJoueur);
    }

    private long getTime() {
        return System.currentTimeMillis() - this.start;
    }

    private ArrayList<Integer> getCasesVides(byte[][] grille) {
        ArrayList<Integer> casesvides = new ArrayList<Integer>();

        int nbligne = grille.length;
        int nbcol = grille[0].length;

        for (int l = 0; l < nbligne; l++)
            for (int c = 0; c < nbcol; c++) {
                if (grille[l][c] == 0)
                    casesvides.add(l * nbcol + c);
            }

        return casesvides;
    }

    private int getJoueur(boolean isJoueur) {
        if (isJoueur)
            return this.joueur;
        else
            return (this.joueur == 1) ? 2 : 1;
    }

    public static void displayGroupes(ArrayList<Groupe> listeGroupes) {
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
