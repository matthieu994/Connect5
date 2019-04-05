package Connect5Game;

import java.util.ArrayList;

public class Groupe {

    private class Direction {
        private int l = 0;
        private int c = 0;
        int type;

        Direction(int type) {
            this.type = type;
            if (type == type1)
                l = 1;
            else if (type == type2)
                c = 1;
            else if (type == type3) {
                l = 1;
                c = 1;
            } else if (type == type4) {
                l = 1;
                c = -1;
            }
        }
    }

    private ArrayList<Noeud> noeuds;
    private static final int type1 = 0; // Configuration horizontale
    private static final int type2 = 1; // Configuration verticale
    private static final int type3 = 2; // Configuration oblique gauche droite
    private static final int type4 = 3; // Configuration oblique droite gauche
    private Direction direction;
    private int joueur;
    private int score;

    private Groupe(Noeud n1, int _type) {
        direction = new Direction(_type);
        joueur = n1.getData();
        noeuds = new ArrayList<>();
        noeuds.add(n1);
        n1.addGroupe(this);
    }

    int getType() {
        return direction.type;
    }

    private Noeud avancer(Position pos, Noeud[][] noeuds) {
        int l = pos.ligne + direction.l;
        int c = pos.colonne + direction.c;

        if (l > noeuds.length || c < 0 || c > noeuds[l].length)
            return null;
        return noeuds[l][c];
    }

    private Noeud reculer(Position pos, Noeud[][] noeuds) {
        int l = pos.ligne - direction.l;
        int c = pos.colonne - direction.c;

        if (l < 0 || c < 0 || c > noeuds[l].length)
            return null;
        return noeuds[l][c];
    }

    public static void initerGroupe(Noeud[][] noeuds, int l, int c) {
        Noeud courant = noeuds[l][c];

        if (l - 1 >= 0) {
            if (noeuds[l - 1][c].sameData(courant) && courant.hasNoGroup(Groupe.type1)) {
                Groupe res = new Groupe(courant, Groupe.type1);
                res.completerGroupe(noeuds);
            }
            if (c - 1 >= 0 && noeuds[l - 1][c - 1].sameData(courant) && courant.hasNoGroup(Groupe.type3)) {
                Groupe res1 = new Groupe(courant, Groupe.type3);
                res1.completerGroupe(noeuds);
            }
            if (c + 1 < noeuds[l - 1].length && noeuds[l - 1][c + 1].sameData(courant) && courant.hasNoGroup(Groupe.type4)) {
                Groupe res1 = new Groupe(courant, Groupe.type4);
                res1.completerGroupe(noeuds);
            }
        }
        if (l + 1 < noeuds.length) {
            if (noeuds[l + 1][c].sameData(courant) && courant.hasNoGroup(Groupe.type1)) {
                Groupe res = new Groupe(courant, Groupe.type1);
                res.completerGroupe(noeuds);
            }
            if (c - 1 >= 0 && noeuds[l + 1][c - 1].sameData(courant) && courant.hasNoGroup(Groupe.type4)) {
                Groupe res1 = new Groupe(courant, Groupe.type4);
                res1.completerGroupe(noeuds);
            }
            if (c + 1 < noeuds[l + 1].length && noeuds[l + 1][c + 1].sameData(courant) && courant.hasNoGroup(Groupe.type3)) {
                Groupe res1 = new Groupe(courant, Groupe.type3);
                res1.completerGroupe(noeuds);
            }
        }

        if (c - 1 >= 0 && noeuds[l][c - 1].sameData(courant) && courant.hasNoGroup(Groupe.type2)) {
            Groupe res = new Groupe(courant, Groupe.type2);
            res.completerGroupe(noeuds);
        }
        if (c + 1 < noeuds[l].length && noeuds[l][c + 1].sameData(courant) && courant.hasNoGroup(Groupe.type2)) {
            Groupe res = new Groupe(courant, Groupe.type2);
            res.completerGroupe(noeuds);
        }
    }

    private void completerGroupe(Noeud[][] noeuds) {
        Noeud ref = this.noeuds.get(0);
        Noeud courant = avancer(ref.pos, noeuds);
        while (courant != null && courant.sameData(ref)) {
            courant.addGroupe(this);
            this.noeuds.add(courant);
            courant = avancer(courant.pos, noeuds);
        }

        courant = reculer(ref.pos, noeuds);
        while (courant != null && courant.sameData(ref)) {
            courant.addGroupe(this);
            this.noeuds.add(courant);
            courant = reculer(courant.pos, noeuds);
        }

        score = (int) Math.pow(10, this.noeuds.size());
        afficher();
    }

    private void afficher() {
        System.out.println("Groupe ** ");
        for (Noeud n : noeuds) {
            System.out.println("joueur #" + (joueur + 1));
            System.out.print(" Noeud pos " + n.pos.ligne + "-" + n.pos.colonne + "= " + score);
            System.out.println("   valeur :" + n.getData());
        }

        System.out.println();
        System.out.println();
    }
}
