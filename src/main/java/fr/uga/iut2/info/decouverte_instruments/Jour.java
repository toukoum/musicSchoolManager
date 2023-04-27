package fr.uga.iut2.info.decouverte_instruments;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Raphaël Bleuse <raphael.bleuse@univ-grenoble-alpes.fr>
 */
public enum Jour implements Serializable {

    MERCREDI {
        @Override
        public String toString() {
            return "mercredi";
        }
    },
    VENDREDI {
        @Override
        public String toString() {
            return "vendredi";
        }
    },
    SAMEDI {
        @Override
        public String toString() {
            return "samedi";
        }
    },
    ;

    private static final long serialVersionUID = 1L;  // nécessaire pour la sérialisation

    // On impose la redéfinition de toString pour chaque variante de la classe
    // enum en marquant la méthode `abstract` : c'est verbeux mais garantit la
    // correction du code (notamment en cas d'ajout de variante).
    // source : *Effective Java*, Joshua BLOCH
    @Override
    public abstract String toString();

    /**
     * Cache associé au fonctionnement de {@link #valueByString}.
     */
    private static final Map<String, Jour> valueByString = new HashMap<>();

    static {
        // On initialise une fois pour la durée de vie de l'application le
        // cache de la fonction `valueOfString`.
        for (Jour jour : Jour.values()) {
            Jour.valueByString.put(jour.toString(), jour);
        }
    }

    /**
     * Renvoie la variante de la classe enum dont la représentation en chaîne
     * de caractère est spécifiée en paramètre.
     *
     * @param str La représentation en chaîne de caractère de la variante à
     *     retourner.
     *
     * @return La variante de la classe enum dont la représentation est celle
     *     spécifiée.
     */
    public static final Jour valueOfString(String str) {
        Jour result = Jour.valueByString.get(str);
        if (result == null) {
            throw new IllegalArgumentException();
        }
        return result;
    }
}
