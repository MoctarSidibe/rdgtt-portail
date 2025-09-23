#!/bin/bash

# RDGTT Portail - Deploy from GitHub to Hetzner
# This script clones the repository and deploys the application

set -e

echo "🚀 Starting RDGTT Portail deployment from GitHub..."

# Configuration
GITHUB_REPO="https://github.com/YOUR_USERNAME/rdgtt-portail.git"
DEPLOY_DIR="/opt/rdgtt-portail"
SERVICE_USER="rdgtt"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
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

# Check if running as root
if [[ $EUID -eq 0 ]]; then
   print_error "This script should not be run as root for security reasons"
   exit 1
fi

# Update system
print_status "Updating system packages..."
sudo apt update && sudo apt upgrade -y

# Install required packages
print_status "Installing required packages..."
sudo apt install -y git curl wget unzip

# Install Docker if not already installed
if ! command -v docker &> /dev/null; then
    print_status "Installing Docker..."
    curl -fsSL https://get.docker.com -o get-docker.sh
    sudo sh get-docker.sh
    sudo usermod -aG docker $USER
    rm get-docker.sh
    print_success "Docker installed successfully"
else
    print_success "Docker is already installed"
fi

# Install Docker Compose if not already installed
if ! command -v docker-compose &> /dev/null; then
    print_status "Installing Docker Compose..."
    sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
    sudo chmod +x /usr/local/bin/docker-compose
    print_success "Docker Compose installed successfully"
else
    print_success "Docker Compose is already installed"
fi

# Create deployment directory
print_status "Creating deployment directory..."
sudo mkdir -p $DEPLOY_DIR
sudo chown $USER:$USER $DEPLOY_DIR

# Clone or update repository
if [ -d "$DEPLOY_DIR/.git" ]; then
    print_status "Updating existing repository..."
    cd $DEPLOY_DIR
    git pull origin main
else
    print_status "Cloning repository from GitHub..."
    git clone $GITHUB_REPO $DEPLOY_DIR
    cd $DEPLOY_DIR
fi

# Set up environment variables
print_status "Setting up environment variables..."
if [ ! -f ".env" ]; then
    cp env.example .env
    print_warning "Please edit .env file with your configuration before continuing"
    print_warning "Required variables: POSTGRES_PASSWORD, DOMAIN, JWT_SECRET"
    read -p "Press Enter after editing .env file..."
fi

# Build and start services
print_status "Building and starting services..."
docker-compose down --remove-orphans
docker-compose build --no-cache
docker-compose up -d

# Wait for services to start
print_status "Waiting for services to start..."
sleep 30

# Check service status
print_status "Checking service status..."
docker-compose ps

# Display access information
print_status "Deployment completed!"
echo ""
print_success "Services are running:"
echo "  - Frontend: http://$(curl -s ifconfig.me):3000"
echo "  - Traefik Dashboard: http://$(curl -s ifconfig.me):8080"
echo "  - Consul UI: http://$(curl -s ifconfig.me):8500"
echo ""
print_warning "Don't forget to:"
echo "  1. Configure your domain DNS to point to this server"
echo "  2. Set up SSL certificates (Let's Encrypt)"
echo "  3. Configure firewall rules"
echo "  4. Set up monitoring and backups"
echo ""
print_status "To view logs: docker-compose logs -f"
print_status "To stop services: docker-compose down"
print_status "To restart services: docker-compose restart"
