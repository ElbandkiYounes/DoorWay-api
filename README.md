# DoorWay API

A comprehensive Spring Boot application for managing candidate interviews, documents, and hiring processes.

## 📋 Overview

DoorWay API is a robust RESTful service designed to streamline the interview process for organizations. It provides a complete solution for managing candidates, interviewers, interview scheduling, document handling, and decision tracking. The system supports both technical and principle-based assessments with a sophisticated rating system.

## ✨ Key Features

- **Role-based Access Control**: Secure endpoints with admin-only access for sensitive operations
- **JWT Authentication**: Secure token-based authentication with role information
- **Candidate Management**: Store and manage candidate profiles with documents
- **Interviewer Management**: Admin-controlled interviewer accounts
- **Interview Scheduling**: Schedule and track interviews
- **Document Handling**: Store and process candidate images and resumes
- **Assessment Framework**: 
  - Technical questions with language-specific answers
  - Principle-based questions aligned with excellence principles
  - Standardized rating system (High/Medium/Low)
- **Decision Tracking**: Track decisions at both interview and process levels
- **School Affiliations**: Track candidates' educational backgrounds

## 🛠️ Technology Stack

- **Backend**: Java 17, Spring Boot 3.x
- **Security**: Spring Security, JWT
- **Database**: PostgreSQL with JPA/Hibernate
- **Build Tool**: Maven
- **API Documentation**: Spring Doc (OpenAPI)
- **Testing**: JUnit, Mockito

## 🚀 Quick Start

### Prerequisites
- JDK 17 or higher
- Maven 3.6+
- PostgreSQL database

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/DoorWay-api.git
   cd DoorWay-api
   ```

2. **Configure the database**

   Edit `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/doorway
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

3. **Build the application**
   ```bash
   mvn clean install
   ```

4. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

   The API will be available at `http://localhost:8080`

## 🔐 Authentication

The API uses JWT (JSON Web Token) for authentication:

1. **Login Endpoint**: `POST /auth/login`
   ```json
   {
     "email": "user@example.com",
     "password": "password"
   }
   ```

2. **Response**:
   ```json
   {
     "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
     "expiresIn": 3600000,
     "role": "ADMIN"
   }
   ```

3. **Using the Token**:
   Include the token in the Authorization header for subsequent requests:
   ```
   Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
   ```

4. **Default Admin Account**:
   - Email: admin@doorway.com
   - Password: password

## 📝 API Endpoints

### Authentication
- `POST /auth/login` - Authenticate user
- `GET /api/me` - Get current user details

### Interviewers (Admin Only)
- `GET /api/interviewers` - List all interviewers
- `GET /api/interviewers/{id}` - Get interviewer by ID
- `POST /api/interviewers` - Create new interviewer
- `PUT /api/interviewers/{id}` - Update interviewer
- `DELETE /api/interviewers/{id}` - Delete interviewer

### Interviewees
- `GET /api/interviewees` - List all candidates
- `GET /api/interviewees/{id}` - Get candidate by ID
- `POST /api/interviewees` - Create new candidate
- `PUT /api/interviewees/{id}` - Update candidate
- `DELETE /api/interviewees/{id}` - Delete candidate

### Interviews
- `GET /api/interviews` - List all interviews
- `GET /api/interviews/{id}` - Get interview by ID
- `POST /api/interviews` - Schedule new interview
- `PUT /api/interviews/{id}` - Update interview
- `DELETE /api/interviews/{id}` - Delete interview

### Schools
- `GET /api/schools` - List all schools (public)
- `POST /api/schools` - Add new school
- `PUT /api/schools/{id}` - Update school
- `DELETE /api/schools/{id}` - Delete school

### Questions & Answers
- Technical and Principle question endpoints for creating, retrieving, and managing assessment content

## 📊 Data Models

### Core Entities
- **Interviewer**: Admin users who conduct interviews
- **Interviewee**: Candidates being interviewed
- **Interview**: Individual interview sessions
- **InterviewingProcess**: Overall hiring process for a candidate
- **School**: Educational institutions

### Assessment
- **TechnicalQuestion/Answer**: Technical assessment with language-specific answers
- **PrincipleQuestion/Answer**: Assessment based on excellence principles
- **Decision**: Evaluation outcome (HIGHLY_INCLINED, INCLINED, NEUTRAL, DECLINED, HIGHLY_DECLINED)
- **Bar**: Rating level (HIGH, MEDIUM, LOW)
- **ExcellencePrinciple**: Core values (IMPACTFUL_DELIVERY, WISE_INSIGHTS, etc.)
- **Language**: Programming languages supported for technical answers

## 🧪 Testing

Run the test suite with:
```bash
mvn test
```

## 🔄 Development Workflow

1. **Build the project**
   ```bash
   mvn clean install
   ```

2. **Run with development profile**
   ```bash
   mvn spring-boot:run -Dspring.profiles.active=dev
   ```

3. **Package for production**
   ```bash
   mvn package -Dspring.profiles.active=prod
   ```

## 🐳 Docker Support

Build and run with Docker:

```bash
docker build -t doorway-api .
docker run -p 8080:8080 doorway-api
```

Or use Docker Compose:

```bash
docker-compose up
```

## 📄 License

MIT License

## 📞 Contact

For support or queries, please create an issue in the repository or contact the development team at support@doorway.com.
