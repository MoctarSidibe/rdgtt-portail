#!/bin/bash

# Script to fix auto-ecole service database schema issues
# This script applies the migration to fix the missing columns

echo "=============================================="
echo "Fixing Auto-Ecole Service Database Schema"
echo "=============================================="

# Check if docker is available
if command -v docker &> /dev/null; then
    echo "Docker is available. Attempting to apply migration..."
    
    # Try to connect to the database and apply migration
    echo "Applying database migration..."
    
    # Check if the database container is running
    if docker ps | grep -q postgres; then
        echo "PostgreSQL container is running. Applying migration..."
        
        # Apply the migration script
        docker exec -i $(docker ps -q --filter "name=postgres") psql -U rdgtt_user -d rdgtt_portail < database/migration-fix-documents.sql
        
        if [ $? -eq 0 ]; then
            echo "✅ Database migration applied successfully!"
            echo "Restarting auto-ecole service..."
            docker compose restart auto-ecole-service
        else
            echo "❌ Failed to apply database migration"
            exit 1
        fi
    else
        echo "❌ PostgreSQL container is not running"
        echo "Please start the database first with: docker compose up -d postgres"
        exit 1
    fi
else
    echo "❌ Docker is not available in this environment"
    echo "Please apply the migration manually by running the SQL script:"
    echo "psql -U rdgtt_user -d rdgtt_portail -f /workspace/database/migration-fix-documents.sql"
    echo ""
    echo "Or recreate the database with the updated schema:"
    echo "1. Drop and recreate the database"
    echo "2. Run the updated init.sql script"
    exit 1
fi

echo "=============================================="
echo "Auto-Ecole Service Schema Fix Complete!"
echo "=============================================="