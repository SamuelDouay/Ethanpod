[![Quality Gate Status](http://sonar.sadory.fr/api/project_badges/measure?project=Ethanpod&metric=alert_status&token=sqb_fe340b1b691071b11fbe3ee03be56775038b39ac)](http://sonar.sadory.fr/dashboard?id=Ethanpod)
[![Maintainability Rating](http://sonar.sadory.fr/api/project_badges/measure?project=Ethanpod&metric=sqale_rating&token=sqb_fe340b1b691071b11fbe3ee03be56775038b39ac)](http://sonar.sadory.fr/dashboard?id=Ethanpod)
[![Reliability Rating](http://sonar.sadory.fr/api/project_badges/measure?project=Ethanpod&metric=reliability_rating&token=sqb_fe340b1b691071b11fbe3ee03be56775038b39ac)](http://sonar.sadory.fr/dashboard?id=Ethanpod)
[![Security Rating](http://sonar.sadory.fr/api/project_badges/measure?project=Ethanpod&metric=security_rating&token=sqb_fe340b1b691071b11fbe3ee03be56775038b39ac)](http://sonar.sadory.fr/dashboard?id=Ethanpod)
[![Lines of Code](http://sonar.sadory.fr/api/project_badges/measure?project=Ethanpod&metric=ncloc&token=sqb_fe340b1b691071b11fbe3ee03be56775038b39ac)](http://sonar.sadory.fr/dashboard?id=Ethanpod)
[![Coverage](http://sonar.sadory.fr/api/project_badges/measure?project=Ethanpod&metric=coverage&token=sqb_fe340b1b691071b11fbe3ee03be56775038b39ac)](http://sonar.sadory.fr/dashboard?id=Ethanpod)
[![Technical Debt](http://sonar.sadory.fr/api/project_badges/measure?project=Ethanpod&metric=sqale_index&token=sqb_fe340b1b691071b11fbe3ee03be56775038b39ac)](http://sonar.sadory.fr/dashboard?id=Ethanpod)
[![Vulnerabilities](http://sonar.sadory.fr/api/project_badges/measure?project=Ethanpod&metric=vulnerabilities&token=sqb_fe340b1b691071b11fbe3ee03be56775038b39ac)](http://sonar.sadory.fr/dashboard?id=Ethanpod)
[![Code Smells](http://sonar.sadory.fr/api/project_badges/measure?project=Ethanpod&metric=code_smells&token=sqb_fe340b1b691071b11fbe3ee03be56775038b39ac)](http://sonar.sadory.fr/dashboard?id=Ethanpod)
[![Bugs](http://sonar.sadory.fr/api/project_badges/measure?project=Ethanpod&metric=bugs&token=sqb_fe340b1b691071b11fbe3ee03be56775038b39ac)](http://sonar.sadory.fr/dashboard?id=Ethanpod)

# EthanPod

EthanPod is a Java application for managing and listening to podcasts.

---

## About the Project 🎧

EthanPod is a desktop application built in Java with JavaFX. It allows you to easily manage and listen to your favorite
podcasts. You can subscribe to podcast RSS feeds, download episodes, and play them directly within the application.


---

# EthanPod Architecture - Maven Modules

## Overview

The EthanPod application follows a modular Maven architecture with a clear separation of responsibilities using a
layered pattern. The project is organized around 9 main modules managed by a parent POM.

---

## Module Structure

### 📁 Parent POM (`ethanpod-parent`)

```xml

<groupId>fr.github</groupId>
<artifactId>ethanpod-parent</artifactId>
<version>0.0.1</version>
<packaging>pom</packaging>
```

**Responsibilities :**

- Centralized dependency version management
- Common Maven configuration
- Shared plugins (JaCoCo, Compiler)
- Java 22 as target version

---

## Architectural Layers

### 1. 🏗️ Foundation Layer (Core)

#### `ethanpod-core`

**Role :** Domain objects and business entities

```yaml
Dependencies:
  - Jackson (JSON serialization)
  - Log4j API
  - JUnit 5 (tests)
  - Mockito (tests)
```

#### `ethanpod-exception`

**Role :** Centralized exception handling

```yaml
Dependencies:
  - No external dependencies
```

#### `ethanpod-util`

**Role :** Utilities and logging configuration

```yaml
Dependencies:
  - SLF4J API
  - Log4j SLF4J Implementation
  - Log4j Core
  - ethanpod-core
  - ethanpod-exception
```

### 2. 🧠 Business Logic Layer

#### `ethanpod-logic`

**Role :** Business logic, data access, repositories

```yaml
Dependencies:
  - ethanpod-core
  - ethanpod-util
  - ethanpod-exception
  - SQLite JDBC
  - HikariCP (connection pooling)
  - Jackson (JSON)
  - Dom4j (XML processing)
  - Jaxen (XPath)
  - Log4j SLF4J Implementation
```

### 3. 🔧 Service Layer

#### `ethanpod-service`

**Role :** Business services and orchestration

```yaml
Dependencies:
  - ethanpod-core
  - ethanpod-util
  - ethanpod-logic
```

### 4. 🖥️ Presentation Layer

#### `ethanpod-view`

**Role :** User interface and visual components

```yaml
Dependencies:
  - ethanpod-core
  - ethanpod-util
  - ethanpod-logic
  - ethanpod-service
  - ethanpod-event
  - JavaFX Controls
  - Ikonli JavaFX
  - Ikonli Material Design 2
```

#### `ethanpod-controller`

**Role :** MVC controllers and presentation logic

```yaml
Dependencies:
  - ethanpod-core
  - ethanpod-service
  - Log4j API
```

#### `ethanpod-event`

**Role :** Event system and handlers

```yaml
Dependencies:
  - ethanpod-core
  - ethanpod-util
  - JavaFX Graphics
```

### 5. 🚀 Application Module

#### `ethanpod-app`

**Role :** Entry point and final assembly

```yaml
Dependencies:
  - ALL internal modules
  - JavaFX Controls
Main Class: fr.github.ethanpod.app.Main
```

**Special Configuration :**

- Maven Assembly plugin to create an executable JAR
- Inclusion of all dependencies
- Main entry point of the application

---

## Dependency Diagram

```
    ┌─────────────────────────┐
    │    ethanpod-parent      │
    │   (POM management)      │
    └─────────────────────────┘
                 │
    ┌────────────┼───────────────┐
    │            │               │
    ▼            ▼               ▼
┌─────────┐ ┌───────────┐ ┌─────────────┐
│  core   │ │ exception │ │    util     │
└─────────┘ └───────────┘ └─────────────┘
    │             │              │
    └─────────────┼──────────────┘
                  ▼
            ┌─────────────┐
            │    logic    │
            │ (Business)  │
            └─────────────┘
                    │
                    ▼
            ┌─────────────┐
            │   service   │
            └─────────────┘
                    │
       ┌────────────┼─────────────┐
       ▼            ▼             ▼
  ┌─────────┐ ┌────────────┐ ┌─────────┐
  │  view   │ │ controller │ │ event   │
  └─────────┘ └────────────┘ └─────────┘
       │            │             │
       └────────────┼─────────────┘
                    ▼
              ┌─────────────┐
              │ethanpod-app │
              │ (Assembly)  │
              └─────────────┘
```

---

## Technologies and Frameworks

### 🗄️ Persistance et Données

| Technology  | Version  | Usage              |
|-------------|----------|--------------------|
| SQLite JDBC | 3.46.0.1 | Embedded database  |
| HikariCP    | 6.3.0    | Connection pooling |

### 🎨 User Interface

| Technology        | Version | Usage               |
|-------------------|---------|---------------------|
| JavaFX            | 24.0.1  | Graphical interface |
| Ikonli            | 12.3.1  | Icon system         |
| Material Design 2 | 12.3.1  | Icon pack           |

### 📊 Data Processing

| Technology | Version | Usage              |
|------------|---------|--------------------|
| Jackson    | 2.18.2  | JSON serialization |
| Dom4j      | 2.1.4   | XML processing     |
| Jaxen      | 2.0.0   | XPath queries      |

### 📝 Logging

| Technology | Version | Usage          |
|------------|---------|----------------|
| Log4j 2    | 2.23.1  | Logging system |
| SLF4J      | 2.0.17  | Logging facade |

### 🧪 Testing

| Technology | Version | Usage             |
|------------|---------|-------------------|
| JUnit 5    | 5.10.0  | Testing framework |
| Mockito    | 5.18.0  | Mocking for tests |

---

## Architecture Flow

### Pattern MVC Distribué

1. **View Layer** (`ethanpod-view`)
    - JavaFX components
    - Data binding
    - Visual presentation

2. **Controller Layer** (`ethanpod-controller`)
    - User event handling
    - Coordination between View and Service

3. **Service Layer** (`ethanpod-service`)
    - Application logic
    - Process orchestration

4. **Business Logic Layer** (`ethanpod-logic`)
    - Business rules
    - Data access
    - Repositories

5. **Core Layer** (`ethanpod-core`)
    - Data model
    - Business entities

### Processing Flow

```
User Input → View → Controller → Service → Logic → Database
                ↓                            ↓
              Event ← Controller ← Service ← Logic
                ↓
            View Update
```

---

## Maven Configuration

### Project Properties

```xml

<properties>
    <maven.compiler.source>22</maven.compiler.source>
    <maven.compiler.target>22</maven.compiler.target>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
</properties>
```

### Main Plugins

- **Maven Compiler** : Java 22 compilation
- **Maven Assembly** : Executable JAR creation
- **JaCoCo** : Code coverage
- **SonarQube** : Code quality analysis

---

## Prerequisites

- Java 22 or higher
- Maven for compilation and dependency management

---

## Installation

### Clone the Repository

```bash
git clone https://github.com/SamuelDouay/EthanPod.git
cd EthanPod
```

---

## Compile the Project

````bash
mvn clean package
````

This command generates an executable JAR file with all dependencies included.


---

## Run the Application

````bash
java -jar target/EthanPod-0.0.1-jar-with-dependencies.jar
````

---

### Tests

To run the tests :

```bash
mvn test
```