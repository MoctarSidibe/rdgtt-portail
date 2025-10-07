# 🏗️ Architecture R-DGTT Portail

## Vue d'ensemble du Système

```mermaid
graph TB
    subgraph "Client Layer"
        WEB[Web Browser]
        MOBILE[Mobile Browser]
    end
    
    subgraph "Load Balancer & Gateway"
        TRAEFIK[Traefik<br/>API Gateway & Load Balancer]
    end
    
    subgraph "Service Discovery"
        CONSUL[Consul<br/>Service Discovery & Health Checks]
    end
    
    subgraph "Application Layer"
        FRONTEND[Frontend React<br/>Port 80]
        ADMIN[Admin Service<br/>Port 8081]
        AUTOECOLE[Auto-École Service<br/>Port 8082]
        PERMIS[Permis Service<br/>Port 8083]
    end
    
    subgraph "Data Layer"
        POSTGRES[(PostgreSQL<br/>Database)]
    end
    
    WEB --> TRAEFIK
    MOBILE --> TRAEFIK
    TRAEFIK --> FRONTEND
    TRAEFIK --> ADMIN
    TRAEFIK --> AUTOECOLE
    TRAEFIK --> PERMIS
    
    CONSUL --> ADMIN
    CONSUL --> AUTOECOLE
    CONSUL --> PERMIS
    
    ADMIN --> POSTGRES
    AUTOECOLE --> POSTGRES
    PERMIS --> POSTGRES
```

## Hiérarchie des Rôles et Permissions

```mermaid
graph TD
    subgraph "Niveau 5 - Direction Générale"
        DGTT[DGTT<br/>Directeur Général<br/>🔑 Contrôle Total]
    end
    
    subgraph "Niveau 4 - Direction"
        DIR[DIRECTEUR<br/>Directeur de Département<br/>🏢 Gestion Départements]
    end
    
    subgraph "Niveau 3 - Service"
        CS[CHEF_SERVICE<br/>Chef de Service<br/>⚙️ Gestion Workflows]
    end
    
    subgraph "Niveau 2 - Bureau"
        CB[CHEF_BUREAU<br/>Chef de Bureau<br/>📋 Gestion Bureau]
    end
    
    subgraph "Niveau 1 - Opérationnel"
        AG[AGENT<br/>Agent Administratif<br/>📝 Exécution Tâches]
    end
    
    DGTT -->|"Peut gérer tous"| DIR
    DIR -->|"Peut gérer départements"| CS
    CS -->|"Peut créer workflows"| CB
    CB -->|"Peut gérer bureau"| AG
    
    DGTT -.->|"Approbation finale"| DGTT
    DIR -.->|"Validation département"| DIR
    CS -.->|"Validation service"| CS
    CB -.->|"Vérification bureau"| CB
    AG -.->|"Traitement documents"| AG
```

## Workflow Métier Complet

### Processus Auto-École → Candidat → Permis

```mermaid
flowchart TD
    subgraph "Phase 1: Inscription Auto-École"
        AE1[📝 Réception Demande<br/>AGENT] --> AE2[📋 Vérification Documents<br/>CHEF_BUREAU]
        AE2 --> AE3[🏢 Inspection Locaux<br/>CHEF_SERVICE]
        AE3 --> AE4[✅ Validation Directeur<br/>DIRECTEUR]
        AE4 --> AE5[🎯 Approbation DGTT<br/>DGTT]
    end
    
    subgraph "Phase 2: Inscription Candidat"
        C1[👤 Réception Candidat<br/>AGENT] --> C2[📄 Vérification Documents<br/>CHEF_BUREAU]
        C2 --> C3[✅ Validation Chef Service<br/>CHEF_SERVICE]
        C3 --> C4[📁 Génération Dossier<br/>AGENT]
    end
    
    subgraph "Phase 3: Permis de Conduire"
        P1[🚗 Réception Demande<br/>AGENT] --> P2[📂 Vérification Dossier<br/>CHEF_BUREAU]
        P2 --> P3[📝 Validation Examens<br/>CHEF_SERVICE]
        P3 --> P4[🔍 Contrôle Directeur<br/>DIRECTEUR]
        P4 --> P5[🎯 Approbation DGTT<br/>DGTT]
        P5 --> P6[📄 Génération Permis<br/>AGENT]
    end
    
    AE5 -->|"Auto-École validée"| C1
    C4 -->|"Candidat inscrit"| P1
```

### Services Connexes (Connexes)

```mermaid
graph LR
    subgraph "Services Connexes"
        DUP[📋 Duplicata Permis<br/>⏱️ 7 jours<br/>🔄 4 étapes]
        REN[🔄 Renouvellement<br/>⏱️ 15 jours<br/>🔄 5 étapes]
        CONV[🌍 Conversion Étranger<br/>⏱️ 20 jours<br/>🔄 5 étapes]
        ATT[📜 Attestation<br/>⏱️ 3 jours<br/>🔄 3 étapes]
    end
    
    P6[Permis Généré] --> DUP
    P6 --> REN
    P6 --> CONV
    P6 --> ATT
```

## Architecture des Services

### Admin Service (Workflow Engine)

```mermaid
graph TB
    subgraph "Admin Service - Port 8081"
        subgraph "Controllers"
            AMC[AdminManagementController]
            CWC[CompleteWorkflowController]
            CC[CitizenController]
        end
        
        subgraph "Services"
            WMS[WorkflowManagementService]
            CWS[CompleteWorkflowService]
            AUS[AdminUserService]
            DS[DepartmentService]
            BS[BureauService]
        end
        
        subgraph "Models"
            AU[AdminUser]
            D[Department]
            B[Bureau]
            DT[DocumentType]
            PS[ProcessStep]
            WI[WorkflowInstance]
            WSE[WorkflowStepExecution]
        end
        
        subgraph "Repositories"
            AUR[AdminUserRepository]
            DR[DepartmentRepository]
            BR[BureauRepository]
            DTR[DocumentTypeRepository]
            PSR[ProcessStepRepository]
            WIR[WorkflowInstanceRepository]
            WSER[WorkflowStepExecutionRepository]
        end
    end
    
    AMC --> WMS
    CWC --> CWS
    CC --> WMS
    
    WMS --> DTR
    WMS --> PSR
    WMS --> WIR
    WMS --> WSER
    
    CWS --> WMS
    AUS --> AUR
    DS --> DR
    BS --> BR
```

### Auto-École Service

```mermaid
graph TB
    subgraph "Auto-École Service - Port 8082"
        subgraph "Models"
            AE[AutoEcole]
            C[Candidat]
            E[Examen]
            CD[CandidatDocument]
        end
        
        subgraph "Controllers"
            AEC[AutoEcoleController]
            CC[CandidatController]
            EC[ExamenController]
        end
        
        subgraph "Services"
            AES[AutoEcoleService]
            CS[CandidatService]
            ES[ExamenService]
        end
    end
    
    AEC --> AES
    CC --> CS
    EC --> ES
    
    AES --> AE
    CS --> C
    ES --> E
```

### Permis Service

```mermaid
graph TB
    subgraph "Permis Service - Port 8083"
        subgraph "Models"
            P[Permis]
            PT[PermisType]
            PC[PermisCategory]
        end
        
        subgraph "Controllers"
            PC[PermisController]
        end
        
        subgraph "Services"
            PS[PermisService]
        end
    end
    
    PC --> PS
    PS --> P
```

## Flux de Données

### Workflow Execution Flow

```mermaid
sequenceDiagram
    participant C as Client
    participant T as Traefik
    participant A as Admin Service
    participant DB as PostgreSQL
    participant CS as Consul
    
    C->>T: POST /api/workflow/permis/start
    T->>A: Route to Admin Service
    A->>DB: Create WorkflowInstance
    A->>CS: Register Service Health
    A->>DB: Get Process Steps
    A->>A: Set Current Step
    A->>DB: Save WorkflowInstance
    A->>C: Return WorkflowInstance
    
    Note over C,CS: Workflow Started
    
    C->>T: POST /api/workflow/execute
    T->>A: Route to Admin Service
    A->>DB: Get WorkflowInstance
    A->>DB: Get ProcessStep
    A->>A: Execute Step Logic
    A->>DB: Create WorkflowStepExecution
    A->>DB: Update WorkflowInstance
    A->>C: Return Execution Result
```

### Citizen Status Check Flow

```mermaid
sequenceDiagram
    participant C as Citizen
    participant T as Traefik
    participant A as Admin Service
    participant DB as PostgreSQL
    
    C->>T: GET /api/citizen/status/DEM-2024-001
    T->>A: Route to Admin Service
    A->>DB: Find WorkflowInstance by demandeId
    A->>DB: Get DocumentType
    A->>DB: Get ProcessSteps
    A->>A: Build Status Response
    A->>C: Return Status Information
```

## Configuration et Déploiement

### Docker Compose Architecture

```mermaid
graph TB
    subgraph "Docker Network"
        subgraph "Frontend Container"
            FE[Frontend React<br/>nginx:alpine]
        end
        
        subgraph "Backend Containers"
            AS[Admin Service<br/>openjdk:17-jdk-slim]
            AES[Auto-École Service<br/>openjdk:17-jdk-slim]
            PS[Permis Service<br/>openjdk:17-jdk-slim]
        end
        
        subgraph "Infrastructure Containers"
            CONSUL[Consul<br/>consul:latest]
            TRAEFIK[Traefik<br/>traefik:v2.10]
            POSTGRES[PostgreSQL<br/>postgres:13]
        end
    end
    
    FE --> TRAEFIK
    TRAEFIK --> AS
    TRAEFIK --> AES
    TRAEFIK --> PS
    
    AS --> CONSUL
    AES --> CONSUL
    PS --> CONSUL
    
    AS --> POSTGRES
    AES --> POSTGRES
    PS --> POSTGRES
```

### Service Discovery

```mermaid
graph LR
    subgraph "Consul Service Discovery"
        subgraph "Registered Services"
            AS[admin-service<br/>8081]
            AES[auto-ecole-service<br/>8082]
            PS[permis-service<br/>8083]
        end
        
        subgraph "Health Checks"
            HC1[Admin Health Check]
            HC2[Auto-École Health Check]
            HC3[Permis Health Check]
        end
    end
    
    AS --> HC1
    AES --> HC2
    PS --> HC3
```

## Sécurité et Authentification

### Authentication Flow

```mermaid
sequenceDiagram
    participant U as User
    participant F as Frontend
    participant A as Admin Service
    participant DB as Database
    
    U->>F: Login Request
    F->>A: POST /api/auth/login
    A->>DB: Validate Credentials
    A->>A: Generate JWT Token
    A->>F: Return Token + User Info
    F->>F: Store Token in Context
    F->>U: Redirect to Dashboard
    
    Note over U,DB: User Authenticated
    
    U->>F: API Request
    F->>A: Request with JWT Token
    A->>A: Validate JWT Token
    A->>A: Check User Role
    A->>A: Process Request
    A->>F: Return Response
```

### Role-Based Access Control

```mermaid
graph TD
    subgraph "Access Control Matrix"
        subgraph "DGTT Permissions"
            DGTT_ALL[✅ All Operations]
            DGTT_USERS[✅ Manage All Users]
            DGTT_WORKFLOWS[✅ Manage All Workflows]
            DGTT_APPROVE[✅ Final Approvals]
        end
        
        subgraph "DIRECTEUR Permissions"
            DIR_DEPT[✅ Manage Departments]
            DIR_BUREAU[✅ Manage Bureaus]
            DIR_APPROVE[✅ Department Approvals]
        end
        
        subgraph "CHEF_SERVICE Permissions"
            CS_WORKFLOW[✅ Create Workflows]
            CS_USERS[✅ Manage Users]
            CS_VALIDATE[✅ Service Validations]
        end
        
        subgraph "CHEF_BUREAU Permissions"
            CB_BUREAU[✅ Manage Bureau]
            CB_VERIFY[✅ Bureau Verifications]
        end
        
        subgraph "AGENT Permissions"
            AG_EXECUTE[✅ Execute Tasks]
            AG_DOCUMENTS[✅ Process Documents]
        end
    end
```

## Monitoring et Observabilité

### Health Check Architecture

```mermaid
graph TB
    subgraph "Health Check System"
        subgraph "Application Health"
            AH1[Admin Service Health<br/>/actuator/health]
            AH2[Auto-École Health<br/>/actuator/health]
            AH3[Permis Health<br/>/actuator/health]
        end
        
        subgraph "Infrastructure Health"
            IH1[PostgreSQL Health]
            IH2[Consul Health]
            IH3[Traefik Health]
        end
        
        subgraph "Monitoring Dashboard"
            MD[Consul Dashboard<br/>:8500]
            TD[Traefik Dashboard<br/>:8080]
        end
    end
    
    AH1 --> MD
    AH2 --> MD
    AH3 --> MD
    IH1 --> MD
    IH2 --> MD
    IH3 --> MD
    
    AH1 --> TD
    AH2 --> TD
    AH3 --> TD
```

## Performance et Scalabilité

### Load Balancing Strategy

```mermaid
graph TB
    subgraph "Load Balancing"
        LB[Traefik Load Balancer]
        
        subgraph "Service Instances"
            AS1[Admin Service 1]
            AS2[Admin Service 2]
            AES1[Auto-École Service 1]
            AES2[Auto-École Service 2]
        end
        
        subgraph "Database"
            DB[(PostgreSQL<br/>Primary)]
            DBR[(PostgreSQL<br/>Read Replica)]
        end
    end
    
    LB --> AS1
    LB --> AS2
    LB --> AES1
    LB --> AES2
    
    AS1 --> DB
    AS2 --> DB
    AES1 --> DBR
    AES2 --> DBR
```

---

**Cette architecture garantit une scalabilité, une maintenabilité et une performance optimales pour le système R-DGTT Portail.**

