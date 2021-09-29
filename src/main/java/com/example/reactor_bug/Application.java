package com.example.reactor_bug;

import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
* Application
*/
@SpringBootApplication
public class Application {

	public static void main(final String args[]) throws InterruptedException {
		final Thread mainThread = new Thread(() -> {
			final SpringApplication app = new SpringApplication(Application.class);
			app.setDefaultProperties(Map.of("server.port", "9999"));
			app.run(args);
		});
		mainThread.run();

		Thread.sleep(5 * 1000);
		final Client client = new Client();
		client.testPost();
	}

	
}
