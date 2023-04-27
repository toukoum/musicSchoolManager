package fr.uga.iut2.info.decouverte_instruments;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Raphaël Bleuse <raphael.bleuse@univ-grenoble-alpes.fr>
 */
public class Enfant implements Serializable {

    private static final long serialVersionUID = 1L;  // nécessaire pour la sérialisation
    private final String nom;

    ArrayList<Seance> seances = new ArrayList<>();

    private int nbSeances = 0;

    public Enfant(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return this.nom;
    }

    public ArrayList<Seance> getSeances() {
        return seances;
    }

    public int getNbSeances() {
        return nbSeances;
    }

    @Override
    public String toString() {
        return this.nom;
    }

    public Collection<String> getInstruments() {
        // retourne le nom des instruments de l'enfant
        ArrayList<String> instruments = new ArrayList<>();
        for (Seance seance : seances) {
            instruments.add(seance.getInstrument());
        }
        return instruments;
    }

    public void addSeance(Seance seance) {
        this.seances.add(seance);
        this.nbSeances++;
    }
}
