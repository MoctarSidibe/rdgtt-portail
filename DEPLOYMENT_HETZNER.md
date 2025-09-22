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

## 🚀 **Étape 1: Création du Serveur sur Hetzner**

### **Via Interface Web:**
1. Aller sur [Hetzner Cloud Console](https://console.hetzner.cloud/)
2. Cliquer "New Project"
3. Nommer: "R-DGTT Portail"
4. Cliquer "Create Project"

### **Création du Serveur:**
1. Dans le projet, cliquer "Add Server"
2. **Nom**: `rdgtt-portail`
3. **Image**: Ubuntu 22.04
4. **Type**: CX11 (1 vCPU, 2GB RAM) - €3.29/mois
5. **Localisation**: Frankfurt ou Nuremberg
6. **SSH Key**: Ajouter votre clé SSH
7. **Réseau**: IPv4 + IPv6
8. Cliquer "Create & Buy Now"

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

## 🚀 **Étape 2: Configuration du Serveur**

### **Connexion SSH:**
```bash
# Récupérer l'IP du serveur depuis la console Hetzner
ssh root@YOUR_SERVER_IP
```

### **Configuration Automatique:**
```bash
# Télécharger et exécuter le script de configuration
curl -sSL https://raw.githubusercontent.com/your-repo/rdgtt-portail/main/setup-hetzner.sh | bash
```

### **Configuration Manuelle:**
```bash
# Mise à jour du système
apt update && apt upgrade -y

# Installation de Docker
curl -fsSL https://get.docker.com -o get-docker.sh
sh get-docker.sh
rm get-docker.sh

# Installation de Docker Compose
curl -L "https://github.com/docker/compose/releases/download/v2.24.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
chmod +x /usr/local/bin/docker-compose

# Installation d'outils utiles
apt install curl wget git nano htop -y

# Configuration du firewall
ufw allow 22/tcp    # SSH
ufw allow 80/tcp    # HTTP
ufw allow 443/tcp   # HTTPS
ufw allow 8080/tcp  # Traefik Dashboard
ufw allow 8500/tcp  # Consul UI
ufw --force enable

# Vérification
docker --version
docker-compose --version
```

## 🚀 **Étape 3: Déploiement de l'Application**

### **Transférer le Projet:**
```bash
# Option 1: Via SCP (depuis votre machine locale)
scp -r rdgtt-portail.zip root@YOUR_SERVER_IP:/root/
ssh root@YOUR_SERVER_IP
unzip rdgtt-portail.zip
cd rdgtt-portail

# Option 2: Via Git (si vous avez un repository)
git clone https://github.com/your-username/rdgtt-portail.git
cd rdgtt-portail
```

### **Déploiement Automatique:**
```bash
# Rendre le script exécutable et lancer
chmod +x deploy-hetzner.sh
./deploy-hetzner.sh
```

### **Déploiement Manuel:**
```bash
# Créer le fichier .env
cat > .env << EOF
POSTGRES_PASSWORD=rdgtt_password
DOMAIN=localhost
ACME_EMAIL=admin@rdgtt.ga
JWT_SECRET=rdgtt_jwt_secret_2025
SPRING_PROFILES_ACTIVE=production
EOF

# Démarrer les services
docker-compose -f docker-compose.simple.yml up -d
```

## 🚀 **Étape 4: Configuration du DNS**

### **Configuration DNS:**
1. Aller chez votre fournisseur DNS (Cloudflare, Namecheap, etc.)
2. Créer un enregistrement A:
   - **Nom**: `rdgtt.ga` ou `www.rdgtt.ga`
   - **Type**: A
   - **Valeur**: IP de votre serveur Hetzner
   - **TTL**: 300

### **Vérification DNS:**
```bash
# Vérifier la résolution DNS
nslookup rdgtt.ga
dig rdgtt.ga
```

## 🚀 **Étape 5: Configuration SSL (Optionnel)**

### **Avec Let's Encrypt:**
```bash
# Modifier le fichier .env
sed -i 's/DOMAIN=localhost/DOMAIN=rdgtt.ga/' .env

# Redémarrer Traefik
docker-compose -f docker-compose.simple.yml restart traefik
```

## 🌐 **Accès aux Services**

### **URLs d'Accès:**
- **Portail Principal**: `http://YOUR_SERVER_IP` ou `https://rdgtt.ga`
- **Traefik Dashboard**: `http://YOUR_SERVER_IP:8080`
- **Consul UI**: `http://YOUR_SERVER_IP:8500`

### **Connexion par Défaut:**
- **Email**: admin@rdgtt.ga
- **Mot de passe**: admin123

## 🔧 **Gestion des Services**

### **Commandes Utiles:**
```bash
# Voir le statut des services
docker-compose -f docker-compose.simple.yml ps

# Voir les logs d'un service
docker-compose -f docker-compose.simple.yml logs -f usager-service

# Redémarrer un service
docker-compose -f docker-compose.simple.yml restart usager-service

# Arrêter tous les services
docker-compose -f docker-compose.simple.yml down

# Redémarrer tous les services
docker-compose -f docker-compose.simple.yml restart

# Voir l'utilisation des ressources
docker stats
```

### **Sauvegarde de la Base de Données:**
```bash
# Sauvegarder
docker-compose -f docker-compose.simple.yml exec postgres pg_dump -U rdgtt_user rdgtt_portail > backup-$(date +%Y%m%d).sql

# Restaurer
docker-compose -f docker-compose.simple.yml exec -T postgres psql -U rdgtt_user rdgtt_portail < backup-20250120.sql
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
POSTGRES_PASSWORD=rdgtt_password_2025
DOMAIN=localhost
ACME_EMAIL=admin@rdgtt.ga
JWT_SECRET=rdgtt_jwt_secret_2025_secure
SPRING_PROFILES_ACTIVE=production
POSTGRES_DB=rdgtt_portail
POSTGRES_USER=rdgtt_user
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

## 🎉 **Résultat**

Un système R-DGTT Portail **professionnel**, **fiable** et **économique** déployé sur Hetzner Cloud pour seulement €3.29/mois ! 🇬🇦✨

---

**Coût**: €3.29/mois  
**Performance**: Excellente  
**Fiabilité**: 99.9% uptime  
**Support**: Français disponible
