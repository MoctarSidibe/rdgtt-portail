# Environment Configuration Guide

## Database Connection Setup

The R-DGTT Portail uses environment variables for database configuration. Here's how to set them up:

### 1. Environment Variables

Create a `.env` file in the project root with the following variables:

```bash
# Database Configuration
DB_HOST=localhost
DB_PORT=5432
DB_NAME=rdgtt_portail
DB_USERNAME=rdgtt_user
DB_PASSWORD=rdgtt_password

# JWT Configuration
JWT_SECRET=rdgtt-secret-key-for-jwt-token-generation-2024
JWT_EXPIRATION=86400000

# Consul Configuration (for admin-service)
CONSUL_HOST=localhost
CONSUL_PORT=8500
CONSUL_DISCOVERY_ENABLED=false

# Application Profiles
SPRING_PROFILES_ACTIVE=dev
```

### 2. For Production Deployment

For production deployment on Hetzner, set these environment variables:

```bash
# Database Configuration
DB_HOST=localhost
DB_PORT=5432
DB_NAME=rdgtt_portail
DB_USERNAME=rdgtt_user
DB_PASSWORD=your_secure_password_here

# JWT Configuration
JWT_SECRET=your_very_secure_jwt_secret_here
JWT_EXPIRATION=86400000

# Application Profiles
SPRING_PROFILES_ACTIVE=prod
```

### 3. Setting Environment Variables

#### Windows (PowerShell):
```powershell
$env:DB_PASSWORD="your_password_here"
$env:JWT_SECRET="your_jwt_secret_here"
$env:SPRING_PROFILES_ACTIVE="prod"
```

#### Windows (Command Prompt):
```cmd
set DB_PASSWORD=your_password_here
set JWT_SECRET=your_jwt_secret_here
set SPRING_PROFILES_ACTIVE=prod
```

#### Linux/macOS:
```bash
export DB_PASSWORD="your_password_here"
export JWT_SECRET="your_jwt_secret_here"
export SPRING_PROFILES_ACTIVE="prod"
```

### 4. Docker Environment

For Docker deployment, create a `.env` file in the project root:

```bash
# Database Configuration
POSTGRES_PASSWORD=rdgtt_password
DB_HOST=postgres
DB_PORT=5432
DB_NAME=rdgtt_portail
DB_USERNAME=rdgtt_user
DB_PASSWORD=rdgtt_password

# JWT Configuration
JWT_SECRET=rdgtt_jwt_secret_2025

# Application Profiles
SPRING_PROFILES_ACTIVE=production
```

### 5. Application Profiles

The system supports different profiles:

- **dev**: Development mode (default)
- **prod**: Production mode
- **test**: Testing mode

Set the `SPRING_PROFILES_ACTIVE` environment variable to switch between profiles.

### 6. Security Notes

- **Never commit** `.env` files to version control
- Use **strong passwords** for production
- Use **long, random JWT secrets** for production
- Consider using **secrets management** for production deployments

### 7. Database Setup

Before running the application, ensure:

1. PostgreSQL is running
2. Database `rdgtt_portail` exists
3. User `rdgtt_user` exists with proper permissions
4. Run the initialization script: `database/init.sql`

### 8. Verification

To verify your environment variables are set correctly:

```bash
# Check if variables are set
echo $DB_PASSWORD
echo $JWT_SECRET
echo $SPRING_PROFILES_ACTIVE
```

The application will use these environment variables automatically when running in production mode.
