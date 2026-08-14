import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProtocolTest {

    @Test
    void convertsPayloadToUppercase() {
        assertEquals("OK|HEJ VERDEN", Protocol.handle("UPPER|hej verden"));
    }

    @Test
    void preservesExtraSeparatorsInPayload() {
        assertEquals("OK|HEJ|VERDEN", Protocol.handle("UPPER|hej|verden"));
    }

    @Test
    void rejectsUnknownCommand() {
        assertEquals("ERROR|UNKNOWN_COMMAND|LOWER", Protocol.handle("LOWER|Hej"));
    }

    @Test
    void rejectsMessageWithoutSeparator() {
        assertEquals("ERROR|INVALID_FORMAT", Protocol.handle("hej verden"));
    }

    @Test
    void rejectsEmptyMessage() {
        assertEquals("ERROR|INVALID_FORMAT", Protocol.handle(""));
    }
}
