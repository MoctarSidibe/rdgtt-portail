# R-DGTT Portail - Déploiement sur Hetzner Cloud

## 🎯 **Déploiement Optimisé pour Hetzner Cloud**

Guide complet et mis à jour pour déployer R-DGTT Portail sur Hetzner Cloud avec configuration optimisée pour la production.

## 🏗️ **Pourquoi Hetzner est Parfait pour R-DGTT Portail**

- ✅ **Très fiable** - 99.9% uptime
- ✅ **Très bon marché** - €3.29/mois pour 2GB RAM
- ✅ **Cartes prépayées acceptées**
- ✅ **Serveurs européens** (Frankfurt, Nuremberg)
- ✅ **Performance excellente**
- ✅ **Support en français**

## 📋 **Déploiement Manuel Étape par Étape**

### **Approche Choisie: Déploiement Manuel**
- ✅ **Apprentissage complet** - Comprendre chaque étape
- ✅ **Contrôle total** - Voir exactement ce qui se passe
- ✅ **Débogage facile** - Identifier les problèmes rapidement
- ✅ **Pas de scripts cachés** - Transparence totale

## 🚀 **Étape 1: Création du Serveur sur Hetzner**

### **1.1 Créer un Compte Hetzner**
1. Aller sur [https://console.hetzner.cloud/](https://console.hetzner.cloud/)
2. Cliquer "Sign up" et créer votre compte
3. Vérifier votre email
4. Ajouter une méthode de paiement (carte prépayée acceptée)

### **1.2 Créer un Projet**
1. Se connecter à la console Hetzner
2. Cliquer "New Project"
3. **Nom du projet**: `R-DGTT Portail`
4. **Description**: `Serveur pour le portail R-DGTT du Gabon`
5. Cliquer "Create Project"

### **1.3 Créer une Clé SSH (Important!)**
1. **Sur votre machine Windows**, ouvrir PowerShell
2. Exécuter cette commande pour créer une clé SSH:
```powershell
ssh-keygen -t rsa -b 4096 -C "votre-email@example.com"
```
3. Appuyer sur **Entrée** pour accepter l'emplacement par défaut
4. **Optionnel**: Entrer une passphrase ou appuyer sur **Entrée** pour aucune
5. La clé sera créée dans `C:\Users\user\.ssh\id_rsa.pub`

### **1.4 Ajouter la Clé SSH à Hetzner**
1. Dans la console Hetzner, aller dans "Security" → "SSH Keys"
2. Cliquer "Add SSH Key"
3. **Nom**: `Ma clé SSH Windows`
4. **Clé publique**: Copier le contenu de `C:\Users\user\.ssh\id_rsa.pub`
5. Cliquer "Add SSH Key"

### **1.5 Créer le Serveur**
1. Dans votre projet, cliquer "Add Server"
2. **Nom**: `rdgtt-portail`
3. **Image**: Ubuntu 22.04
4. **Type**: CX11 (1 vCPU, 2GB RAM) - €3.29/mois
5. **Localisation**: Frankfurt (fsn1) ou Nuremberg (nbg1)
6. **SSH Key**: Sélectionner "Ma clé SSH Windows"
7. **Réseau**: IPv4 + IPv6
8. **Backup**: Désactivé (pour économiser)
9. Cliquer "Create & Buy Now"

### **1.6 Récupérer les Informations du Serveur**
1. Attendre que le serveur soit créé (2-3 minutes)
2. **Noter l'IP publique** (ex: 95.216.123.45)
3. **Noter l'IP privée** (ex: 10.0.0.2)
4. Le serveur sera dans l'état "Running"

### **Via CLI (Optionnel):**
```bash
# Installer hcloud CLI
curl -fsSL https://github.com/hetznercloud/cli/releases/latest/download/hcloud-linux-amd64.tar.gz | tar -xz
sudo mv hcloud /usr/local/bin/

# Configurer l'API token
hcloud context create rdgtt
hcloud context use rdgtt

# Créer le serveur
hcloud server create \
  --name rdgtt-portail \
  --type cx11 \
  --image ubuntu-22.04 \
  --location fsn1 \
  --ssh-key your-ssh-key
```

## 🚀 **Étape 2: Connexion au Serveur**

### **2.1 Tester la Connexion SSH**
1. **Sur votre machine Windows**, ouvrir PowerShell
2. Tester la connexion SSH (remplacer `VOTRE_IP` par l'IP de votre serveur):
```powershell
ssh root@VOTRE_IP
```
3. Si c'est la première connexion, accepter la clé en tapant `yes`
4. Vous devriez voir le prompt: `root@rdgtt-portail:~#`

### **2.2 Si la Connexion SSH Échoue**
1. **Vérifier l'IP** dans la console Hetzner
2. **Vérifier que la clé SSH** est bien ajoutée
3. **Essayer avec la console web** de Hetzner:
   - Dans la console Hetzner, cliquer sur votre serveur
   - Cliquer "Console" → "Launch Console"
   - Se connecter avec `root` (pas de mot de passe)

## 🚀 **Étape 3: Configuration du Serveur (Manuel)**

### **3.1 Mise à Jour du Système**
```bash
# Mettre à jour la liste des paquets
apt update

# Mettre à jour tous les paquets installés
apt upgrade -y

# Installer des outils utiles
apt install curl wget git nano htop unzip -y
```

### **3.2 Installation de Docker (Étape par Étape)**
```bash
# Étape 1: Supprimer les anciennes versions
apt remove docker docker-engine docker.io containerd runc -y

# Étape 2: Installer les prérequis
apt install ca-certificates curl gnupg lsb-release -y

# Étape 3: Ajouter la clé GPG officielle de Docker
mkdir -p /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | gpg --dearmor -o /etc/apt/keyrings/docker.gpg

# Étape 4: Ajouter le repository Docker
echo "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" | tee /etc/apt/sources.list.d/docker.list > /dev/null

# Étape 5: Mettre à jour la liste des paquets
apt update

# Étape 6: Installer Docker
apt install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin -y

# Étape 7: Vérifier l'installation
docker --version
```

### **3.3 Installation de Docker Compose**
```bash
# Télécharger Docker Compose
curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose

# Rendre le fichier exécutable
chmod +x /usr/local/bin/docker-compose

# Vérifier l'installation
docker-compose --version
```

### **3.4 Configuration du Firewall**
```bash
# Activer le firewall
ufw --force enable

# Autoriser SSH (important!)
ufw allow 22/tcp

# Autoriser HTTP
ufw allow 80/tcp

# Autoriser HTTPS
ufw allow 443/tcp

# Autoriser Traefik Dashboard
ufw allow 8080/tcp

# Autoriser Consul UI
ufw allow 8500/tcp

# Vérifier le statut du firewall
ufw status
```

### **3.5 Vérification de l'Installation**
```bash
# Vérifier Docker
docker --version
docker-compose --version

# Tester Docker avec un conteneur simple
docker run hello-world

# Vérifier l'espace disque
df -h

# Vérifier la mémoire
free -h
```

## 🚀 **Étape 4: Workflow de Développement - Mise à Jour GitHub et Serveur**

### **4.1 Workflow de Développement Complet**

#### **A. Développement Local (Sur votre Machine Windows)**
```bash
# 1. Aller dans le dossier du projet
cd C:\Users\user\OneDrive\Documents\r_dgtt

# 2. Vérifier le statut Git
git status

# 3. Ajouter tous les fichiers modifiés
git add .

# 4. Créer un commit avec un message descriptif
git commit -m "feat: Description de vos modifications

- Détail 1 de vos changements
- Détail 2 de vos changements
- Détail 3 de vos changements"

# 5. Pousser vers GitHub
git push origin main

# 6. Vérifier que le push a réussi
git log --oneline -5
```

#### **B. Déploiement sur le Serveur (Sur Hetzner)**
```bash
# 1. Se connecter au serveur
ssh root@168.119.123.247

# 2. Aller dans le dossier du projet
cd rdgtt-portail

# 3. Récupérer les dernières modifications depuis GitHub
git pull origin main

# 4. Vérifier que les modifications ont été récupérées
git log --oneline -3

# 5. Arrêter les services pour la mise à jour
docker compose down

# 6. Reconstruire les images avec les nouvelles modifications
docker compose build

# 7. Redémarrer tous les services
docker compose up -d

# 8. Vérifier que tous les services sont en cours d'exécution
docker compose ps

# 9. Vérifier les logs pour s'assurer qu'il n'y a pas d'erreurs
docker compose logs --tail=20
```

### **4.2 Workflow de Développement Quotidien**

#### **Scénario 1: Modification du Code Backend (Java)**
```bash
# Sur votre machine Windows:
# 1. Modifier le code Java dans les services
# 2. Tester localement si possible
# 3. Commit et push vers GitHub
git add .
git commit -m "fix: Correction du service usager"
git push origin main

# Sur le serveur Hetzner:
# 1. Récupérer les modifications
git pull origin main

# 2. Redémarrer seulement le service modifié
docker compose restart usager-service

# 3. Vérifier les logs
docker compose logs usager-service
```

#### **Scénario 2: Modification du Frontend (React)**
```bash
# Sur votre machine Windows:
# 1. Modifier le code React
# 2. Commit et push vers GitHub
git add .
git commit -m "feat: Nouvelle interface utilisateur"
git push origin main

# Sur le serveur Hetzner:
# 1. Récupérer les modifications
git pull origin main

# 2. Reconstruire et redémarrer le frontend
docker compose build frontend
docker compose up -d frontend

# 3. Vérifier que le frontend fonctionne
curl -I http://localhost
```

#### **Scénario 3: Modification de la Base de Données**
```bash
# Sur votre machine Windows:
# 1. Modifier database/init.sql
# 2. Commit et push vers GitHub
git add database/init.sql
git commit -m "feat: Nouvelle table utilisateurs"
git push origin main

# Sur le serveur Hetzner:
# 1. Récupérer les modifications
git pull origin main

# 2. Arrêter tous les services
docker compose down

# 3. Supprimer le volume de la base de données (ATTENTION: perte de données!)
docker volume rm rdgtt-portail_postgres_data

# 4. Redémarrer avec la nouvelle base de données
docker compose up -d

# 5. Vérifier que la base de données est initialisée
docker compose exec postgres psql -U rdgtt_user -d rdgtt_portail -c "SELECT COUNT(*) FROM users;"
```

### **4.3 Commandes de Maintenance Quotidienne**

#### **Vérification de l'État du Système**
```bash
# Sur le serveur Hetzner:
# 1. Vérifier le statut de tous les services
docker compose ps

# 2. Vérifier l'utilisation des ressources
htop

# 3. Vérifier l'espace disque
df -h

# 4. Vérifier les logs récents
docker compose logs --tail=50

# 5. Vérifier la connectivité des services
curl -I http://localhost:8081/actuator/health  # Usager Service
curl -I http://localhost:8082/actuator/health  # Auto-École Service
curl -I http://localhost:8083/actuator/health  # Permis Service
curl -I http://localhost:8085/actuator/health  # Admin Service
```

#### **Sauvegarde de la Base de Données**
```bash
# Sur le serveur Hetzner:
# 1. Créer une sauvegarde
docker compose exec postgres pg_dump -U rdgtt_user rdgtt_portail > backup-$(date +%Y%m%d-%H%M).sql

# 2. Vérifier la taille de la sauvegarde
ls -lh backup-*.sql

# 3. Tester la restauration (optionnel)
docker compose exec -T postgres psql -U rdgtt_user rdgtt_portail < backup-20250120-1200.sql
```

### **4.4 Gestion des Erreurs Courantes**

#### **Service ne démarre pas**
```bash
# 1. Vérifier les logs du service
docker compose logs nom-du-service

# 2. Vérifier la configuration
docker compose config

# 3. Redémarrer le service
docker compose restart nom-du-service

# 4. Si nécessaire, reconstruire l'image
docker compose build nom-du-service
docker compose up -d nom-du-service
```

#### **Problème de base de données**
```bash
# 1. Vérifier que PostgreSQL fonctionne
docker compose ps postgres

# 2. Vérifier les logs PostgreSQL
docker compose logs postgres

# 3. Tester la connexion
docker compose exec postgres psql -U rdgtt_user -d rdgtt_portail -c "SELECT 1;"

# 4. Redémarrer PostgreSQL si nécessaire
docker compose restart postgres
```

#### **Problème de mémoire**
```bash
# 1. Vérifier l'utilisation de la mémoire
free -h

# 2. Vérifier les processus qui consomment le plus
htop

# 3. Créer un fichier swap si nécessaire
fallocate -l 2G /swapfile
chmod 600 /swapfile
mkswap /swapfile
swapon /swapfile
```

### **4.5 Vérification sur GitHub**
1. Aller sur [https://github.com/MoctarSidibe/rdgtt-portail](https://github.com/MoctarSidibe/rdgtt-portail)
2. Vérifier que le dernier commit est visible
3. Vérifier que tous les nouveaux fichiers sont présents
4. Utiliser l'historique des commits pour suivre les modifications

## 🚀 **Étape 5: Déploiement de l'Application sur Hetzner**

### **5.1 Cloner le Repository GitHub (Sur le Serveur)**
```bash
# Se connecter au serveur (si pas déjà connecté)
ssh root@VOTRE_IP

# Supprimer l'ancien dossier s'il existe
rm -rf rdgtt-portail

# Cloner le repository depuis GitHub (version mise à jour)
git clone https://github.com/MoctarSidibe/rdgtt-portail.git

# Aller dans le dossier du projet
cd rdgtt-portail

# Vérifier que tous les fichiers sont présents
ls -la

# Vérifier la version du code
git log --oneline -3
```

### **5.2 Initialiser la Base de Données**
```bash
# Démarrer PostgreSQL en premier
docker-compose up -d postgres

# Attendre que PostgreSQL soit prêt (30 secondes)
sleep 30

# Vérifier que PostgreSQL fonctionne
docker-compose logs postgres

# Initialiser la base de données avec le schéma complet
docker-compose exec postgres psql -U postgres -c "CREATE DATABASE rdgtt_portail;"
docker-compose exec postgres psql -U postgres -c "CREATE USER rdgtt_user WITH PASSWORD 'rdgtt_password_2025';"
docker-compose exec postgres psql -U postgres -c "GRANT ALL PRIVILEGES ON DATABASE rdgtt_portail TO rdgtt_user;"

# Exécuter le script d'initialisation
docker-compose exec -T postgres psql -U postgres -d rdgtt_portail < database/init.sql

# Vérifier que l'initialisation a réussi
docker-compose exec postgres psql -U postgres -d rdgtt_portail -c "SELECT COUNT(*) FROM users;"
docker-compose exec postgres psql -U postgres -d rdgtt_portail -c "SELECT COUNT(*) FROM document_types;"
docker-compose exec postgres psql -U postgres -d rdgtt_portail -c "SELECT COUNT(*) FROM payment_methods;"
```

### **5.3 Créer le Fichier de Configuration**
```bash
# Créer le fichier .env avec les variables d'environnement
nano .env
```

**Contenu du fichier .env:**
```bash
# Base de données
POSTGRES_PASSWORD=rdgtt_password_2025
POSTGRES_DB=rdgtt_portail
POSTGRES_USER=rdgtt_user

# Domaine (remplacer par votre IP ou domaine)
DOMAIN=localhost

# Email pour Let's Encrypt (optionnel)
ACME_EMAIL=admin@rdgtt.ga

# Clé secrète JWT
JWT_SECRET=rdgtt_jwt_secret_2025_secure

# Profil Spring
SPRING_PROFILES_ACTIVE=production
```

**Pour sauvegarder dans nano:**
- Appuyer sur `Ctrl + X`
- Appuyer sur `Y` pour confirmer
- Appuyer sur `Entrée` pour sauvegarder

### **5.4 Obtenir l'IP Publique du Serveur**
```bash
# Obtenir l'IP publique du serveur
curl -s ifconfig.me

# Noter cette IP (ex: 95.216.123.45)
# Nous l'utiliserons pour accéder au portail
```

### **4.4 Construire les Images Docker**
```bash
# Construire toutes les images Docker (cela peut prendre 5-10 minutes)
docker-compose build

# Vérifier que les images ont été créées
docker images
```

### **4.5 Démarrer les Services**
```bash
# Démarrer tous les services en arrière-plan
docker-compose up -d

# Vérifier le statut des services
docker-compose ps
```

### **4.6 Vérifier les Logs**
```bash
# Voir les logs de tous les services
docker-compose logs

# Voir les logs d'un service spécifique
docker-compose logs usager-service
docker-compose logs postgres
docker-compose logs traefik
```

### **4.7 Attendre que les Services soient Prêts**
```bash
# Attendre 2-3 minutes que tous les services démarrent
sleep 180

# Vérifier à nouveau le statut
docker-compose ps

# Tous les services doivent être "Up" et "healthy"
```

## 🚀 **Étape 5: Test et Accès au Portail**

### **5.1 Vérifier que Tout Fonctionne**
```bash
# Vérifier le statut de tous les services
docker-compose ps

# Vérifier les logs pour des erreurs
docker-compose logs --tail=50

# Tester la connectivité des services
curl -I http://localhost:8081/actuator/health  # Usager Service
curl -I http://localhost:8082/actuator/health  # Auto-École Service
curl -I http://localhost:8083/actuator/health  # Permis Service
curl -I http://localhost:8085/actuator/health  # Admin Service
```

### **5.2 Accéder au Portail**
1. **Ouvrir votre navigateur web**
2. **Aller à l'adresse**: `http://VOTRE_IP_SERVEUR`
3. **Vous devriez voir** la page d'accueil du portail R-DGTT

### **5.3 Accéder aux Interfaces d'Administration**
- **Traefik Dashboard**: `http://VOTRE_IP_SERVEUR:8080`
- **Consul UI**: `http://VOTRE_IP_SERVEUR:8500`

### **5.4 Connexion par Défaut**
- **Email**: `admin@rdgtt.ga`
- **Mot de passe**: `admin123`
- **Rôle**: `ADMIN`

## 🚀 **Étape 6: Configuration DNS (Optionnel)**

### **6.1 Si Vous Avez un Domaine**
1. Aller chez votre fournisseur DNS (Cloudflare, Namecheap, etc.)
2. Créer un enregistrement A:
   - **Nom**: `rdgtt.ga` ou `www.rdgtt.ga`
   - **Type**: A
   - **Valeur**: IP de votre serveur Hetzner
   - **TTL**: 300

### **6.2 Vérification DNS:**
```bash
# Vérifier la résolution DNS
nslookup rdgtt.ga
dig rdgtt.ga
```

## 🚀 **Étape 7: Configuration SSL (Optionnel)**

### **7.1 Avec Let's Encrypt (si vous avez un domaine):**
```bash
# Modifier le fichier .env
nano .env

# Changer la ligne DOMAIN=localhost en DOMAIN=rdgtt.ga
# Sauvegarder avec Ctrl+X, Y, Entrée

# Redémarrer Traefik
docker-compose restart traefik
```

### **7.2 Sans Domaine (HTTP seulement):**
- Le portail fonctionne parfaitement en HTTP
- Pas besoin de SSL pour les tests
- L'IP directe fonctionne: `http://VOTRE_IP_SERVEUR`

## 🌐 **Résumé des Accès**

### **URLs d'Accès:**
- **Portail Principal**: `http://VOTRE_IP_SERVEUR`
- **Traefik Dashboard**: `http://VOTRE_IP_SERVEUR:8080`
- **Consul UI**: `http://VOTRE_IP_SERVEUR:8500`

### **Connexion par Défaut:**
- **Email**: `admin@rdgtt.ga`
- **Mot de passe**: `admin123`
- **Rôle**: `ADMIN`

## 🔧 **Gestion des Services (Commandes Utiles)**

### **Commandes de Base:**
```bash
# Voir le statut des services
docker-compose ps

# Voir les logs de tous les services
docker-compose logs

# Voir les logs d'un service spécifique
docker-compose logs usager-service
docker-compose logs postgres
docker-compose logs traefik

# Suivre les logs en temps réel
docker-compose logs -f usager-service
```

### **Commandes de Gestion:**
```bash
# Redémarrer un service
docker-compose restart usager-service

# Redémarrer tous les services
docker-compose restart

# Arrêter tous les services
docker-compose down

# Démarrer tous les services
docker-compose up -d

# Voir l'utilisation des ressources
docker stats
```

### **Commandes de Debug:**
```bash
# Entrer dans un conteneur
docker-compose exec usager-service bash
docker-compose exec postgres psql -U rdgtt_user -d rdgtt_portail

# Voir les variables d'environnement
docker-compose exec usager-service env

# Vérifier la connectivité réseau
docker-compose exec usager-service ping postgres
```

### **Sauvegarde de la Base de Données:**
```bash
# Sauvegarder la base de données
docker-compose exec postgres pg_dump -U rdgtt_user rdgtt_portail > backup-$(date +%Y%m%d).sql

# Restaurer la base de données
docker-compose exec -T postgres psql -U rdgtt_user rdgtt_portail < backup-20250120.sql

# Voir la taille de la base de données
docker-compose exec postgres psql -U rdgtt_user -d rdgtt_portail -c "SELECT pg_size_pretty(pg_database_size('rdgtt_portail'));"
```

## 🔧 **Optimisations Spécifiques à Hetzner**

### **Configuration du Swap:**
```bash
# Créer un fichier swap (2GB)
fallocate -l 2G /swapfile
chmod 600 /swapfile
mkswap /swapfile
swapon /swapfile

# Ajouter au fstab
echo '/swapfile none swap sw 0 0' >> /etc/fstab
```

### **Optimisation de PostgreSQL:**
```bash
# Modifier la configuration PostgreSQL
docker-compose -f docker-compose.simple.yml exec postgres psql -U rdgtt_user -d rdgtt_portail -c "
ALTER SYSTEM SET shared_buffers = '512MB';
ALTER SYSTEM SET effective_cache_size = '1GB';
ALTER SYSTEM SET maintenance_work_mem = '128MB';
ALTER SYSTEM SET checkpoint_completion_target = 0.9;
ALTER SYSTEM SET wal_buffers = '16MB';
ALTER SYSTEM SET default_statistics_target = 100;
SELECT pg_reload_conf();
"
```

### **Monitoring des Ressources:**
```bash
# Installer htop pour le monitoring
apt install htop -y

# Voir l'utilisation des ressources
htop

# Voir l'utilisation de l'espace disque
df -h

# Voir l'utilisation de la mémoire
free -h
```

## 💰 **Coût Estimé**

### **Configuration de Base:**
- **Serveur CX11**: €3.29/mois
- **Stockage**: 20GB inclus
- **Réseau**: 20TB inclus
- **Total**: €3.29/mois (~€0.11/jour)

### **Configuration Recommandée:**
- **Serveur CX21**: €5.83/mois (2 vCPU, 4GB RAM)
- **Stockage**: 40GB inclus
- **Réseau**: 20TB inclus
- **Total**: €5.83/mois (~€0.19/jour)

## 🎯 **Avantages de Hetzner pour R-DGTT Portail**

- ✅ **Très fiable** - 99.9% uptime
- ✅ **Très bon marché** - €3.29/mois
- ✅ **Performance excellente** - SSD rapides
- ✅ **Serveurs européens** - Conformité RGPD
- ✅ **Support en français**
- ✅ **Facile à gérer** via console web
- ✅ **Sauvegarde automatique** disponible
- ✅ **Scaling facile** - Upgrade en 1 clic

## 🚀 **Script de Déploiement Rapide**

```bash
#!/bin/bash
# deploy-hetzner.sh

echo "🚀 Déploiement de R-DGTT Portail sur Hetzner..."

# Vérifier que Docker est installé
if ! command -v docker &> /dev/null; then
    echo "❌ Docker n'est pas installé. Veuillez d'abord exécuter setup-hetzner.sh"
    exit 1
fi

# Configuration de l'environnement
cat > .env << EOF
# Database Configuration
POSTGRES_PASSWORD=rdgtt_password_2025
DB_HOST=localhost
DB_PORT=5432
DB_NAME=rdgtt_portail
DB_USERNAME=rdgtt_user
DB_PASSWORD=rdgtt_password_2025

# JWT Configuration
JWT_SECRET=rdgtt_jwt_secret_2025_secure
JWT_EXPIRATION=86400000

# Application Configuration
SPRING_PROFILES_ACTIVE=production
DOMAIN=localhost
ACME_EMAIL=admin@rdgtt.ga

# Consul Configuration (optional)
CONSUL_HOST=localhost
CONSUL_PORT=8500
CONSUL_DISCOVERY_ENABLED=false
EOF

# Obtenir l'IP publique
PUBLIC_IP=$(curl -s ifconfig.me)
echo "🌐 IP publique détectée: $PUBLIC_IP"

# Mettre à jour le domaine dans .env
sed -i "s/DOMAIN=localhost/DOMAIN=$PUBLIC_IP/" .env

# Construire et démarrer
echo "🔨 Construction des images Docker..."
docker-compose -f docker-compose.simple.yml build

echo "🚀 Démarrage des services..."
docker-compose -f docker-compose.simple.yml up -d

# Attendre que les services soient prêts
echo "⏳ Attente du démarrage des services..."
sleep 30

# Vérifier le statut
echo "📊 Statut des services:"
docker-compose -f docker-compose.simple.yml ps

echo "✅ Déploiement terminé!"
echo "🌐 Accédez au portail: http://$PUBLIC_IP"
echo "📊 Dashboard Traefik: http://$PUBLIC_IP:8080"
echo "🔧 Consul UI: http://$PUBLIC_IP:8500"
echo ""
echo "🔑 Connexion par défaut:"
echo "   Email: admin@rdgtt.ga"
echo "   Mot de passe: admin123"
```

## 🚨 **Dépannage (Troubleshooting)**

### **Problèmes Courants:**

#### **1. Services ne démarrent pas:**
```bash
# Vérifier les logs
docker-compose logs

# Vérifier l'espace disque
df -h

# Vérifier la mémoire
free -h

# Redémarrer Docker
systemctl restart docker
```

#### **2. Erreur de connexion à la base de données:**
```bash
# Vérifier que PostgreSQL est démarré
docker-compose ps postgres

# Vérifier les logs PostgreSQL
docker-compose logs postgres

# Tester la connexion
docker-compose exec postgres psql -U rdgtt_user -d rdgtt_portail -c "SELECT 1;"
```

#### **3. Portail inaccessible:**
```bash
# Vérifier que Traefik fonctionne
docker-compose ps traefik

# Vérifier les logs Traefik
docker-compose logs traefik

# Tester l'accès local
curl -I http://localhost
```

#### **4. Problème de mémoire:**
```bash
# Créer un fichier swap
fallocate -l 2G /swapfile
chmod 600 /swapfile
mkswap /swapfile
swapon /swapfile

# Ajouter au fstab
echo '/swapfile none swap sw 0 0' >> /etc/fstab
```

### **Commandes de Diagnostic:**
```bash
# Voir l'utilisation des ressources
htop

# Voir l'espace disque
df -h

# Voir les processus Docker
docker ps -a

# Voir l'utilisation réseau
netstat -tulpn

# Voir les erreurs système
journalctl -f
```

## 🎉 **Résultat Final**

### **✅ Ce que vous avez accompli:**
- **Serveur Hetzner** configuré et sécurisé
- **Docker et Docker Compose** installés
- **R-DGTT Portail** déployé avec tous les microservices
- **Base de données PostgreSQL** fonctionnelle
- **API Gateway Traefik** configuré
- **Service Discovery Consul** opérationnel
- **Interface d'administration** accessible

### **🌐 Accès au Portail:**
- **URL**: `http://VOTRE_IP_SERVEUR`
- **Email**: `admin@rdgtt.ga`
- **Mot de passe**: `admin123`

### **💰 Coût Total:**
- **Serveur Hetzner CX11**: €3.29/mois
- **Total**: €3.29/mois (~€0.11/jour)

### **🎯 Avantages:**
- ✅ **Apprentissage complet** - Vous comprenez chaque étape
- ✅ **Contrôle total** - Gestion manuelle de tous les services
- ✅ **Débogage facile** - Commandes de diagnostic disponibles
- ✅ **Économique** - Seulement €3.29/mois
- ✅ **Professionnel** - Architecture microservices complète

**Félicitations ! Vous avez déployé avec succès le portail R-DGTT sur Hetzner Cloud !** 🇬🇦✨

---

**Coût**: €3.29/mois  
**Performance**: Excellente  
**Fiabilité**: 99.9% uptime  
**Support**: Français disponible  
**Méthode**: Déploiement manuel étape par étape
