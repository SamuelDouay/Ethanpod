# EthanPod

EthanPod is a Java application for managing and listening to podcasts.

## About the Project

EthanPod is an application developed in Java with JavaFX that allows you to subscribe to podcast RSS feeds, download episodes, and listen to them.

## Features

- Subscribe to podcast RSS feeds
- Download and manage episodes
- Intuitive user interface with JavaFX
- Local data storage with SQLite

## Prerequisites

- Java 22 or higher
- Maven for compilation and dependency management

## Installation

### Clone the Repository

```bash
git clone https://github.com/SamuelDouay/EthanPod.git
cd EthanPod
```

## Compile the Project

````bash
mvn clean package
````
This command generates an executable JAR file with all dependencies included.

## Run the Application

````bash
java -jar target/EthanPod-0.0.1-jar-with-dependencies.jar
````
## Technologies Used

- Java 24
- JavaFX 25 for the user interface
- SQLite for the database
- Log4j2 for logging
- JUnit 5 for testing
- Jackson for JSON processing
- RSSReader for parsing RSS feeds
- Ikonli for icons in the interface

### Structure du projet

The project follows the standard Maven project structure :

```
EthanPod/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── podcast/
│   │   │           └── ethanpod/
│   │   │               └── Main.java
│   │   └── resources/
│   └── test/
│       └── java/
└── pom.xml
```

## Top Level:

```
ethanpod-app [javafx-controls]
|
+--- ethanpod-view [javafx-controls, ikonli-javafx, ikonli-materialdesign2-pack]
|       |
|       +--- ethanpod-util [log4j-api, log4j-core]
|               |
|               +--- ethanpod-core [jackson-databind]
|
+--- ethanpod-service
|       |
|       +--- ethanpod-logic [sqlite-jdbc, jaxen, dom4j, jackson-databind]
|               |
|               +--- ethanpod-util [log4j-api, log4j-core]
|                       |
|                       +--- ethanpod-core [jackson-databind]
|
+--- ethanpod-logic [sqlite-jdbc, jaxen, dom4j, jackson-databind]
|
+--- ethanpod-util [log4j-api, log4j-core]
|
+--- ethanpod-core [jackson-databind]
```

### Tests

To run the tests :

```bash
mvn test
```