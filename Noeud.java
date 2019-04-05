package Connect5Game;

import java.util.Map;
import java.util.HashMap;

import Connect5Game.Groupe;

public class Noeud {

    public Map<Groupe.Type, Groupe> groupes;

    public Noeud() {
        groupes = new HashMap<>();
    }

    public void ajouterGroupe(Groupe G) {
        groupes.put(G.type, G);
    }
}
