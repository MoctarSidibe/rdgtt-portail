#!/bin/bash

# Script to apply the database migration for auto-ecole service
# Run this script on your server where Docker is available

echo "=============================================="
echo "Applying Auto-Ecole Database Migration"
echo "=============================================="

# Check if docker-compose is available
if command -v docker-compose &> /dev/null; then
    COMPOSE_CMD="docker-compose"
elif command -v docker &> /dev/null && docker compose version &> /dev/null; then
    COMPOSE_CMD="docker compose"
else
    echo "❌ Neither docker-compose nor 'docker compose' is available"
    exit 1
fi

# Check if the database container is running
if docker ps | grep -q postgres; then
    echo "✅ PostgreSQL container is running"
    
    # Get the container name/ID
    POSTGRES_CONTAINER=$(docker ps -q --filter "name=postgres")
    
    if [ -z "$POSTGRES_CONTAINER" ]; then
        echo "❌ Could not find PostgreSQL container"
        exit 1
    fi
    
    echo "Applying migration to container: $POSTGRES_CONTAINER"
    
    # Copy the migration file to the container and execute it
    docker cp database/migration-fix-documents.sql $POSTGRES_CONTAINER:/tmp/migration.sql
    docker exec $POSTGRES_CONTAINER psql -U rdgtt_user -d rdgtt_portail -f /tmp/migration.sql
    
    if [ $? -eq 0 ]; then
        echo "✅ Database migration applied successfully!"
        echo "Restarting auto-ecole service..."
        $COMPOSE_CMD restart auto-ecole-service
        echo "✅ Auto-ecole service restarted"
    else
        echo "❌ Failed to apply database migration"
        exit 1
    fi
else
    echo "❌ PostgreSQL container is not running"
    echo "Please start the database first with: $COMPOSE_CMD up -d postgres"
    exit 1
fi

echo "=============================================="
echo "Migration completed successfully!"
echo "=============================================="