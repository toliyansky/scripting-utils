public class Tests {
    public static void main(String[] args) {
        var logTest = new LogTest();
        logTest.testInfoLogByDefault();
        logTest.testDebugLogWithLevelAndTime();

        var sleepTest = new SleepTest();
        sleepTest.sleepSecondsTest();
        sleepTest.sleepMsTest();

        var httpTest = new HttpTest();
        httpTest.testHttpDefaultGet();
        httpTest.testHttpPost();
    }
}
