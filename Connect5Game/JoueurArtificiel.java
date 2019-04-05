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
        ArrayList<Integer> casesvides = new ArrayList<>();
        int nbcol = grille.getData()[0].length,
                nbligne = grille.getData().length;
        for(int l=0;l<nbligne;l++)
            for(int c=0;c<nbcol;c++)
                if(grille.getData()[l][c]==0)
                    casesvides.add(l*nbcol+c);
        int choix = random.nextInt(casesvides.size());
        choix = casesvides.get(choix);

        Noeud[][] noeud = new Noeud[nbligne][nbcol];

        for(int l=0;l<nbligne;l++)
            for(int c = 0;c < nbcol;c++)
                noeud[l][c] = new Noeud(l,c,grille.getData()[l][c]);

        for(int l=0;l<nbligne;l++)
            for(int c = 0;c < nbcol;c++)
                if(noeud[l][c].joueur())
                    Groupe.initerGroupe(noeud,l,c);

        return new Position(choix / nbcol, choix % nbcol);
    }
}
