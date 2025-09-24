# 🔐 Docker Secrets Setup Guide

## Overview
This guide explains how to implement Docker Secrets for secure configuration management in the R-DGTT Portail project.

## ⚠️ **Current Status: Environment Variables**
Due to compatibility issues with Docker Secrets in the current setup, the project now uses **environment variables** for configuration management. This provides a simpler and more reliable approach for deployment.

## 🚀 Step-by-Step Implementation

### Step 1: Create Secrets Directory
```bash
# Create secrets directory
mkdir -p secrets

# Create secret files
echo "rdgtt_password" > secrets/db_password.txt
echo "rdgtt_jwt_secret_2025" > secrets/jwt_secret.txt
echo "admin@rdgtt.ga" > secrets/acme_email.txt

# Set proper permissions (only readable by owner)
chmod 600 secrets/*.txt

# Verify files
ls -la secrets/
```

### Step 2: Update Docker Compose
The `docker-compose.yml` file has been updated to use Docker Secrets instead of environment variables.

**Key Changes:**
- `POSTGRES_PASSWORD_FILE: /run/secrets/db_password`
- `JWT_SECRET_FILE: /run/secrets/jwt_secret`
- `ACME_EMAIL_FILE: /run/secrets/acme_email`

### Step 3: Security Benefits
- ✅ **Encrypted at rest**: Secrets are encrypted in Docker
- ✅ **Not visible in process lists**: Secrets are mounted as files
- ✅ **Not in environment variables**: More secure than env vars
- ✅ **Access control**: Only services that need secrets can access them
- ✅ **Not committed to Git**: Secrets directory is in .gitignore

### Step 4: Deployment Commands
```bash
# Stop current services
docker compose down

# Start with Docker Secrets
docker compose up -d

# Verify secrets are mounted
docker compose exec usager-service ls -la /run/secrets/

# Check service health
docker compose ps
```

## 🔧 Troubleshooting

### Check Secret Mounting
```bash
# Verify secrets are accessible
docker compose exec usager-service cat /run/secrets/db_password
docker compose exec usager-service cat /run/secrets/jwt_secret
```

### Check Service Logs
```bash
# Check if services are starting properly
docker compose logs --tail=20 usager-service
docker compose logs --tail=20 admin-service
```

## 📝 Notes
- The `secrets/` directory is added to `.gitignore` for security
- All sensitive data is now managed through Docker Secrets
- Services automatically read secrets from `/run/secrets/` directory
- No more plain text passwords in environment variables

## 🔄 Migration from Environment Variables
If migrating from `.env` files:
1. Create the secrets directory and files
2. Update docker-compose.yml (already done)
3. Remove or backup old `.env` files
4. Restart services with new configuration
