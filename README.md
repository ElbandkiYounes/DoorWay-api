# DoorWay API

A Spring Boot application for managing candidate interviews and documents.

## Overview

DoorWay API provides a RESTful interface for managing interview candidates, their documents, and application statuses. Built with Spring Boot, it offers robust document handling and decision tracking capabilities.

## Tech Stack

- Java 17
- Spring Boot 3.x
- Spring Data JPA
- PostgreSQL/MySQL
- Maven

## Quick Start

1. Prerequisites:
   - JDK 17+
   - Maven 3.6+
   - PostgreSQL/MySQL (optional)

2. Setup:
```bash
git clone https://github.com/yourusername/DoorWay-api.git
cd DoorWay-api
mvn clean install
```

3. Configure database in `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/doorway
spring.datasource.username=your_username
spring.datasource.password=your_password
```

4. Run:
```bash
mvn spring-boot:run
```

## Key Features

- Candidate profile management
- Document storage (images/resumes)
- Application status tracking
- File validation and processing
- RESTful API design

## Data Structure

```java
Interviewee {
    UUID id;
    String firstName;
    String lastName;
    String imageUrl;
    String resumeUrl;
    Decision decision; (PENDING/ACCEPTED/REJECTED)
}
```

## File Guidelines

- Images: JPG, PNG (max 10MB)
- Resumes: PDF, DOC, DOCX (max 10MB)

## Development

- Build: `mvn clean install`
- Test: `mvn test`
- Run: `mvn spring-boot:run`

## License

MIT License

## Contact

For support or queries, create an issue in the repository.
