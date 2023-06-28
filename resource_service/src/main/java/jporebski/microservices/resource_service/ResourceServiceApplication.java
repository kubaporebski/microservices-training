package jporebski.microservices.resource_service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Objects;

@SpringBootApplication
public class ResourceServiceApplication {

	private final static Logger logger = LoggerFactory.getLogger(ResourceServiceApplication.class);

	public static void main(String[] args) {

		if (tryConnectToDB())
			SpringApplication.run(ResourceServiceApplication.class, args);
		else
			System.exit(1);
	}

	/**
	 * Low-level (TCP socket) checking if connection to the database is possible.
	 *
	 * @return true - the db is accepting connections / false - otherwise
	 */
	private static boolean tryConnectToDB() {
		var databaseHost = System.getenv("SC_RESOURCE_DB_HOST");
		var databasePort = Integer.parseInt(System.getenv("SC_RESOURCE_DB_PORT"));
		var sa = new InetSocketAddress(databaseHost, databasePort);
		var tries = 1;
		var maxTries = Integer.parseInt(Objects.toString(System.getenv("SC_DB_RETRY_COUNT"), "15"));
		var lastException = (Exception)null;

		logger.info("Trying to connect to database at {}:{}", databaseHost, databasePort);

		while (tries <= maxTries) {
			logger.warn("Try #{} out of {}... ", tries, maxTries);
			try {
				try (var tcp = new Socket()) {

					tcp.connect(sa);
					if (tcp.isConnected()) {
						logger.info("OK. Database connection possible.");
						return true;
					}

				} catch (IOException ioEx) {
					lastException = ioEx;
					logger.warn("Database connection error occured: {}", ioEx.getMessage());
					tries++;
					Thread.sleep((long) (Math.random() * 1111 + 1111));
				}
			}
			catch (Exception wtfEx) {
				logger.error("Unexpected error", wtfEx);
				return false;
			}
		}

		logger.error("Could not obtain a connection to the database because the following error", lastException);
		return false;
	}
}
