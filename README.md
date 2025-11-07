# Shop Sense 🛍️

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.6-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Database-blue.svg)](https://www.postgresql.org/)
[![Redis](https://img.shields.io/badge/Redis-Cache-red.svg)](https://redis.io/)
[![Stripe](https://img.shields.io/badge/Stripe-Payment-blueviolet.svg)](https://stripe.com/)

> A modern, secure e-commerce backend API built with Spring Boot, featuring JWT authentication, comprehensive cart management, and seamless Stripe payment integration.

## 📋 Table of Contents

- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Getting Started](#-getting-started)
  - [Prerequisites](#prerequisites)
  - [Environment Variables](#environment-variables)
  - [Installation](#installation)
  - [Docker Setup](#docker-setup)
- [API Documentation](#-api-documentation)
- [Project Structure](#-project-structure)
- [Security](#-security)
- [Roadmap](#-roadmap)

---

## ✨ Features

### Product Management
- **Full CRUD Operations**: Create, Read, Update, and Delete products
- Product catalog with detailed information
- Inventory management capabilities

### Authentication & Authorization
- **JWT-based Authentication**: Secure token-based user authentication
- User registration and login
- Role-based access control with Spring Security
- **Redis Token Blacklist**: Secure logout implementation preventing token reuse

### Shopping Cart Management
- Add items to cart
- Update item quantities
- Remove individual items
- Clear entire cart
- Persistent cart storage per user

### Order Processing & Checkout
- Create and manage orders
- Complete checkout flow
- Order history and tracking
- **Stripe Payment Integration**: Secure payment processing

### Coming Soon 🚀
- **AI Chatbot Assistant**: Intelligent customer support and product recommendations

---

## 🛠️ Tech Stack

### Backend
- **Spring Boot** 3.5.6
- **Spring Security** - Authentication & Authorization
- **Spring Data JPA** - Database ORM
- **JWT** - Token-based authentication

### Database & Cache
- **PostgreSQL** - Primary database
- **Redis** - Token blacklist and caching

### Payment
- **Stripe** - Payment gateway integration

### Build & Deployment
- **Maven** - Dependency management
- **Docker** - Containerization
- **Java 17** - Runtime environment

---

## 🚀 Getting Started

### Prerequisites

Ensure you have the following installed on your system:

- **Java 17** or higher
- **Maven 3.9+**
- **PostgreSQL** (running instance)
- **Redis** (running instance)
- **Docker** (optional, for containerized setup)
- **Stripe Account** (for payment integration)

### Environment Variables

Create an `application.properties` or `application.yml` file in `src/main/resources/` with the following configurations:

```properties
spring.application.name=${APP_NAME}
server.port=${SERVER_PORT}
# JWT
jwt.access.secret=${JWT_ACCESS_SECRET}
jwt.access.expiration-ms=${JWT_ACCESS_EXPIRATION_MS}
jwt.issuer=${JWT_ISSUER}
jwt.audience=${JWT_AUDIENCE}

jwt.refresh.secret=${JWT_REFRESH_SECRET}
jwt.refresh.expiration-ms=${JWT_REFRESH_EXPIRATION_MS}

# DB Setup
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.jpa.hibernate.ddl-auto=${DB_DDL_AUTO}
# Redis Setup
spring.data.redis.host=${REDIS_HOST}
spring.data.redis.port=${REDIS_PORT}
spring.data.redis.password=${REDIS_PASSWORD}
spring.data.redis.timeout=2000ms
spring.data.redis.jedis.pool.max-active=8
spring.data.redis.jedis.pool.max-idle=8
spring.data.redis.jedis.pool.min-idle=0

# Email Setup
spring.mail.host=${MAIL_HOST}
spring.mail.port=${MAIL_PORT}
spring.mail.username=${MAIL_USERNAME}
spring.mail.password=${MAIL_PASSWORD}
spring.mail.properties.mail.smtp.auth=${MAIL_AUTH}
spring.mail.properties.mail.smtp.starttls.enable=${MAIL_STARTTLS_ENABLE}

#ADMIN
admin.email=${ADMIN_EMAIL}
admin.password=${ADMIN_PASSWORD}

#DEBUG MODE
logging.level.com.app.mentora=${DEBUG_MODE}


logging.level.org.springframework.security=${SECURITY_DEBUG_MODE}
logging.level.org.springframework.web.filter=${FILTER_DEBUG_MODE}



# Stripe Configuration
stripe.secret-key=${STRIPE_SECRET_KEY}
stripe.currency=usd
# For testing (use test key)
# stripe.secret-key=sk_test_...


# Cookie Security (set to false for localhost development)
cookie.secure=${COOKIE_SECURE}

rate.limit.requests=${RATE_LIMIT_REQUESTS}
rate.limit.duration-minutes=${RATE_LIMIT_DURATION_MINUTES}


# CORS allowed origins (comma-separated)
cors.allowed.origins=${CORS_ORIGINS}


# Account lockout security settings
user.max_failed_attempts=${USER_MAX_FAILED_ATTEMPTS}
user.lock_time_duration=${USER_LOCK_TIME_DURATION}
```

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/GHYounesse/ShopSense.git
   cd shopsense
   ```

2. **Configure the application**
   - Update `application.properties` with your database credentials
   - Add your Stripe API keys
   - Set a secure JWT secret key

3. **Build the project**
   ```bash
   mvn clean install
   ```

4. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

The API will be available at `http://localhost:8080`



## 📡 API Documentation

### Base URL
```
http://localhost:8080/api
```

### Main Endpoints

#### Authentication
```
POST   /api/v1/auth/register    - Register new user
POST   /api/v1/auth/login       - User login (returns JWT)
POST   /api/v1/auth/refresh     - Refresh token
POST   /api/v1/auth/logout      - User logout (blacklists token)
```

#### Products
```
GET    /api/v1/products         - Get all products
GET    /api/v1/products/{id}    - Get product by ID
POST   /api/v1/products         - Create new product 
PUT    /api/v1/products/{id}    - Update product 
DELETE /api/v1/products/{id}    - Delete product 
```

#### Shopping Cart
```
GET    /api/v1/cart/{userId}             - Get user's cart
POST   /api/v1/cart/{userId}/items         - Add item to cart
PUT    /api/v1/cart/items/{productId}     - Update cart item quantity
DELETE /api/v1/cart/{userId}/items/{productId} - Remove item from cart
DELETE /api/v1/cart/{userId}        - Clear entire cart
```

#### Orders & Checkout
```
POST   /api/v1/orders           - Create new order
GET    /api/v1/orders/user/{userId}           - Get user's orders
GET    /api/v1/orders/{orderId}      - Get order details
POST   /api/v1/orders/user/{userId}/checkout         - Process checkout with Stripe
POST   /api/v1/orders/{orderId}/confirm-payment         - Confirm payment with Stripe

```


---

## 🔒 Security

Shop Sense implements robust security measures:

- **JWT Authentication**: Stateless authentication using JSON Web Tokens
- **Password Encryption**: BCrypt hashing for secure password storage
- **Token Blacklist**: Redis-based blacklist prevents reuse of logged-out tokens
- **Role-Based Access Control**: Different permissions for users and administrators
- **Spring Security**: Industry-standard security framework
- **HTTPS Ready**: Configured for secure communication in production
- **CORS Configuration**: Controlled cross-origin resource sharing

---

## 🗺️ Roadmap

### Current Features ✅
- Product management system
- JWT authentication
- Shopping cart functionality
- Order processing
- Stripe payment integration

### Upcoming Features 🚧
- **AI-Powered Chatbot**: Intelligent assistant for customer support and product recommendations
- Email notifications for orders
- Advanced search and filtering
- Product reviews and ratings
- Wishlist functionality
- Admin dashboard
- Analytics and reporting
- Multi-currency support

---

## 🤝 Contributing

This is a private project and is not currently accepting contributions.

---

## 📞 Contact

For questions or feedback, please open an issue in the repository.

---

