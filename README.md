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
- ✅ **Hetzner Cloud** - Déploiement optimisé
- ✅ **Scripts de déploiement** - Installation automatique

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

### **3. Moteur de Workflow**
- ✅ Exécution automatique des processus
- ✅ Gestion des délais et escalations
- ✅ Suivi de l'historique complet
- ✅ Redirections et rejets configurables
- ✅ Notifications automatiques

## 🛠️ **Installation et Démarrage**

### **Prérequis**
- Java 17+
- Maven 3.8+
- Node.js 18+
- PostgreSQL 13+
- Consul
- Traefik

### **1. Configuration de la Base de Données**
```bash
# Créer la base de données
psql -U postgres -c "CREATE DATABASE rdgtt_portail;"
psql -U postgres -c "CREATE USER rdgtt_user WITH PASSWORD 'rdgtt_password';"
psql -U postgres -c "GRANT ALL PRIVILEGES ON DATABASE rdgtt_portail TO rdgtt_user;"

# Initialiser le schéma complet
psql -U rdgtt_user -d rdgtt_portail -f database/init.sql
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

### **Pour les Auto-Écoles:**
- ✅ Gestion des candidats
- ✅ Suivi des formations
- ✅ Gestion des documents
- ✅ Tableau de bord complet

### **Pour les Administrateurs:**
- ✅ Configuration des types de documents
- ✅ Gestion des processus de validation
- ✅ Suivi des workflows
- ✅ Gestion des utilisateurs et rôles
- ✅ Configuration des départements et bureaux

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

## 🚀 **Déploiement en Production**

### **Recommandations:**
- Utiliser HTTPS avec certificats SSL
- Configurer un reverse proxy (Nginx)
- Mettre en place la sauvegarde automatique
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