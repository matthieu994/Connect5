package Connect5Game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.Map;
import javafx.util.Pair;
import java.util.AbstractMap.SimpleEntry;

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
    private int profondeur = 3;

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

        Etat test = new Etat(grille.getData(), this.joueur);
        System.out.println("Evaluation: " + test.evalFunction());

        SimpleEntry<Double, Position> bestMove;
        bestMove = minimax(grille.getData(), this.profondeur, -10000.0, 10000.0, true);

        if (bestMove != null && bestMove.getValue() != null) {
            System.out.println(bestMove.getKey() + "   Meilleur coup: " + bestMove.getValue().toString());
            return bestMove.getValue();
        } else {
            System.out.println("PLACEMENT RANDOM");
            ArrayList<Position> casesvides = getCasesVides(grille.getData());
            int choix = random.nextInt(casesvides.size());
            return casesvides.get(choix);
        }
    }

    private SimpleEntry<Double, Position> minimax(byte[][] grille, int profondeur, double alpha, double beta,
            boolean isJoueur) {

        // Profondeur max atteinte
        if (profondeur == 0) {
            Etat feuille = new Etat(grille, getJoueur(!isJoueur));
            return new SimpleEntry<Double, Position>((double) feuille.evalFunction(), null);
        }

        ArrayList<Position> casesVides = getCasesVides(grille);
        // Noeud est une feuille
        if (casesVides.isEmpty()) {
            Etat feuille = new Etat(grille, getJoueur(!isJoueur));
            return new SimpleEntry<Double, Position>((double) feuille.evalFunction(), null);
        }

        byte[][] grilleCourant;
        SimpleEntry<Double, Position> bestMove;

        if (isJoueur) {
            bestMove = new SimpleEntry<Double, Position>(-10000.0, null);
            for (Position successeur : casesVides) {
                grilleCourant = deepCopy(grille);
                grilleCourant[successeur.ligne][successeur.colonne] = (byte) getJoueur(isJoueur);
                SimpleEntry<Double, Position> tempMove = minimax(grilleCourant, profondeur - 1, alpha, beta, !isJoueur);

                if (tempMove.getKey() > alpha) {
                    alpha = tempMove.getKey();
                }
                if (tempMove.getKey() >= beta) {
                    return tempMove;
                }
                if (tempMove.getKey() > bestMove.getKey()) {
                    bestMove = tempMove;
                    bestMove.setValue(successeur);
                }
            }
            System.out.println(profondeur + ": " + isJoueur + ": best move: " + bestMove.getValue()
                    + " : " + bestMove.getKey());
        } else {
            bestMove = new SimpleEntry<Double, Position>(10000.0, casesVides.get(0));
            for (Position successeur : casesVides) {
                grilleCourant = deepCopy(grille);
                grilleCourant[successeur.ligne][successeur.colonne] = (byte) getJoueur(isJoueur);
                SimpleEntry<Double, Position> tempMove = minimax(grilleCourant, profondeur - 1, alpha, beta, !isJoueur);

                if (tempMove.getKey() < beta) {
                    beta = tempMove.getKey();
                }
                if (tempMove.getKey() <= alpha) {
                    return tempMove;
                }
                if (tempMove.getKey() < bestMove.getKey()) {
                    bestMove = tempMove;
                    bestMove.setValue(successeur);
                }
            }
            System.out.println(profondeur + ": " + isJoueur + ": best move: " + bestMove.getValue()
                    + " : " + bestMove.getKey());
        }

        return bestMove;
    }

    private long getTime() {
        return System.currentTimeMillis() - this.start;
    }

    private ArrayList<Position> getCasesVides(byte[][] grille) {
        ArrayList<Position> casesvides = new ArrayList<Position>();

        int nbligne = grille.length;
        int nbcol = grille[0].length;

        for (int l = 0; l < nbligne; l++)
            for (int c = 0; c < nbcol; c++) {
                if (grille[l][c] == 0)
                    casesvides.add(new Position(l, c));
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

    private int getScore(byte[][] grille, boolean isJoueur) {
        Etat etat = new Etat(grille, getJoueur(isJoueur));
        return etat.evalFunction();
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
