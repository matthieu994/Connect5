package Connect5Game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Map;
import java.util.AbstractMap.SimpleEntry;

import Connect5Game.Grille;
import Connect5Game.Joueur;
import Connect5Game.Position;
import Connect5Game.Noeud;
import Connect5Game.Groupe;

public class JoueurArtificiel implements Joueur {

    // Ces attributs sont réinitialisés à chaque appel de getProchainCoup.
    // L'information n'est donc pas conservée.
    private final Random random = new Random();
    Noeud[][] noeuds; // Chaque case est un pointeur vers un / des groupes
    int joueurCourant; // Contient le joueur courant
    int nbligne; // Nombre max lignes
    int nbcol; // Nombre max colonnes

    // DEBUG
    ArrayList<Groupe> listeGroupes;

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
        ArrayList<Integer> casesvides = new ArrayList<Integer>();
        nbligne = grille.getData().length;
        nbcol = grille.getData()[0].length;
        for (int l = 0; l < nbligne; l++)
            for (int c = 0; c < nbcol; c++)
                if (grille.getData()[l][c] == 0)
                    casesvides.add(l * nbcol + c);

        int colonne = 0, ligne = 0;
        noeuds = new Noeud[nbligne][nbcol];
        // joueurCourant = casesvides.size() % 2;
        // joueurCourant = joueurCourant + 1;
        joueurCourant = 1;

        System.out.println("Joueur courant: " + joueurCourant);

        listeGroupes = new ArrayList<>();

        for (int l = 0; l < nbligne; l++) {
            for (int c = 0; c < nbcol; c++) {
                if (grille.getData()[l][c] == joueurCourant)
                    initGroupes(grille.getData(), l, c, null);
            }
        }

        // return new Position(ligne, colonne);
        displayGroupes();

        int choix = random.nextInt(casesvides.size());
        choix = casesvides.get(choix);
        return new Position(choix / nbcol, choix % nbcol);
    }

    private void initGroupes(byte[][] data, int ligne, int colonne, Groupe.Type recType) {
        ArrayList<Map.Entry<Integer, Groupe.Type>> adjacents = new ArrayList<>();

        if (ligne > 0) {
            if (colonne > 0 && checkCase(data, ligne - 1, colonne - 1, Groupe.Type.DIAG_GAUCHE, recType))
                adjacents.add(new SimpleEntry<>((ligne - 1) * nbcol + colonne - 1, Groupe.Type.DIAG_GAUCHE));
            if (checkCase(data, ligne - 1, colonne, Groupe.Type.VERTICAL, recType))
                adjacents.add(new SimpleEntry<>((ligne - 1) * nbcol + colonne, Groupe.Type.VERTICAL));
            if (colonne < noeuds[0].length - 1
                    && checkCase(data, ligne - 1, colonne + 1, Groupe.Type.DIAG_DROITE, recType))
                adjacents.add(new SimpleEntry<>((ligne - 1) * nbcol + colonne + 1, Groupe.Type.DIAG_DROITE));
        }
        if (colonne > 0 && checkCase(data, ligne, colonne - 1, Groupe.Type.HORIZONTAL, recType))
            adjacents.add(new SimpleEntry<>((ligne * nbcol) + colonne - 1, Groupe.Type.HORIZONTAL));
        if (colonne < noeuds[0].length - 1 && checkCase(data, ligne, colonne + 1, Groupe.Type.HORIZONTAL, recType))
            adjacents.add(new SimpleEntry<>((ligne * nbcol) + colonne + 1, Groupe.Type.HORIZONTAL));
        if (ligne < noeuds.length - 1) {
            if (colonne > 0 && checkCase(data, ligne + 1, colonne - 1, Groupe.Type.DIAG_DROITE, recType))
                adjacents.add(new SimpleEntry<>((ligne + 1) * nbcol + colonne - 1, Groupe.Type.DIAG_DROITE));
            if (checkCase(data, ligne + 1, colonne, Groupe.Type.VERTICAL, recType))
                adjacents.add(new SimpleEntry<>((ligne + 1) * nbcol + colonne, Groupe.Type.VERTICAL));
            if (colonne < noeuds[0].length - 1
                    && checkCase(data, ligne + 1, colonne + 1, Groupe.Type.DIAG_GAUCHE, recType))
                adjacents.add(new SimpleEntry<>((ligne + 1) * nbcol + colonne + 1, Groupe.Type.DIAG_GAUCHE));
        }

        Groupe groupe = new Groupe();
        if (adjacents.isEmpty()) {
            System.out.print(recType + " : Noeud " + ligne + "," + colonne + " : ");
            afficherAdjacents(adjacents);
            if (noeuds[ligne][colonne] == null) {
                groupe.ajouter(ligne, colonne);
                groupe.setType(Groupe.Type.SINGLE);
                listeGroupes.add(groupe);
            }
            return;
        }

        for (Map.Entry<Integer, Groupe.Type> adjacent : adjacents) {
            int aLigne = adjacent.getKey() / nbcol;
            int aColonne = adjacent.getKey() % nbcol;
            Groupe.Type type = adjacent.getValue();

            System.out.print(recType + " : Noeud " + ligne + "," + colonne + " : ");
            afficherAdjacents(adjacents);
            groupe = new Groupe();
            if (noeuds[ligne][colonne] != null && noeuds[ligne][colonne].groupes.containsKey(type)) {
                groupe = noeuds[ligne][colonne].groupes.get(type);
                System.out.println("Recup groupe: " + type);
            } else {
                if (noeuds[ligne][colonne] == null) {
                    noeuds[ligne][colonne] = new Noeud();
                }
                groupe.ajouter(ligne, colonne);
                groupe.setType(type);
                noeuds[ligne][colonne].ajouterGroupe(groupe);
                listeGroupes.add(groupe);
                System.out.println("Ajout groupe: " + type);
            }

            creerGroupe(data, aLigne, aColonne, groupe);
            displayGroupes();
            initGroupes(data, aLigne, aColonne, type);
        }
    }

    private void creerGroupe(byte[][] data, int ligne, int colonne, Groupe courant) {
        if (noeuds[ligne][colonne] != null && noeuds[ligne][colonne].groupes.containsKey(courant.type))
            return;

        if (noeuds[ligne][colonne] == null) {
            noeuds[ligne][colonne] = new Noeud();
        }
        courant.ajouter(ligne, colonne);
        noeuds[ligne][colonne].ajouterGroupe(courant);
    }

    private boolean checkCase(byte[][] data, int ligne, int colonne, Groupe.Type type, Groupe.Type recType) {
        if (data[ligne][colonne] != joueurCourant) // Case n'appartient pas au joueur courant
            return false;
        if (recType != null && type != recType)
            return false;
        if (noeuds[ligne][colonne] != null)
            if (recType != null)
                return false;
        return true;
    }

    private void displayGroupes() {
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

    private void afficherAdjacents(ArrayList<Map.Entry<Integer, Groupe.Type>> adjacents) {
        if (adjacents.isEmpty()) {
            System.out.println("0");
            return;
        }

        System.out.print("{ ");
        for (Map.Entry<Integer, Groupe.Type> adjacent : adjacents) {
            System.out.print("[" + adjacent.getKey() / nbcol + "," + adjacent.getKey() % nbcol + "]");
        }
        System.out.println(" }");
    }
}
