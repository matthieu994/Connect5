package Connect5Game;

public class GrilleEtat {
    protected static boolean compteExact = true;
    protected static int jetonPrecedent = 0;
    protected static int compteur = 0;
    protected static int quiGagne = 0;
    protected static int utilite = 0;
    protected static byte jeton = 0;
    protected static byte[][] data2;

    public static boolean determineGagnant(byte[][] grille) {
        quiGagne = jetonPrecedent = 0; // reset status
        int nbligne = grille.length;
        int nbcol = grille[0].length;

        // horizontal
        for (int row = 0; row < nbligne; row++) {
            for (int c = 0; c < nbcol; c++) {
                check(grille[row][c]);
            }
            check(0);
        }

        // vertical
        for (int c = 0; c < nbcol; c++) {
            for (int row = 0; row < nbligne; row++) {
                check(grille[row][c]);
            }
            check(0);
        }

        // Diagonal \\\\\\\
        for (int c = -nbligne; c < nbcol; c++) {
            int c2 = c;
            int row = 0;
            if (c2 < 0) {
                row = -c2;
                c2 = 0;
            }
            for (; c2 < nbcol && row < nbligne; c2++, row++) {
                check(grille[row][c2]);
            }
            check(0);
        }

        // Diagonal //////
        for (int c = -nbligne; c < nbcol; c++) {
            int c2 = c;
            int row = nbligne - 1;
            if (c2 < 0) {
                row += c2;
                c2 = 0;
            }
            for (; c2 < nbcol && row >= 0; c2++, row--) {
                check(grille[row][c2]);
            }
            check(0);
        }

        return quiGagne > 0;
    }

    private static void check(int value) {
        if (value == jetonPrecedent) {
            compteur++;
        } else {
            if (jetonPrecedent > 0 && (compteExact ? compteur == 5 : compteur >= 5)) {
                quiGagne = jetonPrecedent;
            }
            compteur = 1;
            jetonPrecedent = value;
        }
    }

    public static int evalFunction(byte[][] grille, byte joueur, byte adversaire) {
        return evalHorizontal(grille, joueur, adversaire) + evalVertical(grille, joueur, adversaire)
                + evalDiagonal(grille, joueur, adversaire);
    }

    public static int evalHorizontal(byte[][] grille, byte joueur, byte adversaire) {
        int streak = 0; // Pions du joueur qui se suivent
        int streakAdverse = 0; // Pions du joueur adverse qui se suivent
        int score = 0;
        int scoreAdverse = 0;

        for (int i = 0; i < grille.length; i++) {
            for (int j = 0; j < grille[0].length; j++) {
                if (grille[i][j] == joueur) {
                    streak++;
                    scoreAdverse += getGroupScore(streakAdverse);
                    streakAdverse = 0;
                } else if (grille[i][j] == adversaire) {
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
    public static int evalVertical(byte[][] grille, byte joueur, byte adversaire) {
        int streak = 0; // Pions du joueur qui se suivent
        int streakAdverse = 0; // Pions du joueur adverse qui se suivent
        int score = 0;
        int scoreAdverse = 0;

        for (int j = 0; j < grille[0].length; j++) {
            for (int i = 0; i < grille.length; i++) {
                if (grille[i][j] == joueur) {
                    streak++;
                    scoreAdverse += getGroupScore(streakAdverse);
                    streakAdverse = 0;
                } else if (grille[i][j] == adversaire) {
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
    public static int evalDiagonal(byte[][] grille, byte joueur, byte adversaire) {
        int streak = 0; // Pions du joueur qui se suivent
        int streakAdverse = 0; // Pions du joueur adverse qui se suivent
        int score = 0;
        int scoreAdverse = 0;

        // Depuis en bas à gauche vers en haut à droite
        for (int k = 0; k <= 2 * (grille.length - 1); k++) {
            int iStart = Math.max(0, k - grille.length + 1);
            int iEnd = Math.min(grille.length - 1, k);
            for (int i = iStart; i <= iEnd; ++i) {
                int j = k - i;

                if (grille[i][j] == joueur) {
                    streak++;
                    scoreAdverse += getGroupScore(streakAdverse);
                    streakAdverse = 0;
                } else if (grille[i][j] == adversaire) {
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
        for (int k = 1 - grille.length; k < grille.length; k++) {
            int iStart = Math.max(0, k);
            int iEnd = Math.min(grille.length + k - 1, grille.length - 1);
            for (int i = iStart; i <= iEnd; ++i) {
                int j = i - k;

                if (grille[i][j] == joueur) {
                    streak++;
                    scoreAdverse += getGroupScore(streakAdverse);
                    streakAdverse = 0;
                } else if (grille[i][j] == adversaire) {
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

    private static int getGroupScore(int streak) {
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
