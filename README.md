# Battleship Multiplayer (Java + Spring Boot + SQLite)
![Status](https://img.shields.io/badge/status-WIP-yellow)

A backend REST API for playing **Battleship** with **two human players** and a **persistent leaderboard** (SQLite).  
Built as a **learning project** to demonstrate clean architecture, Java development skills, and backend API design.

## ✨ Features

- Create and join games
- Place ships and play turn-by-turn
- Persistent leaderboard (wins/losses, optional ELO system[^1])
- Data stored in SQLite
- REST API endpoints
- Swagger/OpenAPI documentation

## 🧪 Tech Stack

- **Java 17**
- **Spring Boot 3**
- **SQLite** (via JDBC or Spring Data JPA)
- **JUnit 5** for testing

## 🚀 Quick Start

```bash
# 1) Run tests (skip integration tests, run only unit tests)
mvn -q -DskipITs test 

# 2) Start the application
mvn spring-boot:run

# 3) Health check
curl http://localhost:8080/health
```

## 🚢 Live Demo

The latest build is deployed and running on AWS EC2 with Docker.

**Base URL:** `domain-name-to-define` ![WIP](https://img.shields.io/badge/status-WIP-yellow)

You can explore the API using the provided Postman collection in `docs/`, or visit:
- `GET /health` → check server status
- `GET /api/leaderboard` → view current rankings

## 📡 API Endpoints (REST-aligned)

### Game lifecycle
- **POST** `/api/games`  
  Create a new game.  
  **Body:**  
  ```json
  { "playerName": "Alice" }
  ```

- **POST** `/api/games/{gameId}/players`  
  Join an existing game as the second player.  
  **Body:**  
  ```json
  { "playerName": "Bob" }
  ```

### Ship placement
- **POST** `/api/games/{gameId}/ships`  
  Place a ship for the specified player.  
  **Body:**  
  ```json
  {
    "playerName": "Alice",
    "ship": "BATTLESHIP",
    "x": 2,
    "y": 2,
    "orientation": "HORIZONTAL"
  }
  ```

- **GET** `/api/games/placement-example`  
  Returns a sample ship placement payload.

### Gameplay
- **POST** `/api/games/{gameId}/shots`  
  Fire at coordinates.  
  **Body:**  
  ```json
  {
    "playerName": "Bob",
    "x": 1,
    "y": 5
  }
  ```

- **GET** `/api/games/{gameId}/ships/remaining?playerName=Alice`  
  Get the remaining ships for a player in the given game.

> Postman collection in the `docs/` folder. ![WIP](https://img.shields.io/badge/status-WIP-yellow)

---

## ⚙️ Configuration

Default SQLite database configuration:

```properties
# src/main/resources/application.properties
spring.datasource.url=jdbc:sqlite:./data/battleship.db
spring.datasource.driver-class-name=org.sqlite.JDBC
spring.jpa.hibernate.ddl-auto=update
```

## 🧱 Architecture Overview

- `controller/` → REST endpoints  
- `service/` → game rules and orchestration  
- `model/` → Game, Board, Ship, Player, etc.  
- `repository/` → database access  

More details: see [`docs/architecture.md`](docs/architecture.md). ![WIP](https://img.shields.io/badge/status-WIP-yellow)

---

## 📊 ELO Rating System

The leaderboard uses the **ELO rating system** to rank players more fairly:

- Each player starts with a default rating (e.g., 1000 points).
- Winning against a higher-rated player = bigger gain.
- Losing to a lower-rated player = bigger loss.
- Ratings are updated after each game using:

```java
double expectedScoreA = 1 / (1 + Math.pow(10, (ratingB - ratingA) / 400.0));
ratingA = ratingA + K * (scoreA - expectedScoreA);
```

## ✅ Roadmap

- [ ] Two-player game mode  
- [ ] Add Swagger/OpenAPI docs for API exploration  
- [ ] Add screenshots of API usage and example game states  
- [ ] Input validation & improved error handling  
- [ ] Persistent leaderboard (ELO-based)  
- [ ] Dockerized application  
- [ ] Live AWS EC2 deployment with public domain  
- [ ] WebSocket live game updates (bonus)

---

## 🖼️ Screenshots

See [`docs/screenshots/`](docs/screenshots/). ![WIP](https://img.shields.io/badge/status-WIP-yellow)

---

## 📄 License

MIT License



[^1]: Leaderboard uses ELO rating system for fairer ranking, rewarding wins against higher-ranked opponents
