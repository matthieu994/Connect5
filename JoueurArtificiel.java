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

        // displayGroupes(etatInitial.listeGroupes);
        // displayGroupes(etatInitial.listeGroupesAdversaire);

        int profondeur = 1;
        double bestValue = 0;

        while (getTime() < this.delais) {
            bestValue = minimax(grille.getData(), profondeur, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, true);
            profondeur++;
            // System.out.println(profondeur);
        }
        System.out.println("Profondeur: " + profondeur + " Valeur: " + bestValue);

        Etat etatInitial = new Etat(grille.getData(), this.joueur);
        etatInitial.genererEtat();
        ArrayList<Integer> casesvides = getCasesVides(grille.getData());
        ArrayList<Pair<Etat, Position>> listeSuccesseurs = getSuccesseurs(grille.getData(), casesvides, joueur);

        for (Pair<Etat, Position> successeur : listeSuccesseurs) {
            System.out.println("Successeur: " + successeur.getValue().ligne + "," + successeur.getValue().colonne
                    + " : " + successeur.getKey().evalFunction());
            if (successeur.getKey().evalFunction() == bestValue)
                return successeur.getValue();
        }

        int nbcol = grille.getData()[0].length;
        int choix = random.nextInt(casesvides.size());
        choix = casesvides.get(choix);
        System.out.println("Random");
        return new Position(choix / nbcol, choix % nbcol);
    }

    private double minimax(byte[][] grille, int profondeur, double alpha, double beta, boolean isJoueur) {
        // Arreter si temps dépassé ou grille explorée
        ArrayList<Integer> casesvides = getCasesVides(grille);

        // Noeud est une feuille ou profondeur maximal atteinte
        if (profondeur == 0 || casesvides.size() <= 1) {
            Etat initial = new Etat(grille, getJoueur(isJoueur));
            initial.genererEtat();
            return initial.evalFunction();
        }

        double value;
        byte[][] grilleCourant;
        ArrayList<Pair<Etat, Position>> listeSuccesseurs = getSuccesseurs(grille, casesvides, getJoueur(isJoueur));

        if (isJoueur) {
            value = Double.NEGATIVE_INFINITY;
            for (Pair<Etat, Position> successeur : listeSuccesseurs) {
                grilleCourant = deepCopy(grille);
                grilleCourant[successeur.getValue().ligne][successeur.getValue().colonne] = (byte) getJoueur(isJoueur);
                value = Math.max(value, minimax(grilleCourant, profondeur - 1, alpha, beta, !isJoueur));
                alpha = Math.max(alpha, value);
                if (alpha >= beta)
                    break;
            }
            return value;
        } else {
            value = Double.POSITIVE_INFINITY;
            for (Pair<Etat, Position> successeur : listeSuccesseurs) {
                grilleCourant = deepCopy(grille);
                grilleCourant[successeur.getValue().ligne][successeur.getValue().colonne] = (byte) getJoueur(isJoueur);
                value = Math.min(value, minimax(grilleCourant, profondeur - 1, alpha, beta, !isJoueur));
                beta = Math.min(beta, value);
                if (alpha >= beta)
                    break;
            }
            return value;
        }
    }

    private long getTime() {
        return System.currentTimeMillis() - this.start;
    }

    private ArrayList<Pair<Etat, Position>> getSuccesseurs(byte[][] grille, ArrayList<Integer> casesvides, int joueur) {
        ArrayList<Pair<Etat, Position>> listeSuccesseurs = new ArrayList<>();
        int nbcol = grille[0].length;

        for (int casevide : casesvides) {
            Etat courant = new Etat(grille, joueur);
            Position position = new Position(casevide / nbcol, casevide % nbcol);
            courant.genererSuccesseur(position.ligne, position.colonne);
            listeSuccesseurs.add(new Pair<>(courant, position));
        }
        return listeSuccesseurs;
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

    public static byte[][] deepCopy(byte[][] input) {
        if (input == null)
            return null;
        byte[][] result = new byte[input.length][];
        for (byte r = 0; r < input.length; r++) {
            result[r] = input[r].clone();
        }
        return result;
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
