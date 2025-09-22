-- R-DGTT Portail Database Setup Script
-- Run this as PostgreSQL superuser (postgres)

-- Create database
CREATE DATABASE rdgtt_portail;

-- Create user
CREATE USER rdgtt_user WITH PASSWORD 'rdgtt_password';

-- Grant privileges
GRANT ALL PRIVILEGES ON DATABASE rdgtt_portail TO rdgtt_user;

-- Connect to the database
\c rdgtt_portail;

-- Grant schema privileges
GRANT ALL ON SCHEMA public TO rdgtt_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO rdgtt_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO rdgtt_user;

-- Set default privileges for future tables
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO rdgtt_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO rdgtt_user;

-- Show created user and database
\du rdgtt_user
\l rdgtt_portail
