# Undervisernoter til livekodningen

Projektet er færdigt og kørbart, men indeholder markerede alternativer, så gennemgangen kan bygges op trinvis.

## Forslag til rækkefølge

### 1. Opret serveren

Livekod `ServerSocket`, `accept()` og en `Socket`. Stop og spørg de studerende:

- Hvilken side skal være startet først?
- "Starter først ServerSocket(5000) objekt som lytter til Socket(5000) klienter"

- Hvad betyder port 5000?
- "Den IP port som serveren lytter til" 

- Hvorfor blokerer `accept()`?
- "Den venter på en forbindelse fra en klient" 

### 2. Send én simpel besked

I `TcpServer.java`:

1. Kommentér `String response = Protocol.handle(request);` ud.
2. Fjern kommentaren foran `String response = "ECHO|" + request;`.
3. Kør server og klient i to terminaler.

På dette tidspunkt kan klienten sende almindelig tekst, eksempelvis `hej`.

### 3. Indfør protokollen

Skift tilbage til den aktive `Protocol.handle(request)` og brug:

```text
UPPER|hej verden
```

Gennemgå især `split("\\|", 2)`. Tallet 2 forhindrer, at tekstens eventuelle ekstra `|` bliver fortolket som nye protokolfelter.

### 4. Håndtér ukendt kommando

I `Protocol.java` kan `default` midlertidigt returnere:

```java
default -> "ERROR|NOT_IMPLEMENTED";
```

Skift derefter til den færdige linje:

```java
default -> "ERROR|UNKNOWN_COMMAND|" + command;
```

Afprøv med `LOWER|Hej` og tal om forskellen mellem transportfejl og protokolfejl.

### 5. Håndtér afbrydelse

Start serveren og kør:

```bash
java -cp target/classes dk.zealand.tcp.TcpClient --disconnect
```

Peg på, at `readLine()` returnerer `null`. Det er ikke det samme som en tom tekstbesked.

### 6. Afslut med kontrol

Kør:

```bash
mvn test
git diff
git status
```

Lad en studerende forklare dataflowet fra `TcpClient` til `Protocol` og tilbage. Afslut med commit:

```bash
git add .
git commit -m "Implement TCP client-server protocol"
```

## Overgang til agentøvelsen

Brug samme kodebase til `LENGTH|tekst`:

1. De studerende undersøger først `Protocol.java` og de eksisterende tests.
2. Agenten skal først levere en plan uden kode.
3. Den studerende afgrænser ændringen til protokollen og testene.
4. Diff og testresultat kontrolleres før commit.
5. Ved checkpointet ændres et krav, eksempelvis om mellemrum skal tælles med.
