# R-DGTT Portail - Système de Gestion des Transports Terrestres

## 🎯 **Système de Gestion Gouvernementale avec Configuration Métier**

Système complet de gestion des transports terrestres pour le Ministère des Transports du Gabon, permettant aux **administrateurs système de gérer facilement tous les processus métier sans modification du code backend**.

## 🏗️ **Architecture du Système**

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Frontend      │    │   Traefik       │    │   Consul        │
│   (React)       │◄───┤   (API Gateway) │◄───┤   (Discovery)   │
│   Port 3000     │    │   Port 80       │    │   Port 8500     │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                                │
                ┌───────────────┼───────────────┐
                │               │               │
        ┌───────▼───────┐ ┌─────▼─────┐ ┌──────▼──────┐
        │ Usager Service│ │Auto-École │ │Permis Service│
        │   Port 8081   │ │Port 8082  │ │  Port 8083  │
        └───────────────┘ └───────────┘ └─────────────┘
                │
        ┌───────▼───────┐
        │ Admin Service │
        │   Port 8085   │
        └───────────────┘
                │
        ┌───────▼───────┐
        │  PostgreSQL   │
        │   Port 5432   │
        └───────────────┘
```

## 🚀 **Services Inclus**

### **Core Services:**
- ✅ **PostgreSQL** - Base de données centralisée
- ✅ **Consul** - Découverte et configuration des services
- ✅ **Traefik** - API Gateway et routage
- ✅ **Usager Service** - Gestion des utilisateurs et authentification
- ✅ **Auto-École Service** - Gestion des auto-écoles et candidats
- ✅ **Permis Service** - Gestion des permis de conduire
- ✅ **Admin Service** - Configuration métier et workflows
- ✅ **Frontend** - Interface utilisateur React

### **Deployment Ready:**
- ✅ **Docker Compose** - Orchestration des services
- ✅ **Docker Secrets** - Gestion sécurisée des mots de passe
- ✅ **Hetzner Cloud** - Déploiement optimisé
- ✅ **Scripts de déploiement** - Installation automatique
- ✅ **Multi-stage Docker builds** - Optimisation des performances
- ✅ **Maven dependency caching** - Builds plus rapides

## 🎛️ **Système de Configuration Admin**

### **1. Gestion des Types de Documents**
L'administrateur peut créer et configurer tous les types de documents :

```json
{
  "nom": "Permis de Conduire",
  "code": "PERMIS_CONDUIRE",
  "service_code": "permis",
  "categorie": "principal",
  "delai_traitement_jours": 30,
  "frais_obligatoire": true,
  "montant_frais": 15000,
  "documents_requis": ["CNI", "Photo", "Certificat médical"],
  "conditions_eligibilite": {"age_min": 18, "permis_valide": true}
}
```

### **2. Gestion des Processus de Validation**
Configuration du circuit de validation :

```json
{
  "nom": "Validation par Chef de Service",
  "code": "VALIDATION_CHEF",
  "ordre": 3,
  "departement_id": "uuid-departement",
  "role_requis": "DC",
  "type_validation": "manuelle",
  "delai_max_jours": 3,
  "peut_rejeter": true,
  "peut_rediriger": false
}
```

### **3. Moteur de Workflow Métier**
Le système utilise un moteur de workflow configurable qui permet de :

#### **Configuration Dynamique des Processus:**
- ✅ **Types de Documents**: Création et modification sans redéploiement
- ✅ **Étapes de Validation**: Configuration des circuits d'approbation
- ✅ **Règles Métier**: Définition des conditions et critères
- ✅ **Statuts Personnalisés**: Création de nouveaux statuts selon les besoins
- ✅ **Transitions Automatiques**: Passage automatique entre étapes

#### **Exemple de Configuration Workflow:**
```json
{
  "workflow_name": "Permis de Conduire",
  "document_type": "PERMIS_CONDUIRE",
  "steps": [
    {
      "step_name": "Dépôt de la demande",
      "step_code": "DEPOT",
      "order": 1,
      "auto_approve": true,
      "next_step": "VERIFICATION_DOCUMENTS"
    },
    {
      "step_name": "Vérification des documents",
      "step_code": "VERIFICATION_DOCUMENTS",
      "order": 2,
      "required_role": "DC",
      "department": "Direction des Contrôles",
      "next_step": "VALIDATION_CHEF"
    },
    {
      "step_name": "Validation par le chef de service",
      "step_code": "VALIDATION_CHEF",
      "order": 3,
      "required_role": "CHEF_SERVICE",
      "next_step": "APPROBATION_FINALE"
    }
  ]
}
```

#### **Fonctionnalités Avancées:**
- 🔄 **Exécution automatique** des processus
- ⏰ **Gestion des délais** et escalations
- 📋 **Suivi de l'historique** complet
- 🔀 **Redirections et rejets** configurables
- 🔔 **Notifications automatiques** à chaque étape
- 🎯 **Règles conditionnelles** basées sur les données
- 📊 **Tableaux de bord** de suivi en temps réel

## 🛠️ **Installation et Démarrage**

### **Prérequis**
- Java 17+
- Maven 3.8+
- Node.js 18+
- PostgreSQL 13+
- Consul
- Traefik

### **1. Configuration de la Base de Données**

#### **Option A: Avec l'utilisateur par défaut (Recommandé)**
```bash
# Se connecter en tant que superutilisateur postgres
psql -U postgres

# Dans psql, exécuter:
CREATE DATABASE rdgtt_portail;
CREATE USER rdgtt_user WITH PASSWORD 'rdgtt_password_2025';
GRANT ALL PRIVILEGES ON DATABASE rdgtt_portail TO rdgtt_user;
\q

# Initialiser le schéma complet avec les données
psql -U postgres -d rdgtt_portail -f database/init.sql
```

#### **Option B: Avec l'utilisateur créé**
```bash
# Si vous avez déjà créé l'utilisateur rdgtt_user
psql -U rdgtt_user -d rdgtt_portail -f database/init.sql
```

#### **Vérification de l'installation:**
```bash
# Vérifier que les tables ont été créées
psql -U postgres -d rdgtt_portail -c "\dt"

# Vérifier les données d'exemple
psql -U postgres -d rdgtt_portail -c "SELECT COUNT(*) FROM users;"
psql -U postgres -d rdgtt_portail -c "SELECT COUNT(*) FROM document_types;"
psql -U postgres -d rdgtt_portail -c "SELECT COUNT(*) FROM payment_methods;"
```

### **2. Démarrage des Services**
```bash
# Terminal 1 - Consul
cd consul-config
.\consul.exe agent -config-file consul.json -dev

# Terminal 2 - Traefik
cd traefik-config
.\traefik.exe --configfile=traefik-simple.yml

# Terminal 3 - Usager Service
cd usager-service
mvn spring-boot:run

# Terminal 4 - Auto-École Service
cd auto-ecole-service
mvn spring-boot:run

# Terminal 5 - Permis Service
cd permis-service
mvn spring-boot:run

# Terminal 6 - Admin Service
cd admin-service
mvn spring-boot:run

# Terminal 7 - Frontend
cd frontend
npm start
```

## 🌐 **Accès aux Services**

- **R-DGTT Portail**: `http://localhost`
- **Traefik Dashboard**: `http://localhost/traefik` (admin/admin123)
- **Consul UI**: `http://localhost:8500`
- **Admin API**: `http://localhost/api/admin`

## 🔑 **Connexion par Défaut**

- **Email**: admin@rdgtt.ga
- **Mot de passe**: admin123
- **Rôle**: ADMIN

## 📋 **Fonctionnalités Principales**

### **Pour les Citoyens:**
- ✅ Inscription et connexion sécurisée
- ✅ Demande de permis de conduire
- ✅ Suivi en temps réel des demandes
- ✅ Gestion des documents
- ✅ Interface intuitive et responsive
- ✅ **Tableau de bord personnalisé** avec suivi des applications
- ✅ **Notifications in-app** pour les mises à jour
- ✅ **Paiement Mobile Money** (Airtel Money simulé)
- ✅ **Historique des paiements** et des transactions

### **Pour les Auto-Écoles:**
- ✅ Gestion des candidats
- ✅ Suivi des formations
- ✅ Gestion des documents
- ✅ Tableau de bord complet

### **Pour les Administrateurs:**
- ✅ **Configuration métier dynamique** sans redéploiement
- ✅ **Types de documents personnalisés** avec règles spécifiques
- ✅ **Processus de validation configurables** par type de document
- ✅ **Workflows adaptatifs** selon les besoins métier
- ✅ **Gestion des utilisateurs et rôles** avec permissions granulaires
- ✅ **Configuration des départements et bureaux** hiérarchiques
- ✅ **Monitoring des paiements** et transactions en temps réel
- ✅ **Gestion des notifications** système personnalisables
- ✅ **Statistiques détaillées** des utilisateurs et processus
- ✅ **Interface d'administration** intuitive et complète

## 🎯 **Avantages du Système**

### **1. Flexibilité Métier**
- ✅ Modification des processus sans code
- ✅ Ajout de nouveaux types de documents
- ✅ Configuration des délais et frais
- ✅ Gestion des rôles et permissions

### **2. Maintenance Simplifiée**
- ✅ Architecture microservices modulaire
- ✅ Configuration centralisée
- ✅ Déploiement indépendant des services
- ✅ Debugging facilité

### **3. Évolutivité**
- ✅ Ajout facile de nouveaux services
- ✅ Workflow engine réutilisable
- ✅ API standardisée
- ✅ Scalabilité horizontale

## 📚 **Structure du Projet**

```
r_dgtt/
├── admin-service/        # Configuration métier et workflows
├── auto-ecole-service/   # Gestion des auto-écoles
├── consul-config/        # Configuration Consul
├── database/             # Scripts de base de données
├── frontend/             # Interface React
├── permis-service/       # Gestion des permis
├── traefik-config/       # Configuration Traefik
└── usager-service/       # Gestion des utilisateurs
```

## 🔧 **Configuration Avancée**

### **Types de Documents Supportés:**
- Permis de Conduire
- Duplicata Permis
- Renouvellement Permis
- Carte Grise
- Duplicata Carte Grise
- Licence de Transport
- Attestation d'Authenticité

### **Processus de Validation:**
- Réception de la demande
- Vérification des documents
- Validation par les services
- Approbation finale
- Délivrance du document

### **Rôles Utilisateur:**
- **ADMIN** - Administration complète
- **DGTT** - Direction Générale
- **DC** - Direction des Contrôles
- **SEV** - Service des Examens
- **SAF** - Service des Affaires Financières
- **CITOYEN** - Utilisateur final

### **Système de Paiement Airtel Money:**
- ✅ **Simulation complète** pour les tests
- ✅ **Validation du numéro** gabonais (+241XXXXXXXX)
- ✅ **Gestion des erreurs** (solde insuffisant, PIN incorrect, etc.)
- ✅ **Notifications automatiques** de succès/échec
- ✅ **Historique des transactions** avec références
- ✅ **Interface utilisateur** intuitive
- ✅ **Prêt pour l'intégration** API Airtel réelle

## 🔧 **API Admin Service - Configuration Métier**

### **Endpoints Principaux:**

#### **Gestion des Types de Documents:**
```bash
# Créer un nouveau type de document
POST /api/admin/document-types
{
  "nom": "Permis de Conduire",
  "code": "PERMIS_CONDUIRE",
  "service_code": "permis",
  "categorie": "principal",
  "delai_traitement_jours": 30,
  "frais_obligatoire": true,
  "montant_frais": 15000
}

# Lister tous les types de documents
GET /api/admin/document-types

# Modifier un type de document
PUT /api/admin/document-types/{id}
```

#### **Gestion des Workflows:**
```bash
# Créer un workflow
POST /api/admin/workflows
{
  "nom": "Workflow Permis de Conduire",
  "document_type_id": "uuid",
  "etapes": [...]
}

# Activer/Désactiver un workflow
PUT /api/admin/workflows/{id}/status
{
  "actif": true
}
```

#### **Gestion des Statuts:**
```bash
# Créer un nouveau statut
POST /api/admin/application-statuses
{
  "nom": "En Attente de Paiement",
  "code": "EN_ATTENTE_PAIEMENT",
  "couleur": "#FFA500",
  "description": "Demande en attente de paiement"
}
```

## 🔐 **Sécurité - Docker Secrets**

### **Gestion Sécurisée des Mots de Passe**
Le système utilise Docker Secrets pour une gestion sécurisée des données sensibles:

```bash
# Créer les secrets
mkdir -p secrets
echo "rdgtt_password" > secrets/db_password.txt
echo "rdgtt_jwt_secret_2025" > secrets/jwt_secret.txt
echo "admin@rdgtt.ga" > secrets/acme_email.txt
chmod 600 secrets/*.txt

# Démarrer avec Docker Secrets
docker compose up -d
```

### **Avantages de Docker Secrets:**
- ✅ **Chiffrement au repos** - Les secrets sont chiffrés dans Docker
- ✅ **Contrôle d'accès** - Seuls les services autorisés y accèdent
- ✅ **Non visible dans les processus** - Plus sécurisé que les variables d'environnement
- ✅ **Non commité dans Git** - Le dossier `secrets/` est dans `.gitignore`

Voir le guide complet: [DOCKER_SECRETS_SETUP.md](DOCKER_SECRETS_SETUP.md)

## 🚀 **Déploiement en Production**

### **Déploiement sur Hetzner Cloud (Recommandé):**
- ✅ **Guide complet** - `DEPLOYMENT_HETZNER.md`
- ✅ **Déploiement manuel** - Étape par étape pour l'apprentissage
- ✅ **Docker Secrets** - Gestion sécurisée des mots de passe
- ✅ **Workflow de développement** - Mise à jour GitHub → Serveur
- ✅ **Optimisations Docker** - Multi-stage builds pour des déploiements rapides
- ✅ **Coût optimisé** - €3.29/mois pour 2GB RAM

### **Recommandations:**
- Utiliser HTTPS avec certificats SSL
- Configurer un reverse proxy (Nginx/Traefik)
- Mettre en place des sauvegardes automatiques
- Monitorer les performances et logs
- Utiliser des variables d'environnement pour la configuration
- Configurer la surveillance des services
- Implémenter la haute disponibilité

### **Sécurité:**
- Authentification JWT
- Chiffrement des données sensibles
- Validation des entrées utilisateur
- Audit des actions administratives
- Protection contre les attaques courantes

## 🎉 **Résultat**

Un système **professionnel**, **configurable** et **maintenable** qui permet aux administrateurs DGTT de gérer facilement tous les processus métier sans intervention technique ! 

**Développé pour le Ministère des Transports du Gabon** 🇬🇦✨

---

## 📞 **Support Technique**

Pour toute question ou support technique, contactez l'équipe de développement.

**Version**: 1.0.0  
**Dernière mise à jour**: Janvier 2025