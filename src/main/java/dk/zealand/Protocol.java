package dk.zealand;

import java.util.Locale;

/**
 * Fortolker tekstbeskeder i den lille applikationsprotokol.
 *
 * <p>En gyldig forespørgsel har formatet:</p>
 *
 * <pre>UPPER|tekst</pre>
 *
 * <p>Vi splitter kun ved den første lodrette streg. Derfor er
 * {@code UPPER|hej|verden} også gyldig og giver {@code OK|HEJ|VERDEN}.</p>
 */
public final class Protocol {

    private Protocol() {
        // Klassen indeholder kun statiske metoder og skal ikke instantieres.
    }

    /**
     * Behandler én besked fra klienten.
     *
     * @param request klientens tekstbesked
     * @return et svar, som kan sendes direkte tilbage til klienten
     */
    public static String handle(String request) {
        if (request == null || request.isBlank()) {
            return "ERROR|INVALID_FORMAT";
        }

        // Tallet 2 er vigtigt: Teksten efter kommandoen må gerne indeholde flere |.
        String[] parts = request.split("\\|", 2);

        if (parts.length != 2 || parts[0].isBlank()) {
            return "ERROR|INVALID_FORMAT";
        }

        String command = parts[0].trim().toUpperCase(Locale.ROOT);
        String payload = parts[1];

        return switch (command) {
            case "UPPER" -> "OK|" + payload.toUpperCase(Locale.ROOT);
            case "LOWER" -> "OK|" + payload.toLowerCase(Locale.ROOT);
            case "REVERSE" -> "OK|" + new StringBuilder(payload).reverse();

            /*
             * Livekodning – ukendt kommando:
             * Start eventuelt med at lade default returnere ERROR|NOT_IMPLEMENTED.
             * Erstat derefter den linje med den aktive linje nedenfor.
             */
//             default -> "ERROR|NOT_IMPLEMENTED";

            default -> "ERROR|UNKNOWN_COMMAND|" + command;
        };
    }
}
