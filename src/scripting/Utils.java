package scripting;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.*;

import static java.lang.StringTemplate.STR;

public class Utils {

    public static Log log = new Log();

    /**
     * The object to work with files
     */
    public static File file = new File();

    /**
     * The object to make HTTP requests
     */
    public static Http http = new Http();

    /**
     * The object to work with threads
     */
    public static Thread thread = new Thread();

    /**
     * The object to work with terminal
     */
    public static Terminal terminal = new Terminal();

    /**
     * print/println/readln methods are appear in Java 23 (in preview).
     * <a href="https://cr.openjdk.org/~prappo/8305457/java.base/java/io/IO.html">documentation</a>
     */
    public static void print(Object obj) {
        System.out.print(obj);
    }

    public static void println(Object obj) {
        System.out.println(obj);
    }

    public static String readln(String prompt) {
        return System.console().readLine(prompt);
    }

    public static class Log {
        public enum Level {
            ERROR, WARN, INFO, DEBUG, TRACE
        }

        private static Level level = Level.INFO;

        private static boolean printLogLevelEnabled = false;

        private static boolean printTimeEnabled = false;

        private static String timeFormat = "HH:mm:ss.SSS";

        public void setLevel(Level level) {
            Log.level = level;
        }

        public void setLevel(String level) {
            Log.level = Level.valueOf(level.toUpperCase());
        }

        public void enableLogLevelPrinting(boolean printLogLevel) {
            Log.printLogLevelEnabled = printLogLevel;
        }

        public void enableTimePrinting(boolean printTime) {
            Log.printTimeEnabled = printTime;
        }

        public void setTimeFormat(String timeFormat) {
            Log.timeFormat = timeFormat;
        }

        private void print(Object obj, Level level) {
            if (level.ordinal() > Log.level.ordinal()) {
                return;
            }
            var messageToPrint = new StringBuilder();
            if (printTimeEnabled) {
                messageToPrint.append(LocalTime.now().format(DateTimeFormatter.ofPattern(timeFormat)));
            }
            if (printLogLevelEnabled) {
                messageToPrint.append(" [").append(level).append("]");
            }
            messageToPrint.append(" ");
            messageToPrint.append(obj);
            System.out.println(messageToPrint);

        }

        /**
         * Logs a message to the system out in INFO level. And also duplicate it to the system err.
         *
         * @param message the message to log
         */
        public void error(String message) {
            print(message, Level.ERROR);
        }

        /**
         * Logs a message to the system out in INFO level.
         *
         * @param obj the message to log
         */
        public void info(Object obj) {
            print(obj, Level.INFO);
        }

        /**
         * Logs a message to the system out in WARN level.
         *
         * @param obj the message to log
         */
        public void warn(Object obj) {
            print(obj, Level.WARN);
        }

        /**
         * Logs a message to the system out in DEBUG level.
         *
         * @param obj the message to log
         */
        public void debug(Object obj) {
            print(obj, Level.DEBUG);
        }

        /**
         * Logs a message to the system out in TRACE level.
         *
         * @param obj the message to log
         */
        public void trace(Object obj) {
            print(obj, Level.TRACE);
        }
    }

    public static class Http {
        public <T> HttpResponse<T> get(String uri, HttpRequest.Builder request, HttpResponse.BodyHandler<T> bodyHandler) {
            try (var httpClient = HttpClient.newHttpClient()) {
                if (request == null) {
                    request = HttpRequest.newBuilder()
                            .GET()
                            .uri(URI.create(uri));
                }
                return httpClient.send(request.build(), bodyHandler);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            } catch (InterruptedException e) {
                throw new UncheckedInterruptedException(e);
            }
        }

        public HttpResponse<String> get(String uri, HttpRequest.Builder request) {
            return get(uri, request, HttpResponse.BodyHandlers.ofString());
        }

        public HttpResponse<String> get(String uri) {
            return get(uri, null);
        }

        public <T> HttpResponse<T> request(HttpRequest.Builder request, HttpResponse.BodyHandler<T> bodyHandler) {
            try (var httpClient = HttpClient.newHttpClient()) {
                if (request == null) {
                    throw new IllegalArgumentException("Request must be provided");
                }
                if (bodyHandler == null) {
                    throw new IllegalArgumentException("Body handler must be provided for POST request");
                }
                return httpClient.send(request.build(), bodyHandler);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            } catch (InterruptedException e) {
                throw new UncheckedInterruptedException(e);
            }
        }
    }

    public static class File {

        /**
         * Creates a file at the specified path.
         *
         * @param path path to the file (include file name)
         * @return path to the created file
         */
        public Path create(String path) {
            var _path = Path.of(path);
            try {
                return Files.createFile(_path);
            } catch (FileAlreadyExistsException e) {
                log.debug(STR."File \{_path} already exists");
                return _path;
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        /**
         * Writes content to the end of file at the specified path.
         * If the file does not exist, it will be created.
         *
         * @param path path to the file
         * @param text content to write
         * @return path to the file
         */
        public Path append(String path, String text) {
            var isFileExist = Files.exists(Path.of(path));
            if (!isFileExist) {
                create(path);
            }
            try {
                return Files.writeString(Path.of(path), text, StandardOpenOption.APPEND);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        /**
         * Writes the content the file at the specified path.
         * If the file does not exist, it will be created.
         * If the file exists, it will be deleted and created new one.
         *
         * @param path path to the file
         * @param text content to write
         * @return path to the file
         */
        public Path rewrite(String path, String text) {
            var isFileExist = Files.exists(Path.of(path));
            if (isFileExist) {
                delete(path);
            }
            return append(path, text);
        }

        /**
         * Deletes the file or directory at the specified path.
         *
         * @param path path to the file or directory
         */
        public void delete(String path) {
            try {
                Files.delete(Path.of(path));
            } catch (DirectoryNotEmptyException e) {
                // remove directory content and directory itself
                log.debug(STR."Directory \{path} is not empty. Deleting directory content and directory itself");
                try {
                    Files.walk(Path.of(path))
                            .sorted(Comparator.reverseOrder())
                            .map(Path::toFile)
                            .forEach(java.io.File::delete);
                } catch (IOException ex) {
                    throw new UncheckedIOException(ex);
                }
            } catch (NoSuchFileException e) {
                log.debug(STR."File \{path} does not exist. Nothing to delete");
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        public List<String> readAllLines(Path path) {
            try {
                return Files.readAllLines(path);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        public List<String> readAllLines(String path) {
            return readAllLines(Path.of(path));
        }

        public boolean exists(String path) {
            return Files.exists(Path.of(path));
        }
    }

    public static class Terminal {

        /**
         * Executes the command in the terminal.
         *
         * @param command the command with flags etc. to execute
         * @param timeout the maximum time to wait for the command to complete (milliseconds)
         * @return the terminal response
         */
        public Response execute(String command, long timeout) {
            var executor = Executors.newSingleThreadExecutor();
            var processBuilder = new ProcessBuilder();
            processBuilder.command("bash", "-c", command); // Adjust this line if you're not using a Unix-like OS

            Callable<Response> task = () -> {
                Process process = processBuilder.start();
                StringBuilder output = new StringBuilder();
                Response response = new Response();
                try (var reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        output.append(line).append("\n");
                    }
                }
                response.exitCode = process.waitFor();
                response.output = output.toString();
                if (response.exitCode != 0) {
                    try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                        StringBuilder errorOutput = new StringBuilder();
                        String line;
                        while ((line = errorReader.readLine()) != null) {
                            errorOutput.append(line).append("\n");
                        }
                        response.output += "Error: " + errorOutput.toString();
                    }
                }
                return response;
            };

            var future = executor.submit(task);
            try {
                return future.get(timeout, TimeUnit.MILLISECONDS);
            } catch (TimeoutException e) {
                future.cancel(true);
                log.error(STR."Command \{command} timed out after \{timeout} milliseconds");
                throw new UncheckedTimeoutException("Command timed out after " + timeout + " milliseconds", e);
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException("Command execution failed", e);
            } finally {
                executor.shutdownNow();
            }
        }

        public static class Response {
            public String output;
            public int exitCode;
        }
    }

    public static class Thread {
        /**
         * Sleeps for the specified number of milliseconds.
         *
         * @param ms the number of milliseconds to sleep
         */
        public static void sleepMs(long ms) {
            var sleepStartTime = System.currentTimeMillis();
            try {
                java.lang.Thread.sleep(ms);
            } catch (InterruptedException e) {
                log.debug(STR."Sleep interrupted. Planned sleep time: \{ms} ms. Actual sleep time: \{System.currentTimeMillis() - sleepStartTime} ms");
            }
        }

        /**
         * Sleeps for the specified number of seconds.
         *
         * @param seconds the number of seconds to sleep
         */
        public static void sleepSeconds(long seconds) {
            sleepMs(seconds * 1000);
        }

        /**
         * Runs the task in a separate thread.
         *
         * @param task the task to run
         * @return the Future representing the task
         */
        public static Future<?> run(Runnable task) {
            return Executors.newSingleThreadExecutor().submit(task);
        }
    }

    public static class UncheckedTimeoutException extends RuntimeException {
        public UncheckedTimeoutException(String message, TimeoutException cause) {
            super(message, cause);
        }
    }

    public static class UncheckedInterruptedException extends RuntimeException {
        public UncheckedInterruptedException(InterruptedException cause) {
            super(cause);
        }
    }

}