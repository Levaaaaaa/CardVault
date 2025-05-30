# CardVault
Test task for Java internship in EffectiveMobile

## 🛠 Technologies

- Java 21+
- Spring Boot
- Spring Security (JWT)
- PostgreSQL
- Gradle
- Docker / Docker Compose
- Postman

---

## Getting Started

### Prerequisites

- Docker & Docker Compose (for local DB)
- Git

### Run locally
1. 
```
docker build -t cardvault-db-image ./postgres/
``` 
2.
```
docker run --env-file=./postgres/cardvault_db.env -p 5000:5432 cardvault-db-image
```
### Run the application by Docker Compose

1. Clone repo, change directory
    ```
   cd CardVault
   ```
2. Add environment:
```
    echo SPRING_DATASOURCE_URL=jdbc:postgresql://cardvault_db-host:5432/cardvault_db>> src/main/resources/cardvault-app.env
    echo SPRING_DATASOURCE_USERNAME=cardvault>> src/main/resources/cardvault-app.env 
    echo SPRING_DATASOURCE_PASSWORD=cardvault>> src/main/resources/cardvault-app.env
    echo APP_JWT_SECRET_KEY=secretsecretsecretsecretsecretsecret>>src/main/resources/cardvault-app.env
    echo APP_JWT_EXPIRATION_MS=900000>>src/main/resources/cardvault-app.env
    echo SPRING_PROFILES_ACTIVE=docker>>src/main/resources/cardvault-app.env
    echo APP_CARD_NUMBER_ENCRYPT_SECRET=CARD_NUMBER_ENCR>>src/main/resources/cardvault-app.env
    echo ADMIN_EMAIL=admin@example.com>>src/main/resources/cardvault-app.env
    echo ADMIN_PASSWORD=strong_password>>src/main/resources/cardvault-app.env
```
   
   ```
     echo POSTGRES_USER=cardvault>>postgres/cardvault_db.env
     echo POSTGRES_PASSWORD=cardvault>>postgres/cardvault_db.env
     echo POSTGRES_DB=cardvault_db>>postgres/cardvault_db.env
     echo PGDATA=/var/lib/postgresql/data>>postgres/cardvault_db.env
   ```

3. Run
```
    docker compose up
  ```

### 🔐 Authentication
The API uses JWT-based authentication.
To access protected endpoints, follow these steps:

1. Register
   Endpoint: POST /api/auth/register
   Body (JSON):
```json
{
"firstName": "John",
"lastName": "Doe",
"email": "john@example.com",
"password": "yourPassword123"
}
```
2. Login
   Endpoint: POST /api/auth/login
   Body (JSON):
```json
{
"email": "john@example.com",
"password": "yourPassword123"
}
```
Response:

```json
{
"token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```
Use this token in the Authorization header for all protected requests:

```
Authorization: Bearer <your_token>
```

📬 Postman Collection
You can find the full [[Postman collection|postman/]] and environment in the postman/ folder.

Import Collection
Open Postman.

Click Import.

Upload the file:
```
CardVault.postman_collection.json
```

#### Attention! Don't forget to add actual JWT token to your headers!
#### If you execute operations with cards don't forget update card's UUID in URLs and request bodies!

### 📘 API Endpoints Overview
<table>
   <tr>
      <th>Method</th>	
      <th>Endpoint</th>	
      <th>Description</th> 
   </tr>
   <tr>
      <td>POST</td>	
      <td>/auth/register</td>	
      <td>Register new user</td>
   </tr>
   <tr>
      <td>POST</td>	
      <td>/auth/login</td>	
      <td>Authenticate and get JWT token</td>
   </tr>
   <tr>
      <td>GET</td>	
      <td>/cards</td>	
      <td>Get paginated list of cards (auth)</td>
   </tr>
   <tr>
      <td>GET</td>	
      <td>/cards/{uuid}</td>	
      <td>Get single card by UUID (auth)</td>
   </tr>
   <tr>
      <td>GET</td>
      <td>cards/search?some_parameters</td>
      <td>Parametrized search</td>
   </tr>
</table>
More endpoints are available in the Postman collection.

### Use

#### Patterns:
- Factory
- Builder
- Specification
- Singleton