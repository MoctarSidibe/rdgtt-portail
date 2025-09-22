-- R-DGTT Admin System Schema
-- Configuration and Workflow Management

-- Document Types Table
CREATE TABLE document_types (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    nom VARCHAR(255) NOT NULL,
    code VARCHAR(100) UNIQUE NOT NULL,
    description TEXT,
    service_code VARCHAR(100) NOT NULL, -- auto-ecole, permis, carte-grise, etc.
    categorie VARCHAR(100), -- principal, connexe, administratif
    actif BOOLEAN DEFAULT true,
    delai_traitement_jours INTEGER,
    frais_obligatoire BOOLEAN DEFAULT false,
    montant_frais DECIMAL(10,2),
    documents_requis TEXT, -- JSON array of required documents
    conditions_eligibilite TEXT, -- JSON conditions
    workflow_config TEXT, -- JSON workflow configuration
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
    role_requis VARCHAR(100), -- ADMIN, DC, SEV, SAF, etc.
    type_validation VARCHAR(50), -- automatique, manuelle, hybride
    conditions_validation TEXT, -- JSON conditions
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
    demande_id VARCHAR(255) NOT NULL, -- Reference to main request
    document_type_id UUID REFERENCES document_types(id),
    statut VARCHAR(50) NOT NULL, -- EN_ATTENTE, EN_COURS, VALIDE, REJETE, ANNULE
    etape_actuelle_id UUID,
    utilisateur_actuel_id UUID,
    departement_actuel_id UUID,
    bureau_actuel_id UUID,
    date_debut TIMESTAMP,
    date_fin TIMESTAMP,
    delai_max_jours INTEGER,
    commentaires TEXT,
    donnees_contexte TEXT, -- JSON context data
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Workflow Step Executions Table
CREATE TABLE workflow_step_executions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    workflow_instance_id UUID REFERENCES workflow_instances(id) ON DELETE CASCADE,
    process_step_id UUID REFERENCES process_steps(id),
    statut VARCHAR(50) NOT NULL, -- EN_ATTENTE, EN_COURS, VALIDE, REJETE, IGNORE
    utilisateur_id UUID,
    departement_id UUID,
    bureau_id UUID,
    date_debut TIMESTAMP,
    date_fin TIMESTAMP,
    commentaires TEXT,
    donnees_validation TEXT, -- JSON validation data
    decision VARCHAR(50), -- VALIDE, REJETE, REDIRIGE
    etape_suivante_id UUID,
    raison_rejet TEXT,
    delai_depasse BOOLEAN DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Indexes for performance
CREATE INDEX idx_document_types_service_code ON document_types(service_code);
CREATE INDEX idx_document_types_actif ON document_types(actif);
CREATE INDEX idx_process_steps_document_type ON process_steps(document_type_id);
CREATE INDEX idx_process_steps_ordre ON process_steps(document_type_id, ordre);
CREATE INDEX idx_workflow_instances_demande ON workflow_instances(demande_id);
CREATE INDEX idx_workflow_instances_statut ON workflow_instances(statut);
CREATE INDEX idx_workflow_instances_utilisateur ON workflow_instances(utilisateur_actuel_id);
CREATE INDEX idx_workflow_instances_departement ON workflow_instances(departement_actuel_id);
CREATE INDEX idx_workflow_instances_bureau ON workflow_instances(bureau_actuel_id);
CREATE INDEX idx_workflow_step_executions_workflow ON workflow_step_executions(workflow_instance_id);
CREATE INDEX idx_workflow_step_executions_process ON workflow_step_executions(process_step_id);
CREATE INDEX idx_workflow_step_executions_utilisateur ON workflow_step_executions(utilisateur_id);

-- Sample data for testing
INSERT INTO document_types (nom, code, service_code, categorie, delai_traitement_jours, frais_obligatoire, montant_frais) VALUES
('Permis de Conduire', 'PERMIS_CONDUIRE', 'permis', 'principal', 30, true, 15000),
('Duplicata Permis de Conduire', 'DUPLICATA_PERMIS', 'permis', 'connexe', 15, true, 5000),
('Renouvellement Permis de Conduire', 'RENOUVELLEMENT_PERMIS', 'permis', 'connexe', 20, true, 10000),
('Carte Grise', 'CARTE_GRISE', 'carte-grise', 'principal', 7, true, 25000),
('Duplicata Carte Grise', 'DUPLICATA_CARTE_GRISE', 'carte-grise', 'connexe', 5, true, 8000),
('Licence de Transport', 'LICENCE_TRANSPORT', 'transport', 'principal', 45, true, 50000);

-- Sample process steps for Permis de Conduire
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
