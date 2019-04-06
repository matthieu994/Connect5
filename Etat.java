package Connect5Game;

import java.util.ArrayList;
import java.util.Map;
import java.util.AbstractMap.SimpleEntry;

import Connect5Game.Groupe;
import Connect5Game.Noeud;

public class Etat implements Comparable<Etat> {
    final byte[][] grille;

    public ArrayList<Groupe> listeGroupes;
    public ArrayList<Groupe> listeGroupesAdversaire;

    Noeud[][] noeuds;
    Noeud[][] noeudsAdversaire;

    int joueur; // Contient le joueur courant
    int adversaire;

    public Etat(byte[][] grille, int joueur) {
        this.grille = grille;

        listeGroupes = new ArrayList<>();
        listeGroupesAdversaire = new ArrayList<>();

        noeuds = new Noeud[grille.length][grille[0].length];
        noeudsAdversaire = new Noeud[grille.length][grille[0].length];

        this.joueur = joueur;
        this.adversaire = (joueur == 1) ? 2 : 1;
    }

    public Etat(Etat etat) {
        this(etat.grille, etat.joueur);
    }

    public Etat genererSuccesseur(int ligne, int colonne) {
        this.genererEtat();

        ArrayList<Map.Entry<Integer, Groupe.Type>> adjacents = getAdjacents(ligne, colonne, null, true);
        // System.out.println(ligne + "," + colonne + " adjacents: " +
        // adjacents.size());

        for (Map.Entry<Integer, Groupe.Type> adjacent : adjacents) {
            int caseLigne = adjacent.getKey() / grille[0].length;
            int caseCol = adjacent.getKey() % grille[0].length;

            // Case adjacente est SINGLE
            if (noeuds[caseLigne][caseCol].groupes.size() == 1
                    && noeuds[caseLigne][caseCol].groupes.containsKey(Groupe.Type.SINGLE)) {
                Groupe newGroupe = noeuds[caseLigne][caseCol].groupes.get(Groupe.Type.SINGLE);
                newGroupe.setType(adjacent.getValue());
                newGroupe.ajouter(ligne, colonne);
                noeuds[ligne][colonne] = new Noeud();
                noeuds[ligne][colonne].ajouterGroupe(newGroupe);
                continue;
            }

            for (Map.Entry<Groupe.Type, Groupe> groupe : noeuds[caseLigne][caseCol].groupes.entrySet()) {
                if (groupe.getKey() == adjacent.getValue()) {
                    Groupe newGroupe = noeuds[caseLigne][caseCol].groupes.get(groupe.getKey());
                    newGroupe.ajouter(ligne, colonne);
                    noeuds[ligne][colonne] = new Noeud();
                    noeuds[ligne][colonne].ajouterGroupe(newGroupe);
                }
                // System.out.println(caseLigne + "," + caseCol + " type: " + groupe.getKey());
            }
        }

        return this;
    }

    public void genererEtat() {
        for (int l = 0; l < grille.length; l++) {
            for (int c = 0; c < grille[0].length; c++) {
                if (grille[l][c] == joueur)
                    initGroupes(l, c, null, true);
                if (grille[l][c] == adversaire)
                    initGroupes(l, c, null, false);
            }
        }
    }

    private void ajouterGroupe(Groupe groupe, boolean joueur) {
        if (joueur)
            listeGroupes.add(groupe);
        else
            listeGroupesAdversaire.add(groupe);
    }

    private Noeud[][] getNoeuds(boolean joueur) {
        if (joueur)
            return noeuds;
        else
            return noeudsAdversaire;
    }

    private ArrayList<Map.Entry<Integer, Groupe.Type>> getAdjacents(int ligne, int colonne, Groupe.Type refType,
            boolean joueur) {
        ArrayList<Map.Entry<Integer, Groupe.Type>> adjacents = new ArrayList<>();

        Noeud[][] noeuds = getNoeuds(joueur);

        if (ligne > 0) {
            if (colonne > 0)
                insertAdjacent(adjacents, ligne - 1, colonne - 1, Groupe.Type.DIAG_GAUCHE, refType, joueur);
            insertAdjacent(adjacents, ligne - 1, colonne, Groupe.Type.VERTICAL, refType, joueur);
            if (colonne < noeuds[0].length - 1)
                insertAdjacent(adjacents, ligne - 1, colonne + 1, Groupe.Type.DIAG_DROITE, refType, joueur);
        }
        if (colonne > 0)
            insertAdjacent(adjacents, ligne, colonne - 1, Groupe.Type.HORIZONTAL, refType, joueur);
        if (colonne < noeuds[0].length - 1)
            insertAdjacent(adjacents, ligne, colonne + 1, Groupe.Type.HORIZONTAL, refType, joueur);
        if (ligne < noeuds.length - 1) {
            if (colonne > 0)
                insertAdjacent(adjacents, ligne + 1, colonne - 1, Groupe.Type.DIAG_DROITE, refType, joueur);
            insertAdjacent(adjacents, ligne + 1, colonne, Groupe.Type.VERTICAL, refType, joueur);
            if (colonne < noeuds[0].length - 1)
                insertAdjacent(adjacents, ligne + 1, colonne + 1, Groupe.Type.DIAG_GAUCHE, refType, joueur);
        }

        return adjacents;
    }

    private void initGroupes(int ligne, int colonne, Groupe.Type refType, boolean joueur) {
        ArrayList<Map.Entry<Integer, Groupe.Type>> adjacents = getAdjacents(ligne, colonne, refType, joueur);

        Noeud[][] noeuds = getNoeuds(joueur);

        Groupe groupe = new Groupe();
        if (adjacents.isEmpty()) {
            // System.out.print(refType + " : Noeud " + ligne + "," + colonne + " : ");
            // afficherAdjacents(adjacents);
            if (noeuds[ligne][colonne] == null) {
                groupe.ajouter(ligne, colonne);
                groupe.setType(Groupe.Type.SINGLE);
                ajouterGroupe(groupe, joueur);
                noeuds[ligne][colonne] = new Noeud();
                noeuds[ligne][colonne].ajouterGroupe(groupe);
            }
            return;
        }

        for (Map.Entry<Integer, Groupe.Type> adjacent : adjacents) {
            int aLigne = adjacent.getKey() / grille[0].length;
            int aColonne = adjacent.getKey() % grille[0].length;
            Groupe.Type type = adjacent.getValue();

            // System.out.print(refType + " : Noeud " + ligne + "," + colonne + " : ");
            // afficherAdjacents(adjacents);
            groupe = new Groupe();
            if (noeuds[ligne][colonne] != null && noeuds[ligne][colonne].groupes.containsKey(type)) {
                groupe = noeuds[ligne][colonne].groupes.get(type);
                // System.out.println("Recup groupe: " + type);
            } else {
                if (noeuds[ligne][colonne] == null) {
                    noeuds[ligne][colonne] = new Noeud();
                }
                groupe.ajouter(ligne, colonne);
                groupe.setType(type);
                noeuds[ligne][colonne].ajouterGroupe(groupe);
                ajouterGroupe(groupe, joueur);
                // System.out.println("Ajout groupe: " + type);
            }

            creerGroupe(aLigne, aColonne, groupe, joueur);
            // displayGroupes();
            initGroupes(aLigne, aColonne, type, joueur);
        }

        return;
    }

    private void creerGroupe(int ligne, int colonne, Groupe courant, boolean joueur) {
        Noeud[][] noeuds = getNoeuds(joueur);

        if (noeuds[ligne][colonne] != null && noeuds[ligne][colonne].groupes.containsKey(courant.type))
            return;

        if (noeuds[ligne][colonne] == null) {
            noeuds[ligne][colonne] = new Noeud();
        }
        courant.ajouter(ligne, colonne);
        noeuds[ligne][colonne].ajouterGroupe(courant);
    }

    private void insertAdjacent(ArrayList<Map.Entry<Integer, Groupe.Type>> adjacents, int ligne, int colonne,
            Groupe.Type type, Groupe.Type refType, boolean isJoueur) {
        int joueur = isJoueur ? this.joueur : this.adversaire;

        if (grille[ligne][colonne] != joueur) // Case n'appartient pas au joueur courant
            return;
        if (refType != null && type != refType) // Case ne correspond pas au groupe courant
            return;
        if (getNoeuds(isJoueur)[ligne][colonne] != null && refType != null)
            return; // La case ne contient pas le groupe voulu
        adjacents.add(new SimpleEntry<>(ligne * grille[0].length + colonne, type));
    }

    public int evalFunction() {
        return countGroupes(listeGroupes) - countGroupes(listeGroupesAdversaire);
    }

    private int countGroupes(ArrayList<Groupe> groupes) {
        int count = 0;
        for (Groupe groupe : groupes) {
            if (groupe.size() == 1)
                count += 1;
            if (groupe.size() == 2)
                count += 10;
            if (groupe.size() == 3)
                count += 100;
            if (groupe.size() == 4)
                count += 1000;
            if (groupe.size() == 5)
                count += 10000;
        }
        return count;
    }

    private void afficherAdjacents(ArrayList<Map.Entry<Integer, Groupe.Type>> adjacents) {
        if (adjacents.isEmpty()) {
            System.out.println("0");
            return;
        }

        System.out.print("{ ");
        for (Map.Entry<Integer, Groupe.Type> adjacent : adjacents) {
            System.out.print(
                    "[" + adjacent.getKey() / grille[0].length + "," + adjacent.getKey() % grille[0].length + "]");
        }
        System.out.println(" }");
    }

    public void deepCopy(Etat autre) {
        // Deep-Copier noeuds joueur et liste groupes
        // Copier grille
    }

    @Override
    public int compareTo(Etat autre) {
        return this.evalFunction() - autre.evalFunction();
    }
}