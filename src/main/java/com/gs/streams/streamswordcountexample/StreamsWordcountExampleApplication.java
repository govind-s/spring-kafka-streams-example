package com.gs.streams.streamswordcountexample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class StreamsWordcountExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(StreamsWordcountExampleApplication.class, args);
	}

	@Autowired
	private KafkaStreamAgent kafkaAgent;

	@EventListener(ApplicationReadyEvent.class)
	public void applicationReady() throws InterruptedException {
		kafkaAgent.start();
	}
}
