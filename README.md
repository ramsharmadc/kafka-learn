## Start Kafka, local installation

    start bin\windows\zookeeper-server-start.bat config\zookeeper.properties
    start bin\windows\kafka-server-start.bat config\server.properties

## Start consumer

    java -jar target\consumer-1.0-SNAPSHOT.jar server config.yml
    
## Start producer

    java -jar target\producer-1.0-SNAPSHOT.jar server config.yml

## Post a message to the producer

    curl -H "Content-Type: application/json" -X POST -d '{"some-key":"some-value"}' http://localhost:8080/my-topic
