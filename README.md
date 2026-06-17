# REST API Automation
> **Eesha Noor** | SDET | Java + RestAssured + TestNG + Allure

## Tech Stack
- Java 11
- RestAssured 5.x
- TestNG
- Maven
- Allure Reports
- JSON Schema Validation
- Faker (test data generation)

## APIs Under Test
- **ReqRes** (https://reqres.in) — Users CRUD, Auth
- **Restful Booker** (https://restful-booker.herokuapp.com) — Booking management

## Test Coverage
- GET /users — list, single, not found
- POST /users — create, validate response schema
- PUT/PATCH /users — full & partial update
- DELETE /users — 204 response
- POST /login — valid token, invalid credentials
- Booking CRUD — create, retrieve, update, delete
- Auth token flow — end-to-end

## Run Tests
```bash
mvn clean test
mvn clean test -Dgroups=smoke
mvn clean test -Dgroups=regression
mvn allure:serve    # live report
mvn allure:report   # static report
```