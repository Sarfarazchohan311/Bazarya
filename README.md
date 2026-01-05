# Bazarya

Production-grade ecommerce scaffold for Pakistan using Java 17, Spring Boot 3, PostgreSQL, and Thymeleaf (Phase 1).

## Features
- API-first architecture with `/api/v1/**` endpoints
- Layered design: domain/entities, repositories, services, web + api controllers
- Pricing engine with coupon support and shipping rules
- Order lifecycle, inventory reservation, and audit logging scaffolding
- Flyway migrations
- Docker Compose deployment with Nginx reverse proxy

## Local Development
1. Build the application:
   ```bash
   mvn clean package
   ```
2. Start services:
   ```bash
   docker compose up --build
   ```
3. Access storefront: `http://localhost`
4. Health check: `http://localhost:8080/actuator/health`

## OCI Deployment Steps
1. Create an OCI Compute VM (Ubuntu 22.04).
2. Open security list rules for ports 80/443.
3. Install Docker + Docker Compose:
   ```bash
   sudo apt-get update
   sudo apt-get install -y docker.io docker-compose-plugin
   sudo usermod -aG docker ubuntu
   ```
4. Upload project files to the VM and build the app:
   ```bash
   mvn clean package
   ```
5. Configure `nginx.conf` with your domain and SSL cert paths.
6. Start the stack:
   ```bash
   docker compose up -d --build
   ```
7. Point your domain DNS A record to the VM public IP.

## Notes
- Replace placeholder SSL paths with Let's Encrypt certificates.
- Add real OCI Object Storage integration by implementing `StorageService`.
