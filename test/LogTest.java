import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static scripting.Utils.*;

public class LogTest {
    public void testInfoLogByDefault() {
        final var TEST_OUTPUT_STRING = "Hello, world!";
        log.setLevel(Log.Level.INFO);
        var originalOut = System.out;
        var outputStream = new ByteArrayOutputStream();
        var captureStream = new PrintStream(outputStream);
        System.setOut(captureStream);

        log.info(TEST_OUTPUT_STRING);

        System.setOut(originalOut);
        var output = outputStream.toString().trim();
        if (!TEST_OUTPUT_STRING.equals(output)) {
            System.err.println("[testInfoLog] failed");
            System.err.println(STR."Expected: '\{TEST_OUTPUT_STRING}'");
            System.err.println(STR."Got: '\{output}'");
        }
    }

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
        if (!output.contains(expectedOutput)) {
            System.err.println("[testDebugLogWithLevelAndTime] failed");
            System.err.println(STR."Expected: '\{expectedOutput}'");
            System.err.println(STR."Got: '\{output}'");
        }
    }
}
