# 📚 Documentation API R-DGTT

## Vue d'ensemble

L'API R-DGTT fournit des endpoints RESTful pour la gestion des workflows, des utilisateurs, et des processus métier.

## 🔗 Base URL

- **Développement**: `http://localhost:8081`
- **Production**: `https://rdgtt.ga/api`

## 🔐 Authentification

### Login

```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "dgtt@rdgtt.ga",
  "password": "admin123"
}
```

**Réponse**:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": "uuid",
    "nom": "Directeur",
    "prenom": "Général",
    "email": "dgtt@rdgtt.ga",
    "role": "DGTT",
    "departement": {
      "nom": "Direction Générale"
    }
  }
}
```

### Headers d'Authentification

```http
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

## 👥 Gestion des Utilisateurs

### Lister les Utilisateurs

```http
GET /api/admin/users
Authorization: Bearer {token}
```

**Réponse**:
```json
[
  {
    "id": "uuid",
    "nom": "Directeur",
    "prenom": "Général",
    "email": "dgtt@rdgtt.ga",
    "role": "DGTT",
    "departement": {
      "nom": "Direction Générale"
    },
    "bureau": {
      "nom": "Bureau Directeur"
    },
    "actif": true
  }
]
```

### Créer un Utilisateur

```http
POST /api/admin/users
Authorization: Bearer {token}
Content-Type: application/json

{
  "nom": "Dupont",
  "prenom": "Jean",
  "email": "jean.dupont@rdgtt.ga",
  "password": "password123",
  "role": "AGENT",
  "departementId": "uuid",
  "bureauId": "uuid"
}
```

### Mettre à Jour un Utilisateur

```http
PUT /api/admin/users/{id}
Authorization: Bearer {token}
Content-Type: application/json

{
  "nom": "Dupont",
  "prenom": "Jean",
  "email": "jean.dupont@rdgtt.ga",
  "role": "CHEF_BUREAU"
}
```

## 🏢 Gestion des Départements

### Lister les Départements

```http
GET /api/admin/departments
```

**Réponse**:
```json
[
  {
    "id": "uuid",
    "nom": "Direction Générale",
    "code": "DGTT",
    "description": "Direction Générale des Transports Terrestres",
    "chefDepartement": {
      "nom": "Directeur",
      "prenom": "Général"
    },
    "actif": true
  }
]
```

### Créer un Département

```http
POST /api/admin/departments
Authorization: Bearer {token}
Content-Type: application/json

{
  "nom": "Service des Examens",
  "code": "SEV",
  "description": "Service chargé des examens et vérifications",
  "chefDepartementId": "uuid"
}
```

## 🏛️ Gestion des Bureaux

### Lister les Bureaux

```http
GET /api/admin/bureaus
```

### Lister les Bureaux par Département

```http
GET /api/admin/bureaus/department/{departmentId}
```

### Créer un Bureau

```http
POST /api/admin/bureaus
Authorization: Bearer {token}
Content-Type: application/json

{
  "nom": "Bureau des Permis",
  "code": "BP",
  "description": "Bureau de délivrance des permis",
  "departementId": "uuid",
  "chefBureauId": "uuid"
}
```

## 🔄 Gestion des Workflows

### Démarrer un Workflow Auto-École

```http
POST /api/workflow/auto-ecole/start
Authorization: Bearer {token}
Content-Type: application/json

{
  "autoEcoleName": "Auto-École Excellence",
  "demandeId": "AE-2024-001",
  "userId": "uuid"
}
```

### Démarrer un Workflow Candidat

```http
POST /api/workflow/candidate/start
Authorization: Bearer {token}
Content-Type: application/json

{
  "candidateName": "Jean Dupont",
  "autoEcoleId": "uuid",
  "demandeId": "CAND-2024-001",
  "userId": "uuid"
}
```

### Démarrer un Workflow Permis

```http
POST /api/workflow/permis/start
Authorization: Bearer {token}
Content-Type: application/json

{
  "candidateId": "uuid",
  "demandeId": "PERMIS-2024-001",
  "userId": "uuid"
}
```

### Démarrer un Workflow Connexe

```http
POST /api/workflow/connexe/start
Authorization: Bearer {token}
Content-Type: application/json

{
  "connexeType": "DUPLICATA_PERMIS",
  "permisId": "uuid",
  "demandeId": "DUP-2024-001",
  "userId": "uuid"
}
```

### Exécuter une Étape

```http
POST /api/workflow/execute
Authorization: Bearer {token}
Content-Type: application/json

{
  "workflowId": "uuid",
  "stepId": "uuid",
  "decision": "APPROUVE",
  "commentaires": "Documents conformes",
  "userId": "uuid"
}
```

**Décisions possibles**:
- `APPROUVE` - Approuver l'étape
- `REJETTE` - Rejeter l'étape
- `REDIRIGER` - Rediriger vers une autre étape

### Consulter le Statut d'un Workflow

```http
GET /api/workflow/instance/{demandeNumber}
```

**Réponse**:
```json
{
  "id": "uuid",
  "demandeId": "PERMIS-2024-001",
  "statut": "EN_COURS",
  "dateDebut": "2024-01-15T10:30:00",
  "dateFin": null,
  "documentType": {
    "nom": "Permis de Conduire",
    "code": "PERMIS_CONDUIRE"
  },
  "etapeActuelle": {
    "nom": "Vérification des documents",
    "code": "VERIFICATION_DOCS"
  },
  "utilisateurActuel": {
    "nom": "Dupont",
    "prenom": "Jean"
  }
}
```

### Consulter l'Historique d'un Workflow

```http
GET /api/workflow/history/{workflowId}
Authorization: Bearer {token}
```

**Réponse**:
```json
[
  {
    "id": "uuid",
    "nom": "Réception de la demande",
    "statut": "TERMINE",
    "dateDebut": "2024-01-15T10:30:00",
    "dateFin": "2024-01-15T10:35:00",
    "utilisateur": {
      "nom": "Dupont",
      "prenom": "Jean"
    },
    "commentaires": "Demande reçue et enregistrée",
    "decision": "APPROUVE"
  }
]
```

## 📊 Types de Documents

### Lister les Types de Documents

```http
GET /api/workflow/document-types
```

### Lister par Service

```http
GET /api/workflow/document-types/service/{serviceCode}
```

### Lister par Catégorie

```http
GET /api/workflow/document-types/category/{category}
```

### Consulter les Étapes d'un Type

```http
GET /api/workflow/document-types/{documentTypeCode}/steps
```

## 📈 Statistiques

### Statistiques des Workflows

```http
GET /api/workflow/statistics
Authorization: Bearer {token}
```

**Réponse**:
```json
{
  "total": 150,
  "enCours": 45,
  "termines": 100,
  "rejetes": 5
}
```

### Workflows par Utilisateur

```http
GET /api/workflow/user/{userId}/workflows
Authorization: Bearer {token}
```

### Workflows par Département

```http
GET /api/workflow/department/{departmentId}/workflows
Authorization: Bearer {token}
```

### Workflows par Bureau

```http
GET /api/workflow/bureau/{bureauId}/workflows
Authorization: Bearer {token}
```

## 👥 Portail Citoyen

### Consulter le Statut d'une Demande

```http
GET /api/citizen/status/{demandeNumber}
```

**Réponse**:
```json
{
  "numero_demande": "PERMIS-2024-001",
  "statut": "EN_COURS",
  "date_depot": "2024-01-15T10:30:00",
  "date_traitement": null,
  "document_type": {
    "nom": "Permis de Conduire",
    "code": "PERMIS_CONDUIRE"
  },
  "delai_estime_jours": 30,
  "commentaires": "Demande en cours de traitement"
}
```

### Consulter l'Historique d'une Demande

```http
GET /api/citizen/history/{demandeNumber}
```

**Réponse**:
```json
{
  "numero_demande": "PERMIS-2024-001",
  "statut_actuel": "EN_COURS",
  "historique": [
    {
      "etape": "Réception de la demande",
      "statut": "TERMINE",
      "date": "2024-01-15T10:30:00",
      "commentaire": "Demande reçue et enregistrée"
    },
    {
      "etape": "Vérification des documents",
      "statut": "EN_COURS",
      "date": "2024-01-15T10:35:00",
      "commentaire": "En cours de vérification"
    }
  ]
}
```

## 🚨 Gestion des Erreurs

### Codes d'Erreur HTTP

| Code | Description |
|------|-------------|
| 200 | Succès |
| 201 | Créé avec succès |
| 400 | Requête invalide |
| 401 | Non authentifié |
| 403 | Non autorisé |
| 404 | Ressource non trouvée |
| 500 | Erreur serveur |

### Format des Erreurs

```json
{
  "error": "Validation Error",
  "message": "Le champ 'email' est obligatoire",
  "timestamp": "2024-01-15T10:30:00Z",
  "path": "/api/admin/users"
}
```

## 📝 Exemples d'Utilisation

### Workflow Complet: Auto-École → Candidat → Permis

```bash
# 1. Démarrer l'inscription Auto-École
curl -X POST http://localhost:8081/api/workflow/auto-ecole/start \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "autoEcoleName": "Auto-École Excellence",
    "demandeId": "AE-2024-001",
    "userId": "uuid"
  }'

# 2. Exécuter les étapes Auto-École
curl -X POST http://localhost:8081/api/workflow/execute \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "workflowId": "uuid",
    "stepId": "uuid",
    "decision": "APPROUVE",
    "commentaires": "Auto-École validée",
    "userId": "uuid"
  }'

# 3. Démarrer l'inscription Candidat
curl -X POST http://localhost:8081/api/workflow/candidate/start \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "candidateName": "Jean Dupont",
    "autoEcoleId": "uuid",
    "demandeId": "CAND-2024-001",
    "userId": "uuid"
  }'

# 4. Démarrer le processus Permis
curl -X POST http://localhost:8081/api/workflow/permis/start \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "candidateId": "uuid",
    "demandeId": "PERMIS-2024-001",
    "userId": "uuid"
  }'

# 5. Vérifier le statut (Citoyen)
curl http://localhost:8081/api/citizen/status/PERMIS-2024-001
```

## 🔧 Configuration

### Variables d'Environnement

```bash
# Base de données
SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/rdgtt_portail
SPRING_DATASOURCE_USERNAME=rdgtt_user
SPRING_DATASOURCE_PASSWORD=rdgtt_password_2025

# JWT
JWT_SECRET=your-secret-key
JWT_EXPIRATION=86400000

# Consul
CONSUL_HOST=consul
CONSUL_PORT=8500
```

### Configuration CORS

```java
@CrossOrigin(origins = "*")
@RestController
public class ApiController {
    // Controllers
}
```

## 📊 Monitoring

### Health Checks

```http
GET /actuator/health
```

**Réponse**:
```json
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP"
    },
    "diskSpace": {
      "status": "UP"
    }
  }
}
```

### Métriques

```http
GET /actuator/metrics
```

### Info de l'Application

```http
GET /actuator/info
```

---

**Cette documentation API assure une intégration efficace avec le système R-DGTT.**

