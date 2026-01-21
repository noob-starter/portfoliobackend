# 🚀 Portfolio Backend Application

A robust Spring Boot backend application for managing personal portfolio website data. It provides comprehensive endpoints for showcasing professional experience, projects, education, and skills.

## 📋 Table of Contents

- [Overview](#overview)
- [Testing](#-testing)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Architecture](#architecture)
- [Prerequisites](#prerequisites)
- [Quick Start](#quick-start)
- [Running the Project](#running-the-project)
- [API Documentation](#api-documentation)
- [Project Structure](#project-structure)
- [Additional Resources](#additional-resources)

## 🎯 Overview

This backend application serves as the API layer for a personal portfolio website, offering secure CRUD operations for portfolio content management. It features both public endpoints for portfolio visitors and protected admin endpoints for content management.

## 🧪 Testing

**Production-ready test suite with 100% service layer coverage**

- ✅ **Test Files** covering all services
- ✅ **Test Methods** with comprehensive scenarios
- ✅ **JUnit 5 + Mockito** for unit testing
- ✅ **Docker support** for consistent test environment
- ✅ **CI/CD ready** with GitHub Actions workflow

### Quick Test Commands

```bash
# Run all tests (Docker - recommended)
./test-run.sh docker

# Run all tests locally
./mvnw test

# Run specific service tests
./mvnw test -Dtest="ProfileServiceTest"

# Compile tests only
./mvnw test-compile
```

📖 **Full Documentation**: See [README_TESTS.md](README_TESTS.md) for detailed testing guide.

**Key Highlights:**
- RESTful API design with comprehensive CRUD operations
- Secure admin authentication with Spring Security
- Rate limiting and API protection with Resilience4j
- Production-grade logging with ELK Stack (Elasticsearch, Logstash, Kibana)
- Interactive API documentation with Swagger/OpenAPI
- Docker support for development and production environments
- PostgreSQL database with optimized schemas

## ✨ Features

### Core Functionality
- **Profile Management**: Personal information, bio, and professional summary
- **Experience Tracking**: Work experience with detailed descriptions and achievement points
- **Project Showcase**: Projects with technology stacks, descriptions, and key highlights
- **Education History**: Academic background and qualifications
- **Skills & Technologies**: Categorized technical skills and proficiencies
- **Achievements**: Professional accomplishments and certifications
- **Contact Management**: Contact information and inquiry submissions
- **FAQ System**: Frequently asked questions and answers

## 🛠 Tech Stack

### Core Framework
- **Java 21** - Latest LTS version
- **Spring Boot 3.5.6** - Application framework
- **Maven** - Dependency management and build tool

### Database
- **PostgreSQL** - Primary relational database

### DevOps
- **Docker & Docker Compose** - Containerization
- **Lombok** - Boilerplate code reduction

## 🏗 Architecture

```
portfolio-backend/
├── controllers/       # REST endpoints (Admin & Public)
├── services/         # Business logic layer
├── repositories/     # Data access layer
├── models/
│   ├── entities/     # JPA entities
│   ├── requests/     # Request DTOs
│   ├── responses/    # Response DTOs
│   └── updates/      # Update DTOs
├── configurations/   # Security, Swagger, etc.
├── exceptions/       # Custom exceptions & handlers
└── resources/        # Application configs & templates
```

## 📋 Prerequisites

Before running this project, ensure you have the following installed:

- **Java 21** or higher ([Download](https://adoptium.net/))
- **Maven 3.6+** ([Download](https://maven.apache.org/download.cgi))
- **PostgreSQL 14+** ([Download](https://www.postgresql.org/download/))
- **Docker & Docker Compose** ([Download](https://www.docker.com/get-started))
- **Git** ([Download](https://git-scm.com/downloads))

### Verify Installations

```bash
# Check Java version
java -version

# Check Maven version
mvn -version

# Check PostgreSQL status
psql --version

# Check Docker
docker --version
docker-compose --version
```

## ⚡ Quick Start

### 1. Clone the Repository

```bash
git clone <your-repository-url>
cd backend/portfolio
```

### 2. Set Up PostgreSQL Database

```bash
# Start PostgreSQL (macOS with Homebrew)
brew services start postgresql

# Create database
psql -U postgres -c "CREATE DATABASE portfolio;"

# Run database setup scripts (optional - for initial data)
psql -U postgres -d portfolio -f documents/scripts/manages/create/create.sql
psql -U postgres -d portfolio -f documents/scripts/manages/create/create_admin.sql
psql -U postgres -d portfolio -f documents/scripts/manages/insert/insert.sql
psql -U postgres -d portfolio -f documents/scripts/manages/insert/insert_admin.sql

```

### 3. Configure Application

Default database connection:
- **Host**: localhost:5432
- **Database**: portfolio
- **Username**: postgres
- **Password**: (your local postgres password)

### 4. Start ELK Stack (for logging)

```bash
# Start Elasticsearch, Logstash, and Kibana
docker-compose up -d

# Verify containers are running
docker-compose ps
```

### 5. Build and Run

```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

The application will start at: **http://localhost:8080**

## 🚀 Running the Project


### Running with Different Profiles

```bash
# Local Development
mvn spring-boot:run -Dspring-boot.run.profiles=local

# Production
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

## 👤 Contributers

**Pratik Yawalkar**
- Portfolio: [Your Portfolio URL (Frontend)]
- GitHub: [@your-github](https://github.com/your-github)
- LinkedIn: [Your LinkedIn](https://linkedin.com/in/your-profile)

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- PostgreSQL community
- Elastic team for ELK stack
- All contributors and supporters

---

**Made with ❤️ for showcasing professional portfolio**

For questions or support, please open an issue in the repository.

