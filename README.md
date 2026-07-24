# Page Pulse

Page Pulse is a production-oriented web application that audits any public website URL and returns a compact SEO and page-quality report. The backend is built with Java 21 and Spring Boot 3.x, and the frontend is built with React and Vite.

## Features

- Validates URLs before any request is made.
- Fetches remote pages with Java `HttpClient` and measures response time.
- Rejects non-HTML responses.
- Parses HTML with Jsoup.
- Returns page title, meta description, H1 count, missing alt image count, and approximate visible word count.
- Handles invalid URL, timeout, DNS, SSL, redirect, non-HTML, 404, 500, and malformed payload scenarios with JSON error responses.
- Responsive dashboard UI with loading, error, results, dark mode, copy JSON, download JSON, and audit history.

## Technology Stack

### Backend

- Java 21
- Spring Boot 3.x
- Maven
- Spring Web
- Jsoup
- JUnit 5
- Mockito
- Lombok

### Frontend

- React 18
- Vite
- Axios
- CSS

## Architecture

The backend follows a Controller -> Service -> Utility structure:

- Controller: receives the request and delegates work.
- Service: performs fetch, timing, error handling, and response assembly.
- Utility: parses HTML and extracts metrics.
- Validation: ensures only safe `http` and `https` URLs are accepted.
- Exception handling: `@RestControllerAdvice` converts failures into JSON error responses.

The frontend is a single-page dashboard that calls the backend API and renders the resulting report cards.

## Folder Structure

```text
backend/
  src/main/java/com/digitalheroes/pagepulse/
    controller/
    config/
    dto/
    exception/
    service/
    service/impl/
    util/
    validation/
  src/main/resources/
  src/test/java/

frontend/
  src/
    components/
    pages/
    services/
```

## Setup Instructions

### Backend

1. Open a terminal in `backend`.
2. Run `mvn clean test` to build and test.
3. Run `mvn spring-boot:run` to start the API.

The backend listens on `http://localhost:8080` by default.

### Frontend

1. Open a terminal in `frontend`.
2. Install dependencies with `npm install`.
3. Run `npm run dev` to start the Vite app.

Set `VITE_API_BASE_URL` if the frontend should call a deployed backend instead of localhost.

## How to Run Backend

```bash
cd backend
mvn spring-boot:run
```

## How to Run Frontend

```bash
cd frontend
npm install
npm run dev
```

## API Contract

### POST `/api/audit`

#### Request

```json
{
  "url": "https://openai.com"
}
```

#### Success Response

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

#### Error Response

```json
{
  "timestamp": "2026-07-24T12:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid URL",
  "path": "/api/audit"
}
```

## Testing Instructions

### Backend

```bash
cd backend
mvn test
```

The backend tests cover:

- Happy path
- Invalid URL
- Missing meta description
- No H1
- No images
- Images with alt
- Images without alt
- Non HTML content
- Timeout
- Word count
- Parsing utility
- Service layer

### Frontend

There are no automated frontend tests in this submission, but the UI is structured so that component and integration tests can be added easily with Vitest and React Testing Library.

## Deployment Instructions

### Render Backend

The repository includes `backend/render.yaml`.

Suggested Render settings:

- Root directory: `backend`
- Build command: `mvn clean package -DskipTests`
- Start command: `java -jar target/page-pulse-backend-1.0.0.jar`

### Vercel Frontend

The repository includes `frontend/vercel.json`.

Suggested Vercel settings:

- Root directory: `frontend`
- Build command: `npm run build`
- Output directory: `dist`
- Environment variable: `VITE_API_BASE_URL=<your Render backend URL>`

## Design Decisions

1. The backend uses `HttpClient` instead of a third-party HTTP library so the audit flow stays lightweight and aligns with the assignment requirements.
2. HTML parsing is isolated in `HtmlParserUtil` so the service remains focused on orchestration, error handling, and response building.
3. The frontend keeps audit history in `localStorage` instead of using a database, which satisfies the no-database constraint while still improving usability.

## Future Improvements

- Add frontend tests with Vitest and React Testing Library.
- Add request rate limiting to protect the public API.
- Add support for additional SEO signals such as canonical URL, robots meta, and Open Graph tags.
- Add PDF export for reports.
- Add a results compare view for audit history.

## Notes

- The backend allows only public `http` and `https` URLs.
- `localhost`, `file://`, `ftp://`, and `javascript:` URLs are rejected.
- No database or authentication is used.