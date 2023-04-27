package fr.uga.iut2.info.decouverte_instruments;

import java.io.IOException;

/**
 *
 * @author Raphaël Bleuse <raphael.bleuse@univ-grenoble-alpes.fr>
 */
public final class Main {

    public static final int EXIT_ERR_LOAD = 2;
    public static final int EXIT_ERR_SAVE = 3;

    public static void main(String[] args) {
        Application app = null;

        try {
            app = Persisteur.lireEtat();
        }
        catch (ClassNotFoundException | IOException ignored) {
            System.err.println("Erreur irrécupérable pendant le chargement de l'état : fin d'exécution !");
            System.err.flush();
            System.exit(Main.EXIT_ERR_LOAD);
        }

        // app.run() garde le contrôle de l'exécution tant que
        // l'utilisa·teur/trice n'a pas saisi la commande QUITTER.
        app.run();

        try {
            Persisteur.sauverEtat(app);
        }
        catch (IOException ignored) {
            System.err.println("Erreur irrécupérable pendant la sauvegarde de l'état : fin d'exécution !");
            System.err.flush();
            System.exit(Main.EXIT_ERR_SAVE);
        }
    }
}
