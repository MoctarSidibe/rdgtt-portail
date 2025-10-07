# 🗄️ Base de Données R-DGTT

## Structure des Fichiers

```
database/
├── init.sql                    # Schéma principal et données initiales
├── complete-workflow-metier.sql # Workflows métier complets
└── README.md                   # Documentation de la base de données
```

## 🚀 Installation

### 1. Création de la Base de Données

```bash
# Se connecter en tant que superutilisateur postgres
psql -U postgres

# Dans psql, exécuter:
CREATE DATABASE rdgtt_portail;
CREATE USER rdgtt_user WITH PASSWORD 'rdgtt_password_2025';
GRANT ALL PRIVILEGES ON DATABASE rdgtt_portail TO rdgtt_user;
\q
```

### 2. Initialisation du Schéma

```bash
# Appliquer le schéma principal
psql -U postgres -d rdgtt_portail -f database/init.sql

# Appliquer les workflows métier
psql -U postgres -d rdgtt_portail -f complete-workflow-metier.sql
```

### 3. Vérification

```bash
# Vérifier les tables créées
psql -U postgres -d rdgtt_portail -c "\dt"

# Vérifier les données d'exemple
psql -U postgres -d rdgtt_portail -c "SELECT * FROM admin_users;"
```

## 📊 Schéma de la Base de Données

### Tables Principales

- **admin_users** - Utilisateurs administratifs
- **departments** - Départements
- **bureaus** - Bureaux
- **document_types** - Types de documents
- **process_steps** - Étapes de processus
- **workflow_instances** - Instances de workflow
- **workflow_step_executions** - Exécutions d'étapes

### Tables Auto-École

- **auto_ecoles** - Auto-écoles
- **candidats** - Candidats
- **examens** - Examens
- **auto_ecole_documents** - Documents auto-école
- **candidat_documents** - Documents candidat

### Tables Permis

- **permis** - Permis de conduire

## 🔧 Maintenance

### Sauvegarde

```bash
# Sauvegarde complète
pg_dump -U postgres rdgtt_portail > backup_$(date +%Y%m%d_%H%M%S).sql

# Sauvegarde des données uniquement
pg_dump -U postgres -a rdgtt_portail > data_backup_$(date +%Y%m%d_%H%M%S).sql
```

### Restauration

```bash
# Restaurer depuis une sauvegarde
psql -U postgres rdgtt_portail < backup_20240101_120000.sql
```

## 📈 Performance

### Index Recommandés

```sql
-- Index sur les colonnes fréquemment utilisées
CREATE INDEX idx_workflow_instances_demande ON workflow_instances(demande_id);
CREATE INDEX idx_workflow_instances_statut ON workflow_instances(statut);
CREATE INDEX idx_admin_users_email ON admin_users(email);
CREATE INDEX idx_admin_users_role ON admin_users(role);
```

### Monitoring

```sql
-- Vérifier les performances
SELECT schemaname, tablename, attname, n_distinct, correlation 
FROM pg_stats 
WHERE schemaname = 'public' 
ORDER BY tablename, attname;

-- Vérifier les index utilisés
SELECT schemaname, tablename, indexname, idx_scan, idx_tup_read, idx_tup_fetch 
FROM pg_stat_user_indexes 
ORDER BY idx_scan DESC;
```

## 🔒 Sécurité

### Utilisateurs et Permissions

```sql
-- Créer un utilisateur en lecture seule pour les rapports
CREATE USER rdgtt_readonly WITH PASSWORD 'readonly_password';
GRANT CONNECT ON DATABASE rdgtt_portail TO rdgtt_readonly;
GRANT USAGE ON SCHEMA public TO rdgtt_readonly;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO rdgtt_readonly;
```

### Audit

```sql
-- Activer l'audit des connexions
ALTER SYSTEM SET log_connections = on;
ALTER SYSTEM SET log_disconnections = on;
SELECT pg_reload_conf();
```

## 📋 Données de Test

### Utilisateurs par Défaut

| Rôle | Email | Mot de passe | Permissions |
|------|-------|--------------|-------------|
| DGTT | dgtt@rdgtt.ga | admin123 | Contrôle total |
| CHEF_SERVICE | admin@rdgtt.ga | admin123 | Gestion workflows |

### Workflows de Test

| Type | Numéro | Description |
|------|--------|-------------|
| Auto-École | AE-2024-001 | Inscription Auto-École Excellence |
| Candidat | CAND-2024-001 | Inscription candidat Jean Dupont |
| Permis | PERMIS-2024-001 | Demande permis candidat Jean Dupont |
| Duplicata | DUP-2024-001 | Demande duplicata permis perdu |

## 🚨 Dépannage

### Problèmes Courants

1. **Erreur de connexion**
   ```bash
   # Vérifier que PostgreSQL est démarré
   sudo systemctl status postgresql
   
   # Vérifier les permissions
   psql -U postgres -c "\du"
   ```

2. **Tables manquantes**
   ```bash
   # Réappliquer le schéma
   psql -U postgres -d rdgtt_portail -f database/init.sql
   ```

3. **Données corrompues**
   ```bash
   # Vérifier l'intégrité
   psql -U postgres -d rdgtt_portail -c "VACUUM ANALYZE;"
   ```

### Logs

```bash
# Consulter les logs PostgreSQL
sudo tail -f /var/log/postgresql/postgresql-13-main.log

# Logs d'erreur
sudo grep ERROR /var/log/postgresql/postgresql-13-main.log
```

---

**Cette documentation assure une gestion efficace de la base de données R-DGTT.**

