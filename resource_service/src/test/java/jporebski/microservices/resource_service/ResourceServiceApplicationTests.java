package jporebski.microservices.resource_service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.Assert;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ResourceServiceApplicationTests {

	private static final String URL_PATTERN = "http://localhost:%d/resources";

	@Autowired
	private TestRestTemplate restTemplate;

	@LocalServerPort
	int randomServerPort;

	@Value("classpath:sample0.mp3")
	Resource sample0mp3;

	private HttpEntity<byte[]> prepareRequest(byte[] mp3data) {
		var headers = new HttpHeaders();
		headers.set("Content-Type", "audio/mpeg");
		return new HttpEntity<>(mp3data, headers);
	}

	private String url() {
		return String.format(URL_PATTERN, randomServerPort);
	}

	@BeforeEach
	public void checkSample0mp3Loaded() {
		Assert.notNull(sample0mp3, "Sample file #0 should be there for testing");
	}

	// @BeforeAll
	// recommended to run a temporary docker mysql container:
	//   1. sudo docker run --rm -it -p 3306:3306 --name qmy1 -e MYSQL_ROOT_PASSWORD=root  mysql/mysql-server:8.0
	//   2. execute scripts/mysql_prepare_root.sql
	//   3. create database resources

	@Test
	public void upload_a_resource_happyPath() throws Exception {
		// Arrange
		var request = prepareRequest(sample0mp3.getContentAsByteArray());

		// Act
		var response = restTemplate.postForEntity(url(), request, String.class);

		// Assert
		Assert.isTrue(response.getStatusCode().is2xxSuccessful(), "Resource should be created");
	}

	@Test
	public void upload_an_empty_resource() throws Exception {
		// Arrange
		var request = prepareRequest(new byte[0]);


		// Act
		var response = restTemplate.postForEntity(url(), request, String.class);

		// Assert
		Assert.isTrue(response.getStatusCode().isError(), "Sent empty byte[] -> a resource should NOT be created");
	}

	@Test
	public void upload_a_null() throws Exception {
		// Arrange
		var request = prepareRequest(null);

		// Act
		var response = restTemplate.postForEntity(url(), request, String.class);

		// Assert
		Assert.isTrue(response.getStatusCode().isError(), "Sent NULL -> a resource should NOT be created");
	}

	@Test
	public void upload_a_wrong_data() throws Exception {
		// Arrange
		var request = prepareRequest(new byte[] { 1, 2, 3, 5, 8, 13, 21});

		// Act
		var response = restTemplate.postForEntity(url(), request, String.class);

		// Assert
		Assert.isTrue(response.getStatusCode().isError(), "Sent not a mp3 data -> a resource should NOT be created");
	}

}
