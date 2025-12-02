# Multi-Tenant Banking API

A Spring Boot REST API for managing customer bank accounts and transactions in a multi-tenant banking system with asynchronous statement generation.

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![H2 Database](https://img.shields.io/badge/H2-Database-blue.svg)](https://www.h2database.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

---

## 📋 Table of Contents

- [Features](#-features)
- [Technology Stack](#-technology-stack)
- [Project Structure](#-project-structure)
- [Getting Started](#-getting-started)
- [Configuration](#-configuration)
- [Database Schema](#-database-schema)
- [API Documentation](#-api-documentation)
- [Testing Scenarios](#-testing-scenarios)
- [Multi-Tenancy Architecture](#-multi-tenancy-architecture)
- [Asynchronous Processing](#-asynchronous-processing)
- [Spring Modules Used](#-spring-modules-used)
- [Contributing](#-contributing)
- [License](#-license)

---

## 🚀 Features

- ✅ **Multi-Tenant Support** - Complete data isolation between tenants
- ✅ **Account Management** - Full CRUD operations for bank accounts
- ✅ **Transaction Processing** - Deposits and withdrawals with balance updates
- ✅ **Asynchronous Statement Generation** - Background job processing
- ✅ **Spring Boot Profiles** - Environment-specific configurations (dev/prod)
- ✅ **H2 Database** - In-memory database for development
- ✅ **Input Validation** - Request validation using Bean Validation
- ✅ **Exception Handling** - Centralized error handling
- ✅ **RESTful API Design** - Standard HTTP methods and status codes

---

## 🛠 Technology Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 17 | Programming Language |
| Spring Boot | 3.2.0 | Application Framework |
| Spring Data JPA | 3.2.0 | Data Access Layer |
| H2 Database | 2.2.224 | In-Memory Database |
| Maven | 3.8+ | Build Tool |
| Jakarta Validation | 3.0 | Input Validation |
| Jakarta Persistence | 3.1 | ORM Specification |

---

## 📁 Project Structure

```
banking-api/
├── src/
│   ├── main/
│   │   ├── java/com/banking/
│   │   │   ├── BankingApplication.java          # Main application class
│   │   │   │
│   │   │   ├── config/                          # Configuration classes
│   │   │   │   ├── AsyncConfig.java             # Async task executor config
│   │   │   │   ├── TenantContext.java           # ThreadLocal tenant storage
│   │   │   │   └── TenantFilter.java            # HTTP request filter
│   │   │   │
│   │   │   ├── controller/                      # REST Controllers
│   │   │   │   ├── AccountController.java       # Account endpoints
│   │   │   │   ├── TransactionController.java   # Transaction endpoints
│   │   │   │   └── StatementController.java     # Statement endpoints
│   │   │   │
│   │   │   ├── dto/                             # Data Transfer Objects
│   │   │   │   ├── AccountRequest.java          # Account creation/update DTO
│   │   │   │   ├── TransactionRequest.java      # Transaction creation DTO
│   │   │   │   └── StatementRequest.java        # Statement request DTO
│   │   │   │
│   │   │   ├── entity/                          # JPA Entities
│   │   │   │   ├── Account.java                 # Account entity
│   │   │   │   ├── Transaction.java             # Transaction entity
│   │   │   │   ├── TransactionType.java         # Transaction type enum
│   │   │   │   ├── StatementJob.java            # Statement job entity
│   │   │   │   └── JobStatus.java               # Job status enum
│   │   │   │
│   │   │   ├── exception/                       # Exception handling
│   │   │   │   └── GlobalExceptionHandler.java  # Centralized error handler
│   │   │   │
│   │   │   ├── repository/                      # Data Access Layer
│   │   │   │   ├── AccountRepository.java       # Account repository
│   │   │   │   ├── TransactionRepository.java   # Transaction repository
│   │   │   │   └── StatementJobRepository.java  # Statement job repository
│   │   │   │
│   │   │   └── service/                         # Business Logic Layer
│   │   │       ├── AccountService.java          # Account business logic
│   │   │       ├── TransactionService.java      # Transaction business logic
│   │   │       └── StatementService.java        # Statement business logic
│   │   │
│   │   └── resources/
│   │       ├── application.properties           # Common configuration
│   │       ├── application-dev.properties       # Development configuration
│   │       └── application-prod.properties      # Production configuration
│   │
│   └── test/                                    # Test classes (future)
│
├── pom.xml                                      # Maven dependencies
└── README.md                                    # This file
```

---

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.8 or higher
- Git (for cloning the repository)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/banking-api.git
   cd banking-api
   ```

2. **Build the project**
   ```bash
   mvn clean install
   ```

3. **Run the application (Development mode)**
   ```bash
   mvn spring-boot:run -Dspring-boot.run.profiles=dev
   ```

4. **Run the application (Production mode)**
   ```bash
   mvn spring-boot:run -Dspring-boot.run.profiles=prod
   ```

The application will start on `http://localhost:8080`

### Access H2 Console (Development Mode Only)

- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:bankingdb_dev`
- Username: `sa`
- Password: (leave blank)

---

## ⚙️ Configuration

### Application Properties

#### `application.properties` (Common Settings)
```properties
spring.application.name=banking-api
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

#### `application-dev.properties` (Development)
```properties
# H2 In-Memory Database
spring.datasource.url=jdbc:h2:mem:bankingdb_dev
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# Logging
logging.level.com.banking=DEBUG
```

#### `application-prod.properties` (Production)
```properties
# H2 In-Memory Database (Replace with PostgreSQL/MySQL for production)
spring.datasource.url=jdbc:h2:mem:bankingdb_prod
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# H2 Console
spring.h2.console.enabled=false

# Logging
logging.level.com.banking=INFO
```

### Async Configuration

Thread pool settings for asynchronous statement generation:

```java
Core Pool Size: 2
Max Pool Size: 5
Queue Capacity: 100
Thread Name Prefix: async-statement-
```

---

## 🗄️ Database Schema

### Tables

#### **ACCOUNTS**
| Column | Type | Constraints |
|--------|------|-------------|
| account_id | VARCHAR(255) | PRIMARY KEY |
| tenant_id | VARCHAR(255) | NOT NULL |
| customer_name | VARCHAR(255) | NOT NULL |
| balance | DECIMAL(19,2) | NOT NULL |
| created_at | TIMESTAMP | NOT NULL |

#### **TRANSACTIONS**
| Column | Type | Constraints |
|--------|------|-------------|
| transaction_id | VARCHAR(255) | PRIMARY KEY |
| account_id | VARCHAR(255) | NOT NULL |
| tenant_id | VARCHAR(255) | NOT NULL |
| type | VARCHAR(50) | NOT NULL (DEPOSIT/WITHDRAWAL) |
| amount | DECIMAL(19,2) | NOT NULL |
| timestamp | TIMESTAMP | NOT NULL |

#### **STATEMENT_JOBS**
| Column | Type | Constraints |
|--------|------|-------------|
| job_id | VARCHAR(255) | PRIMARY KEY |
| account_id | VARCHAR(255) | NOT NULL |
| tenant_id | VARCHAR(255) | NOT NULL |
| start_date | TIMESTAMP | NOT NULL |
| end_date | TIMESTAMP | NOT NULL |
| status | VARCHAR(50) | NOT NULL (PENDING/PROCESSING/COMPLETED/FAILED) |
| result | TEXT | NULLABLE |
| created_at | TIMESTAMP | NOT NULL |
| completed_at | TIMESTAMP | NULLABLE |

### SQL Queries (Auto-Generated by Hibernate)

```sql
-- Create Account Table
CREATE TABLE accounts (
    account_id VARCHAR(255) PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    customer_name VARCHAR(255) NOT NULL,
    balance DECIMAL(19,2) NOT NULL,
    created_at TIMESTAMP NOT NULL
);

-- Create Transaction Table
CREATE TABLE transactions (
    transaction_id VARCHAR(255) PRIMARY KEY,
    account_id VARCHAR(255) NOT NULL,
    tenant_id VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    amount DECIMAL(19,2) NOT NULL,
    timestamp TIMESTAMP NOT NULL
);

-- Create Statement Jobs Table
CREATE TABLE statement_jobs (
    job_id VARCHAR(255) PRIMARY KEY,
    account_id VARCHAR(255) NOT NULL,
    tenant_id VARCHAR(255) NOT NULL,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    status VARCHAR(50) NOT NULL,
    result TEXT,
    created_at TIMESTAMP NOT NULL,
    completed_at TIMESTAMP
);
```

---

## 📚 API Documentation

### Base URL
```
http://localhost:8080/api
```

### Required Header
All endpoints require the following header:
```
X-Tenant-Id: BANK001
```

---

### 1️⃣ Account Management

#### Create Account
**Endpoint:** `POST /api/accounts`

**Request:**
```bash
curl -X POST http://localhost:8080/api/accounts \
  -H "Content-Type: application/json" \
  -H "X-Tenant-Id: BANK001" \
  -d '{
    "customerName": "Alice Smith",
    "balance": 1000.00
  }'
```

**Response:** `201 Created`
```json
{
  "accountId": "550e8400-e29b-41d4-a716-446655440000",
  "tenantId": "BANK001",
  "customerName": "Alice Smith",
  "balance": 1000.00,
  "createdAt": "2025-12-01T10:30:00"
}
```

---

#### Get All Accounts
**Endpoint:** `GET /api/accounts`

**Request:**
```bash
curl -X GET http://localhost:8080/api/accounts \
  -H "X-Tenant-Id: BANK001"
```

**Response:** `200 OK`
```json
[
  {
    "accountId": "550e8400-e29b-41d4-a716-446655440000",
    "tenantId": "BANK001",
    "customerName": "Alice Smith",
    "balance": 1000.00,
    "createdAt": "2025-12-01T10:30:00"
  }
]
```

---

#### Get Account by ID
**Endpoint:** `GET /api/accounts/{accountId}`

**Request:**
```bash
curl -X GET http://localhost:8080/api/accounts/550e8400-e29b-41d4-a716-446655440000 \
  -H "X-Tenant-Id: BANK001"
```

**Response:** `200 OK`
```json
{
  "accountId": "550e8400-e29b-41d4-a716-446655440000",
  "tenantId": "BANK001",
  "customerName": "Alice Smith",
  "balance": 1000.00,
  "createdAt": "2025-12-01T10:30:00"
}
```

---

#### Update Account
**Endpoint:** `PUT /api/accounts/{accountId}`

**Request:**
```bash
curl -X PUT http://localhost:8080/api/accounts/550e8400-e29b-41d4-a716-446655440000 \
  -H "Content-Type: application/json" \
  -H "X-Tenant-Id: BANK001" \
  -d '{
    "customerName": "Alice Johnson",
    "balance": 1500.00
  }'
```

**Response:** `200 OK`
```json
{
  "accountId": "550e8400-e29b-41d4-a716-446655440000",
  "tenantId": "BANK001",
  "customerName": "Alice Johnson",
  "balance": 1500.00,
  "createdAt": "2025-12-01T10:30:00"
}
```

---

#### Delete Account
**Endpoint:** `DELETE /api/accounts/{accountId}`

**Request:**
```bash
curl -X DELETE http://localhost:8080/api/accounts/550e8400-e29b-41d4-a716-446655440000 \
  -H "X-Tenant-Id: BANK001"
```

**Response:** `204 No Content`

---

### 2️⃣ Transaction Management

#### Create Transaction (Deposit)
**Endpoint:** `POST /api/accounts/{accountId}/transactions`

**Request:**
```bash
curl -X POST http://localhost:8080/api/accounts/550e8400-e29b-41d4-a716-446655440000/transactions \
  -H "Content-Type: application/json" \
  -H "X-Tenant-Id: BANK001" \
  -d '{
    "type": "DEPOSIT",
    "amount": 500.00
  }'
```

**Response:** `201 Created`
```json
{
  "transactionId": "660e8400-e29b-41d4-a716-446655440001",
  "accountId": "550e8400-e29b-41d4-a716-446655440000",
  "tenantId": "BANK001",
  "type": "DEPOSIT",
  "amount": 500.00,
  "timestamp": "2025-12-01T10:35:00"
}
```

---

#### Create Transaction (Withdrawal)
**Endpoint:** `POST /api/accounts/{accountId}/transactions`

**Request:**
```bash
curl -X POST http://localhost:8080/api/accounts/550e8400-e29b-41d4-a716-446655440000/transactions \
  -H "Content-Type: application/json" \
  -H "X-Tenant-Id: BANK001" \
  -d '{
    "type": "WITHDRAWAL",
    "amount": 200.00
  }'
```

**Response:** `201 Created`
```json
{
  "transactionId": "770e8400-e29b-41d4-a716-446655440002",
  "accountId": "550e8400-e29b-41d4-a716-446655440000",
  "tenantId": "BANK001",
  "type": "WITHDRAWAL",
  "amount": 200.00,
  "timestamp": "2025-12-01T10:40:00"
}
```

---

#### Get All Transactions for Account
**Endpoint:** `GET /api/accounts/{accountId}/transactions`

**Request:**
```bash
curl -X GET http://localhost:8080/api/accounts/550e8400-e29b-41d4-a716-446655440000/transactions \
  -H "X-Tenant-Id: BANK001"
```

**Response:** `200 OK`
```json
[
  {
    "transactionId": "660e8400-e29b-41d4-a716-446655440001",
    "accountId": "550e8400-e29b-41d4-a716-446655440000",
    "tenantId": "BANK001",
    "type": "DEPOSIT",
    "amount": 500.00,
    "timestamp": "2025-12-01T10:35:00"
  },
  {
    "transactionId": "770e8400-e29b-41d4-a716-446655440002",
    "accountId": "550e8400-e29b-41d4-a716-446655440000",
    "tenantId": "BANK001",
    "type": "WITHDRAWAL",
    "amount": 200.00,
    "timestamp": "2025-12-01T10:40:00"
  }
]
```

---

### 3️⃣ Statement Generation (Asynchronous)

#### Request Statement Generation
**Endpoint:** `POST /api/statements`

**Request:**
```bash
curl -X POST http://localhost:8080/api/statements \
  -H "Content-Type: application/json" \
  -H "X-Tenant-Id: BANK001" \
  -d '{
    "accountId": "550e8400-e29b-41d4-a716-446655440000",
    "startDate": "2025-12-01",
    "endDate": "2025-12-31"
  }'
```

**Response:** `202 Accepted`
```json
{
  "jobId": "880e8400-e29b-41d4-a716-446655440003",
  "status": "PENDING",
  "message": "Statement generation started. Poll /api/statements/880e8400... for results"
}
```

---

#### Check Statement Status
**Endpoint:** `GET /api/statements/{jobId}`

**Request:**
```bash
curl -X GET http://localhost:8080/api/statements/880e8400-e29b-41d4-a716-446655440003 \
  -H "X-Tenant-Id: BANK001"
```

**Response (Processing):** `200 OK`
```json
{
  "jobId": "880e8400-e29b-41d4-a716-446655440003",
  "status": "PROCESSING",
  "accountId": "550e8400-e29b-41d4-a716-446655440000",
  "createdAt": "2025-12-01T10:45:00",
  "completedAt": null
}
```

**Response (Completed):** `200 OK`
```json
{
  "jobId": "880e8400-e29b-41d4-a716-446655440003",
  "status": "COMPLETED",
  "accountId": "550e8400-e29b-41d4-a716-446655440000",
  "createdAt": "2025-12-01T10:45:00",
  "completedAt": "2025-12-01T10:45:03",
  "result": "=== ACCOUNT STATEMENT ===\nAccount ID: 550e8400...\nCustomer: Alice Smith\nPeriod: 2025-12-01 to 2025-12-31\n\nOpening Balance: $1000.00\n\nTRANSACTIONS:\n2025-12-01T10:35:00 | DEPOSIT | $500.00\n2025-12-01T10:40:00 | WITHDRAWAL | $200.00\n\nClosing Balance: $1300.00\n========================"
}
```

---

## 🧪 Testing Scenarios

### Scenario 1: Complete Account Lifecycle

```bash
# 1. Create an account
curl -X POST http://localhost:8080/api/accounts \
  -H "Content-Type: application/json" \
  -H "X-Tenant-Id: BANK001" \
  -d '{"customerName":"John Doe","balance":5000.00}'

# Save the accountId from response

# 2. Get account details
curl -X GET http://localhost:8080/api/accounts/{accountId} \
  -H "X-Tenant-Id: BANK001"

# 3. Update account
curl -X PUT http://localhost:8080/api/accounts/{accountId} \
  -H "Content-Type: application/json" \
  -H "X-Tenant-Id: BANK001" \
  -d '{"customerName":"John Smith","balance":5000.00}'

# 4. Delete account
curl -X DELETE http://localhost:8080/api/accounts/{accountId} \
  -H "X-Tenant-Id: BANK001"
```

---

### Scenario 2: Transaction Flow with Balance Updates

```bash
# 1. Create account with $1000
curl -X POST http://localhost:8080/api/accounts \
  -H "Content-Type: application/json" \
  -H "X-Tenant-Id: BANK001" \
  -d '{"customerName":"Jane Doe","balance":1000.00}'

# 2. Deposit $500 (balance becomes $1500)
curl -X POST http://localhost:8080/api/accounts/{accountId}/transactions \
  -H "Content-Type: application/json" \
  -H "X-Tenant-Id: BANK001" \
  -d '{"type":"DEPOSIT","amount":500.00}'

# 3. Withdraw $300 (balance becomes $1200)
curl -X POST http://localhost:8080/api/accounts/{accountId}/transactions \
  -H "Content-Type: application/json" \
  -H "X-Tenant-Id: BANK001" \
  -d '{"type":"WITHDRAWAL","amount":300.00}'

# 4. View all transactions
curl -X GET http://localhost:8080/api/accounts/{accountId}/transactions \
  -H "X-Tenant-Id: BANK001"

# 5. Verify balance in account
curl -X GET http://localhost:8080/api/accounts/{accountId} \
  -H "X-Tenant-Id: BANK001"
```

---

### Scenario 3: Multi-Tenant Isolation Test

```bash
# 1. Create account for BANK001
curl -X POST http://localhost:8080/api/accounts \
  -H "Content-Type: application/json" \
  -H "X-Tenant-Id: BANK001" \
  -d '{"customerName":"Alice","balance":1000.00}'
# Save accountId as ACCOUNT_BANK001

# 2. Create account for BANK002
curl -X POST http://localhost:8080/api/accounts \
  -H "Content-Type: application/json" \
  -H "X-Tenant-Id: BANK002" \
  -d '{"customerName":"Bob","balance":2000.00}'
# Save accountId as ACCOUNT_BANK002

# 3. BANK001 lists accounts (should only see Alice)
curl -X GET http://localhost:8080/api/accounts \
  -H "X-Tenant-Id: BANK001"

# 4. BANK002 lists accounts (should only see Bob)
curl -X GET http://localhost:8080/api/accounts \
  -H "X-Tenant-Id: BANK002"

# 5. BANK001 tries to access BANK002's account (should fail)
curl -X GET http://localhost:8080/api/accounts/{ACCOUNT_BANK002} \
  -H "X-Tenant-Id: BANK001"
# Expected: 404 Not Found
```

---

### Scenario 4: Asynchronous Statement Generation

```bash
# 1. Create account
curl -X POST http://localhost:8080/api/accounts \
  -H "Content-Type: application/json" \
  -H "X-Tenant-Id: BANK001" \
  -d '{"customerName":"Test User","balance":1000.00}'

# 2. Create multiple transactions
curl -X POST http://localhost:8080/api/accounts/{accountId}/transactions \
  -H "Content-Type: application/json" \
  -H "X-Tenant-Id: BANK001" \
  -d '{"type":"DEPOSIT","amount":500.00}'

curl -X POST http://localhost:8080/api/accounts/{accountId}/transactions \
  -H "Content-Type: application/json" \
  -H "X-Tenant-Id: BANK001" \
  -d '{"type":"WITHDRAWAL","amount":200.00}'

curl -X POST http://localhost:8080/api/accounts/{accountId}/transactions \
  -H "Content-Type: application/json" \
  -H "X-Tenant-Id: BANK001" \
  -d '{"type":"DEPOSIT","amount":300.00}'

# 3. Request statement
curl -X POST http://localhost:8080/api/statements \
  -H "Content-Type: application/json" \
  -H "X-Tenant-Id: BANK001" \
  -d '{
    "accountId": "{accountId}",
    "startDate": "2025-12-01",
    "endDate": "2025-12-31"
  }'
# Save jobId from response

# 4. Poll for statement (wait 3-4 seconds)
sleep 4
curl -X GET http://localhost:8080/api/statements/{jobId} \
  -H "X-Tenant-Id: BANK001"
```

---

### Scenario 5: Error Handling Tests

```bash
# 1. Missing tenant header (should return 400)
curl -X GET http://localhost:8080/api/accounts

# 2. Invalid account ID (should return 404)
curl -X GET http://localhost:8080/api/accounts/invalid-id \
  -H "X-Tenant-Id: BANK001"

# 3. Insufficient balance for withdrawal (should return 400)
curl -X POST http://localhost:8080/api/accounts/{accountId}/transactions \
  -H "Content-Type: application/json" \
  -H "X-Tenant-Id: BANK001" \
  -d '{"type":"WITHDRAWAL","amount":999999.00}'

# 4. Invalid transaction type (should return 400)
curl -X POST http://localhost:8080/api/accounts/{accountId}/transactions \
  -H "Content-Type: application/json" \
  -H "X-Tenant-Id: BANK001" \
  -d '{"type":"INVALID","amount":100.00}'

# 5. Negative balance (should return 400)
curl -X POST http://localhost:8080/api/accounts \
  -H "Content-Type: application/json" \
  -H "X-Tenant-Id: BANK001" \
  -d '{"customerName":"Test","balance":-100.00}'
```

---

## 🏗️ Multi-Tenancy Architecture

### How It Works

1. **Request Interception**
   - `TenantFilter` intercepts all HTTP requests
   - Extracts `X-Tenant-Id` header
   - Stores tenant ID in `TenantContext` (ThreadLocal)

2. **Data Isolation**
   - All repository methods filter by `tenantId`
   - Queries automatically include `WHERE tenant_id = ?`
   - Prevents cross-tenant data access

3. **Context Management**
   ```
   HTTP Request → TenantFilter → TenantContext.set(tenantId)
        ↓
   Controller → Service → Repository (filters by tenantId)
        ↓
   Database Query (WHERE tenant_id = 'BANK001')
        ↓
   Response → TenantContext.clear()
   ```

### ThreadLocal Pattern

```java
// TenantContext stores tenant ID per thread
public class TenantContext {
    private static final ThreadLocal<String> currentTenant = new ThreadLocal<>();
    
    public static void setTenantId(String tenantId) {
        currentTenant.set(tenantId);
    }
    
    public static String getTenantId() {
        return currentTenant.get();
    }
    
    public static void clear() {
        currentTenant.remove(); // Prevents memory leaks
    }
}
```

---

## ⚡ Asynchronous Processing

### How Statement Generation Works

1. **Client Request**
   ```
   POST /api/statements → Returns jobId immediately (202 Accepted)
   ```

2. **Background Processing**
   ```java
   @Async("taskExecutor")
   public void processStatementAsync(String jobId, String tenantId) {
       // Runs in separate thread
       // Updates job status: PENDING → PROCESSING → COMPLETED
   }
   ```

3. **Polling for Results**
   ```
   GET /api/statements/{jobId} → Returns status and result
   ```

### Thread Pool Configuration

```java
@Bean(name = "taskExecutor")
public Executor taskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(2);      // Minimum threads
    executor.setMaxPoolSize(5);       // Maximum threads
    executor.setQueueCapacity(100);   // Queue size
    executor.setThreadNamePrefix("async-statement-");
    executor.initialize();
    return executor;
}
```

### Job Status Flow

```
PENDING → PROCESSING → COMPLETED
                    ↘ FAILED (on error)
```

---

## 🔧 Spring Modules Used

### 1. **Spring Boot Starter Web**
- **Purpose:** REST API development
- **Provides:**
  - Embedded Tomcat server
  - Spring MVC for controllers
  - Jackson for JSON serialization
  - HTTP request/response handling

### 2. **Spring Boot Starter Data JPA**
- **Purpose:** Data access layer
- **Provides:**
  - JPA implementation (Hibernate)
  - Repository pattern
  - Entity management
  - Transaction management
  - Query derivation from method names

### 3. **Spring Boot Starter Validation**
- **Purpose:** Input validation
- **Provides:**
  - Bean Validation (JSR-303)
  - Annotations: `@NotNull`, `@NotBlank`, `@Positive`, etc.
  - Automatic validation on `@Valid` parameters

### 4. **H2 Database**
- **Purpose:** In-memory database for development
- **Features:**
  - Zero configuration
  - Fast startup
  - Web console for SQL queries
  - Compatible with PostgreSQL/MySQL for production migration

### 5. **Spring Boot Async**
- **Purpose:** Asynchronous task execution
- **Provides:**
  - `@Async` annotation
  - Thread pool management
  - Non-blocking operations

### 6. **Spring Boot Filter**
- **Purpose:** Request interception
- **Provides:**
  - `Filter` interface implementation
  - Request/response manipulation
  - Tenant context management

---

## 📊 Design Patterns Used

### 1. **Repository Pattern**
- Abstracts data access logic
- Separates business logic from data persistence
- Example: `AccountRepository`, `TransactionRepository`

### 2. **DTO Pattern**
- Separates API contracts from domain entities
- Example: `AccountRequest`, `TransactionRequest`

### 3. **Service Layer Pattern**
- Encapsulates business logic
- Manages transactions
- Example: `AccountService`, `TransactionService`

### 4. **Filter Pattern**
- Intercepts requests for cross-cutting concerns
- Example: `TenantFilter` for multi-tenancy

### 5. **ThreadLocal Pattern**
- Provides thread-specific data storage
- Example: `TenantContext` for tenant isolation

---

## 🔐 Security Considerations

### Current Implementation
- ✅ Multi-tenant data isolation
- ✅ Input validation
- ✅ Exception handling
- ✅ Transaction atomicity

### Production Recommendations
- 🔒 Add Spring Security for authentication
- 🔒 Use JWT tokens instead of plain tenant headers
- 🔒 Implement rate limiting
- 🔒 Add audit logging
- 🔒 Enable HTTPS
- 🔒 Encrypt sensitive data at rest
- 🔒 Implement CORS properly
- 🔒 Add API versioning

---

## 🚀 Future Enhancements

- [ ] Spring Security integration (JWT authentication)
- [ ] PostgreSQL/MySQL database support
- [ ] Pagination for list endpoints
- [ ] Advanced search and filtering
- [ ] Transaction history export (CSV, PDF)
- [ ] Email notifications for statements
- [ ] Account balance alerts
- [ ] Transaction limits and rules
- [ ] Audit logging
- [ ] API rate limiting
- [ ] Swagger/OpenAPI documentation
- [ ] Docker containerization
- [ ] Kubernetes deployment
- [ ] Unit and integration tests
- [ ] Performance monitoring

---

## 🧪 Running Tests

```bash
# Run all tests
mvn test

# Run with coverage
mvn clean test jacoco:report

# Run specific test class
mvn test -Dtest=AccountServiceTest
```

---

## 🐛 Troubleshooting

### Issue: Application won't start
**Solution:** Check Java version (must be 17+)
```bash
java -version
```

### Issue: Port 8080 already in use
**Solution:** Change port in `application.properties`
```properties
server.port=8081
```

### Issue: H2 Console not accessible
**Solution:** Ensure dev profile is active
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Issue: Missing tenant header error
**Solution:** Always include `X-Tenant-Id` header in requests
```bash
curl -H "X-Tenant-Id: BANK001" http://localhost:8080/api/accounts
```

---

## 📝 API Status Codes

| Code | Description | Usage |
|------|-------------|-------|
| 200 | OK | Successful GET, PUT requests |
| 201 | Created | Successful POST (resource created) |
| 202 | Accepted | Async job accepted |
| 204 | No Content | Successful DELETE |
| 400 | Bad Request | Invalid input, validation errors |
| 404 | Not Found | Resource doesn't exist or access denied |
| 500 | Internal Server Error | Unexpected server error |

---

## 📖 Additional Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Data JPA Documentation](https://spring.io/projects/spring-data-jpa)
- [H2 Database Documentation](https://www.h2database.com/html/main.html)
- [REST API Best Practices](https://restfulapi.net/)
- [Multi-Tenancy Patterns](https://docs.microsoft.com/en-us/azure/architecture/patterns/multitenancy)

---

## 👥 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 👤 Author

**Jose Noe Hernandez Vivanco**
- GitHub: [@elinformatico.net](https://github.com/elinformatico)
- LinkedIn: [jnoehdezvivanco](https://www.linkedin.com/in/jnoehdezvivanco/)

---

## 🙏 Acknowledgments

- Spring Boot Team for the excellent framework
- H2 Database for easy development database
- Community contributors and testers

---

## 📞 Support
- Documentation: [Wiki](https://deepwiki.com/elinformatico/multi-tenant-banking-api)

---

**⭐ If you find this project helpful, please give it a star!**