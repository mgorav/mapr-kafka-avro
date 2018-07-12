package com.gm.avro.kafka.producer1;

import com.gm.message.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.cloud.stream.schema.client.EnableSchemaRegistryClient;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@SpringBootApplication
@EnableSchemaRegistryClient
@EnableBinding(Source.class)
@RestController
public class SpringCloudStreamKafkaProducer {

    @Autowired
    private Source source;

    private Random random = new Random();
    private AtomicInteger cnt = new AtomicInteger(1);

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudStreamKafkaProducer.class, args);
    }

    @RequestMapping(value = "/persons", method = POST)
    public String sendMessage() {
        source.output().send(MessageBuilder.withPayload(aPerson()).build());
        return "CREATED: Person with verson v1.0 payload!";
    }

    private Person aPerson() {
        Person person = new Person();
        person.setId(UUID.randomUUID().toString() + "-v1.0");
        person.setName("Name" + cnt.getAndIncrement());
        person.setAge(20 + cnt.getAndIncrement());
        person.setMarried(false);
        return person;
    }

    @RequestMapping(value = "/person", method = POST)
    public String sendMessageX(@RequestParam(value = "id") String id, @RequestParam(value = "name") String name,
                               @RequestParam(value = "age") Integer age, @RequestParam(value = "married") Boolean married) {

        Person person = new Person();
        person.setId(id + "-v1");
        person.setName(name);
        person.setAge(age);
        person.setMarried(married);

        source.output().send(MessageBuilder.withPayload(person).build());
        return "CREATED: Person with version = v1.0 payload!";
    }
}
