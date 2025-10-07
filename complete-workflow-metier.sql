-- R-DGTT Complete Workflow Metier
-- Auto-École → Candidate → Permis de Conduire + Connexes

-- ==============================================
-- COMPLETE WORKFLOW METIER FOR DRIVING PERMITS
-- ==============================================

-- 1. AUTO-ÉCOLE REGISTRATION WORKFLOW
INSERT INTO document_types (nom, code, service_code, categorie, delai_traitement_jours, documents_requis, conditions_eligibilite) VALUES
('Inscription Auto-École', 'INSCRIPTION_AUTO_ECOLE', 'auto-ecole', 'principal', 15, 
 '["Statuts de l''auto-école", "Agrément", "Liste des véhicules", "Liste des moniteurs", "Plan de formation"]',
 '{"experience_min": 2, "vehicules_min": 3, "moniteurs_min": 2}');

-- Auto-École Registration Process Steps
INSERT INTO process_steps (document_type_id, nom, code, ordre, role_requis, type_validation, delai_max_jours, obligatoire) 
SELECT dt.id, 'Réception de la demande d''inscription', 'RECEPTION_INSCRIPTION', 1, 'AGENT', 'manuelle', 1, true
FROM document_types dt WHERE dt.code = 'INSCRIPTION_AUTO_ECOLE';

INSERT INTO process_steps (document_type_id, nom, code, ordre, role_requis, type_validation, delai_max_jours, obligatoire) 
SELECT dt.id, 'Vérification des documents', 'VERIFICATION_DOCS_AUTO_ECOLE', 2, 'CHEF_BUREAU', 'manuelle', 3, true
FROM document_types dt WHERE dt.code = 'INSCRIPTION_AUTO_ECOLE';

INSERT INTO process_steps (document_type_id, nom, code, ordre, role_requis, type_validation, delai_max_jours, obligatoire) 
SELECT dt.id, 'Inspection des locaux et véhicules', 'INSPECTION_LOCAUX', 3, 'CHEF_SERVICE', 'manuelle', 5, true
FROM document_types dt WHERE dt.code = 'INSCRIPTION_AUTO_ECOLE';

INSERT INTO process_steps (document_type_id, nom, code, ordre, role_requis, type_validation, delai_max_jours, obligatoire) 
SELECT dt.id, 'Validation par le Directeur', 'VALIDATION_DIRECTEUR', 4, 'DIRECTEUR', 'manuelle', 3, true
FROM document_types dt WHERE dt.code = 'INSCRIPTION_AUTO_ECOLE';

INSERT INTO process_steps (document_type_id, nom, code, ordre, role_requis, type_validation, delai_max_jours, obligatoire) 
SELECT dt.id, 'Approbation finale DGTT', 'APPROBATION_DGTT_AUTO_ECOLE', 5, 'DGTT', 'manuelle', 3, true
FROM document_types dt WHERE dt.code = 'INSCRIPTION_AUTO_ECOLE';

-- 2. CANDIDATE REGISTRATION WORKFLOW
INSERT INTO document_types (nom, code, service_code, categorie, delai_traitement_jours, documents_requis, conditions_eligibilite) VALUES
('Inscription Candidat', 'INSCRIPTION_CANDIDAT', 'auto-ecole', 'principal', 7, 
 '["CNI", "Photo d''identité", "Certificat médical", "Attestation de domicile", "Justificatif de formation"]',
 '{"age_min": 18, "auto_ecole_valide": true, "certificat_medical_valide": true}');

-- Candidate Registration Process Steps
INSERT INTO process_steps (document_type_id, nom, code, ordre, role_requis, type_validation, delai_max_jours, obligatoire) 
SELECT dt.id, 'Réception de la demande candidat', 'RECEPTION_CANDIDAT', 1, 'AGENT', 'manuelle', 1, true
FROM document_types dt WHERE dt.code = 'INSCRIPTION_CANDIDAT';

INSERT INTO process_steps (document_type_id, nom, code, ordre, role_requis, type_validation, delai_max_jours, obligatoire) 
SELECT dt.id, 'Vérification des documents candidat', 'VERIFICATION_DOCS_CANDIDAT', 2, 'CHEF_BUREAU', 'manuelle', 2, true
FROM document_types dt WHERE dt.code = 'INSCRIPTION_CANDIDAT';

INSERT INTO process_steps (document_type_id, nom, code, ordre, role_requis, type_validation, delai_max_jours, obligatoire) 
SELECT dt.id, 'Validation par le Chef de Service', 'VALIDATION_CHEF_CANDIDAT', 3, 'CHEF_SERVICE', 'manuelle', 2, true
FROM document_types dt WHERE dt.code = 'INSCRIPTION_CANDIDAT';

INSERT INTO process_steps (document_type_id, nom, code, ordre, role_requis, type_validation, delai_max_jours, obligatoire) 
SELECT dt.id, 'Génération du numéro de dossier', 'GENERATION_DOSSIER', 4, 'AGENT', 'automatique', 1, true
FROM document_types dt WHERE dt.code = 'INSCRIPTION_CANDIDAT';

-- 3. PERMIS DE CONDUIRE WORKFLOW (Main Process)
INSERT INTO document_types (nom, code, service_code, categorie, delai_traitement_jours, documents_requis, conditions_eligibilite) VALUES
('Permis de Conduire', 'PERMIS_CONDUIRE', 'permis', 'principal', 30, 
 '["Dossier candidat complet", "Certificat de formation", "Résultats examens", "Certificat médical valide"]',
 '{"candidat_admis": true, "formation_complete": true, "examens_reussis": true}');

-- Permis de Conduire Process Steps
INSERT INTO process_steps (document_type_id, nom, code, ordre, role_requis, type_validation, delai_max_jours, obligatoire) 
SELECT dt.id, 'Réception de la demande permis', 'RECEPTION_PERMIS', 1, 'AGENT', 'manuelle', 1, true
FROM document_types dt WHERE dt.code = 'PERMIS_CONDUIRE';

INSERT INTO process_steps (document_type_id, nom, code, ordre, role_requis, type_validation, delai_max_jours, obligatoire) 
SELECT dt.id, 'Vérification du dossier candidat', 'VERIFICATION_DOSSIER', 2, 'CHEF_BUREAU', 'manuelle', 3, true
FROM document_types dt WHERE dt.code = 'PERMIS_CONDUIRE';

INSERT INTO process_steps (document_type_id, nom, code, ordre, role_requis, type_validation, delai_max_jours, obligatoire) 
SELECT dt.id, 'Validation des examens', 'VALIDATION_EXAMENS', 3, 'CHEF_SERVICE', 'manuelle', 5, true
FROM document_types dt WHERE dt.code = 'PERMIS_CONDUIRE';

INSERT INTO process_steps (document_type_id, nom, code, ordre, role_requis, type_validation, delai_max_jours, obligatoire) 
SELECT dt.id, 'Contrôle final par le Directeur', 'CONTROLE_DIRECTEUR', 4, 'DIRECTEUR', 'manuelle', 3, true
FROM document_types dt WHERE dt.code = 'PERMIS_CONDUIRE';

INSERT INTO process_steps (document_type_id, nom, code, ordre, role_requis, type_validation, delai_max_jours, obligatoire) 
SELECT dt.id, 'Approbation finale DGTT', 'APPROBATION_DGTT_PERMIS', 5, 'DGTT', 'manuelle', 5, true
FROM document_types dt WHERE dt.code = 'PERMIS_CONDUIRE';

INSERT INTO process_steps (document_type_id, nom, code, ordre, role_requis, type_validation, delai_max_jours, obligatoire) 
SELECT dt.id, 'Génération du permis', 'GENERATION_PERMIS', 6, 'AGENT', 'automatique', 2, true
FROM document_types dt WHERE dt.code = 'PERMIS_CONDUIRE';

-- 4. CONNEXES (Related Services)

-- 4.1 Duplicata Permis de Conduire
INSERT INTO document_types (nom, code, service_code, categorie, delai_traitement_jours, documents_requis, conditions_eligibilite) VALUES
('Duplicata Permis de Conduire', 'DUPLICATA_PERMIS', 'permis', 'connexe', 7, 
 '["Déclaration de perte/vol", "CNI", "Photo d''identité", "Justificatif de domicile"]',
 '{"permis_original_existe": true, "declaration_valide": true}');

INSERT INTO process_steps (document_type_id, nom, code, ordre, role_requis, type_validation, delai_max_jours, obligatoire) 
SELECT dt.id, 'Réception demande duplicata', 'RECEPTION_DUPLICATA', 1, 'AGENT', 'manuelle', 1, true
FROM document_types dt WHERE dt.code = 'DUPLICATA_PERMIS';

INSERT INTO process_steps (document_type_id, nom, code, ordre, role_requis, type_validation, delai_max_jours, obligatoire) 
SELECT dt.id, 'Vérification de l''original', 'VERIFICATION_ORIGINAL', 2, 'CHEF_BUREAU', 'manuelle', 2, true
FROM document_types dt WHERE dt.code = 'DUPLICATA_PERMIS';

INSERT INTO process_steps (document_type_id, nom, code, ordre, role_requis, type_validation, delai_max_jours, obligatoire) 
SELECT dt.id, 'Validation par le Chef de Service', 'VALIDATION_CHEF_DUPLICATA', 3, 'CHEF_SERVICE', 'manuelle', 2, true
FROM document_types dt WHERE dt.code = 'DUPLICATA_PERMIS';

INSERT INTO process_steps (document_type_id, nom, code, ordre, role_requis, type_validation, delai_max_jours, obligatoire) 
SELECT dt.id, 'Génération du duplicata', 'GENERATION_DUPLICATA', 4, 'AGENT', 'automatique', 2, true
FROM document_types dt WHERE dt.code = 'DUPLICATA_PERMIS';

-- 4.2 Renouvellement Permis de Conduire
INSERT INTO document_types (nom, code, service_code, categorie, delai_traitement_jours, documents_requis, conditions_eligibilite) VALUES
('Renouvellement Permis de Conduire', 'RENOUVELLEMENT_PERMIS', 'permis', 'connexe', 15, 
 '["Permis expiré", "Certificat médical", "Photo d''identité", "Justificatif de domicile"]',
 '{"permis_expire": true, "certificat_medical_valide": true}');

INSERT INTO process_steps (document_type_id, nom, code, ordre, role_requis, type_validation, delai_max_jours, obligatoire) 
SELECT dt.id, 'Réception demande renouvellement', 'RECEPTION_RENOUVELLEMENT', 1, 'AGENT', 'manuelle', 1, true
FROM document_types dt WHERE dt.code = 'RENOUVELLEMENT_PERMIS';

INSERT INTO process_steps (document_type_id, nom, code, ordre, role_requis, type_validation, delai_max_jours, obligatoire) 
SELECT dt.id, 'Vérification de l''ancien permis', 'VERIFICATION_ANCIEN_PERMIS', 2, 'CHEF_BUREAU', 'manuelle', 3, true
FROM document_types dt WHERE dt.code = 'RENOUVELLEMENT_PERMIS';

INSERT INTO process_steps (document_type_id, nom, code, ordre, role_requis, type_validation, delai_max_jours, obligatoire) 
SELECT dt.id, 'Validation médicale', 'VALIDATION_MEDICALE', 3, 'CHEF_SERVICE', 'manuelle', 5, true
FROM document_types dt WHERE dt.code = 'RENOUVELLEMENT_PERMIS';

INSERT INTO process_steps (document_type_id, nom, code, ordre, role_requis, type_validation, delai_max_jours, obligatoire) 
SELECT dt.id, 'Approbation par le Directeur', 'APPROBATION_DIRECTEUR_RENOUVELLEMENT', 4, 'DIRECTEUR', 'manuelle', 3, true
FROM document_types dt WHERE dt.code = 'RENOUVELLEMENT_PERMIS';

INSERT INTO process_steps (document_type_id, nom, code, ordre, role_requis, type_validation, delai_max_jours, obligatoire) 
SELECT dt.id, 'Génération du nouveau permis', 'GENERATION_NOUVEAU_PERMIS', 5, 'AGENT', 'automatique', 3, true
FROM document_types dt WHERE dt.code = 'RENOUVELLEMENT_PERMIS';

-- 4.3 Conversion Permis Étranger
INSERT INTO document_types (nom, code, service_code, categorie, delai_traitement_jours, documents_requis, conditions_eligibilite) VALUES
('Conversion Permis Étranger', 'CONVERSION_PERMIS_ETRANGER', 'permis', 'connexe', 20, 
 '["Permis étranger original", "Traduction certifiée", "Certificat médical", "CNI", "Photo d''identité"]',
 '{"permis_etranger_valide": true, "traduction_certifiee": true}');

INSERT INTO process_steps (document_type_id, nom, code, ordre, role_requis, type_validation, delai_max_jours, obligatoire) 
SELECT dt.id, 'Réception demande conversion', 'RECEPTION_CONVERSION', 1, 'AGENT', 'manuelle', 1, true
FROM document_types dt WHERE dt.code = 'CONVERSION_PERMIS_ETRANGER';

INSERT INTO process_steps (document_type_id, nom, code, ordre, role_requis, type_validation, delai_max_jours, obligatoire) 
SELECT dt.id, 'Vérification du permis étranger', 'VERIFICATION_PERMIS_ETRANGER', 2, 'CHEF_BUREAU', 'manuelle', 5, true
FROM document_types dt WHERE dt.code = 'CONVERSION_PERMIS_ETRANGER';

INSERT INTO process_steps (document_type_id, nom, code, ordre, role_requis, type_validation, delai_max_jours, obligatoire) 
SELECT dt.id, 'Validation par le Chef de Service', 'VALIDATION_CHEF_CONVERSION', 3, 'CHEF_SERVICE', 'manuelle', 5, true
FROM document_types dt WHERE dt.code = 'CONVERSION_PERMIS_ETRANGER';

INSERT INTO process_steps (document_type_id, nom, code, ordre, role_requis, type_validation, delai_max_jours, obligatoire) 
SELECT dt.id, 'Approbation par le Directeur', 'APPROBATION_DIRECTEUR_CONVERSION', 4, 'DIRECTEUR', 'manuelle', 5, true
FROM document_types dt WHERE dt.code = 'CONVERSION_PERMIS_ETRANGER';

INSERT INTO process_steps (document_type_id, nom, code, ordre, role_requis, type_validation, delai_max_jours, obligatoire) 
SELECT dt.id, 'Génération du permis gabonais', 'GENERATION_PERMIS_GABONAIS', 5, 'AGENT', 'automatique', 4, true
FROM document_types dt WHERE dt.code = 'CONVERSION_PERMIS_ETRANGER';

-- 4.4 Attestation d'Authenticité
INSERT INTO document_types (nom, code, service_code, categorie, delai_traitement_jours, documents_requis, conditions_eligibilite) VALUES
('Attestation d''Authenticité', 'ATTESTATION_AUTHENTICITE', 'permis', 'connexe', 3, 
 '["Permis à vérifier", "Demande d''attestation", "CNI"]',
 '{"permis_existe": true, "demande_valide": true}');

INSERT INTO process_steps (document_type_id, nom, code, ordre, role_requis, type_validation, delai_max_jours, obligatoire) 
SELECT dt.id, 'Réception demande attestation', 'RECEPTION_ATTESTATION', 1, 'AGENT', 'manuelle', 1, true
FROM document_types dt WHERE dt.code = 'ATTESTATION_AUTHENTICITE';

INSERT INTO process_steps (document_type_id, nom, code, ordre, role_requis, type_validation, delai_max_jours, obligatoire) 
SELECT dt.id, 'Vérification de l''authenticité', 'VERIFICATION_AUTHENTICITE', 2, 'CHEF_BUREAU', 'manuelle', 1, true
FROM document_types dt WHERE dt.code = 'ATTESTATION_AUTHENTICITE';

INSERT INTO process_steps (document_type_id, nom, code, ordre, role_requis, type_validation, delai_max_jours, obligatoire) 
SELECT dt.id, 'Génération de l''attestation', 'GENERATION_ATTESTATION', 3, 'AGENT', 'automatique', 1, true
FROM document_types dt WHERE dt.code = 'ATTESTATION_AUTHENTICITE';

-- ==============================================
-- SAMPLE WORKFLOW INSTANCES FOR TESTING
-- ==============================================

-- Sample Auto-École Registration
INSERT INTO workflow_instances (demande_id, document_type_id, statut, date_debut, commentaires) 
SELECT 'AE-2024-001', dt.id, 'EN_COURS', CURRENT_TIMESTAMP, 'Inscription Auto-École Excellence'
FROM document_types dt WHERE dt.code = 'INSCRIPTION_AUTO_ECOLE';

-- Sample Candidate Registration
INSERT INTO workflow_instances (demande_id, document_type_id, statut, date_debut, commentaires) 
SELECT 'CAND-2024-001', dt.id, 'EN_COURS', CURRENT_TIMESTAMP, 'Inscription candidat Jean Dupont'
FROM document_types dt WHERE dt.code = 'INSCRIPTION_CANDIDAT';

-- Sample Permis de Conduire
INSERT INTO workflow_instances (demande_id, document_type_id, statut, date_debut, commentaires) 
SELECT 'PERMIS-2024-001', dt.id, 'EN_COURS', CURRENT_TIMESTAMP, 'Demande permis candidat Jean Dupont'
FROM document_types dt WHERE dt.code = 'PERMIS_CONDUIRE';

-- Sample Duplicata
INSERT INTO workflow_instances (demande_id, document_type_id, statut, date_debut, commentaires) 
SELECT 'DUP-2024-001', dt.id, 'EN_COURS', CURRENT_TIMESTAMP, 'Demande duplicata permis perdu'
FROM document_types dt WHERE dt.code = 'DUPLICATA_PERMIS';

-- ==============================================
-- SUCCESS MESSAGE
-- ==============================================

DO $$
BEGIN
    RAISE NOTICE '==============================================';
    RAISE NOTICE 'R-DGTT Complete Workflow Metier Initialized!';
    RAISE NOTICE '==============================================';
    RAISE NOTICE 'Workflows created:';
    RAISE NOTICE '1. Auto-École Registration (5 steps)';
    RAISE NOTICE '2. Candidate Registration (4 steps)';
    RAISE NOTICE '3. Permis de Conduire (6 steps)';
    RAISE NOTICE '4. Duplicata Permis (4 steps)';
    RAISE NOTICE '5. Renouvellement Permis (5 steps)';
    RAISE NOTICE '6. Conversion Permis Étranger (5 steps)';
    RAISE NOTICE '7. Attestation d''Authenticité (3 steps)';
    RAISE NOTICE 'Sample workflow instances created for testing';
    RAISE NOTICE '==============================================';
END $$;


