package org.kafkainaction.kstreams;

import java.util.Properties;

import org.apache.commons.text.WordUtils;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.ValueMapper;

public class GrammerProcessor {

    public static void main(String[] args) throws Exception {

        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "Grammer");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        
        Serde<String> stringSerde = Serdes.String();
    	StreamsBuilder builder = new StreamsBuilder();

    	KStream<String, String> grammarStream = builder.stream("grammar-landing",
    	    Consumed.with(stringSerde, stringSerde));

    	KStream<String, String> capStream = grammarStream.mapValues(
			(ValueMapper<String, String>) WordUtils::capitalizeFully); 

    	capStream.to("grammar-cap", Produced.with(stringSerde, stringSerde));

		KafkaStreams kafkaStreams = new KafkaStreams(builder.build(), props);
    	kafkaStreams.start();
    	Thread.sleep(5000);
    	kafkaStreams.close(); 
    }
}
