#!/bin/bash

# R-DGTT Portail - Déploiement sur Hetzner
# Optimisé pour les serveurs Hetzner Cloud

set -e

echo "🚀 Déploiement de R-DGTT Portail sur Hetzner Cloud..."

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

print_status() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Vérifier Docker
check_docker() {
    print_status "Vérification de Docker..."
    if ! command -v docker &> /dev/null; then
        print_error "Docker n'est pas installé. Exécutez d'abord setup-hetzner.sh"
        exit 1
    fi
    print_success "Docker est prêt"
}

# Vérifier Docker Compose
check_docker_compose() {
    print_status "Vérification de Docker Compose..."
    if ! command -v docker-compose &> /dev/null; then
        print_error "Docker Compose n'est pas installé. Exécutez d'abord setup-hetzner.sh"
        exit 1
    fi
    print_success "Docker Compose est prêt"
}

# Configuration de l'environnement
setup_environment() {
    print_status "Configuration de l'environnement..."
    
    if [ ! -f .env ]; then
        print_warning "Création du fichier .env..."
        cat > .env << EOF
POSTGRES_PASSWORD=rdgtt_password
DOMAIN=localhost
ACME_EMAIL=admin@rdgtt.ga
JWT_SECRET=rdgtt_jwt_secret_2025
SPRING_PROFILES_ACTIVE=production
EOF
        print_success "Fichier .env créé"
    else
        print_success "Fichier .env existe déjà"
    fi
}

# Obtenir l'IP publique
get_public_ip() {
    print_status "Récupération de l'IP publique..."
    PUBLIC_IP=$(curl -s ifconfig.me || curl -s ipinfo.io/ip || echo "localhost")
    print_success "IP publique: $PUBLIC_IP"
    
    # Mettre à jour le domaine dans .env
    sed -i "s/DOMAIN=.*/DOMAIN=$PUBLIC_IP/" .env
    print_success "Domaine mis à jour: $PUBLIC_IP"
}

# Optimisation pour Hetzner
optimize_for_hetzner() {
    print_status "Optimisation pour Hetzner Cloud..."
    
    # Vérifier la mémoire disponible
    MEMORY_GB=$(free -g | awk '/^Mem:/ {print $2}')
    print_status "Mémoire disponible: ${MEMORY_GB}GB"
    
    if [ "$MEMORY_GB" -lt 2 ]; then
        print_warning "Mémoire limitée détectée. Optimisation des paramètres..."
        
        # Optimiser PostgreSQL pour 2GB RAM
        cat >> .env << EOF

# Optimisations pour serveur 2GB
POSTGRES_SHARED_BUFFERS=256MB
POSTGRES_EFFECTIVE_CACHE_SIZE=1GB
POSTGRES_MAINTENANCE_WORK_MEM=64MB
EOF
    fi
    
    print_success "Optimisations appliquées"
}

# Construire les images
build_images() {
    print_status "Construction des images Docker..."
    
    # Construire tous les services
    docker-compose -f docker-compose.simple.yml build
    
    print_success "Images construites avec succès"
}

# Démarrer les services
start_services() {
    print_status "Démarrage des services..."
    
    # Démarrer l'infrastructure d'abord
    print_status "Démarrage de l'infrastructure (PostgreSQL, Consul)..."
    docker-compose -f docker-compose.simple.yml up -d postgres consul
    
    # Attendre que l'infrastructure soit prête
    print_status "Attente de la disponibilité de l'infrastructure..."
    sleep 30
    
    # Démarrer les services applicatifs
    print_status "Démarrage des services applicatifs..."
    docker-compose -f docker-compose.simple.yml up -d usager-service auto-ecole-service permis-service admin-service
    
    # Attendre que les services soient prêts
    print_status "Attente de la disponibilité des services..."
    sleep 30
    
    # Démarrer le frontend
    print_status "Démarrage du frontend..."
    docker-compose -f docker-compose.simple.yml up -d frontend
    
    print_success "Services démarrés avec succès"
}

# Vérifier la santé des services
check_health() {
    print_status "Vérification de la santé des services..."
    
    # Attendre un peu plus
    sleep 10
    
    # Vérifier PostgreSQL
    if docker-compose -f docker-compose.simple.yml exec -T postgres pg_isready -U rdgtt_user -d rdgtt_portail > /dev/null 2>&1; then
        print_success "PostgreSQL est opérationnel"
    else
        print_warning "PostgreSQL n'est pas encore accessible"
    fi
    
    # Vérifier Consul
    if curl -s http://localhost:8500/v1/status/leader > /dev/null 2>&1; then
        print_success "Consul est opérationnel"
    else
        print_warning "Consul n'est pas encore accessible"
    fi
    
    # Vérifier les services (avec timeout)
    services=("usager-service:8081" "auto-ecole-service:8082" "permis-service:8083" "admin-service:8085")
    
    for service in "${services[@]}"; do
        IFS=':' read -r name port <<< "$service"
        if timeout 5 curl -s http://localhost:$port/actuator/health > /dev/null 2>&1; then
            print_success "$name est opérationnel"
        else
            print_warning "$name n'est pas encore accessible (peut prendre quelques minutes)"
        fi
    done
}

# Afficher les informations de déploiement
show_info() {
    PUBLIC_IP=$(curl -s ifconfig.me || curl -s ipinfo.io/ip || echo "localhost")
    
    print_success "Déploiement terminé!"
    echo ""
    echo "🌐 Accès aux services:"
    echo "   • Portail Principal: http://$PUBLIC_IP"
    echo "   • Traefik Dashboard: http://$PUBLIC_IP:8080"
    echo "   • Consul UI: http://$PUBLIC_IP:8500"
    echo ""
    echo "🔑 Connexion par défaut:"
    echo "   • Email: admin@rdgtt.ga"
    echo "   • Mot de passe: admin123"
    echo ""
    echo "📊 Commandes utiles:"
    echo "   • Voir les logs: docker-compose -f docker-compose.simple.yml logs -f [service]"
    echo "   • Arrêter: docker-compose -f docker-compose.simple.yml down"
    echo "   • Redémarrer: docker-compose -f docker-compose.simple.yml restart [service]"
    echo "   • Statut: docker-compose -f docker-compose.simple.yml ps"
    echo "   • Monitoring: htop"
    echo ""
    echo "💰 Coût: €3.29/mois (CX11) ou €5.83/mois (CX21)"
    echo "🚀 Performance: Optimisée pour Hetzner Cloud"
    echo ""
    echo "🎉 Votre système R-DGTT Portail est maintenant opérationnel sur Hetzner!"
}

# Fonction principale
main() {
    print_status "Démarrage du déploiement R-DGTT Portail sur Hetzner..."
    
    check_docker
    check_docker_compose
    setup_environment
    get_public_ip
    optimize_for_hetzner
    build_images
    start_services
    check_health
    show_info
}

# Exécuter la fonction principale
main "$@"




