package dk.zealand;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * En bevidst lille TCP-server, der accepterer én klient og behandler én besked.
 * Serveren stopper derefter, så forbindelsens livscyklus er let at se.
 */
public class TcpServer {

    private static final int DEFAULT_PORT = 5000;

    public static void main(String[] args) {
        int port = readPort(args);

        System.out.println("Starter server på port " + port + " ...");

        // ServerSocket lytter efter nye TCP-forbindelser.
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Serveren venter på en klient.");

            // accept() blokerer, indtil en klient opretter forbindelse.
            try (Socket clientSocket = serverSocket.accept();
                 BufferedReader reader = new BufferedReader(
                         new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));
                 PrintWriter writer = new PrintWriter(
                         clientSocket.getOutputStream(), true, StandardCharsets.UTF_8)) {

                System.out.println("Klient forbundet: " + clientSocket.getRemoteSocketAddress());

                // readLine() blokerer, indtil klienten sender en linje eller lukker forbindelsen.
                String request = reader.readLine();

                // null betyder, at klienten lukkede forbindelsen uden at sende en hel linje.
                if (request == null) {
                    System.out.println("Klienten afbrød forbindelsen uden at sende en besked.");
                    return;
                }

                System.out.println("Modtaget: " + request);

                /*
                 * Livekodning – trin 1, én simpel besked:
                 * Kommentér den aktive Protocol-linje ud og fjern // på linjen herunder.
                 * Så svarer serveren blot med den tekst, den har modtaget.
                 */
                // String response = "ECHO|" + request;

                /*
                 * Livekodning – trin 2, protokollen UPPER|tekst:
                 * Aktivér denne linje, når den simple besked virker.
                 */
                String response = Protocol.handle(request);

                // println afslutter beskeden med linjeskift. autoFlush=true sender straks data.
                writer.println(response);
                System.out.println("Sendt: " + response);
            }
        } catch (IOException exception) {
            // Eksempler: Porten er optaget, eller forbindelsen forsvinder under kommunikationen.
            System.err.println("Serverfejl: " + exception.getMessage());
        }

        System.out.println("Serveren er stoppet.");
    }

    private static int readPort(String[] args) {
        if (args.length == 0) {
            return DEFAULT_PORT;
        }

        try {
            return Integer.parseInt(args[0]);
        } catch (NumberFormatException exception) {
            System.err.println("Ugyldig port. Bruger standardport " + DEFAULT_PORT + ".");
            return DEFAULT_PORT;
        }
    }
}
