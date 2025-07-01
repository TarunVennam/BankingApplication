# BankingApplication
# BankingApplication

A simple Banking Application built with **Spring Boot** that provides core banking operations such as user management, transactions, and account statements.  

## ✨ Features

- User registration and login with JWT authentication
- Role-based access control (Admin/User)
- Account balance management
- Transaction history and statements
- RESTful API endpoints

## 🛠️ Tech Stack

- **Java 17**
- **Spring Boot 3**
- **Spring Security**
- **Spring Data JPA**
- **MySQL**
- **Maven**
- **Swagger/OpenAPI** (if you have it integrated)

## ⚙️ Getting Started

### Prerequisites

- Java 17+
- Maven 3+
- MySQL Server

### Clone the repository

```bash
git clone https://github.com/TarunVennam/BankingApplication.git
cd BankingApplication

###Configure MySQL

spring.datasource.url=jdbc:mysql://localhost:3306/bankapp
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD
spring.jpa.hibernate.ddl-auto=update
