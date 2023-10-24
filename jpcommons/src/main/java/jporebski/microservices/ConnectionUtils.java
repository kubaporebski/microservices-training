package jporebski.microservices;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Objects;
import java.util.logging.Logger;

public final class ConnectionUtils {

    private final static Logger logger = Logger.getLogger(ConnectionUtils.class.getName());

    private ConnectionUtils() { }

    /**
     * Low-level (TCP socket) checking if a connection to some server is possible.
     *
     * @return true - the db is accepting connections / false - otherwise
     */
    public static boolean tryConnect(String host, int port) {
        var sa = new InetSocketAddress(host, port);
        var tries = 1;
        var maxTries = Integer.parseInt(Objects.toString(System.getenv("SC_DB_RETRY_COUNT"), "3"));
        var lastException = (Exception)null;

        logger.info(String.format("Trying to connect to the server at %s:%d", host, port));

        while (tries < maxTries) {
            logger.warning(String.format("Try #%d out of %d... ", tries, maxTries));

            try (var tcp = new Socket()) {
                tcp.connect(sa);
                if (tcp.isConnected()) {
                    logger.info("OK. Database connection possible.");
                    return true;
                }

            } catch (IOException ioEx) {
                lastException = ioEx;
                logger.warning(String.format("Database connection error occured: %s", ioEx.getMessage()));

                tries++;
                if (tries <= maxTries)
                    sleep(5_000); // 5 seconds
            }
        }

        logger.severe(String.format("Could not obtain a connection to the database because the following error: %s", lastException));
        return false;
    }

    public static void sleep(int milliseconds)
    {
        try {
            Thread.sleep(milliseconds);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
