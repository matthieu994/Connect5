package Connect5Game;

public class GrilleEtat {
    protected static boolean compteExact = true;
    protected static int jetonPrecedent = 0;
    protected static int compteur = 0;
    protected static int quiGagne = 0;
    protected static int utilite = 0;
    protected static byte jeton = 0;
    protected static byte[][] data2;

    public static boolean determineGagnant(byte[][] data) {
        quiGagne = jetonPrecedent = 0; // reset status
        int nbligne = grille.length;
        int nbcol = grille[0].length;

        // horizontal
        for (int row = 0; row < nbligne; row++) {
            for (int c = 0; c < nbcol; c++) {
                check(data[row][c]);
            }
            check(0);
        }

        // vertical
        for (int c = 0; c < nbcol; c++) {
            for (int row = 0; row < nbligne; row++) {
                check(data[row][c]);
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
                check(data[row][c2]);
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
                check(data[row][c2]);
            }
            check(0);
        }

        return quiGagne;
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

    private static void setUtilite(int jetonCourrant, byte monJeton, byte oppJeton) {
        if (jetonCourrant == monJeton) {
            if (utilite == 0) {
                utilite = 1;
            } else {
                if (utilite < 0)
                    utilite = 0;
                else
                    utilite *= 10;
            }
        } else if (jetonCourrant == oppJeton) {
            if (utilite == 0) {
                utilite = -1;
            } else {
                if (utilite > 0)
                    utilite = 0;
                else
                    utilite *= 10;
            }
        }
    }

    public static int evaluation(byte[][] data, byte monJeton, byte oppJeton) {
        utilite = 0; // reset status
        int nbligne = data.length;
        int nbcol = data[0].length;
        int utilite2 = 0;
        // horizontal
        for (int row = 0; row < nbligne; row++) {
            for (int c = 0; c < nbcol; c++) {
                setUtilite(data[row][c], monJeton, oppJeton);
                utilite2 += utilite;
            }
            utilite = 0;
        }

        // vertical
        for (int c = 0; c < nbcol; c++) {
            for (int row = 0; row < nbligne; row++) {
                setUtilite(data[row][c], monJeton, oppJeton);
                utilite2 += utilite;
            }
            utilite = 0;
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
                setUtilite(data[row][c2], monJeton, oppJeton);
                utilite2 += utilite;
            }
            utilite = 0;
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
                setUtilite(data[row][c2], monJeton, oppJeton);
                utilite2 += utilite;
            }
            utilite = 0;
        }

        return utilite2;
    }

}
