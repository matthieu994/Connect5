package Connect5Game;

public class Noeud {
    Position pos;
    private byte data;
    Groupe[] groupes;

    Noeud(int l, int c, byte _data) {
        data = _data;
        pos = new Position(l,c);
        groupes = new Groupe[4];
    }

    boolean sameData(Noeud n) {
        return this.data == n.data;
    }

    byte getData() {
        return data;
    }

    boolean joueur() {
        return data == 1 || data == 2;
    }

    boolean hasNoGroup(int t) {
        return groupes[t] == null;
    }

    public void addGroupe(Groupe p) {
        if(groupes[p.getType()] != null) {
            System.out.println("AddGroupe Groupe existe déjà");
            return;
        }

        groupes[p.getType()] = p;
    }
}
