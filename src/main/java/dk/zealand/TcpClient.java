package dk.zealand;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * TCP-klient, der opretter forbindelse, sender én besked og læser ét svar.
 */
public class TcpClient {

    private static final String HOST = "localhost";
    private static final int PORT = 5000;

    public static void main(String[] args) {
        System.out.println("Forbinder til " + HOST + ":" + PORT + " ...");

        try (Socket socket = new Socket(HOST, PORT);
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter writer = new PrintWriter(
                     socket.getOutputStream(), true, StandardCharsets.UTF_8);
             Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8)) {

            System.out.println("Forbindelsen er oprettet.");

            /*
             * Demonstration af en afbrudt forbindelse:
             * Kør klienten med argumentet --disconnect.
             * try-with-resources lukker socketen, når main returnerer.
             */
            if (args.length > 0 && "--disconnect".equalsIgnoreCase(args[0])) {
                System.out.println("Klienten lukker uden at sende en besked.");
                return;
            }

            System.out.print("Skriv en besked, fx UPPER|hej verden: ");
            String request = scanner.nextLine();

            // println sender præcis én tekstlinje til serveren.
            writer.println(request);
            System.out.println("Sendt: " + request);

            // Klienten forventer præcis ét svar fra serveren.
            String response = reader.readLine();

            if (response == null) {
                System.out.println("Serveren afbrød forbindelsen uden at sende et svar.");
                return;
            }

            System.out.println("Svar: " + response);
        } catch (ConnectException exception) {
            System.err.println("Kunne ikke forbinde. Er TcpServer startet på port " + PORT + "?");
        } catch (IOException exception) {
            System.err.println("Klientfejl: " + exception.getMessage());
        }
    }
}
