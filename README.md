# Job Processor Application

A Spring Boot application that provides a job processing system with a modern web interface. The application allows users to submit jobs and track their processing status through a user-friendly dashboard.

## Features

- Submit new jobs with user ID and job data
- View job processing history
- Real-time job status updates
- Modern and responsive web interface
- Redis-based job queue for reliable processing

## Tech Stack

- **Backend:**
  - Spring Boot 2.7.0
  - Java 11
  - Spring Data Redis
  - Spring Web

- **Frontend:**
  - HTML5
  - CSS3
  - Vanilla JavaScript
  - Modern UI with responsive design

- **Infrastructure:**
  - Redis 7.2.7 (for job queue)
  - Maven (build tool)

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher
- Redis 7.2.7 or higher

## Getting Started

1. **Start Redis Server**
   ```bash
   redis-server
   ```

2. **Build and Run the Application**
   ```bash
   mvn spring-boot:run
   ```

3. **Access the Application**
   - Open your web browser and navigate to `http://localhost:8081`
   - The Job Processor Dashboard will be displayed

## API Endpoints

- `POST /api/job` - Submit a new job
- `GET /api/job/{userId}` - Get latest job result for a user
- `GET /api/job/{userId}/history` - Get job history for a user

## Project Structure

```
src/main/java/com/example/
├── config/         # Configuration classes
├── controller/     # REST controllers
├── model/          # Data models
├── service/        # Business logic
└── jobprocessor/   # Job processing implementation
```

## Configuration

The application uses the following default configuration:
- Server Port: 8081
- Redis Host: localhost
- Redis Port: 6379

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details. 