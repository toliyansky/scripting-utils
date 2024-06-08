import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static scripting.Utils.*;

public class LogTest {

    @Test
    public void testInfoLogByDefault() {
        final var TEST_OUTPUT_STRING = "Hello, world!";
        log.setLevel(Log.Level.INFO);
        log.enableLogLevelPrinting(false);
        log.enableTimePrinting(false);
        var originalOut = System.out;
        var outputStream = new ByteArrayOutputStream();
        var captureStream = new PrintStream(outputStream);
        System.setOut(captureStream);

        log.info(TEST_OUTPUT_STRING);

        System.setOut(originalOut);
        var output = outputStream.toString().trim();

        assertEquals(TEST_OUTPUT_STRING, output);
    }

    @Test
    public void testDebugLogWithLevelAndTime() {
        final var TEST_OUTPUT_STRING = "Hello, world!";
        log.setLevel(Log.Level.DEBUG);
        log.enableLogLevelPrinting(true);
        log.enableTimePrinting(true);
        var originalOut = System.out;
        var outputStream = new ByteArrayOutputStream();
        var captureStream = new PrintStream(outputStream);
        System.setOut(captureStream);

        log.debug(TEST_OUTPUT_STRING);

        System.setOut(originalOut);
        var output = outputStream.toString().trim();
        var expectedOutput = STR." [DEBUG] \{TEST_OUTPUT_STRING}";

        assertTrue(output.contains(expectedOutput));
    }
}
