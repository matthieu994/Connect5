package Connect5Game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.Map;
import java.util.AbstractMap.SimpleEntry;

import Connect5Game.Grille;
import Connect5Game.Joueur;
import Connect5Game.Position;
import Connect5Game.GrilleEtat;
import Connect5Game.Etat;

public class JoueurArtificiel implements Joueur {

    private final Random random = new Random();

    private long start; // Début fonction
    private int joueur;
    private int profondeur;

    /**
     * Fonction à modifier
     * 
     * @param grille Grille reçu (état courant). Il faut ajouter le prochain coup.
     * @param delais Délais de rélexion en temps réel.
     * @return Retourne le meilleur coup calculé.
     */
    @Override
    public Position getProchainCoup(Grille grille, int delais) {
        this.start = System.currentTimeMillis();

        // Calcul des pierres pour obtenir le joueur courant et l'adversaire
        int count = 0;
        for (int l = 0; l < grille.getData().length; l++)
            for (int c = 0; c < grille.getData()[0].length; c++)
                if (grille.getData()[l][c] == 1 || grille.getData()[l][c] == 2)
                    count++;

        this.joueur = (count % 2) + 1;

        /*
        * Si le joueur artificiel courant est le premier à jouer,
        * on renvoie la meilleure position à une profondeur 1
        */
        if (count == 0)
            this.profondeur = 2;
        else
            this.profondeur = 1;

        SimpleEntry<Integer, Position> bestMove;
        
        /* Approche iterative deepening
        do {
            bestMove = minimax(grille.getData(), this.profondeur, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
            this.profondeur++;
        } while (getTime() < delais); */

        // Approche alternative
        if(count != 0)
            this.profondeur = 

        /*
         * - Cas normal: On renvoie la meilleure position trouvée.
         * - Dans le cas d'un bug non attendu : on renvoie une position aléatoire
         */
        if (bestMove.getValue().ligne != -1)
            return bestMove.getValue();
        else {
            System.out.println("-----RANDOM-----");
            ArrayList<Position> casesvides = getCasesVides(grille.getData());
            int choix = random.nextInt(casesvides.size());
            return casesvides.get(choix);
        }
    }

    /**
    * Algorithme minimax
    * 
    * @param grille Grille reçu (état courant). Il faut ajouter le prochain coup.
    * @param profondeur Profondeur courante.
    * @param isJoueur Joueur dont on veut maximiser le coup.
    * @return Retourne le meilleur coup calculé et l'évaluation correspondante.
    */
    private SimpleEntry<Integer, Position> minimax(byte[][] grille, int profondeur, int alpha, int beta,
            boolean isJoueur) {

        ArrayList<Position> casesVides = getCasesVides(grille);
        Position currentBest = new Position(-1, -1);
        int evaluation;
        boolean isMax = (getJoueur(isJoueur) == this.joueur);

        /*
         * Aucune case n'est vide (partie terminée) ou la profondeur max est atteinte, 
         * ou une configuration gagnante est trouvée :
         * -> On renvoie l'evaluation de la grille courante pour le joueur artificiel,
         * et la position (-1,-1).
         * 
         * Sinon: on itère sur la liste des cases vides
         */
        if (casesVides.isEmpty() || profondeur == 0) {
            evaluation = GrilleEtat.evalFunction(grille, (byte) getJoueur(true), (byte) getJoueur(false));
            return new SimpleEntry<Integer, Position>(evaluation, currentBest);
        } else {
            for (Position position : casesVides) {
                grille[position.ligne][position.colonne] = (byte) getJoueur(isJoueur);
                // Le joueur dont on veut maximiser le coup est le joueur courant : MAX
                if (isMax) {
                    evaluation = minimax(grille, profondeur - 1, alpha, beta, !isJoueur).getKey();
                    if (evaluation > alpha) {
                        alpha = evaluation;
                        currentBest = position;
                    }
                    // Le joueur dont on veut maximiser le coup est l'adversaire : MIN
                } else {
                    evaluation = minimax(grille, profondeur - 1, alpha, beta, !isJoueur).getKey();
                    if (evaluation < beta) {
                        beta = evaluation;
                        currentBest = position;
                    }
                }
                // On reset le dernier coup simulé dans la grille courante
                grille[position.ligne][position.colonne] = 0;
                // Élagage
                if (alpha >= beta)
                    break;
            }
        }
        // On retourne la meilleure position associée à l'évaluation correspondante au joueur (Max: alpha, Min: beta)
        if (isMax)
            return new SimpleEntry<Integer, Position>(alpha, currentBest);
        else
            return new SimpleEntry<Integer, Position>(beta, currentBest);
    }

    // Retourne le temps écoulé depuis le début
    private long getTime() {
        return System.currentTimeMillis() - this.start;
    }

    // Retourne la liste des cases vides disponibles
    private ArrayList<Position> getCasesVides(byte[][] grille) {
        ArrayList<Position> casesvides = new ArrayList<Position>();

        // Si une configuration gagnante est trouvée
        boolean winGrille = GrilleEtat.determineGagnant(grille);
        if (winGrille) {
            // System.out.println("config win");
            return casesvides;
        }

        int nbligne = grille.length;
        int nbcol = grille[0].length;

        for (int l = 0; l < nbligne; l++)
            for (int c = 0; c < nbcol; c++)
                if (grille[l][c] == 0)
                    casesvides.add(new Position(l, c));

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

    // private double getScore(byte[][] grille, boolean isJoueur) {
    // Etat etat = new Etat(grille, getJoueur(isJoueur));
    // return etat.evalFunction();
    // }

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
