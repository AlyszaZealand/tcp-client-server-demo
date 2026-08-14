# TCP-klient og -server i Java

Dette repository bruges til den første demonstration af socketprogrammering i Programmering 2. Koden viser en lille TCP-server og -klient, der udveksler én tekstbesked.

Undervisningsmateriale: [Grundlæggende socket-programmering i Java](https://zealanddk-my.sharepoint.com/:w:/g/personal/mikc_zealand_dk/ERU21IjgVbJIo_-4junwxQ0Brmhah1QJzLAS3NrkptVYvg)

Efter forløbet skal du kunne:

- forklare forskellen på serverens `accept()` og klientens oprettelse af en `Socket`
- forklare, hvorfor begge programmer bruger samme port
- sende og modtage en linjebaseret besked
- forklare protokollen `UPPER|tekst`
- håndtere en ukendt kommando og en afbrudt forbindelse
- køre klient og server i hver sin terminal
- kontrollere ændringer med test, `git diff` og commit

## Projektets struktur

```text
tcp-client-server-demo/
├── pom.xml
├── README.md
├── UNDERVISERNOTER.md
└── src
    ├── main/java/dk/zealand/tcp
    │   ├── Protocol.java
    │   ├── TcpClient.java
    │   └── TcpServer.java
    └── test/java/dk/zealand/tcp
        └── ProtocolTest.java
```

`TcpServer` lytter efter én klient. `TcpClient` opretter forbindelse og sender én besked. `Protocol` fortolker beskeden, så netværkskode og protokol ikke blandes sammen.

## Forudsætninger

- Java 17 eller nyere
- Maven 3.9 eller nyere
- Git

Kontrollér installationen:

```bash
java -version
mvn -version
git --version
```

## Fork repositoryet

1. Fork repositoryet på GitHub.
2. Klon din fork.
3. Åbn mappen som Maven-projekt i IntelliJ IDEA.
4. Kør testene, før du ændrer koden.

```bash
git clone <url-til-din-fork>
cd tcp-client-server-demo
mvn test
```

## Kør i to terminaler

Kompilér først projektet:

```bash
mvn clean test
```

Start serveren i terminal 1:

```bash
java -cp target/classes dk.zealand.tcp.TcpServer
```

Start klienten i terminal 2:

```bash
java -cp target/classes dk.zealand.tcp.TcpClient
```

Skriv derefter:

```text
UPPER|hej verden
```

Klienten bør modtage:

```text
OK|HEJ VERDEN
```

Serveren behandler kun én klient og én besked. Start derfor serveren igen før hver ny afprøvning.

## Afprøv protokollen

Start serveren igen før hvert eksempel.

| Besked fra klienten | Forventet svar |
|---|---|
| `UPPER|hej verden` | `OK|HEJ VERDEN` |
| `UPPER|hej|verden` | `OK|HEJ|VERDEN` |
| `LOWER|Hej` | `ERROR|UNKNOWN_COMMAND|LOWER` |
| `hej verden` | `ERROR|INVALID_FORMAT` |

Protokollen bruger `|` som separator. Den første del er kommandoen, og resten er data.

## Afprøv en afbrudt forbindelse

Start serveren og kør derefter klienten sådan:

```bash
java -cp target/classes dk.zealand.tcp.TcpClient --disconnect
```

Klienten opretter forbindelsen, men lukker uden at sende en besked. Serveren skal registrere afbrydelsen uden at gå ned.

## Livekodningens trin

Koden indeholder kommentarer mærket `Livekodning`. De gør det muligt at skifte mellem trinene uden at skrive hele projektet på ny.

1. Opret `ServerSocket` og `Socket`.
2. Send én simpel besked og returnér `ECHO|...`.
3. Erstat echo-svaret med `Protocol.handle(request)`.
4. Implementér `UPPER|tekst`.
5. Tilføj svar ved ukendt kommando og ugyldigt format.
6. Håndtér, at `readLine()` returnerer `null`, når modparten afbryder.
7. Kør testene og kontrollér ændringerne.

## Kontrollér og gem ændringerne

```bash
mvn test
git status
git diff
git add .
git commit -m "Implement TCP client-server protocol"
```

Inden du committer, skal du kunne forklare:

- hvilken kode agenten eller du selv ændrede
- hvorfor `readLine()` kan returnere `null`
- hvorfor protokollen skal være kendt af både klient og server
- hvordan du testede en gyldig besked, en ukendt kommando og en afbrydelse

## Guidet agentøvelse

Når grundløsningen virker, kan du bede en AI-agent om en plan for kommandoen:

```text
LENGTH|tekst
```

Serveren skal svare med tekstens længde, eksempelvis `OK|10` for `LENGTH|hej verden`. Bed først agenten om en plan uden kode. Kontrollér derefter hvilke filer agenten vil ændre, gennemgå diffen og tilføj tests for en almindelig, tom og ugyldig besked.
