-- R-DGTT Portail Database Initialization
-- Clean, comprehensive setup for all services

-- Enable UUID extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create database and user (run as postgres superuser)
-- CREATE DATABASE rdgtt_portail;
-- CREATE USER rdgtt_user WITH PASSWORD 'rdgtt_password';
-- GRANT ALL PRIVILEGES ON DATABASE rdgtt_portail TO rdgtt_user;

-- ==============================================
-- USAGER SERVICE TABLES
-- ==============================================

-- Users table
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    telephone VARCHAR(20),
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL DEFAULT 'CITOYEN',
    actif BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Departments table
CREATE TABLE departments (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    nom VARCHAR(255) NOT NULL,
    code VARCHAR(50) UNIQUE NOT NULL,
    description TEXT,
    actif BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Bureaus table
CREATE TABLE bureaus (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    nom VARCHAR(255) NOT NULL,
    code VARCHAR(50) UNIQUE NOT NULL,
    description TEXT,
    departement_id UUID REFERENCES departments(id),
    actif BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ==============================================
-- AUTO-ÉCOLE SERVICE TABLES
-- ==============================================

-- Auto-écoles table
CREATE TABLE auto_ecoles (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    nom VARCHAR(255) NOT NULL,
    code VARCHAR(50) UNIQUE NOT NULL,
    adresse TEXT,
    telephone VARCHAR(20),
    email VARCHAR(255),
    statut VARCHAR(50) DEFAULT 'EN_ATTENTE',
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Auto-école documents
CREATE TABLE auto_ecole_documents (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    auto_ecole_id UUID REFERENCES auto_ecoles(id) ON DELETE CASCADE,
    nom_document VARCHAR(255) NOT NULL,
    type_document VARCHAR(100) NOT NULL,
    chemin_fichier VARCHAR(500),
    statut VARCHAR(50) DEFAULT 'EN_ATTENTE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Candidates table
CREATE TABLE candidats (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    telephone VARCHAR(20),
    date_naissance DATE,
    lieu_naissance VARCHAR(100),
    adresse TEXT,
    auto_ecole_id UUID REFERENCES auto_ecoles(id),
    statut VARCHAR(50) DEFAULT 'INSCRIT',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Candidate documents
CREATE TABLE candidat_documents (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    candidat_id UUID REFERENCES candidats(id) ON DELETE CASCADE,
    nom_document VARCHAR(255) NOT NULL,
    type_document VARCHAR(100) NOT NULL,
    chemin_fichier VARCHAR(500),
    statut VARCHAR(50) DEFAULT 'EN_ATTENTE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ==============================================
-- PERMIS SERVICE TABLES
-- ==============================================

-- Permis table
CREATE TABLE permis (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    numero_permis VARCHAR(50) UNIQUE NOT NULL,
    candidat_id UUID REFERENCES candidats(id),
    type_permis VARCHAR(50) NOT NULL,
    categorie VARCHAR(10) NOT NULL,
    date_obtention DATE,
    date_expiration DATE,
    statut VARCHAR(50) DEFAULT 'EN_ATTENTE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ==============================================
-- ADMIN SERVICE TABLES (Workflow Management)
-- ==============================================

-- Document Types Table
CREATE TABLE document_types (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    nom VARCHAR(255) NOT NULL,
    code VARCHAR(100) UNIQUE NOT NULL,
    description TEXT,
    service_code VARCHAR(100) NOT NULL,
    categorie VARCHAR(100),
    actif BOOLEAN DEFAULT true,
    delai_traitement_jours INTEGER,
    frais_obligatoire BOOLEAN DEFAULT false,
    montant_frais DECIMAL(10,2),
    documents_requis TEXT,
    conditions_eligibilite TEXT,
    workflow_config TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Process Steps Table
CREATE TABLE process_steps (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    document_type_id UUID REFERENCES document_types(id) ON DELETE CASCADE,
    nom VARCHAR(255) NOT NULL,
    code VARCHAR(100) NOT NULL,
    description TEXT,
    ordre INTEGER NOT NULL,
    departement_id UUID,
    bureau_id UUID,
    role_requis VARCHAR(100),
    type_validation VARCHAR(50),
    conditions_validation TEXT,
    delai_max_jours INTEGER,
    obligatoire BOOLEAN DEFAULT true,
    peut_rejeter BOOLEAN DEFAULT true,
    peut_rediriger BOOLEAN DEFAULT false,
    etape_suivante_id UUID,
    etape_rejet_id UUID,
    actif BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Workflow Instances Table
CREATE TABLE workflow_instances (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    demande_id VARCHAR(255) NOT NULL,
    document_type_id UUID REFERENCES document_types(id),
    statut VARCHAR(50) NOT NULL,
    etape_actuelle_id UUID,
    utilisateur_actuel_id UUID,
    departement_actuel_id UUID,
    bureau_actuel_id UUID,
    date_debut TIMESTAMP,
    date_fin TIMESTAMP,
    delai_max_jours INTEGER,
    commentaires TEXT,
    donnees_contexte TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Workflow Step Executions Table
CREATE TABLE workflow_step_executions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    workflow_instance_id UUID REFERENCES workflow_instances(id) ON DELETE CASCADE,
    process_step_id UUID REFERENCES process_steps(id),
    statut VARCHAR(50) NOT NULL,
    utilisateur_id UUID,
    departement_id UUID,
    bureau_id UUID,
    date_debut TIMESTAMP,
    date_fin TIMESTAMP,
    commentaires TEXT,
    donnees_validation TEXT,
    decision VARCHAR(50),
    etape_suivante_id UUID,
    raison_rejet TEXT,
    delai_depasse BOOLEAN DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ==============================================
-- INDEXES FOR PERFORMANCE
-- ==============================================

-- Users indexes
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role ON users(role);
CREATE INDEX idx_users_actif ON users(actif);

-- Departments indexes
CREATE INDEX idx_departments_code ON departments(code);
CREATE INDEX idx_departments_actif ON departments(actif);

-- Bureaus indexes
CREATE INDEX idx_bureaus_code ON bureaus(code);
CREATE INDEX idx_bureaus_departement ON bureaus(departement_id);
CREATE INDEX idx_bureaus_actif ON bureaus(actif);

-- Auto-écoles indexes
CREATE INDEX idx_auto_ecoles_code ON auto_ecoles(code);
CREATE INDEX idx_auto_ecoles_statut ON auto_ecoles(statut);

-- Candidats indexes
CREATE INDEX idx_candidats_email ON candidats(email);
CREATE INDEX idx_candidats_auto_ecole ON candidats(auto_ecole_id);
CREATE INDEX idx_candidats_statut ON candidats(statut);

-- Permis indexes
CREATE INDEX idx_permis_numero ON permis(numero_permis);
CREATE INDEX idx_permis_candidat ON permis(candidat_id);
CREATE INDEX idx_permis_statut ON permis(statut);

-- Admin service indexes
CREATE INDEX idx_document_types_service_code ON document_types(service_code);
CREATE INDEX idx_document_types_actif ON document_types(actif);
CREATE INDEX idx_process_steps_document_type ON process_steps(document_type_id);
CREATE INDEX idx_process_steps_ordre ON process_steps(document_type_id, ordre);
CREATE INDEX idx_workflow_instances_demande ON workflow_instances(demande_id);
CREATE INDEX idx_workflow_instances_statut ON workflow_instances(statut);
CREATE INDEX idx_workflow_step_executions_workflow ON workflow_step_executions(workflow_instance_id);

-- ==============================================
-- SAMPLE DATA
-- ==============================================

-- Insert default admin user
INSERT INTO users (nom, prenom, email, password, role) VALUES
('Admin', 'System', 'admin@rdgtt.ga', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDi', 'ADMIN');

-- Insert default departments
INSERT INTO departments (nom, code, description) VALUES
('Direction Générale', 'DGTT', 'Direction Générale des Transports Terrestres'),
('Service des Examens et Vérifications', 'SEV', 'Service chargé des examens et vérifications'),
('Service des Affaires Financières', 'SAF', 'Service des affaires financières'),
('Direction des Contrôles', 'DC', 'Direction des contrôles');

-- Insert default bureaus
INSERT INTO bureaus (nom, code, description, departement_id) VALUES
('Bureau des Permis', 'BP', 'Bureau de délivrance des permis', (SELECT id FROM departments WHERE code = 'SEV')),
('Bureau des Cartes Grises', 'BCG', 'Bureau des cartes grises', (SELECT id FROM departments WHERE code = 'SEV')),
('Bureau des Licences', 'BL', 'Bureau des licences de transport', (SELECT id FROM departments WHERE code = 'DC'));

-- Insert sample auto-écoles
INSERT INTO auto_ecoles (nom, code, adresse, telephone, email, statut) VALUES
('Auto-École Excellence', 'AE001', 'Libreville, Quartier Montagne Sainte', '+241 01 23 45 67', 'excellence@autoecole.ga', 'VALIDE'),
('Auto-École Pro', 'AE002', 'Libreville, Quartier Glass', '+241 01 23 45 68', 'pro@autoecole.ga', 'VALIDE');

-- Insert sample document types
INSERT INTO document_types (nom, code, service_code, categorie, delai_traitement_jours, frais_obligatoire, montant_frais) VALUES
('Permis de Conduire', 'PERMIS_CONDUIRE', 'permis', 'principal', 30, true, 15000),
('Duplicata Permis de Conduire', 'DUPLICATA_PERMIS', 'permis', 'connexe', 15, true, 5000),
('Renouvellement Permis de Conduire', 'RENOUVELLEMENT_PERMIS', 'permis', 'connexe', 20, true, 10000),
('Carte Grise', 'CARTE_GRISE', 'carte-grise', 'principal', 7, true, 25000),
('Duplicata Carte Grise', 'DUPLICATA_CARTE_GRISE', 'carte-grise', 'connexe', 5, true, 8000),
('Licence de Transport', 'LICENCE_TRANSPORT', 'transport', 'principal', 45, true, 50000);

-- Insert sample process steps for Permis de Conduire
INSERT INTO process_steps (document_type_id, nom, code, ordre, role_requis, type_validation, delai_max_jours, obligatoire) 
SELECT dt.id, 'Réception de la demande', 'RECEPTION', 1, 'SEV', 'manuelle', 1, true
FROM document_types dt WHERE dt.code = 'PERMIS_CONDUIRE';

INSERT INTO process_steps (document_type_id, nom, code, ordre, role_requis, type_validation, delai_max_jours, obligatoire) 
SELECT dt.id, 'Vérification des documents', 'VERIFICATION_DOCS', 2, 'SEV', 'manuelle', 2, true
FROM document_types dt WHERE dt.code = 'PERMIS_CONDUIRE';

INSERT INTO process_steps (document_type_id, nom, code, ordre, role_requis, type_validation, delai_max_jours, obligatoire) 
SELECT dt.id, 'Validation par le Chef de Service', 'VALIDATION_CHEF', 3, 'DC', 'manuelle', 3, true
FROM document_types dt WHERE dt.code = 'PERMIS_CONDUIRE';

INSERT INTO process_steps (document_type_id, nom, code, ordre, role_requis, type_validation, delai_max_jours, obligatoire) 
SELECT dt.id, 'Approbation finale', 'APPROBATION_FINALE', 4, 'DGTT', 'manuelle', 5, true
FROM document_types dt WHERE dt.code = 'PERMIS_CONDUIRE';

-- ==============================================
-- SUCCESS MESSAGE
-- ==============================================

-- Display success message
DO $$
BEGIN
    RAISE NOTICE '==============================================';
    RAISE NOTICE 'R-DGTT Portail Database Initialized Successfully!';
    RAISE NOTICE '==============================================';
    RAISE NOTICE 'Tables created: users, departments, bureaus, auto_ecoles, candidats, permis, document_types, process_steps, workflow_instances, workflow_step_executions';
    RAISE NOTICE 'Sample data inserted: admin user, departments, bureaus, auto-écoles, document types, process steps';
    RAISE NOTICE 'Default admin: admin@rdgtt.ga / admin123';
    RAISE NOTICE '==============================================';
END $$;