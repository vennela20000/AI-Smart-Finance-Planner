# AI-Smart-Finance-Planner
A full-stack financial management platform that leverages Artificial Intelligence to transform raw transaction data into actionable financial insights.

# Key Features
AI Financial Consultant: Integrated with Spring AI and Groq (Llama 3) to provide natural language analysis of spending habits.

Secure Authentication: Professional-grade security using Spring Security and JWT (JSON Web Tokens).

Data Persistence: Robust relational data management using PostgreSQL and Spring Data JPA.

Containerized Architecture: Fully dockerized setup using Docker Compose for consistent environment deployment.

Automated Budgeting: Smart categorization of expenses with real-time budget tracking.

# Tech Stack
Backend: Java 17, Spring Boot 3.3, Spring AI, Spring Security.

Database: PostgreSQL.

DevOps: Docker, Docker Compose, Maven.

Frontend: HTML5, CSS3, JavaScript (Fetch API).

# SetUp and Installation

Docker & Docker Compose installed.

A Groq API Key (or OpenAI).

# How to Run

Clone the repository:

Bash

git clone https://github.com/YOUR_USERNAME/AI-Smart-Financial-Planner.git

cd AI-Smart-Financial-Planner

Set your API Key:

Set your environment variable for the AI model:

Bash

Windows

setx GROQ_API_KEY "your_key_here"

Launch with Docker:

Bash

docker compose up --build

The application will be available at http://localhost:8080

# Project Structure
src/main/java: Backend logic, AI services, and Security configurations.
src/main/resources: Static frontend assets and application properties.
Dockerfile: Configuration for the Java application container.
docker-compose.yml: Orchestration for the App and Database services.
