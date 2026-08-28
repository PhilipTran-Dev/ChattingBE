# 🚀 TingTing Chat App — Backend Service

[![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.0-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![MongoDB](https://img.shields.io/badge/MongoDB-Atlas-47A248?style=for-the-badge&logo=mongodb&logoColor=white)](https://www.mongodb.com/)
[![Docker](https://img.shields.io/badge/Docker-Enabled-2496ED?style=for-the-badge&logo=docker&logoColor=white)](https://www.docker.com/)
[![AWS EC2](https://img.shields.io/badge/Deployment-AWS_EC2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white)](https://aws.amazon.com/ec2/)

High-performance real-time chat backend built with **Spring Boot 4**, **Spring WebSocket (STOMP + SockJS)**, and **MongoDB Atlas**. Designed for scalable room-based messaging with automated CI/CD deployment to AWS EC2 via Docker.

---

## 📑 Table of Contents
- [Architecture Overview](#-architecture-overview)
- [Tech Stack](#-tech-stack)
- [Project Structure](#-project-structure)
- [API Documentation](#-api-documentation)
  - [REST Endpoints](#rest-endpoints)
  - [WebSocket & STOMP Protocol](#websocket--stomp-protocol)
- [Configuration & Environment](#-configuration--environment)
- [Getting Started](#-getting-started)
  - [Prerequisites](#prerequisites)
  - [Local Development](#local-development)
  - [Docker Setup](#docker-setup)
- [CI/CD & Deployment](#-cicd--deployment)
- [Security Notice](#-security-notice)

---

## 🏗 Architecture Overview

The backend is structured into two main communication channels:
1. **HTTP REST API**: Handles room lifecycle management (creation, fetching room metadata, retrieving paginated message histories).
2. **WebSocket Broker (STOMP)**: Handles bidirectional real-time message broadcasting to subscribed clients per room topic.

```
[ Clients / Frontend ]
       │
       ├── (REST HTTP) ────► [ RoomController / HealthController ] ──► [ RoomRepository ] ──► [ MongoDB ]
       │
       └── (STOMP / WS) ───► [ ChatController (/chat) ] ──────────► [ RoomRepository ] ──► [ MongoDB ]
                                      │
                                      ▼
                             Broadcasting to `/topic/room/{roomId}`
```

---

## 🛠 Tech Stack

- **Framework**: Spring Boot 4.0.0
- **Language**: Java 17 (Eclipse Temurin)
- **Database**: MongoDB Atlas (Spring Data MongoDB)
- **Messaging Protocol**: WebSocket with STOMP broker & SockJS fallback
- **Build Tool**: Apache Maven 3.9+
- **Containerization**: Docker (Multi-stage build)
- **CI/CD**: GitHub Actions + Self-hosted AWS EC2 Runner

---

## 📂 Project Structure

```text
.
├── .github/workflows/
│   └── cicd.yml                 # GitHub Actions pipeline for build & AWS EC2 deployment
├── src/
│   ├── main/
│   │   ├── java/com/ChattingApp/ChattingAppBackend/
│   │   │   ├── Config/          # CORS, MongoDB & WebSocket Configurations
│   │   │   ├── Controller/      # REST & WebSocket Handlers
│   │   │   ├── Entities/        # MongoDB Models (Room, Messages)
│   │   │   ├── Payload/         # DTO Requests (MessageRequest)
│   │   │   ├── Repository/      # Spring Data MongoDB Repositories
│   │   │   └── ChattingAppBackendApplication.java
│   │   └── resources/
│   │       └── application.yml  # Application properties
│   └── test/
├── Dockerfile                   # Multi-stage Docker build file
├── pom.xml                      # Maven dependencies
└── mvnw / mvnw.cmd              # Maven wrapper scripts
```

---

## 📡 API Documentation

### REST Endpoints

Base URL: `http://localhost:8080` (or AWS Host)

| Method | Endpoint | Description | Request Body / Params | Response |
| :--- | :--- | :--- | :--- | :--- |
| `GET` | `/health` | Health check endpoint | None | `"health "` |
| `POST` | `/api/v1/rooms` | Create a new chat room | `text/plain`: `roomId` | Created `Room` JSON (201) / 400 if exists |
| `GET` | `/api/v1/rooms/{roomId}` | Get room details | Path: `roomId` | `Room` JSON (200) / 400 if not found |
| `GET` | `/api/v1/rooms/{roomId}/messages` | Get paginated messages | Query: `page` (default 0), `size` (default 10) | List of `Messages` (200) |

#### Example: Create Room
```http
POST /api/v1/rooms HTTP/1.1
Host: localhost:8080
Content-Type: text/plain

room-developers
```

---

### WebSocket & STOMP Protocol

- **STOMP Endpoint**: `ws://<host>:8080/chat` (Supports SockJS)
- **Application Prefix**: `/app`
- **Topic Broker Prefix**: `/topic`

#### 1. Subscribe to a Room
Clients must subscribe to the target room's topic:
```
DESTINATION: /topic/room/{roomId}
```

#### 2. Send a Message
Publish message payload to:
```
DESTINATION: /app/sendMessage/{roomId}
```
**Payload Format:**
```json
{
  "roomId": "room-developers",
  "sender": "Alice",
  "content": "Hello everyone!",
  "messageTime": "2026-08-25T16:00:00"
}
```

---

## ⚙️ Configuration & Environment

Configuration is managed via Spring configuration classes and `application.yml`.

### MongoDB Configuration
Defined in `MongoConfig.java`:
- Connects to MongoDB Atlas cluster `TingTing`.
- Uses `SimpleMongoClientDatabaseFactory` and `MongoTemplate`.

---

## 🚀 Getting Started

### Prerequisites
- **JDK 17** or higher
- **Maven 3.9+** (or use included `./mvnw`)
- **Docker** (optional, for containerized run)

### Local Development

1. **Clone the repository:**
   ```bash
   git clone https://github.com/your-username/chatting-backend.git
   cd chatting-backend
   ```

2. **Build the application:**
   ```bash
   ./mvnw clean package -DskipTests
   ```

3. **Run the application:**
   ```bash
   ./mvnw spring-boot:run
   ```
   The server will start on port `8080`.

---

### 🐳 Docker Setup

#### Build image locally:
```bash
docker build -t chatting-backend:latest .
```

#### Run container:
```bash
docker run -d -p 8080:8080 --name chatting-backend chatting-backend:latest
```

---

## 🔄 CI/CD & Deployment

Automated CI/CD pipeline configured via **GitHub Actions** (`.github/workflows/cicd.yml`):
1. **Build Job**:
   - Triggers on push to `main` branch.
   - Sets up Java 17 Temurin, compiles project with Maven.
   - Builds and pushes multi-stage Docker image to Docker Hub (`phucngo249/chatting-backend:latest`).
2. **Deploy Job**:
   - Runs on self-hosted AWS EC2 runner (`runs-on: [aws-ec2]`).
   - Pulls the latest Docker image from Docker Hub.
   - Stops the previous container and launches the new instance mapped to port `8080`.
   - Automatically prunes dangling images and frees occupied ports.

---

## ⚠️ Security Notice

> **Important**: Never commit plain text database credentials or connection URIs directly in Java code. Externalize database URI strings to environment variables (`SPRING_DATA_MONGODB_URI` or `application.yml` secrets) when publishing to production.
