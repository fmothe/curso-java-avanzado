# Architecture Documentation - CFA Convenios Microservice

## System Overview

The CFA Convenios Microservice is a Spring Boot application that manages partnership agreements, customer contacts, and WhatsApp messaging for CFA (Cooperativa de Farmacéuticos Argentinos).

---

## High-Level Architecture

```mermaid
graph TB
    subgraph "External Systems"
        CLIENT[Client Applications]
        KEYCLOAK[Keycloak<br/>Authentication]
        ROBBU[Robbu API<br/>WhatsApp Gateway]
    end
    
    subgraph "CFA Convenios Microservice"
        API[REST API Layer]
        SERVICE[Service Layer]
        REPO[Repository Layer]
        CRYPTO[Crypto 3DES<br/>Encryption]
    end
    
    subgraph "Data Layer"
        DB[(PostgreSQL<br/>Database)]
    end
    
    CLIENT -->|HTTP/REST| API
    KEYCLOAK -->|JWT Validation| API
    API --> SERVICE
    SERVICE --> REPO
    SERVICE --> CRYPTO
    SERVICE -->|HTTP Client| ROBBU
    REPO --> DB
    
    style API fill:#4a90e2
    style SERVICE fill:#50c878
    style REPO fill:#ffa500
    style DB fill:#ff6b6b
    style ROBBU fill:#9b59b6
    style KEYCLOAK fill:#e74c3c
```

---

## Component Architecture

```mermaid
graph LR
    subgraph "Controllers"
        EXTERNAL[External Controllers]
        INTERNAL[Internal Controllers]
        HEALTH[Health Controller]
    end
    
    subgraph "Services"
        PROSPECTO[ProspectoConvenio<br/>Service]
        CONTACTO[ContactoCliente<br/>Service]
        BLACKLIST[Blacklist<br/>Service]
        TEMPLATE[Template<br/>Service]
        ROBBU_SVC[Robbu<br/>Service]
    end
    
    subgraph "Repositories"
        PROSPECTO_REPO[(ProspectoConvenio<br/>Repository)]
        CONTACTO_REPO[(ContactoCliente<br/>Repository)]
        BLACKLIST_REPO[(Blacklist<br/>Repository)]
        TEMPLATE_REPO[(Template<br/>Repository)]
    end
    
    subgraph "External Clients"
        TOKEN_CLIENT[Token Client]
        ENVIO_CLIENT[Envio Client]
        WA_CLIENT[WhatsApp Client]
    end
    
    EXTERNAL --> PROSPECTO
    EXTERNAL --> CONTACTO
    EXTERNAL --> BLACKLIST
    EXTERNAL --> TEMPLATE
    
    INTERNAL --> PROSPECTO
    INTERNAL --> CONTACTO
    INTERNAL --> BLACKLIST
    INTERNAL --> TEMPLATE
    
    PROSPECTO --> PROSPECTO_REPO
    CONTACTO --> CONTACTO_REPO
    BLACKLIST --> BLACKLIST_REPO
    TEMPLATE --> TEMPLATE_REPO
    
    TEMPLATE --> ROBBU_SVC
    ROBBU_SVC --> TOKEN_CLIENT
    ROBBU_SVC --> ENVIO_CLIENT
    ROBBU_SVC --> WA_CLIENT
    
    style EXTERNAL fill:#4a90e2
    style INTERNAL fill:#4a90e2
    style PROSPECTO fill:#50c878
    style CONTACTO fill:#50c878
    style BLACKLIST fill:#50c878
    style TEMPLATE fill:#50c878
    style ROBBU_SVC fill:#50c878
```

---

## Data Flow - WhatsApp Message Sending

```mermaid
sequenceDiagram
    participant Client
    participant Controller
    participant TemplateService
    participant RobbuService
    participant TokenClient
    participant EnvioClient
    participant Robbu API
    
    Client->>Controller: POST /sendwhatsapp
    Controller->>TemplateService: sendWhatsapp(request)
    TemplateService->>RobbuService: sendWhatsapp(message)
    
    RobbuService->>TokenClient: getToken()
    TokenClient->>Robbu API: POST /token
    Robbu API-->>TokenClient: {access_token}
    TokenClient-->>RobbuService: Bearer token
    
    RobbuService->>EnvioClient: enviar(token, message)
    EnvioClient->>Robbu API: POST /send
    Robbu API-->>EnvioClient: {message_id}
    EnvioClient-->>RobbuService: Response
    
    RobbuService-->>TemplateService: Message sent
    TemplateService-->>Controller: Response
    Controller-->>Client: 200 OK + message_id
```

---

## Database Schema

```mermaid
erDiagram
    PROSPECTOS_CONVENIOS {
        varchar nu_cuil PK
        varchar nu_documento
        char cd_sexo
        varchar nu_cuit_empleador
        varchar tx_empleador
        varchar grupo_entidad
        int cd_sucursal
        int dist_km_sucursal
    }
    
    CONTACTO_CLIENTES {
        bigint id PK
        varchar usuario_operador
        varchar dni_cliente
        varchar cuil_cliente
        varchar nombre_cliente
        varchar telefono_cliente
        varchar mail_cliente
        timestamp created_at
    }
    
    BLACKLIST {
        bigint id PK
        varchar blacklist
    }
    
    TEMPLATES {
        bigint id PK
        varchar name
        varchar grupo_entidad
    }
    
    PROSPECTOS_CONVENIOS ||--o{ CONTACTO_CLIENTES : "cuil_cliente"
    TEMPLATES ||--o{ PROSPECTOS_CONVENIOS : "grupo_entidad"
```

---

## Authentication Flow

```mermaid
sequenceDiagram
    participant Client
    participant API
    participant Keycloak
    participant Service
    participant Database
    
    Client->>API: Request + Credentials
    
    alt Basic Authentication
        API->>API: Validate Basic Auth
        Note over API: Check username/password
    else JWT Authentication
        API->>Keycloak: Validate JWT
        Keycloak-->>API: Token Valid
    end
    
    API->>Service: Process Request
    Service->>Database: Query Data
    Database-->>Service: Result
    Service-->>API: Response
    API-->>Client: 200 OK + Data
```

---

## Deployment Architecture

```mermaid
graph TB
    subgraph "Docker Environment"
        subgraph "Application Container"
            APP[CFA Convenios MS<br/>Spring Boot App<br/>Port: 8080]
        end
        
        subgraph "Database Container"
            DB[(PostgreSQL<br/>Port: 5432)]
        end
    end
    
    subgraph "External Services"
        KEYCLOAK[Keycloak Server]
        ROBBU[Robbu API Gateway]
    end
    
    LB[Load Balancer]
    CLIENTS[Client Applications]
    
    CLIENTS --> LB
    LB --> APP
    APP --> DB
    APP -->|JWT Validation| KEYCLOAK
    APP -->|WhatsApp Messages| ROBBU
    
    style APP fill:#4a90e2
    style DB fill:#ff6b6b
    style LB fill:#50c878
    style KEYCLOAK fill:#e74c3c
    style ROBBU fill:#9b59b6
```

---

## Technology Stack

```mermaid
graph LR
    subgraph "Backend"
        KOTLIN[Kotlin 1.6.21]
        SPRING[Spring Boot 2.7.13]
        JPA[Spring Data JPA]
    end
    
    subgraph "Database"
        POSTGRES[PostgreSQL]
    end
    
    subgraph "HTTP Clients"
        FEIGN[OpenFeign]
        RETRY[Spring Retry]
    end
    
    subgraph "Security"
        JWT[JWT Authentication]
        CRYPTO[3DES Encryption]
        KEYCLOAK_INT[Keycloak Integration]
    end
    
    subgraph "Documentation"
        SWAGGER[Swagger/OpenAPI]
    end
    
    subgraph "Build & Deployment"
        MAVEN[Maven]
        DOCKER[Docker]
    end
    
    KOTLIN --> SPRING
    SPRING --> JPA
    SPRING --> FEIGN
    SPRING --> JWT
    JPA --> POSTGRES
    
    style KOTLIN fill:#7f52ff
    style SPRING fill:#6db33f
    style POSTGRES fill:#336791
    style DOCKER fill:#2496ed
```

---

## Request/Response Flow

```mermaid
flowchart TB
    START([Client Request]) --> AUTH{Authenticated?}
    
    AUTH -->|No| REJECT[401 Unauthorized]
    AUTH -->|Yes| VALIDATE{Valid Request?}
    
    VALIDATE -->|No| BAD_REQUEST[400 Bad Request]
    VALIDATE -->|Yes| BLACKLIST{Check Blacklist<br/>if WhatsApp?}
    
    BLACKLIST -->|Blacklisted| FORBIDDEN[403 Forbidden]
    BLACKLIST -->|OK| CONTROLLER[Controller Layer]
    
    CONTROLLER --> SERVICE[Service Layer]
    SERVICE --> BUSINESS{Business Logic}
    
    BUSINESS -->|Error| ERROR[Handle Exception]
    BUSINESS -->|Success| REPO[Repository Layer]
    
    REPO --> DATABASE[(Database)]
    DATABASE --> TRANSFORM[Transform to DTO]
    TRANSFORM --> RESPONSE[200 OK Response]
    
    ERROR --> ERROR_HANDLER[Global Exception Handler]
    ERROR_HANDLER --> ERROR_RESPONSE[Error Response]
    
    REJECT --> END([End])
    BAD_REQUEST --> END
    FORBIDDEN --> END
    RESPONSE --> END
    ERROR_RESPONSE --> END
    
    style START fill:#90ee90
    style END fill:#ff6b6b
    style AUTH fill:#ffa500
    style VALIDATE fill:#ffa500
    style BLACKLIST fill:#ffa500
    style BUSINESS fill:#ffa500
    style CONTROLLER fill:#4a90e2
    style SERVICE fill:#50c878
    style REPO fill:#9b59b6
    style DATABASE fill:#ff6b6b
```

---

## Error Handling Flow

```mermaid
flowchart TD
    EXCEPTION[Exception Thrown] --> HANDLER{Exception Type}
    
    HANDLER -->|TokenException| TOKEN_ERROR[Token Error<br/>500 Internal Server Error]
    HANDLER -->|ErrorException| CUSTOM_ERROR[Custom Error<br/>Status from Exception]
    HANDLER -->|ValidationException| VALIDATION_ERROR[Validation Error<br/>400 Bad Request]
    HANDLER -->|NotFoundException| NOT_FOUND[Not Found<br/>404 Not Found]
    HANDLER -->|Other| GENERIC_ERROR[Generic Error<br/>500 Internal Server Error]
    
    TOKEN_ERROR --> LOG[Log Exception]
    CUSTOM_ERROR --> LOG
    VALIDATION_ERROR --> LOG
    NOT_FOUND --> LOG
    GENERIC_ERROR --> LOG
    
    LOG --> RESPONSE[Format Error Response]
    RESPONSE --> CLIENT[Return to Client]
    
    style EXCEPTION fill:#ff6b6b
    style HANDLER fill:#ffa500
    style LOG fill:#4a90e2
    style RESPONSE fill:#50c878
    style CLIENT fill:#90ee90
```

---

## Service Integration Points

### Robbu Integration (WhatsApp)

The microservice integrates with Robbu API for WhatsApp messaging:

1. **Authentication**: Token-based authentication via `/token` endpoint
2. **Message Sending**: POST to `/send` endpoint with message payload
3. **Retry Logic**: Automatic retry on failures using Spring Retry
4. **Error Handling**: Comprehensive error handling for network and API failures

**Robbu Endpoints**:
- Token: `https://api-accounts.robbu.global/token`
- Message: `https://api.robbu.global/send`

### Keycloak Integration (Authentication)

JWT-based authentication with Keycloak:

1. **Token Validation**: Validates JWT tokens using public key
2. **User Authorization**: Extracts user roles and permissions
3. **Realm Configuration**: Supports multiple Keycloak realms

---

## Scalability Considerations

### Horizontal Scaling

```mermaid
graph LR
    LB[Load Balancer]
    MS1[MS Instance 1]
    MS2[MS Instance 2]
    MS3[MS Instance 3]
    DB[(PostgreSQL<br/>Primary)]
    
    LB --> MS1
    LB --> MS2
    LB --> MS3
    
    MS1 --> DB
    MS2 --> DB
    MS3 --> DB
    
    style LB fill:#50c878
    style MS1 fill:#4a90e2
    style MS2 fill:#4a90e2
    style MS3 fill:#4a90e2
    style DB fill:#ff6b6b
```

The microservice is designed for horizontal scaling:
- **Stateless Design**: No session state stored in memory
- **Database Connection Pooling**: Efficient database connections
- **Load Balancing**: Multiple instances behind load balancer
- **Graceful Shutdown**: Proper connection cleanup on shutdown

---

## Security Architecture

```mermaid
graph TB
    subgraph "Security Layers"
        NETWORK[Network Security]
        AUTH[Authentication]
        AUTHZ[Authorization]
        CRYPTO_LAYER[Encryption]
        VALIDATION[Input Validation]
    end
    
    subgraph "Security Features"
        BASIC_AUTH[Basic Authentication]
        JWT_AUTH[JWT Tokens]
        KEYCLOAK_INT[Keycloak Integration]
        CRYPTO_3DES[3DES Encryption]
        SQL_INJECTION[SQL Injection Prevention]
        XSS[XSS Protection]
    end
    
    NETWORK --> AUTH
    AUTH --> AUTHZ
    AUTHZ --> CRYPTO_LAYER
    CRYPTO_LAYER --> VALIDATION
    
    AUTH --> BASIC_AUTH
    AUTH --> JWT_AUTH
    AUTH --> KEYCLOAK_INT
    CRYPTO_LAYER --> CRYPTO_3DES
    VALIDATION --> SQL_INJECTION
    VALIDATION --> XSS
    
    style NETWORK fill:#e74c3c
    style AUTH fill:#3498db
    style AUTHZ fill:#2ecc71
    style CRYPTO_LAYER fill:#f39c12
    style VALIDATION fill:#9b59b6
```

---

## Monitoring and Observability

### Key Metrics to Monitor

1. **Application Metrics**
    - Request rate
    - Response times
    - Error rates
    - Active connections

2. **Database Metrics**
    - Query performance
    - Connection pool utilization
    - Transaction rates

3. **External Integration Metrics**
    - Robbu API response times
    - Robbu API success rates
    - Keycloak validation times

4. **Resource Metrics**
    - CPU usage
    - Memory consumption
    - Thread pool utilization
    - Garbage collection metrics

---

## Data Retention and Archival

```mermaid
graph LR
    ACTIVE[(Active Data<br/>Current Month)]
    ARCHIVE[(Archived Data<br/>Older than 3 months)]
    BACKUP[(Backups<br/>Daily/Weekly)]
    
    ACTIVE -->|Monthly| ARCHIVE
    ACTIVE -->|Daily| BACKUP
    ARCHIVE -->|Weekly| BACKUP
    
    style ACTIVE fill:#50c878
    style ARCHIVE fill:#ffa500
    style BACKUP fill:#ff6b6b
```

**Recommended Retention Policies**:
- **Active Data**: Current month (in main database)
- **Archived Data**: 2 years (in archive storage)
- **Backups**: 90 days (daily), 1 year (weekly)

---

## Future Enhancements

1. **Caching Layer**: Redis for frequently accessed data
2. **Message Queue**: RabbitMQ/Kafka for async messaging
3. **API Gateway**: Centralized API management
4. **Service Mesh**: Enhanced service-to-service communication
5. **Observability**: Distributed tracing with Jaeger/Zipkin

---

## References

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [OpenFeign](https://spring.io/projects/spring-cloud-openfeign)
- [Keycloak](https://www.keycloak.org/)
- [PostgreSQL](https://www.postgresql.org/)

---

This architecture is designed to be:
- **Scalable**: Horizontal scaling support
- **Maintainable**: Clear separation of concerns
- **Secure**: Multiple security layers
- **Observable**: Comprehensive logging and monitoring
- **Resilient**: Graceful error handling and retry logic