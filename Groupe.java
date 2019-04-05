package Connect5Game;

import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.AbstractMap.SimpleEntry;

public class Groupe {
    public static enum Type {
        HORIZONTAL, VERTICAL, DIAG_GAUCHE, DIAG_DROITE, SINGLE
    }

    public ArrayList<Entry<Integer, Integer>> cases;
    public Type type;

    public Groupe() {
        this(null);
    }

    public Groupe(Type type) {
        this.cases = new ArrayList<>();
        setType(type);
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void ajouter(int ligne, int colonne) {
        this.cases.add(new SimpleEntry<>(ligne, colonne));
    }

    public int size() {
        return this.cases.size();
    }
}
