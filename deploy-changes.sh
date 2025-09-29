#!/bin/bash

# Deploy changes to server with database migration
# This script applies the database migration and restarts services

set -e

echo "🚀 Deploying RDGTT Portail changes to server..."

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

# Check if we're in the right directory
if [ ! -f "docker-compose.yml" ]; then
    print_error "docker-compose.yml not found. Please run this script from the project root."
    exit 1
fi

# Check if Docker is available
if ! command -v docker &> /dev/null; then
    print_error "Docker is not available. Please install Docker first."
    exit 1
fi

# Check if docker-compose is available
if ! command -v docker-compose &> /dev/null && ! docker compose version &> /dev/null; then
    print_error "Docker Compose is not available. Please install Docker Compose first."
    exit 1
fi

# Determine compose command
if command -v docker-compose &> /dev/null; then
    COMPOSE_CMD="docker-compose"
else
    COMPOSE_CMD="docker compose"
fi

print_status "Using Docker Compose command: $COMPOSE_CMD"

# Check if services are running
print_status "Checking current service status..."
$COMPOSE_CMD ps

# Apply database migration
print_status "Applying database migration..."
if [ -f "database/migration-fix-documents.sql" ]; then
    # Check if PostgreSQL is running
    if docker ps | grep -q postgres; then
        print_status "PostgreSQL is running. Applying migration..."
        
        # Get PostgreSQL container
        POSTGRES_CONTAINER=$(docker ps -q --filter "name=postgres")
        
        if [ -n "$POSTGRES_CONTAINER" ]; then
            # Copy migration file to container and execute
            docker cp database/migration-fix-documents.sql $POSTGRES_CONTAINER:/tmp/migration.sql
            docker exec $POSTGRES_CONTAINER psql -U rdgtt_user -d rdgtt_portail -f /tmp/migration.sql
            
            if [ $? -eq 0 ]; then
                print_success "Database migration applied successfully!"
            else
                print_error "Failed to apply database migration"
                exit 1
            fi
        else
            print_error "PostgreSQL container not found"
            exit 1
        fi
    else
        print_warning "PostgreSQL is not running. Starting services first..."
        $COMPOSE_CMD up -d postgres
        sleep 30
        # Retry migration
        docker cp database/migration-fix-documents.sql $POSTGRES_CONTAINER:/tmp/migration.sql
        docker exec $POSTGRES_CONTAINER psql -U rdgtt_user -d rdgtt_portail -f /tmp/migration.sql
        print_success "Database migration applied successfully!"
    fi
else
    print_warning "Migration file not found. Skipping database migration."
fi

# Rebuild and restart services
print_status "Rebuilding and restarting services..."

# Stop all services
print_status "Stopping all services..."
$COMPOSE_CMD down

# Rebuild services
print_status "Rebuilding services..."
$COMPOSE_CMD build --no-cache

# Start services in order
print_status "Starting infrastructure services..."
$COMPOSE_CMD up -d postgres consul

# Wait for infrastructure to be ready
print_status "Waiting for infrastructure to be ready..."
sleep 30

# Start application services
print_status "Starting application services..."
$COMPOSE_CMD up -d usager-service auto-ecole-service permis-service admin-service

# Wait for application services
print_status "Waiting for application services to be ready..."
sleep 30

# Start frontend
print_status "Starting frontend..."
$COMPOSE_CMD up -d frontend

# Check service status
print_status "Checking service status..."
sleep 10
$COMPOSE_CMD ps

# Display access information
print_success "Deployment completed!"
echo ""
print_status "Services are running:"
echo "  - Frontend: http://$(curl -s ifconfig.me 2>/dev/null || echo 'localhost')"
echo "  - Traefik Dashboard: http://$(curl -s ifconfig.me 2>/dev/null || echo 'localhost'):8080"
echo "  - Consul UI: http://$(curl -s ifconfig.me 2>/dev/null || echo 'localhost'):8500"
echo ""
print_status "To view logs: $COMPOSE_CMD logs -f [service-name]"
print_status "To check auto-ecole service: $COMPOSE_CMD logs auto-ecole-service"