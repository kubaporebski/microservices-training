package jporebski.microservices.resource_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

@SpringBootApplication
public class ResourceServiceApplication {

	public static void main(String[] args) {

		if (tryConnectToDB())
			SpringApplication.run(ResourceServiceApplication.class, args);
		else
			System.exit(1);
	}

	private static boolean tryConnectToDB() {
		var dbUrl = System.getenv("SC_RESOURCE_DB_URL").split(":");
		var sa = new InetSocketAddress(dbUrl[0], Integer.parseInt(dbUrl[1]));
		var tries = 1;
		var maxTries = 10;

		while (tries <= maxTries) {
			System.out.printf("Trying (%d/%d) to connect do the database... ", tries, maxTries);
			try {
				try (var tcp = new Socket()) {

					tcp.connect(sa);
					if (tcp.isConnected()) {
						System.out.println("OK.");
						return true;
					}

				} catch (IOException e) {
					System.out.printf("failed: %s \r\n", e.getMessage());
					tries++;
					Thread.sleep((long) (Math.random() * 1111 + 1111));
				}
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		System.err.printf("Could not obtain a connection to the database at %s.\r\n", System.getenv("SC_RESOURCE_DB_URL"));
		return false;
	}
}
