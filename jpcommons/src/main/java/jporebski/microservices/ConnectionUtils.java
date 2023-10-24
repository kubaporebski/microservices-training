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
     * @param host server which we are trying to connect to
     * @param port - port on the aforementioned server
     * @param maxTries - how many times we are trying to connect?
     * @return true - the db is accepting connections / false - otherwise
     */
    public static boolean tryConnect(String host, int port, int maxTries) {
        var sa = new InetSocketAddress(host, port);
        var tries = 1;
        var lastException = (Exception)null;
        logger.info(String.format("Trying to connect to the server at %s:%d", host, port));

        while (tries <= maxTries) {
            logger.warning(String.format("Try #%d out of %d... ", tries, maxTries));

            try (var tcp = new Socket()) {
                tcp.connect(sa);
                if (tcp.isConnected()) {
                    logger.info("OK. TCP connection possible.");
                    return true;
                }

            } catch (IOException ioEx) {
                lastException = ioEx;
                logger.warning(String.format("TCP connection error occured: %s", ioEx.getMessage()));

                tries++;
                sleep(5_000); // 5 seconds
            }
        }

        logger.severe(String.format("Could not obtain a connection to the server because the following error: %s", lastException));
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
