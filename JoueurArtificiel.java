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
        long start = System.currentTimeMillis();

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
        if (count <= 1)
            return getCasesVides(grille.getData()).get(0);

        SimpleEntry<Integer, Position> bestMove;

        /* Approche iterative deepening
        this.profondeur = 1;
        do {
            bestMove = minimax(grille.getData(), this.profondeur, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
            this.profondeur++;
        } while (getTime(start) < delais); */

        /* Approche alternative selon un calcul estimatif
        * Pour delais = 2000 et grille: 14 => profondeur = 2
        * Pour delais = 2000 et grille: 12 => profondeur = 3
        * Pour delais = 2000 et grille: 7 => profondeur = 4
        */
        if (count != 0)
            this.profondeur = (int) Math.ceil(Math.log(delais / grille.nbLibre()));

        bestMove = minimax(grille.getData(), this.profondeur, Integer.MIN_VALUE, Integer.MAX_VALUE, true);

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
            // Le joueur dont on veut maximiser le coup est le joueur courant : MAX
            if (isMax) {
                for (Position position : casesVides) {
                    grille[position.ligne][position.colonne] = (byte) getJoueur(true);

                    // Si le joueur artificiel peut jouer un coup gagnant, on le retourne immédiatement
                    if (GrilleEtat.determineGagnant(grille, getJoueur(true)) && profondeur == this.profondeur)
                        return new SimpleEntry<Integer, Position>(alpha, position);

                    evaluation = minimax(grille, profondeur - 1, alpha, beta, false).getKey();
                    if (evaluation > alpha) {
                        alpha = evaluation;
                        currentBest = position;
                    }
                    // On reset le dernier coup simulé dans la grille courante
                    grille[position.ligne][position.colonne] = 0;
                    // Élagage
                    if (alpha >= beta)
                        break;
                }
                // On retourne la meilleure position associée à l'évaluation correspondante au joueur (Max: alpha, Min: beta)
                return new SimpleEntry<Integer, Position>(alpha, currentBest);
            } else {
                // Le joueur dont on veut maximiser le coup est l'adversaire : MIN
                for (Position position : casesVides) {
                    grille[position.ligne][position.colonne] = (byte) getJoueur(false);

                    evaluation = minimax(grille, profondeur - 1, alpha, beta, true).getKey();
                    if (evaluation < beta) {
                        beta = evaluation;
                        currentBest = position;
                    }
                    // On reset le dernier coup simulé dans la grille courante
                    grille[position.ligne][position.colonne] = 0;
                    // Élagage
                    if (alpha >= beta)
                        break;
                }
                // On retourne la meilleure position associée à l'évaluation correspondante au joueur (Max: alpha, Min: beta)
                return new SimpleEntry<Integer, Position>(beta, currentBest);
            }
        }
    }

    // Retourne le temps écoulé depuis le début
    private long getTime(long start) {
        return System.currentTimeMillis() - start;
    }

    // Retourne la liste des cases vides disponibles
    private ArrayList<Position> getCasesVides(byte[][] grille) {
        ArrayList<Position> casesvides = new ArrayList<Position>();

        // Si une configuration gagnante est trouvée
        boolean winGrille = GrilleEtat.determineGagnant(grille, 0);
        if (winGrille) {
            System.out.println("config win");
            return casesvides;
        }

        int nbligne = grille.length;
        int nbcol = grille[0].length;

        // On ajoute toutes les cases disponibles
        for (int l = 0; l < nbligne; l++)
            for (int c = 0; c < nbcol; c++)
                if (grille[l][c] == 0)
                    casesvides.add(new Position(l, c));

        return casesvides;
    }

    /**
     * @return true: joueur courant,
     *         false: adversaire
     */
    private int getJoueur(boolean isJoueur) {
        if (isJoueur)
            return this.joueur;
        else
            return (this.joueur == 1) ? 2 : 1;
    }
}
