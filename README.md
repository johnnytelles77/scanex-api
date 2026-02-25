# Scanex API

**Blockchain Address Analyzer built with Java + Spring Boot**

Scanex is a backend REST API that integrates with the Etherscan public API to fetch Ethereum wallet transactions, normalize the data, and persist them into a PostgreSQL database for further analysis.

This project demonstrates clean backend architecture, external API integration, data normalization, and transactional persistence using Spring Boot.

---

## 🚀 Features Implemented

- Fetch Ethereum transactions from Etherscan
- Normalize JSON responses into DTOs
- Deduplicate transactions by hash
- Persist data into PostgreSQL
- Clean layered architecture:
  - Controller
  - Service
  - Integration
  - Repository
  - DTO
- Custom error handling for external provider failures

---

## 🏗 Architecture Overview
Controller → Service → Integration → Repository → Database

### Flow

1. Client requests wallet transactions
2. Service calls Etherscan API
3. JSON response is parsed into DTOs
4. Transactions are validated and deduplicated
5. New transactions are stored in PostgreSQL
6. API returns normalized JSON response

---

## 🛠 Tech Stack

- Java 21
- Spring Boot 3
- PostgreSQL
- Maven
- Jackson (JSON mapping)
- Etherscan API

---

## 📌 Example Endpoint

GET /api/transactions/{chain}/{address}?limit=10

Example response:

```json
[
  {
    "id": "uuid",
    "hash": "0x...",
    "from": "0x...",
    "to": "0x...",
    "value": 0,
    "timestamp": "2026-02-13T05:12:23Z"
  }
]
```

### ⚙️ Running Locally

1️⃣ Start PostgreSQL (Docker recommended)
- docker compose up -d

2️⃣ Configure Environment Variables

Create a .env file (based on .env.example):

INTEGRATIONS_ETHERSCAN_BASE_URL=https://api.etherscan.io
INTEGRATIONS_ETHERSCAN_API_KEY=YOUR_API_KEY
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/scanex
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=postgres

3️⃣ Run the Application
./mvnw spring-boot:run

### 🧠 What This Project Demonstrates
	•	REST API development
	•	External API integration
	•	Data transformation & normalization
	•	Transactional persistence
	•	Clean separation of concerns
	•	Backend architecture best practices

⸻

### 📍 Roadmap
	•	Risk scoring engine
	•	Watchlist system
	•	Automated sync jobs
	•	CI/CD pipeline
	•	SaaS multi-tenant architecture

⸻

### 👨‍💻 Author

Johnny Telles
Backend Developer | Java | Spring Boot
