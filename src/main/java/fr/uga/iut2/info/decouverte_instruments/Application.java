package fr.uga.iut2.info.decouverte_instruments;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author Raphaël Bleuse <raphael.bleuse@univ-grenoble-alpes.fr>
 */
public class Application implements Serializable {

    private static final long serialVersionUID = 1L;  // nécessaire pour la sérialisation
    private final Map<String, Enfant> enfants;
    private final Map<String, Instrument> instruments;

    public Application() {
        enfants = new HashMap<>();
        instruments = new HashMap<>();
    }

    /**
     * Boucle d'interaction avec l'utilisa·teur/trice.
     *
     * La méthode est bloquante : tant que l'utilisa·teur/trice n'a pas saisi
     * la commande {@link Commande#QUITTER}, la méthode conserve le contrôle de
     * l'exécution.
     */
    public void run() {
        Commande cmd;
        do {
            cmd = CLI.lireCommande();
//            System.out.println(cmd);
//            System.out.flush();
            switch (cmd) {
                case CREER_INSTRUMENT:
                    this.creerInstrument();
                    break;
                case CREER_ENFANT:
                    this.creerEnfant();
                    break;
                case AJOUTER_INSTRUMENT_ENFANT:
                    this.ajouterInstrumentEnfant();
                    break;
                case AFFICHER_INSCRIPTIONS_ENFANTS:
                    this.afficherInscriptionsEnfants();
                    break;
                case AFFICHER_INSCRIPTIONS_INSTRUMENTS:
                    this.afficherInscriptionsInstruments();
                    break;
                case QUITTER:
                    // rien à faire
                    break;
            }
        } while (cmd != Commande.QUITTER);
    }

    private void creerInstrument() {
        String nomInstrument = CLI.saisirNouvelInstrument(this.getInstruments().keySet());
        this.nouvelInstrument(nomInstrument);
        CLI.informerUtilisateur(
                String.join(" ", List.of("Instrument", nomInstrument, "créé.")),
                true
        );
    }

    private void creerEnfant() {
        String nomEnfant = CLI.saisirNouvelEnfant(this.getEnfants().keySet());
        this.nouvelEnfant(nomEnfant);
        CLI.informerUtilisateur(
                String.join(" ", List.of("Enfant", nomEnfant, "créé.")),
                true
        );
    }

    private void ajouterInstrumentEnfant() {
        // Le système propose à l’utilisateur·rice les noms des enfants enregistrés qui peuvent
        //encore s’inscrire à un instrument (ce sont les enfants pour lesquels il reste au moins
        //une séance de libre, et qui ne sont pas déjà inscrits à tous les instruments) ;
        Scanner scanner = new Scanner(System.in);
        for (Enfant enfant : enfants.values()) {
            if (enfant.getNbSeances() <= 3 || instruments.size() != enfant.getNbSeances()) {
                System.out.println(enfant.getNom());
            }
        }


        //L’utilisateur·rice saisit le nom de l’enfant ;
        System.out.println("Choisissez un enfant");
        String nomEnfant = scanner.nextLine();

//        Le système vérifie qu’il existe un enfant de ce nom et qu’elle·il n’est pas déjà inscrit·e
//        à trois séances de découverte ;
        for (Enfant enfant : enfants.values()) {
            if (enfant.getNom().equals(nomEnfant)) {
                if (enfant.getNbSeances() == 3) {
                    System.out.println("L'enfant est déjà inscrit à 3 séances");
                }
            }
        }

//        Le système propose à l’utilisateur·rice les noms des instruments pour lesquels l’enfant
//        n’est pas encore inscrit ;
        for (Instrument instrument : instruments.values()) {
            if (!enfants.get(nomEnfant).getInstruments().contains(instrument)) {
                System.out.println(instrument.getNom());
            }
        }

        //L’utilisateur·rice saisit le nom de l’instrument ;
        System.out.println("Choisissez un instrument");
        String nomInstrument = scanner.nextLine();

//        Le système vérifie que l’instrument existe et que l’enfant n’est pas déjà inscrit à une
//        séance de cet instrument ;
        for (Instrument instrument : instruments.values()) {
            if (instrument.getNom().equals(nomInstrument)) {
                if (enfants.get(nomEnfant).getInstruments().contains(instrument)) {
                    System.out.println("L'enfant est déjà inscrit à cet instrument");
                }
            }
        }

//        Le système propose les jours d’inscription (mercredi, vendredi ou samedi) encore possibles pour l’enfant ;
        for (Enfant enfant : enfants.values()) {
            if (enfant.getNom().equals(nomEnfant)) {
                for (int i = 0; i < enfant.getNbSeances(); i++) {
                    System.out.println(enfant.getSeances().get(i).getJour());
                }
            }
        }

        //L’utilisateur·rice saisit le jour d’inscription ;
        System.out.println("Choisissez un jour");
        String jour = scanner.nextLine();

//        Le système vérifie que ce jour existe et que l’enfant n’a pas déjà une séance ce même
//        jour
        for (Enfant enfant : enfants.values()) {
            if (enfant.getNom().equals(nomEnfant)) {
                for (int i = 0; i < enfant.getNbSeances(); i++) {
                    if (enfant.getSeances().get(i).getJour().equals(jour)) {
                        System.out.println("L'enfant a déjà une séance ce jour");
                    }
                }
            }
        }


    }

    private void afficherInscriptionsEnfants() {
        CLI.afficherInscriptionsEnfants(this.getEnfants().values());
    }

    private void afficherInscriptionsInstruments() {
        CLI.afficherInscriptionsInstruments(this.getInstruments().values());
    }

    /**
     * Trouve l'instrument dont le nom est nom.
     *
     * @param nom Le nom de l'instrument recherché.
     * @return L'instrument dont le nom est nom.
     */
    public Instrument getInstrument(String nom) {
        return this.instruments.get(nom);
    }

    /**
     * Ajoute un nouvel instrument dans la liste des instruments connus.
     * <p>
     * Le nouvel instrument ne doit pas déjà exister dans l'application.
     *
     * @param nom Le nom du nouvel instrument à ajouter dans l'application.
     */
    public void nouvelInstrument(String nom) {
        assert !this.instruments.containsKey(nom);
        this.instruments.put(nom, new Instrument(nom));
    }

    /**
     * Accède au dictionnaire contenant les instruments connus.
     *
     * @return Un dictionnaire des instruments connus.
     */
    public Map<String, Instrument> getInstruments() {
        return this.instruments;
    }

    /**
     * Trouve l'enfant dont le nom est nom.
     *
     * @param nom Le nom de l'enfant recherché.
     * @return L'enfant dont le nom est nom.
     */
    public Enfant getEnfant(String nom) {
        return this.enfants.get(nom);
    }

    /**
     * Ajoute un nouvel enfant dans la liste des enfants connus.
     * <p>
     * Le nouvel enfant ne doit pas déjà exister dans l'application.
     *
     * @param nom Le nom du nouvel enfant à ajouter dans l'application.
     */
    public void nouvelEnfant(String nom) {
        assert !this.enfants.containsKey(nom);
        this.enfants.put(nom, new Enfant(nom));
    }

    /**
     * Accède au dictionnaire contenant les enfants connus.
     *
     * @return Un dictionnaire des enfants connus.
     */
    public Map<String, Enfant> getEnfants() {
        return this.enfants;
    }
}
