package fr.uga.iut2.info.decouverte_instruments;

import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * La classe CLI est responsable des interactions avec l'utilisa·teur/trice.
 *
 * Cette classe concentre est l'unique « vue » de l'application.
 * <p>
 * C'est une classe qui n'est associée à aucun état : elle ne contient aucun
 * attribut d'instance.
 * Aussi, toutes les méthodes sont statiques et la classe n'a pas vocation à
 * être instanciée.
 * <p>
 * Aucune méthode de cette classe n'est pas censée modifier ses paramètres,
 * c'est pourquoi les paramètres des méthodes sont tous marqués comme `final`.
 *
 * @author Raphaël Bleuse <raphael.bleuse@univ-grenoble-alpes.fr>
 */
public final class CLI {

    /**
     * Nombre maximum d'essais pour la lecture d'une saisie utilisa·teur/trice.
     */
    private static final int MAX_ESSAIS = 3;

    /**
     * Interprète un token entier non signé comme une {@link Commande}.
     *
     * @param token La chaîne de caractère à interpréter.
     *
     * @return La {@link Commande} en cas de succès, null en cas d'erreur.
     */
    private static Commande parseCommande(final String token) {
        Commande result;
        try {
            int cmdId = Integer.parseUnsignedInt(token);  // may throw NumberFormatException
            result = Commande.values()[cmdId];  // may throw ArrayIndexOutOfBoundsException
        }
        catch (NumberFormatException | ArrayIndexOutOfBoundsException ignored) {
            // attention à garder synchronisé avec l'enum Commande
            System.out.println("Choix non valide : merci d'entrer un entier entre 0 et 5.");
            System.out.flush();
            result = null;
        }
        return result;
    }

    /**
     * Lit sur l'entrée standard une {@link Commande}.
     *
     * @return La {@link Commande} saisie par l'utilisa·teur/trice.
     */
    public static Commande lireCommande() {
        List<String> lignesEntete = List.of(
                // attention à garder synchronisé avec l'enum Commande
                "******************************************************************",
                "*       Association de musique - Découverte d'instruments        *",
                "******************************************************************",
                "      * 1- Créer un nouvel instrument                            *",
                "      * 2- Inscrire un enfant                                    *",
                "      * 3- Ajouter une demande d'instrument à un enfant inscrit  *",
                "      * 4- Afficher les inscriptions de chaque enfant            *",
                "      * 5- Afficher les inscriptions de chaque instrument        *",
                "******************************************************************",
                "      * 0- Quitter                                               *",
                "******************************************************************"
        );
        System.out.println(
                String.join(
                        System.lineSeparator(),
                        lignesEntete
                )
        );
        System.out.println("Saisir l'identifiant de l'action choisie :");
        System.out.flush();

        Commande result = null;
        Scanner in = new Scanner(System.in);
        String token;
        for (int i = 0; i < CLI.MAX_ESSAIS && result == null; ++i) {
            token = in.next();
            result = CLI.parseCommande(token);
        }
        if (result == null) {
            throw new Error("Erreur de lecture (" + CLI.MAX_ESSAIS + " essais infructueux).");
        }
        return result;
    }

    /**
     * Interprète un token comme une chaîne de caractère et vérifie que la
     * chaîne n'existe pas déjà.
     *
     * @param token La chaîne de caractère à interpréter.
     *
     * @param nomsConnus L'ensemble de chaîne de caractères qui ne sont plus
     *     disponibles.
     *
     * @return La chaîne de caractère en cas de succès, null en cas d'erreur.
     */
    private static String parseNouveauNom(final String token, final Set<String> nomsConnus) {
        if (nomsConnus.contains(token)) {
            System.out.println("Le nom existe déjà dans l'application.");
            System.out.flush();
            return null;
        } else {
            return token;
        }
    }

    /**
     * Lit sur l'entrée standard un nouveau nom.
     * <p>
     * Le nom saisi ne doit pas déjà exister dans l'application.
     *
     * @param nomsConnus L'ensemble des noms connus dans l'application.
     *
     * @return Le nouveau nom saisi par l'utilisa·teur/trice.
     */
    private static String lireNouveauNom(final Set<String> nomsConnus) {
        if (!nomsConnus.isEmpty()) {
            System.out.println("Les noms suivants ne sont plus disponibles :");
            System.out.println("  " + String.join(", ", nomsConnus) + ".");
            System.out.flush();
        }

        String result = null;
        Scanner in = new Scanner(System.in);
        String token;
        for (int i = 0; i < CLI.MAX_ESSAIS && result == null; ++i) {
            token = in.next();
            result = CLI.parseNouveauNom(token, nomsConnus);
        }
        if (result == null) {
            throw new Error("Erreur de lecture (" + CLI.MAX_ESSAIS + " essais infructueux).");
        }
        return result;
    }

    /**
     * Interprète un token comme une chaîne de caractère et vérifie que la
     * chaîne existe déjà.
     *
     * @param token La chaîne de caractère à interpréter.
     *
     * @param nomsConnus L'ensemble de chaîne de caractères valides.
     *
     * @return La chaîne de caractère en cas de succès, null en cas d'erreur.
     */
    private static String parseNomExistant(final String token, final Set<String> nomsConnus) {
        if (nomsConnus.contains(token)) {
            return token;
        } else {
            System.out.println("Le nom n'existe pas dans l'application.");
            System.out.flush();
            return null;
        }
    }

    /**
     * Lit sur l'entrée standard un nom existant dans l'application.
     * <p>
     * Le nom saisi doit exister dans l'application.
     *
     * @param nomsConnus L'ensemble des noms connus dans l'application.
     *
     * @return Le nom (existant) saisi par l'utilisa·teur/trice.
     */
    private static String lireNomExistant(final Set<String> nomsConnus) {
        assert !nomsConnus.isEmpty();
        System.out.println("Choisir un nom parmi les noms suivants :");
        System.out.println("  " + String.join(", ", nomsConnus) + ".");
        System.out.flush();

        String result = null;
        Scanner in = new Scanner(System.in);
        String token;
        for (int i = 0; i < CLI.MAX_ESSAIS && result == null; ++i) {
            token = in.next();
            result = CLI.parseNomExistant(token, nomsConnus);
        }
        if (result == null) {
            throw new Error("Erreur de lecture (" + CLI.MAX_ESSAIS + " essais infructueux).");
        }
        return result;
    }

    /**
     * Lit sur l'entrée standard le nom d'un nouvel {@link Instrument}.
     *
     * @param nomsConnus L'ensemble des noms d'instrument déjà connus
     *     (indisponibles).
     *
     * @return Le nom du nouvel {@link Instrument} saisi par
     *     l'utilisa·teur/trice.
     */
    public static String saisirNouvelInstrument(final Set<String> nomsConnus) {
        System.out.println("Saisir le nom d'un nouvel instrument.");
        System.out.flush();
        return CLI.lireNouveauNom(nomsConnus);
    }

    /**
     * Lit sur l'entrée standard le nom d'un {@link Instrument} existant.
     *
     * @param nomsConnus L'ensemble des noms d'instrument existants parmi
     *     lesquels choisir.
     *
     * @return Le nom de l'{@link Instrument} saisi par l'utilisa·teur/trice.
     */
    public static String choisirInstrument(final Set<String> nomsConnus) {
        System.out.println("Choisir un instrument.");
        System.out.flush();
        return CLI.lireNomExistant(nomsConnus);
    }

    /**
     * Lit sur l'entrée standard le nom d'un nouvel {@link Enfant}.
     *
     * @param nomsConnus L'ensemble des noms d'enfant déjà connus
     *     (indisponibles).
     *
     * @return Le nom du nouvel {@link Enfant} saisi par l'utilisa·teur/trice.
     */
    public static String saisirNouvelEnfant(final Set<String> nomsConnus) {
        System.out.println("Saisir le nom d'un nouvel enfant.");
        System.out.flush();
        return CLI.lireNouveauNom(nomsConnus);
    }

    /**
     * Lit sur l'entrée standard le nom d'un {@link Enfant} existant.
     *
     * @param nomsConnus L'ensemble des noms d'enfant existants parmi lesquels
     *     choisir.
     *
     * @return Le nom de l'{@link Enfant} saisi par l'utilisa·teur/trice.
     */
    public static String choisirEnfant(final Set<String> nomsConnus) {
        System.out.println("Choisir un enfant.");
        System.out.flush();
        return CLI.lireNomExistant(nomsConnus);
    }

    /**
     * Interprète un token comme un {@link Jour}.
     *
     * @param token La chaîne de caractère à interpréter.
     *
     * @param joursOccupes L'ensemble des jours déjà occupés (indisponibles).
     *
     * @return Le {@link Jour} en cas de succès, null en cas d'erreur,
     */
    private static Jour parseJour(final String token, final Set<Jour> joursOccupes) {
        try {
            Jour jour = Jour.valueOfString(token);
            if (joursOccupes.contains(jour)) {
                System.out.println("Le jour " + jour + " n'est plus disponible.");
                System.out.flush();
                return null;
            } else {
                return jour;
            }
        }
        catch (IllegalArgumentException iae) {
            System.out.println("Choix non valide.");
            System.out.flush();
            return null;
        }
    }

    /**
     * Lit sur l'entrée standard un {@link Jour}.
     *
     * @param joursOccupes L'ensemble des jours déjà occupés (indisponibles).
     *
     * @return Le {@link Jour} saisi par l'utilisa·teur/trice.
     */
    public static Jour lireJour(final Set<Jour> joursOccupes) {
        Set<Jour> joursDisponibles = EnumSet.allOf(Jour.class);
        joursDisponibles.removeAll(joursOccupes);

        Set<String> nomsJoursDisponibles = new HashSet<>();
        for (Jour jour : joursDisponibles) {
            nomsJoursDisponibles.add(jour.toString());
        }

        System.out.println("Choisir un jour parmi les jours suivants :");
        System.out.println(
                "  "
                + String.join(", ", nomsJoursDisponibles)
                + "."
        );
        System.out.flush();

        Jour result = null;
        Scanner in = new Scanner(System.in);
        String token;
        for (int i = 0; i < CLI.MAX_ESSAIS && result == null; ++i) {
            token = in.next();
            result = CLI.parseJour(token, joursOccupes);
        }
        if (result == null) {
            throw new Error("Erreur de lecture (" + CLI.MAX_ESSAIS + " essais infructueux).");
        }
        return result;
    }

    /**
     * Affiche sur la sortie standard le récapitulatif des inscriptions par
     * {@link Instrument}.
     *
     * @param instruments L'ensemble des {@link Instrument}s à afficher.
     */
    public static void afficherInscriptionsInstruments(final Collection<Instrument> instruments) {
        System.out.println("===== Liste des inscriptions par instrument =====");
        for (Instrument ins : instruments) {
            System.out.println("· " + ins);
            //parcourir les séance de l'instrument
            for (Seance seance : ins.getSeances()) {
                // afficher les séances de l'instrument en détail avec le jour et l'enfant
                System.out.println("   - [" + seance.getJour() + "] " + seance.getEnfant());
            }

            System.out.println();
        }
        System.out.flush();
    }

    /**
     * Affiche sur la sortie standard le récapitulatif des inscriptions par
     *     {@link Enfant}.
     *
     * @param enfants L'ensemble des {@link Enfant}s à afficher.
     */
    public static void afficherInscriptionsEnfants(final Collection<Enfant> enfants) {
        System.out.println("===== Liste des inscriptions par enfant =====");
        for (Enfant enf : enfants) {
            System.out.println("· " + enf);
            //parcourir les séance de l'enfant
            for (Seance seance : enf.getSeances()) {
                // afficher les séances de l'enfant en détail avec le jour et l'instrument
                System.out.println("   - [" + seance.getJour() + "] " + seance.getInstrument());
            }

            System.out.println();
        }
        System.out.flush();
    }

    /**
     * Affiche un message d'information à l'attention de l'utilisa·teur/trice.
     *
     * @param msg Le message à afficher.
     *
     * @param succes true si le message informe d'une opération réussie, false
     *     sinon.
     */
    public static void informerUtilisateur(final String msg, final boolean succes)
    {
        System.out.println((succes ? "[OK]" : "[KO]") + " " + msg);
        System.out.flush();
    }
}
