package jporebski.microservices.resource_service;

import jporebski.microservices.ConnectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
	 * Low-level (TCP socket) checking if a connection to the database is possible.
	 *
	 * @return true - the db is accepting connections / false - otherwise
	 */
	private static boolean tryConnectToDB() {
		var databaseHost = System.getenv("SC_RESOURCE_DB_HOST");
		var databasePort = Integer.parseInt(System.getenv("SC_RESOURCE_DB_PORT"));
		var maxTries = Integer.parseInt(Objects.toString(System.getenv("SC_DB_RETRY_COUNT"), "3"));
		return ConnectionUtils.tryConnect(databaseHost, databasePort, maxTries);
	}
}
