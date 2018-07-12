## Schema Evolution Using Spring Cloud Stream + MapR Kafka

This project demonstrates schema evolution using following projects:

1. **schema-registry:** Spring Cloud Stream based avro schema registry
2. **avro-producer1:** Spring Boot micro-service build using Spring Cloud Stream which creates a _**Person**_ and 
   writes to MapR Kafka Topic using version - v1.0 of _**Person**_ schema
   The Person schema contain following attributes:
   - id
   - name
   - age
   - sex
   - married
3. **avro-producer2:** Spring Boot micro-service build using Spring Cloud Stream which creates a _**Person**_ and 
      writes to MapR Kafka Topic using version - v2.0 of _**Person**_ schema. In version 2 of _**Person**_ attribute
      _**name**_ is split into two parts:
      - firstname (Person(v2.0).firstname = Person(v1.0).name)
      - lastname
4. **avro-consumer:** A consumer which resilient to _**Person**_ schema changes


## Pre-requisite

The project uses MapR sanbox. To install MapR following steps specified in project - [SpringBootMapR](https://github.com/mgorav/SpringBootMapR)

To install MapR Kafka by issuing following commands in the MapR sandbox:

```bash

apt-get install mapr-kafka

/opt/mapr/server/configure.sh -R

## Go to MapR Kafka 

root@maprdemo:/opt/mapr/kafka/kafka-1.0.1/config#vi server.properties
advertised.host.name=maprdemo
advertised.port=9092
zookeeper.connect=localhost:5181

## Start MapR Kafka

root@maprdemo:/opt/mapr/kafka/kafka-1.0.1/bin# ./kafka-server-start.sh  ../config/server.properties

```

## Play time

1. Run project _schema-registry_
2. Run project _avro-producer1_
```bash
curl -X POST http://localhost:4444/persons
curl -X POST http://localhost:4444/persons
curl -X POST http://localhost:4444/persons
```
3. Run project _avro-producer2_
```bash
curl -X POST http://localhost:5555/persons
curl -X POST http://localhost:5555/persons
curl -X POST http://localhost:5555/persons
```
4. Run project _avro-consumer_
Following output will generated:
```bash

2018-07-12 23:21:34.924  INFO 30711 --- [container-0-C-1] ication$$EnhancerBySpringCGLIB$$f73d4a84 : {"id": "3cac2e61-5557-4fa7-a511-edefb988ba91-v1.0", "firstname": "Name1", "lastname": "", "age": 22, "sex": 0, "married": false}
{"id": "3cac2e61-5557-4fa7-a511-edefb988ba91-v1.0", "firstname": "Name1", "lastname": "", "age": 22, "sex": 0, "married": false}
2018-07-12 23:21:36.155  INFO 30711 --- [container-0-C-1] ication$$EnhancerBySpringCGLIB$$f73d4a84 : {"id": "e755a123-6bff-4921-9609-8e357e38fa8b-v1.0", "firstname": "Name3", "lastname": "", "age": 24, "sex": 0, "married": false}
{"id": "e755a123-6bff-4921-9609-8e357e38fa8b-v1.0", "firstname": "Name3", "lastname": "", "age": 24, "sex": 0, "married": false}
2018-07-12 23:21:42.358  INFO 30711 --- [container-0-C-1] ication$$EnhancerBySpringCGLIB$$f73d4a84 : {"id": "917fc682-3df6-459b-917d-e09984b0a550-v2.0", "firstname": "Name1", "lastname": "LastName2", "age": 23, "sex": 0, "married": true}
{"id": "917fc682-3df6-459b-917d-e09984b0a550-v2.0", "firstname": "Name1", "lastname": "LastName2", "age": 23, "sex": 0, "married": true}
2018-07-12 23:21:43.678  INFO 30711 --- [container-0-C-1] ication$$EnhancerBySpringCGLIB$$f73d4a84 : {"id": "3340e3f5-4af2-4eeb-850e-3a536fa947a1-v2.0", "firstname": "Name4", "lastname": "LastName5", "age": 26, "sex": 0, "married": false}
{"id": "3340e3f5-4af2-4eeb-850e-3a536fa947a1-v2.0", "firstname": "Name4", "lastname": "LastName5", "age": 26, "sex": 0, "married": false}

```

It can be seen that _*Person*_ version 1.0 & 2.0 has no impact on the consumer and both can exist.




   
