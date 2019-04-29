package Connect5Game;

import java.util.ArrayList;
import java.util.Map;
import java.util.AbstractMap.SimpleEntry;

public class Etat {
    final byte[][] grille; // Grille courante
    private int joueur; // Contient le joueur courant
    private int adversaire; // Valeur adversaire

    public Etat(byte[][] grille, int joueur) {
        this.grille = grille;
        this.joueur = joueur;
        this.adversaire = (joueur == 1) ? 2 : 1;
    }

    public int evalFunction() {
        return evalHorizontal() + evalVertical() + evalDiagonal();
    }

    // // Évalue les groupes seuls
    // private int evalSeuls() {
    // int score = 0;
    // int scoreAdverse = 0;

    // for (int i = 0; i < this.grille.length; i++) {
    // for (int j = 0; j < this.grille[0].length; j++) {
    // if (this.grille[i][j] == joueur)
    // score-++;
    // else if (this.grille[i][j] == adversaire)
    // scoreAdverse--;
    // }
    // }

    // System.out.println("Groupes seuls: " + -score + " | " + -scoreAdverse);
    // return scoreAdverse - score;
    // }

    // Évalue les groupes horizontaux
    private int evalHorizontal() {
        int streak = 0; // Pions du joueur qui se suivent
        int streakAdverse = 0; // Pions du joueur adverse qui se suivent
        int score = 0;
        int scoreAdverse = 0;

        for (int i = 0; i < this.grille.length; i++) {
            for (int j = 0; j < this.grille[0].length; j++) {
                if (this.grille[i][j] == joueur) {
                    streak++;
                    scoreAdverse += getGroupScore(streakAdverse);
                    streakAdverse = 0;
                }
                if (this.grille[i][j] == adversaire) {
                    streakAdverse++;
                    score += getGroupScore(streak);
                    streak = 0;
                } else {
                    score += getGroupScore(streak);
                    scoreAdverse += getGroupScore(streakAdverse);
                    streak = streakAdverse = 0;
                }
            }
        }

        // System.out.println("Groupes Horizontal: " + score + " | " + scoreAdverse);
        return score - scoreAdverse;
    }

    // Évalue groupes verticaux
    private int evalVertical() {
        int streak = 0; // Pions du joueur qui se suivent
        int streakAdverse = 0; // Pions du joueur adverse qui se suivent
        int score = 0;
        int scoreAdverse = 0;

        for (int j = 0; j < this.grille[0].length; j++) {
            for (int i = 0; i < this.grille.length; i++) {
                if (this.grille[i][j] == joueur) {
                    streak++;
                    scoreAdverse += getGroupScore(streakAdverse);
                    streakAdverse = 0;
                }
                if (this.grille[i][j] == adversaire) {
                    streakAdverse++;
                    score += getGroupScore(streak);
                    streak = 0;
                } else {
                    score += getGroupScore(streak);
                    scoreAdverse += getGroupScore(streakAdverse);
                    streak = streakAdverse = 0;
                }
            }
        }

        // System.out.println("Groupes Vertical: " + score + " | " + scoreAdverse);
        return score - scoreAdverse;
    }

    // Évalue groupes en diagonal
    private int evalDiagonal() {
        int streak = 0; // Pions du joueur qui se suivent
        int streakAdverse = 0; // Pions du joueur adverse qui se suivent
        int score = 0;
        int scoreAdverse = 0;

        // Depuis en bas à gauche vers en haut à droite
        for (int k = 0; k <= 2 * (this.grille.length - 1); k++) {
            int iStart = Math.max(0, k - this.grille.length + 1);
            int iEnd = Math.min(this.grille.length - 1, k);
            for (int i = iStart; i <= iEnd; ++i) {
                int j = k - i;

                if (this.grille[i][j] == joueur) {
                    streak++;
                    scoreAdverse += getGroupScore(streakAdverse);
                    streakAdverse = 0;
                }
                if (this.grille[i][j] == adversaire) {
                    streakAdverse++;
                    score += getGroupScore(streak);
                    streak = 0;
                } else {
                    score += getGroupScore(streak);
                    scoreAdverse += getGroupScore(streakAdverse);
                    streak = streakAdverse = 0;
                }
            }
        }

        // Depuis en haut à gauche vers en bas à droite
        for (int k = 1 - this.grille.length; k < this.grille.length; k++) {
            int iStart = Math.max(0, k);
            int iEnd = Math.min(this.grille.length + k - 1, this.grille.length - 1);
            for (int i = iStart; i <= iEnd; ++i) {
                int j = i - k;

                if (this.grille[i][j] == joueur) {
                    streak++;
                    scoreAdverse += getGroupScore(streakAdverse);
                    streakAdverse = 0;
                }
                if (this.grille[i][j] == adversaire) {
                    streakAdverse++;
                    score += getGroupScore(streak);
                    streak = 0;
                } else {
                    score += getGroupScore(streak);
                    scoreAdverse += getGroupScore(streakAdverse);
                    streak = streakAdverse = 0;
                }
            }
        }

        // System.out.println("Groupes Diagonal: " + score + " | " + scoreAdverse);
        return score - scoreAdverse;
    }

    private int getGroupScore(int streak) {
        if (streak > 5 || streak == 0)
            return 0;

        if (streak == 5)
            return 10000;
        if (streak == 4)
            return 1000;
        if (streak == 3)
            return 100;
        if (streak == 2)
            return 10;
        return 1; // streak = 1
    }
}