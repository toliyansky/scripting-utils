import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static scripting.Utils.*;

public class SleepTest {

    @Test
    public void sleepSecondsTest() {
        var startTime = System.currentTimeMillis();
        thread.sleepSeconds(1);
        var endTime = System.currentTimeMillis();
        var delta = endTime - startTime;

        assertTrue(delta > 950 && delta < 1050);
    }

    @Test
    public void sleepMsTest() {
        var startTime = System.currentTimeMillis();
        thread.sleepMs(1000);
        var endTime = System.currentTimeMillis();
        var delta = endTime - startTime;

        assertTrue(delta > 950 && delta < 1050);
    }
}
