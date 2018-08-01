package com.gs.streams.streamswordcountexample;

import java.util.Arrays;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.KTable;
import org.springframework.stereotype.Component;

@Component
public class KafkaStreamAgent {
	
	public void start() {
		final KStreamBuilder builder = new KStreamBuilder();

		final KStream<String, String> kafkaStreams = builder.stream(Serdes.String(), Serdes.String(), "streams-plaintext-input");

		// Serializers/deserializers (serde) for String and Long types
		final Serde<String> stringSerde = Serdes.String();
		final Serde<Long> longSerde = Serdes.Long();

		// Construct a `KStream` from the input topic "streams-plaintext-input", where
		// message values
		// represent lines of text (for the sake of this example, we ignore whatever may
		// be stored
		// in the message keys).
		KTable<String, Long> wordCounts = kafkaStreams
				// Split each text line, by whitespace, into words.
				.flatMapValues(value -> Arrays.asList(value.toLowerCase().split("\\W+")))
				// Group the text words as message keys
				.groupBy((key, value) -> value)
				// Count the occurrences of each word (message key).
				.count();

		// Store the running counts as a changelog stream to the output topic.
		wordCounts.toStream().to(stringSerde, longSerde, "streams-wordcount-output");

		final KafkaStreams streams = new KafkaStreams(builder,
				WordCountTaskFactory.newStreamConfig("group-id-example"));

		// attach shutdown handler to catch control-c
		Runtime.getRuntime().addShutdownHook(new Thread("streams-wordcount-shutdown-hook") {
			@Override
			public void run() {
				streams.close();
			}
		});

		try {
			streams.start();
		} catch (Throwable e) {
			System.exit(1);
		}
	}

}
