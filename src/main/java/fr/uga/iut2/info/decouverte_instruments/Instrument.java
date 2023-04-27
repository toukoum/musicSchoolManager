package fr.uga.iut2.info.decouverte_instruments;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Raphaël Bleuse <raphael.bleuse@univ-grenoble-alpes.fr>
 */
public class Instrument implements Serializable {

    private static final long serialVersionUID = 1L;  // nécessaire pour la sérialisation
    private final String nom;
    ArrayList<Seance> seances = new ArrayList<>();


    public Instrument(String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }

    public ArrayList<Seance> getSeances() {
        return seances;
    }

    public String getInstrument() {
        return this.nom;
    }

    @Override
    public String toString() {
        return this.nom;
    }
}
