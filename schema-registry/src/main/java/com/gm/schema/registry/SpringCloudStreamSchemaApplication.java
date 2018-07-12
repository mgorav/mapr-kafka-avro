package com.gm.schema.registry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.schema.server.EnableSchemaRegistryServer;

@SpringBootApplication
@EnableSchemaRegistryServer
public class SpringCloudStreamSchemaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudStreamSchemaApplication.class, args);
    }
}
