# URL Shortener

A modern, high-performance URL shortening service built with **Spring Boot** and **Java 17**. Transform long URLs into short, shareable links with advanced features like Redis caching and PostgreSQL persistence.

- **Fast URL Shortening**: Convert long URLs into compact, shareable links
- **Base62 Encoding**: Efficient encoding algorithm for optimal short URL generation
- **Redis Caching**: Lightning-fast redirections with intelligent caching layer
- **PostgreSQL Storage**: Reliable and scalable data persistence
- **RESTful API**: Clean and intuitive API endpoints
- **Comprehensive Testing**: Full test coverage with integration tests
- **Production Ready**: Built with Spring Boot best practices

## Quick Start

Using Docker Compose (Recommended)

1. **Clone the repository**

   `git clone https://github.com/danbg12/url_shortener.git
   cd url_shortener`

2. **Start the services**

   `docker-compose up -d`

## Architecture
<img width="2396" height="1002" alt="image" src="https://github.com/user-attachments/assets/cd2c4cf8-e0b1-4d2c-bef8-a0fad2f01de3" />


### Key Components

- **Base62Encoder**: Converts numeric IDs to Base62 encoded strings
- **URL Controller**: REST endpoints for URL operations
- **URL Service**: Business logic and caching layer
- **URL Repository**: Data access layer with PostgreSQL
- **Redis Cache**: High-performance caching for frequent redirections
<img width="2388" height="1038" alt="image" src="https://github.com/user-attachments/assets/d85d294b-a7c5-4438-8a83-468ddcf01c7f" />

## Tech Stack

- **Backend**: Spring Boot 3.x, Java 17
- **Database**: PostgreSQL
- **Cache**: Redis
- **Build Tool**: Gradle
- **Testing**: JUnit 5, Mockito, Testcontainers
- **Documentation**: OpenAPI/Swagger
