package com.gm.avro.kafka.consumer;

import com.gm.message.Person;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.cloud.stream.schema.client.EnableSchemaRegistryClient;
import org.apache.commons.logging.Log;
@SpringBootApplication
@EnableBinding(Sink.class)
@EnableSchemaRegistryClient
public class AvroKafkaConsumerApplication {

    private final Log logger = LogFactory.getLog(getClass());

    public static void main(String[] args) {
        SpringApplication.run(AvroKafkaConsumerApplication.class, args);
    }

    @StreamListener(Sink.INPUT)
    public void process(Person data) {
        logger.info(data);
        System.out.println(data);
    }
}
