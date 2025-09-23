-- R-DGTT User Interface System Schema
-- Enhanced tracking, notifications, and payment management

-- ==============================================
-- USER NOTIFICATIONS SYSTEM
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
    priorite VARCHAR(20) DEFAULT 'NORMAL', -- LOW, NORMAL, HIGH, URGENT
    action_requise BOOLEAN DEFAULT false,
    action_url VARCHAR(500),
    action_text VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ==============================================
-- PAYMENT TRACKING SYSTEM
-- ==============================================

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
    candidat_id UUID REFERENCES candidats(id),
    demande_id VARCHAR(255) NOT NULL, -- Reference to main request
    document_type_id UUID REFERENCES document_types(id),
    montant DECIMAL(10,2) NOT NULL,
    devise VARCHAR(3) DEFAULT 'XAF',
    payment_method_id UUID REFERENCES payment_methods(id),
    statut VARCHAR(50) DEFAULT 'EN_ATTENTE', -- EN_ATTENTE, PAYE, ECHEC, REMBOURSE
    reference_paiement VARCHAR(255),
    reference_externe VARCHAR(255),
    date_paiement TIMESTAMP,
    date_expiration TIMESTAMP,
    donnees_paiement TEXT, -- JSON payment data
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ==============================================
-- DOCUMENT MANAGEMENT SYSTEM
-- ==============================================

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
    candidat_id UUID REFERENCES candidats(id) ON DELETE CASCADE,
    demande_id VARCHAR(255) NOT NULL,
    document_category_id UUID REFERENCES document_categories(id),
    nom_fichier VARCHAR(255) NOT NULL,
    nom_original VARCHAR(255) NOT NULL,
    chemin_fichier VARCHAR(500) NOT NULL,
    taille_fichier BIGINT,
    type_mime VARCHAR(100),
    statut VARCHAR(50) DEFAULT 'EN_ATTENTE', -- EN_ATTENTE, VALIDE, REJETE, EXPIRED
    commentaires TEXT,
    date_validation TIMESTAMP,
    valide_par UUID REFERENCES users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ==============================================
-- APPLICATION TRACKING SYSTEM
-- ==============================================

-- Application statuses
CREATE TABLE application_statuses (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    code VARCHAR(50) UNIQUE NOT NULL,
    nom VARCHAR(255) NOT NULL,
    description TEXT,
    couleur VARCHAR(7) DEFAULT '#007bff', -- Hex color
    ordre INTEGER DEFAULT 0,
    actif BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- User applications
CREATE TABLE user_applications (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    candidat_id UUID REFERENCES candidats(id) ON DELETE CASCADE,
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
    donnees_demande TEXT, -- JSON request data
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

-- ==============================================
-- USER PREFERENCES
-- ==============================================

-- User preferences
CREATE TABLE user_preferences (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    notifications_email BOOLEAN DEFAULT true,
    notifications_push BOOLEAN DEFAULT true,
    notifications_sms BOOLEAN DEFAULT false, -- Disabled by default for cost savings
    langue VARCHAR(5) DEFAULT 'fr',
    theme VARCHAR(20) DEFAULT 'light',
    timezone VARCHAR(50) DEFAULT 'Africa/Libreville',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ==============================================
-- INDEXES FOR PERFORMANCE
-- ==============================================

-- Notifications indexes
CREATE INDEX idx_user_notifications_user ON user_notifications(user_id);
CREATE INDEX idx_user_notifications_lu ON user_notifications(lu);
CREATE INDEX idx_user_notifications_created ON user_notifications(created_at);

-- Payments indexes
CREATE INDEX idx_payments_user ON payments(user_id);
CREATE INDEX idx_payments_candidat ON payments(candidat_id);
CREATE INDEX idx_payments_demande ON payments(demande_id);
CREATE INDEX idx_payments_statut ON payments(statut);

-- Documents indexes
CREATE INDEX idx_user_documents_user ON user_documents(user_id);
CREATE INDEX idx_user_documents_candidat ON user_documents(candidat_id);
CREATE INDEX idx_user_documents_demande ON user_documents(demande_id);
CREATE INDEX idx_user_documents_statut ON user_documents(statut);

-- Applications indexes
CREATE INDEX idx_user_applications_user ON user_applications(user_id);
CREATE INDEX idx_user_applications_candidat ON user_applications(candidat_id);
CREATE INDEX idx_user_applications_numero ON user_applications(numero_demande);
CREATE INDEX idx_user_applications_statut ON user_applications(statut_id);

-- ==============================================
-- SAMPLE DATA
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

DO $$
BEGIN
    RAISE NOTICE '==============================================';
    RAISE NOTICE 'R-DGTT User Interface System Schema Created!';
    RAISE NOTICE '==============================================';
    RAISE NOTICE 'New tables: notification_types, user_notifications, payment_methods, payments, document_categories, user_documents, application_statuses, user_applications, application_history, user_preferences';
    RAISE NOTICE 'Features: In-app notifications, payment tracking, document management, application tracking';
    RAISE NOTICE 'Cost savings: SMS notifications disabled by default';
    RAISE NOTICE '==============================================';
END $$;
