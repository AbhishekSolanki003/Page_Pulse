# 🚀 Page Pulse

Page Pulse is a full-stack web application that analyzes any public website URL and generates a concise SEO and page-quality report. It audits a webpage by measuring response time, extracting metadata, and calculating key page metrics through a clean and responsive user interface.

---

## 🌐 Live Demo

**Frontend:**  
https://page-pulse-gilt.vercel.app/

**Backend API:**  
https://page-pulse-backend-production-331c.up.railway.app

---

## ✨ Features

- Analyze any public HTTP/HTTPS website URL
- Validate URLs before making requests
- Measure HTTP response time
- Fetch pages using Java HttpClient
- Parse HTML using Jsoup
- Extract:
  - HTTP Status Code
  - Response Time
  - Page Title
  - Meta Description
  - H1 Heading Count
  - Images Missing Alt Attributes
  - Approximate Visible Word Count
- Responsive React dashboard
- Audit history using Local Storage
- Copy JSON response
- Download audit report as JSON
- Dark mode interface
- Consistent JSON error responses

---

# 🛠 Tech Stack

## Backend

- Java 21
- Spring Boot 3.x
- Maven
- Spring Web
- Jsoup
- Java HttpClient
- Lombok
- JUnit 5
- Mockito

## Frontend

- React 18
- Vite
- Axios
- CSS

## Deployment

- Railway (Backend)
- Vercel (Frontend)

---

# 📁 Project Structure

```text
Page_Pulse/
│
├── backend/
│   ├── src/
│   │   ├── main/
│   │   └── test/
│   ├── pom.xml
│   └── Dockerfile
│
├── frontend/
│   ├── src/
│   ├── public/
│   ├── package.json
│   └── vite.config.js
│
└── README.md
```

---

# 🏗 Architecture

The backend follows a layered architecture.

```
Controller
      │
      ▼
Service
      │
      ▼
HTML Parser Utility
      │
      ▼
Audit Response
```

### Components

- **Controller** – Receives HTTP requests and returns API responses.
- **Service** – Handles business logic, page fetching, timing, and report generation.
- **Utility** – Parses HTML using Jsoup and extracts page metrics.
- **Validation** – Accepts only valid public HTTP/HTTPS URLs.
- **Global Exception Handler** – Returns consistent JSON error responses.

The frontend is a React single-page application that communicates with the backend REST API and displays audit reports.

---

# 🚀 Getting Started

## Clone the Repository

```bash
git clone https://github.com/AbhishekSolanki003/Page_Pulse.git

cd Page_Pulse
```

---

# Backend Setup

Go to the backend directory:

```bash
cd backend
```

Run the application:

### Linux / macOS

```bash
./mvnw spring-boot:run
```

### Windows

```bash
mvnw.cmd spring-boot:run
```

The backend runs on:

```
http://localhost:8080
```

---

# Frontend Setup

Open another terminal.

```bash
cd frontend

npm install

npm run dev
```

The frontend runs on:

```
http://localhost:5173
```

---

# Environment Variables

## Frontend

Create a `.env` file inside the `frontend` folder.

For local development:

```text
VITE_API_BASE_URL=http://localhost:8080
```

For production:

```text
VITE_API_BASE_URL=https://page-pulse-backend-production-331c.up.railway.app
```

---

# API Documentation

## POST `/api/audit`

### Request

```json
{
  "url": "https://openai.com"
}
```

---

### Success Response

```json
{
  "status": 200,
  "responseTime": 342,
  "title": "OpenAI",
  "metaDescription": "Creating safe AGI",
  "h1Count": 2,
  "missingAltImages": 3,
  "wordCount": 1685
}
```

---

### Error Response

```json
{
  "timestamp": "2026-07-25T12:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid URL",
  "path": "/api/audit"
}
```

---

# Testing

## Backend

Run:

```bash
cd backend

./mvnw test
```

or (Windows)

```bash
mvnw.cmd test
```

### Test Coverage

- URL Validation
- Service Layer
- HTML Parser Utility
- Missing Meta Description
- Missing H1 Tags
- Missing Image Alt Attributes
- Word Count Calculation
- Timeout Handling
- Invalid URLs
- Non-HTML Responses

---

# Deployment

## Backend

**Platform:** Railway

Live URL

```
https://page-pulse-backend-production-331c.up.railway.app
```

---

## Frontend

**Platform:** Vercel

Live URL

```
https://page-pulse-gilt.vercel.app
```

---

# Design Decisions

- Used Java HttpClient instead of third-party HTTP libraries to keep the application lightweight.
- Isolated HTML parsing inside a utility class to separate concerns.
- Used Spring Boot layered architecture for better maintainability.
- Stored audit history in Local Storage instead of using a database to satisfy the assignment constraints.
- Implemented centralized exception handling using `@RestControllerAdvice`.
- Used Axios for clean API communication between frontend and backend.

---

# Future Improvements

- Add authentication
- Add PDF report export
- Add CSV export
- Add Lighthouse integration
- Add Open Graph and Twitter Card analysis
- Add Canonical URL detection
- Add Robots Meta analysis
- Add Performance Score
- Add Accessibility Score
- Add Frontend unit tests using Vitest

---

# Notes

- Only public HTTP and HTTPS URLs are accepted.
- Localhost URLs are rejected.
- FTP, File, and JavaScript URLs are rejected.
- No database is used.
- No authentication is required.
- Audit history is stored in the browser using Local Storage.

---

# Author

**Abhishek Solanki**

GitHub:  
https://github.com/AbhishekSolanki003/

LinkedIn:  
[YOUR_LINKEDIN_PROFILE](https://www.linkedin.com/in/abhishek-solanki-024253315/)

---

## Assignment

This project was developed as part of the **Digital Heroes SDE Internship Qualification Task**.