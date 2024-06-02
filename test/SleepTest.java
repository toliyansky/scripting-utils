import static scripting.Utils.*;

public class SleepTest {
    public void sleepSecondsTest() {
        var startTime = System.currentTimeMillis();
        thread.sleepSeconds(1);
        var endTime = System.currentTimeMillis();
        var delta = endTime - startTime;
        if (delta > 1020 || delta < 980) {
            System.err.println("[sleepSecondsTest] failed");
            System.err.println("Expected: sleep for 3 seconds");
            System.err.println(STR."Actual: sleep for \{delta} ms");
        }
    }

    public void sleepMsTest() {
        var startTime = System.currentTimeMillis();
        thread.sleepMs(1000);
        var endTime = System.currentTimeMillis();
        var delta = endTime - startTime;
        if (delta > 1020 || delta < 980) {
            System.err.println("[sleepMsTest] failed");
            System.err.println("Expected: sleep for 3000 ms");
            System.err.println(STR."Actual: sleep for \{delta} ms");
        }
    }
}
