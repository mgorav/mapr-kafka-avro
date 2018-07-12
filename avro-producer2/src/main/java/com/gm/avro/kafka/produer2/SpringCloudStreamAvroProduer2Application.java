package com.gm.avro.kafka.produer2;

import com.gm.message.Person;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.cloud.stream.schema.client.EnableSchemaRegistryClient;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
public class SpringCloudStreamAvroProduer2Application {

    @Autowired
    private Source source;

    private Random random = new Random();
    private AtomicInteger cnt = new AtomicInteger(1);

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudStreamAvroProduer2Application.class, args);
    }

    @RequestMapping(value = "/persons", method = POST)
    public String createDefaultPerson() {
        source.output().send(MessageBuilder.withPayload(aPerson()).build());
        return "CREATED: Person with verson v2.0 payload!";
    }

    private Person aPerson() {
        Person person = new Person();
        person.setId(UUID.randomUUID().toString() + "-v2.0");
        person.setFirstname("Name" + cnt.getAndIncrement());
        person.setLastname("LastName" + cnt.getAndIncrement());
        person.setAge(20 + cnt.getAndIncrement());
        person.setMarried(cnt.intValue() % 2 == 0 ? true : false);
        return person;
    }

    @RequestMapping(value = "/person", method = POST)
    public String createSpecificPerson(@RequestParam(value = "id") String id, @RequestParam(value = "firstname") String firstname,
                               @RequestParam(value = "lastname") String lastname,
                               @RequestParam(value = "age") Integer age, @RequestParam(value = "married") Boolean married) {

        Person person = new Person();
        person.setId(id + "-v2.0");
        person.setFirstname(firstname);
        person.setLastname(lastname);
        person.setAge(age);
        person.setMarried(married);

        source.output().send(MessageBuilder.withPayload(person).build());
        return "CREATED: Person with version = v2.0 payload!";
    }
}
