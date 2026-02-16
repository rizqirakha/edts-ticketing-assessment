# 🎟 Concert Ticket Booking API
A Springboot application that produce set of APIs to be used by front-end and mobile engineers to develop a simple service for concert ticket reservation.

---

## 🚀 Features

- List available concerts
- Book concert tickets

---

## ✅ Prerequisites

| Layer       | Technology                  |
| ----------- | --------------------------- |
| Backend     | Java 17 + Spring Boot       |
| Persistence | Spring Data JPA + Hibernate |
| Dependency  | Maven                       |
| Database    | MySQL                       |


---

## 📦 Project Structure

```
src/main/java
├── controller   → REST endpoints
├── services     → business logic
├── repository   → database access
├── entity       → JPA models
├── dto          → response objects
├── exception    → global error handling
└── api          → API response wrapper
└── logging      → MDC logging for once per request log
```

---

## ▶ Running the Application

Start server:

```
mvn spring-boot:run
```

Application runs at:

```
http://localhost:8080
```

---

## 🔌 API Endpoints

### List Available Concerts

```
GET /api/concerts
```

Response Format:

```json
{
    "success": true,
    "message": "Concerts retrieved successfully",
    "data": [...],
    "requestId": "83552cf5-f819-4861-8f9a-972be498e17e",
    "timestamp": "2026-02-16T07:54:58.422493"
}
```

---

### Book Concert Ticket

```
POST /api/concerts/book
```

Request:

```json
{
    "concertId": 1,
    "userId": "user123"
}
```

Success Response:

```json
{
    "success": true,
    "code": "SUCCESS",
    "message": "Reservation successful",
    "data": {
        "bookingId": "DEW-319648",
        "bookingTime": "2026-02-16T07:54:42.605888",
        "concertName": "DEWA 19",
        "userId": "sa"
    },
    "requestId": "402d99ce-3bd6-4ff8-b29d-ed69b857305f",
    "timestamp": "2026-02-16T07:54:42.638680"
}
```

---

## ⚠ Error Handling

All errors return structured responses:

```json
{
    "success": false,
    "code": "ALREADY_BOOKED",
    "message": "User has already booked a ticket for this concert",
    "data": null,
    "requestId": "3300868e-f41a-4c80-802c-50ef495e48b5",
    "timestamp": "2026-02-16T08:27:54.817029"
}
```

Handled scenarios include:

* Duplicate booking
* Concert not found
* Reservation not open
* Tickets sold out

---

## 🧪 Testing

Automated tests cover:

* Concert listing
* Successful booking
* Duplicate booking rejection
* Concert booking validation (concert exist and open book window)

Run tests:

```
mvn test
```

---

## 🏗 Architectural Rationale

This application follows a layered Spring Boot architecture designed for clarity, scalability, and safe concurrent booking operations.
* Transaction management guarantees atomic operations (using optimistic and pessimistic locking)
* Database locking prevents overselling and unique constraints protect against duplicate bookings.
* Standarized response format for consisten structure
* Using MDC for logging trace per request

---

## 📈 Future Improvements

* User Authentication & Authorization
* Reserve method (Waiting time for payment)
* User error log trace