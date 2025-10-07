# 📝 Changelog R-DGTT Portail

Toutes les modifications notables de ce projet seront documentées dans ce fichier.

Le format est basé sur [Keep a Changelog](https://keepachangelog.com/fr/1.0.0/),
et ce projet adhère au [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [2.0.0] - 2024-01-15

### 🎯 Version Majeure - Refonte Complète

#### ✨ Ajouté
- **Architecture Hybride**: Système admin + portail citoyen en lecture seule
- **Moteur de Workflow Configurable**: Gestion dynamique des processus métier
- **Hiérarchie des Rôles**: 5 niveaux (DGTT → DIRECTEUR → CHEF_SERVICE → CHEF_BUREAU → AGENT)
- **Workflows Métier Complets**:
  - Inscription Auto-École (15 jours, 5 étapes)
  - Inscription Candidat (7 jours, 4 étapes)
  - Permis de Conduire (30 jours, 6 étapes)
  - Services Connexes (Duplicata, Renouvellement, Conversion, Attestation)
- **Portail Citoyen**: Consultation du statut des demandes
- **Documentation Complète**:
  - Architecture avec diagrammes Mermaid
  - Guide des workflows
  - Documentation API
  - Guide de déploiement
- **Monitoring Avancé**: Health checks, métriques, tableaux de bord

#### 🔄 Modifié
- **Suppression des Paiements**: Retrait complet du système de paiement
- **Refonte de l'Interface**: Design moderne avec couleurs Gabon
- **Restructuration des Services**: Optimisation de l'architecture microservices
- **Base de Données**: Nouveau schéma optimisé pour les workflows

#### 🗑️ Supprimé
- **Service Usager**: Remplacé par le portail citoyen
- **Fonctionnalités de Paiement**: Suppression complète
- **Interface Candidat**: Remplacée par le portail citoyen
- **Fichiers Obsolètes**: Nettoyage complet du projet

#### 🔧 Technique
- **Docker Compose**: Configuration optimisée
- **Base de Données**: PostgreSQL avec schéma hybride
- **API REST**: Endpoints complets pour tous les workflows
- **Sécurité**: JWT avec rôles et permissions
- **Service Discovery**: Consul pour la gestion des services

## [1.5.0] - 2024-01-10

### 🔧 Corrections et Améliorations

#### ✨ Ajouté
- **Correction du Schéma Auto-École**: Ajout des colonnes manquantes
- **Gestion des Inspections**: Table et colonnes pour les inspections
- **Gestion des Paiements**: Table et colonnes pour les paiements
- **Configuration Frontend**: Mise à jour nginx.conf

#### 🔄 Modifié
- **Types de Colonnes**: Correction UUID vs BIGINT
- **Configuration Services**: Optimisation des connexions
- **Interface Utilisateur**: Amélioration de l'expérience

#### 🐛 Corrigé
- **Erreurs de Schéma**: Validation Hibernate
- **Connexions DNS**: Résolution des problèmes de service discovery
- **Chargement d'Images**: Correction des 404 sur les assets

## [1.0.0] - 2024-01-01

### 🎉 Version Initiale

#### ✨ Ajouté
- **Architecture Microservices**: Admin, Auto-École, Permis, Usager
- **Base de Données**: PostgreSQL avec schéma initial
- **Interface Web**: React avec Material-UI
- **Authentification**: JWT avec Spring Security
- **Service Discovery**: Consul
- **Load Balancer**: Traefik
- **Déploiement**: Docker Compose

#### 🔧 Technique
- **Backend**: Spring Boot avec Hibernate
- **Frontend**: React avec Material-UI
- **Base de Données**: PostgreSQL 15
- **Conteneurisation**: Docker & Docker Compose
- **Monitoring**: Health checks et métriques

---

## 📊 Statistiques des Versions

| Version | Date | Fonctionnalités | Corrections | Breaking Changes |
|---------|------|-----------------|-------------|------------------|
| 2.0.0 | 2024-01-15 | 15+ | 0 | 3 |
| 1.5.0 | 2024-01-10 | 5 | 8 | 0 |
| 1.0.0 | 2024-01-01 | 20+ | 0 | 0 |

## 🚀 Prochaines Versions

### [2.1.0] - Planifié
- **Notifications Push**: Système de notifications en temps réel
- **Rapports Avancés**: Tableaux de bord avec métriques détaillées
- **API Mobile**: Endpoints optimisés pour mobile
- **Intégration SMS**: Notifications SMS pour les citoyens

### [2.2.0] - Planifié
- **Workflow Builder**: Interface graphique pour créer des workflows
- **Audit Trail**: Traçabilité complète des actions
- **Backup Automatique**: Sauvegarde automatique de la base de données
- **Multi-tenant**: Support multi-organisations

---

**Ce changelog documente l'évolution complète du système R-DGTT Portail.**
