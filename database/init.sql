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
    adresse TEXT NOT NULL,
    telephone VARCHAR(20),
    email VARCHAR(255),
    directeur_nom VARCHAR(255) NOT NULL,
    directeur_telephone VARCHAR(20),
    statut VARCHAR(50) DEFAULT 'EN_ATTENTE',
    numero_agrement VARCHAR(100),
    date_agrement DATE,
    date_expiration DATE,
    user_id UUID REFERENCES users(id) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Auto-école documents
CREATE TABLE auto_ecole_documents (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    auto_ecole_id UUID REFERENCES auto_ecoles(id) ON DELETE CASCADE,
    type_document VARCHAR(100) NOT NULL,
    nom_fichier VARCHAR(255) NOT NULL,
    chemin_fichier VARCHAR(500) NOT NULL,
    taille_fichier BIGINT,
    mime_type VARCHAR(100),
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Candidates table
CREATE TABLE candidats (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    nom_famille VARCHAR(100) NOT NULL,
    nom_jeune_fille VARCHAR(100),
    prenom VARCHAR(100) NOT NULL,
    date_naissance DATE NOT NULL,
    lieu_naissance VARCHAR(100) NOT NULL,
    nationalite VARCHAR(100) NOT NULL,
    telephone VARCHAR(20) NOT NULL,
    email VARCHAR(255),
    photo VARCHAR(500),
    auto_ecole_id UUID REFERENCES auto_ecoles(id) NOT NULL,
    statut VARCHAR(50) DEFAULT 'EN_ATTENTE',
    numero_dossier VARCHAR(100),
    numero_license VARCHAR(100),
    commentaire TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Candidate documents
CREATE TABLE candidat_documents (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    candidat_id UUID REFERENCES candidats(id) ON DELETE CASCADE,
    type_document VARCHAR(100) NOT NULL,
    nom_fichier VARCHAR(255) NOT NULL,
    chemin_fichier VARCHAR(500) NOT NULL,
    taille_fichier BIGINT,
    mime_type VARCHAR(100),
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
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
INSERT INTO auto_ecoles (nom, adresse, telephone, email, directeur_nom, directeur_telephone, statut, user_id) VALUES
('Auto-École Excellence', 'Libreville, Quartier Montagne Sainte', '+241 01 23 45 67', 'excellence@autoecole.ga', 'Jean Excellence', '+241 01 23 45 67', 'APPROUVE', (SELECT id FROM users WHERE email = 'admin@rdgtt.ga')),
('Auto-École Pro', 'Libreville, Quartier Glass', '+241 01 23 45 68', 'pro@autoecole.ga', 'Marie Pro', '+241 01 23 45 68', 'APPROUVE', (SELECT id FROM users WHERE email = 'admin@rdgtt.ga'));

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
-- USER INTERFACE SYSTEM TABLES
-- ==============================================

-- Notification types
CREATE TABLE notification_types (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    code VARCHAR(100) UNIQUE NOT NULL,
    nom VARCHAR(255) NOT NULL,
    description TEXT,
    template_subject TEXT,
    template_body TEXT,
    actif BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- User notifications
CREATE TABLE user_notifications (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    notification_type_id UUID REFERENCES notification_types(id),
    titre VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    lu BOOLEAN DEFAULT false,
    date_lecture TIMESTAMP,
    priorite VARCHAR(20) DEFAULT 'NORMAL',
    action_requise BOOLEAN DEFAULT false,
    action_url VARCHAR(500),
    action_text VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Payment methods
CREATE TABLE payment_methods (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    code VARCHAR(50) UNIQUE NOT NULL,
    nom VARCHAR(255) NOT NULL,
    description TEXT,
    actif BOOLEAN DEFAULT true,
    frais_pourcentage DECIMAL(5,2) DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Payment transactions
CREATE TABLE payments (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES users(id),
    application_id UUID,
    demande_id VARCHAR(255) NOT NULL,
    document_type_id UUID REFERENCES document_types(id),
    montant DECIMAL(10,2) NOT NULL,
    devise VARCHAR(3) DEFAULT 'XAF',
    payment_method_id UUID REFERENCES payment_methods(id),
    statut VARCHAR(50) DEFAULT 'EN_ATTENTE',
    reference_paiement VARCHAR(255),
    reference_externe VARCHAR(255),
    date_paiement TIMESTAMP,
    date_expiration TIMESTAMP,
    donnees_paiement TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Document categories
CREATE TABLE document_categories (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    code VARCHAR(50) UNIQUE NOT NULL,
    nom VARCHAR(255) NOT NULL,
    description TEXT,
    obligatoire BOOLEAN DEFAULT false,
    actif BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- User documents
CREATE TABLE user_documents (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    application_id UUID,
    demande_id VARCHAR(255) NOT NULL,
    document_category_id UUID REFERENCES document_categories(id),
    nom_fichier VARCHAR(255) NOT NULL,
    nom_original VARCHAR(255) NOT NULL,
    chemin_fichier VARCHAR(500) NOT NULL,
    taille_fichier BIGINT,
    type_mime VARCHAR(100),
    statut VARCHAR(50) DEFAULT 'EN_ATTENTE',
    commentaires TEXT,
    date_validation TIMESTAMP,
    valide_par UUID REFERENCES users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Application statuses
CREATE TABLE application_statuses (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    code VARCHAR(50) UNIQUE NOT NULL,
    nom VARCHAR(255) NOT NULL,
    description TEXT,
    couleur VARCHAR(7) DEFAULT '#007bff',
    ordre INTEGER DEFAULT 0,
    actif BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- User applications
CREATE TABLE user_applications (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    document_type_id UUID REFERENCES document_types(id),
    numero_demande VARCHAR(50) UNIQUE NOT NULL,
    statut_id UUID REFERENCES application_statuses(id),
    workflow_instance_id UUID REFERENCES workflow_instances(id),
    montant_total DECIMAL(10,2),
    montant_paye DECIMAL(10,2) DEFAULT 0.00,
    date_depot TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    date_traitement TIMESTAMP,
    date_fin TIMESTAMP,
    delai_estime_jours INTEGER,
    commentaires TEXT,
    donnees_demande TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Application history
CREATE TABLE application_history (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    application_id UUID REFERENCES user_applications(id) ON DELETE CASCADE,
    ancien_statut_id UUID REFERENCES application_statuses(id),
    nouveau_statut_id UUID REFERENCES application_statuses(id),
    commentaire TEXT,
    utilisateur_id UUID REFERENCES users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- User preferences
CREATE TABLE user_preferences (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    notifications_email BOOLEAN DEFAULT true,
    notifications_push BOOLEAN DEFAULT true,
    notifications_sms BOOLEAN DEFAULT false,
    langue VARCHAR(5) DEFAULT 'fr',
    theme VARCHAR(20) DEFAULT 'light',
    timezone VARCHAR(50) DEFAULT 'Africa/Libreville',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ==============================================
-- ADDITIONAL INDEXES FOR USER INTERFACE
-- ==============================================

-- Notifications indexes
CREATE INDEX idx_user_notifications_user ON user_notifications(user_id);
CREATE INDEX idx_user_notifications_lu ON user_notifications(lu);
CREATE INDEX idx_user_notifications_created ON user_notifications(created_at);

-- Payments indexes
CREATE INDEX idx_payments_user ON payments(user_id);
CREATE INDEX idx_payments_demande ON payments(demande_id);
CREATE INDEX idx_payments_statut ON payments(statut);

-- Documents indexes
CREATE INDEX idx_user_documents_user ON user_documents(user_id);
CREATE INDEX idx_user_documents_demande ON user_documents(demande_id);
CREATE INDEX idx_user_documents_statut ON user_documents(statut);

-- Applications indexes
CREATE INDEX idx_user_applications_user ON user_applications(user_id);
CREATE INDEX idx_user_applications_numero ON user_applications(numero_demande);
CREATE INDEX idx_user_applications_statut ON user_applications(statut_id);

-- ==============================================
-- USER INTERFACE SAMPLE DATA
-- ==============================================

-- Insert notification types
INSERT INTO notification_types (code, nom, description, template_subject, template_body) VALUES
('DEMANDE_DEPOSEE', 'Demande déposée', 'Notification quand une demande est déposée', 'Demande déposée - R-DGTT', 'Votre demande {{numero_demande}} a été déposée avec succès.'),
('DOCUMENT_VALIDE', 'Document validé', 'Notification quand un document est validé', 'Document validé - R-DGTT', 'Votre document {{nom_document}} a été validé.'),
('DOCUMENT_REJETE', 'Document rejeté', 'Notification quand un document est rejeté', 'Document rejeté - R-DGTT', 'Votre document {{nom_document}} a été rejeté. Motif: {{motif}}'),
('PAIEMENT_RECU', 'Paiement reçu', 'Notification quand un paiement est reçu', 'Paiement reçu - R-DGTT', 'Votre paiement de {{montant}} XAF a été reçu.'),
('STATUT_CHANGE', 'Changement de statut', 'Notification de changement de statut', 'Statut mis à jour - R-DGTT', 'Le statut de votre demande {{numero_demande}} est maintenant: {{nouveau_statut}}'),
('RAPPEL_DOCUMENT', 'Rappel document', 'Rappel pour document manquant', 'Document manquant - R-DGTT', 'Il manque des documents pour votre demande {{numero_demande}}.');

-- Insert payment methods
INSERT INTO payment_methods (code, nom, description, frais_pourcentage) VALUES
('MOBILE_MONEY', 'Mobile Money', 'Paiement par Mobile Money', 1.50),
('CARTE_BANCAIRE', 'Carte Bancaire', 'Paiement par carte bancaire', 2.00),
('VIREMENT', 'Virement Bancaire', 'Virement bancaire', 0.00),
('ESPECES', 'Espèces', 'Paiement en espèces', 0.00);

-- Insert document categories
INSERT INTO document_categories (code, nom, description, obligatoire) VALUES
('CNI', 'Carte Nationale d''Identité', 'Copie de la carte nationale d''identité', true),
('PHOTO', 'Photo d''identité', 'Photo d''identité récente', true),
('CERTIFICAT_MEDICAL', 'Certificat médical', 'Certificat médical de moins de 3 mois', true),
('JUSTIFICATIF_DOMICILE', 'Justificatif de domicile', 'Facture d''électricité ou d''eau récente', true),
('DIPLOME', 'Diplôme', 'Diplôme ou certificat de scolarité', false),
('AUTRE', 'Autre document', 'Autre document requis', false);

-- Insert application statuses
INSERT INTO application_statuses (code, nom, description, couleur, ordre) VALUES
('DEPOSEE', 'Déposée', 'Demande déposée et en attente de traitement', '#007bff', 1),
('EN_COURS', 'En cours', 'Demande en cours de traitement', '#ffc107', 2),
('DOCUMENTS_MANQUANTS', 'Documents manquants', 'Des documents sont manquants', '#fd7e14', 3),
('EN_ATTENTE_PAIEMENT', 'En attente de paiement', 'En attente du paiement des frais', '#dc3545', 4),
('VALIDATION', 'En validation', 'Demande en cours de validation', '#6f42c1', 5),
('VALIDE', 'Validée', 'Demande validée et approuvée', '#28a745', 6),
('REJETEE', 'Rejetée', 'Demande rejetée', '#dc3545', 7),
('TERMINEE', 'Terminée', 'Demande terminée et document délivré', '#20c997', 8);

-- Insert default user preferences for existing users
INSERT INTO user_preferences (user_id, notifications_email, notifications_push, notifications_sms)
SELECT id, true, true, false FROM users WHERE role = 'CITOYEN';

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
    RAISE NOTICE 'User Interface tables: notification_types, user_notifications, payment_methods, payments, document_categories, user_documents, application_statuses, user_applications, application_history, user_preferences';
    RAISE NOTICE 'Sample data inserted: admin user, departments, bureaus, auto-écoles, document types, process steps, notification types, payment methods, document categories, application statuses';
    RAISE NOTICE 'Default admin: admin@rdgtt.ga / admin123';
    RAISE NOTICE 'Features: In-app notifications, payment tracking, document management, application tracking';
    RAISE NOTICE 'Cost savings: SMS notifications disabled by default';
    RAISE NOTICE '==============================================';
END $$;