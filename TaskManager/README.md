# TaskManager API

## 🎓 Academic Context and Architecture

This application was developed during the academic semester of **Special Topics in Computing: Trending Information Technologies** at **Unisinos (06/2025)**.

The primary focus of this project was the implementation of a **Serverless-Managed Backend** structure on Amazon Web Services (AWS), utilizing the following services:

- **Amazon RDS (PostgreSQL)**: Managed relational database service for storing tasks and user data.
- **Amazon Cognito**: Handles user authentication and authorization via JWT (JSON Web Tokens).
- **Amazon CloudWatch**: Used for monitoring application logs and metrics.

## 🛠️ Prerequisites

Before running the application, ensure you have the following installed:

- **Java 21** (JDK)
- **Gradle** (optional, wrapper provided)
- **AWS Account** (if connecting to real AWS services)

## ⚙️ Configuration

The application uses environment variables to configure connections to AWS services. You can set these in your IDE, terminal, or a `.env` file.

### Required Environment Variables

| Variable | Description | Example |
|----------|-------------|---------|
| `SPRING_DATASOURCE_URL` | JDBC URL for RDS PostgreSQL | `jdbc:postgresql://taskmanager-db.xxx.us-east-2.rds.amazonaws.com:5432/task_manager` |
| `SPRING_DATASOURCE_USERNAME` | Database username | `postgres` |
| `SPRING_DATASOURCE_PASSWORD` | Database password | `MySecurePassword123` |
| `AWS_REGION` | AWS Region for Cognito | `us-east-2` |
| `USER_POOL_ID` | Cognito User Pool ID | `us-east-2_xxxxxxxxx` |

> **Note:** If these variables are not set, the application defaults to `localhost` settings for development (see `application.properties`).

## 🚀 How to Run

### 1. Clone the repository
```bash
git clone https://github.com/strLuckyyy/TaskManager.git
cd TaskManager
```

### 2. Configure Environment (PowerShell Example)
```powershell
$env:SPRING_DATASOURCE_URL="jdbc:postgresql://your-rds-endpoint:5432/task_manager"
$env:SPRING_DATASOURCE_USERNAME="postgres"
$env:SPRING_DATASOURCE_PASSWORD="password"
```

### 3. Run with Gradle Wrapper
```powershell
# Windows
.\gradlew.bat bootRun

# Linux/Mac
./gradlew bootRun
```

The application will start on port **5000** (or 8080 depending on configuration).

## 📚 API Documentation

Once the application is running, you can access the interactive API documentation via Swagger UI:

- **Swagger UI**: [http://localhost:5000/swagger-ui/index.html](http://localhost:5000/swagger-ui/index.html)
- **OpenAPI JSON**: [http://localhost:5000/v3/api-docs](http://localhost:5000/v3/api-docs)

## 🔐 Authentication

Endpoints under `/api/**` are protected and require a valid **Bearer Token** (JWT) issued by AWS Cognito.

1. Authenticate via your Frontend or AWS CLI to get a token.
2. Include the token in the `Authorization` header:
   ```
   Authorization: Bearer <YOUR_JWT_TOKEN>
   ```

## 📦 Deployment

To build the production JAR file:

```bash
.\gradlew.bat bootJar
```

The artifact will be generated in `build/libs/TaskManager-0.0.1-SNAPSHOT.jar`.

## 📝 License

This project is licensed under the MIT License.
