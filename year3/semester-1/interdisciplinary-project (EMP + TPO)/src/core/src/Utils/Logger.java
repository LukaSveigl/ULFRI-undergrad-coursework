package Utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * The message logger.
 */
public class Logger {
    /** The log file path. */
    private String logPath;
    /** Flag that signifies if logging should be enabled. */
    private boolean loggingEnabled;
    /** The file writer. */
    private BufferedWriter writer;
    /** The message type. */
    public enum MessageType {
        LOG, WARNING, ERROR
    }

    /**
     * Constructs a new default Logger instance.
     */
    public Logger(boolean log) {
        this.logPath  = "system.log";
        this.loggingEnabled = log;
    }

    /**
     * Constructs a new custom Logger instance.
     * @param logPath String - The log file path.
     */
    public Logger(String logPath) {
        this.logPath = logPath;
    }

    /**
     * Logs the message in format date time type: message.
     * @param message String - The message to log.
     */
    public void log(String message, MessageType type) {
        if (!this.loggingEnabled) {
            return;
        }
        try {
            this.writer = new BufferedWriter(new FileWriter(this.logPath, true));

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("[yyyy-MM-dd HH:mm:ss]");

            this.writer.append(dtf.format(LocalDateTime.now()));
            this.writer.append(' ');

            switch (type) {
                case LOG -> this.writer.append("Log: ");
                case WARNING -> this.writer.append("Warning: ");
                case ERROR -> this.writer.append("Error: ");
            }

            // Generate string of class_name:method which calls this method.
            this.writer.append(
                    new Exception()
                            .getStackTrace()[1]
                            .getClassName()
            );
            this.writer.append(':');
            this.writer.append(
                    new Exception()
                            .getStackTrace()[1]
                            .getMethodName()
            );
            this.writer.append(": ");
            this.writer.append(message);
            this.writer.append(System.lineSeparator());

            for (int i = 0; i < 80; i++) {
                this.writer.append('=');
            }

            this.writer.append(System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                this.writer.flush();
                // Try to close writer no matter what happens.
                this.writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
