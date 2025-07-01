# BankingApplication

A simple Banking Application built with **Spring Boot** that provides core banking operations such as user management, transactions, and account statements.

## ✨ Features

- User registration and login with JWT authentication
- Role-based access control (Admin/User)
- Account balance management
- Transaction history and statements
- RESTful API endpoints
- Interactive API documentation with Swagger UI

## 🛠️ Tech Stack

- **Java 17**
- **Spring Boot 3**
- **Spring Security**
- **Spring Data JPA**
- **MySQL**
- **Maven**
- **Swagger/OpenAPI**

## ⚙️ Getting Started

### Prerequisites

- Java 17+
- Maven 3+
- MySQL Server

### Clone the repository

```bash
git clone https://github.com/TarunVennam/BankingApplication.git
cd BankingApplication
```

### Configure MySQL

Create a database called `bankapp` and update your `application.properties` or `application.yml`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bankapp
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD
spring.jpa.hibernate.ddl-auto=update
```

### Build and run

```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8080`.

## 🔐 API Endpoints

- `POST /api/auth/register` — Register a new user
- `POST /api/auth/login` — Login and receive JWT token
- `/api/user/...` — User operations (view balance, make transactions, etc.)
- `/api/admin/...` — Admin operations
- **Swagger UI:** [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)


## 👨‍💻 Author

- **Tarun Vennam** — [GitHub](https://github.com/TarunVennam)

---

Feel free to fork, star ⭐, and contribute!


