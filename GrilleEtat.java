package Connect5Game;

public class GrilleEtat {
    /**
     * On cherche si la grille présente un gagnant
     * @param grille
     * @param joueur
     * @return Retourne true/false si la grille est gagnante selon le joueur
     */
    public static boolean determineGagnant(byte[][] grille, int joueur) {
        int streak = 0; // Pions du joueur qui se suivent
        int streakAdverse = 0; // Pions du joueur adverse qui se suivent
        boolean joueurGagne = false;
        boolean adversaireGagne = false;
        int nbligne = grille.length;
        int nbcol = grille[0].length;
        int adversaire = (joueur == 1) ? 2 : 1;

        if (joueur == 0) {
            joueur = 1;
            adversaire = 2;
        }

        // Eval horizontale
        for (int i = 0; i < nbligne; i++) {
            for (int j = 0; j < nbcol; j++) {
                if (grille[i][j] == joueur) {
                    streak++;
                    streakAdverse = 0;
                    if (streak == 5)
                        joueurGagne = true;
                } else if (grille[i][j] == adversaire) {
                    streakAdverse++;
                    streak = 0;
                    if (streakAdverse == 5)
                        adversaireGagne = true;
                } else {
                    streak = streakAdverse = 0;
                }
            }
        }

        // Eval verticale
        for (int j = 0; j < nbcol; j++) {
            for (int i = 0; i < nbligne; i++) {
                if (grille[i][j] == joueur) {
                    streak++;
                    streakAdverse = 0;
                    if (streak == 5)
                        joueurGagne = true;
                } else if (grille[i][j] == adversaire) {
                    streakAdverse++;
                    streak = 0;
                    if (streakAdverse == 5)
                        adversaireGagne = true;
                } else {
                    streak = streakAdverse = 0;
                }
            }
        }

        // Eval diagonale 1
        for (int i = -nbligne; i < nbcol; i++) {
            int j = i;
            int i2 = 0;
            if (j < 0) {
                i2 = -j;
                j = 0;
            }
            for (; j < nbcol && i2 < nbligne; j++, i2++) {
                if (grille[i2][j] == joueur) {
                    streak++;
                    streakAdverse = 0;
                    if (streak == 5)
                        joueurGagne = true;
                } else if (grille[i2][j] == adversaire) {
                    streakAdverse++;
                    streak = 0;
                    if (streakAdverse == 5)
                        adversaireGagne = true;
                } else {
                    streak = streakAdverse = 0;
                }
            }
        }

        // Eval diagonale 2
        for (int i = -nbligne; i < nbcol; i++) {
            int j = i;
            int i2 = nbligne - 1;
            if (j < 0) {
                i2 += j;
                j = 0;
            }
            for (; j < nbcol && i2 >= 0; j++, i2--) {
                if (grille[i2][j] == joueur) {
                    streak++;
                    streakAdverse = 0;
                    if (streak == 5)
                        joueurGagne = true;
                } else if (grille[i2][j] == adversaire) {
                    streakAdverse++;
                    streak = 0;
                    if (streakAdverse == 5)
                        adversaireGagne = true;
                } else {
                    streak = streakAdverse = 0;
                }
            }
        }

        if (joueur == 0)
            return joueurGagne || adversaireGagne;
        else
            return joueurGagne;
    }

    /**
     * @param grille
     * @param joueur : joueur courant
     * @param adversaire
     * @return Valeur d'utilité selon l'évaluation horizontale + verticale + diagonale
     */
    public static int evalFunction(byte[][] grille, byte joueur, byte adversaire) {
        return evalHorizontal(grille, joueur, adversaire) + evalVertical(grille, joueur, adversaire)
                + evalDiagonal(grille, joueur, adversaire);
    }

    // Évalue groupes horizontaux
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

    // Évalue groupes en diagonale
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
