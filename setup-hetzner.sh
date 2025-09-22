#!/bin/bash

# Script de configuration pour Hetzner Cloud
# Optimisé pour les serveurs Hetzner

echo "🚀 Configuration de R-DGTT Portail sur Hetzner Cloud..."

# Mise à jour du système
echo "📦 Mise à jour du système..."
apt update && apt upgrade -y

# Installation de Docker
echo "🐳 Installation de Docker..."
curl -fsSL https://get.docker.com -o get-docker.sh
sh get-docker.sh
rm get-docker.sh

# Installation de Docker Compose
echo "🔧 Installation de Docker Compose..."
curl -L "https://github.com/docker/compose/releases/download/v2.24.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
chmod +x /usr/local/bin/docker-compose

# Installation d'outils utiles
echo "🛠️ Installation d'outils utiles..."
apt install curl wget git nano htop iotop -y

# Configuration du swap (important pour les serveurs 2GB)
echo "💾 Configuration du swap..."
if [ ! -f /swapfile ]; then
    fallocate -l 2G /swapfile
    chmod 600 /swapfile
    mkswap /swapfile
    swapon /swapfile
    echo '/swapfile none swap sw 0 0' >> /etc/fstab
    echo "✅ Swap configuré (2GB)"
else
    echo "✅ Swap déjà configuré"
fi

# Configuration du firewall
echo "🔥 Configuration du firewall..."
ufw allow 22/tcp    # SSH
ufw allow 80/tcp    # HTTP
ufw allow 443/tcp   # HTTPS
ufw allow 8080/tcp  # Traefik Dashboard
ufw allow 8500/tcp  # Consul UI
ufw --force enable

# Optimisation du système pour Hetzner
echo "⚙️ Optimisation du système..."

# Configuration de la limite de fichiers
echo "* soft nofile 65536" >> /etc/security/limits.conf
echo "* hard nofile 65536" >> /etc/security/limits.conf

# Configuration du kernel pour Docker
echo "vm.max_map_count=262144" >> /etc/sysctl.conf
echo "fs.file-max=65536" >> /etc/sysctl.conf
sysctl -p

# Configuration de la mémoire pour PostgreSQL
echo "💾 Configuration de la mémoire pour PostgreSQL..."
echo "vm.overcommit_memory=1" >> /etc/sysctl.conf
sysctl -p

# Vérification des installations
echo "✅ Vérification des installations..."
echo "Docker version: $(docker --version)"
echo "Docker Compose version: $(docker-compose --version)"
echo "Git version: $(git --version)"
echo "Swap: $(swapon --show)"
echo "Mémoire: $(free -h)"

# Affichage des informations système
echo ""
echo "📊 Informations système:"
echo "   • CPU: $(nproc) cores"
echo "   • RAM: $(free -h | awk '/^Mem:/ {print $2}')"
echo "   • Disk: $(df -h / | awk 'NR==2 {print $2}')"
echo "   • Swap: $(free -h | awk '/^Swap:/ {print $2}')"

echo ""
echo "🎉 Configuration terminée!"
echo "📋 Prochaines étapes:"
echo "   1. Cloner votre repository: git clone <your-repo-url>"
echo "   2. Aller dans le dossier: cd rdgtt-portail"
echo "   3. Exécuter le déploiement: chmod +x deploy-hetzner.sh && ./deploy-hetzner.sh"
echo ""
echo "🌐 Votre serveur Hetzner est prêt pour R-DGTT Portail!"
echo "💰 Coût: €3.29/mois (CX11) ou €5.83/mois (CX21)"
echo "🚀 Performance: Optimisée pour Hetzner Cloud"




