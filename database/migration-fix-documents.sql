-- Migration script to fix auto_ecole_documents and candidat_documents tables
-- This script adds missing columns and renames existing ones to match the entity models

-- ==============================================
-- FIX AUTO_ECOLES TABLE
-- ==============================================

-- Add missing columns to auto_ecoles
ALTER TABLE auto_ecoles 
ADD COLUMN IF NOT EXISTS directeur_nom VARCHAR(255),
ADD COLUMN IF NOT EXISTS directeur_telephone VARCHAR(20),
ADD COLUMN IF NOT EXISTS numero_agrement VARCHAR(100),
ADD COLUMN IF NOT EXISTS date_agrement DATE,
ADD COLUMN IF NOT EXISTS date_expiration DATE,
ADD COLUMN IF NOT EXISTS user_id UUID REFERENCES users(id);

-- ==============================================
-- FIX CANDIDATS TABLE
-- ==============================================

-- Add missing columns to candidats
ALTER TABLE candidats 
ADD COLUMN IF NOT EXISTS nom_famille VARCHAR(100),
ADD COLUMN IF NOT EXISTS nom_jeune_fille VARCHAR(100),
ADD COLUMN IF NOT EXISTS nationalite VARCHAR(100),
ADD COLUMN IF NOT EXISTS photo VARCHAR(500),
ADD COLUMN IF NOT EXISTS numero_dossier VARCHAR(100),
ADD COLUMN IF NOT EXISTS numero_license VARCHAR(100),
ADD COLUMN IF NOT EXISTS commentaire TEXT;

-- Rename existing columns to match entity
ALTER TABLE candidats 
RENAME COLUMN nom TO nom_famille;

-- ==============================================
-- FIX AUTO_ECOLE_DOCUMENTS TABLE
-- ==============================================

-- Add missing columns to auto_ecole_documents
ALTER TABLE auto_ecole_documents 
ADD COLUMN IF NOT EXISTS taille_fichier BIGINT,
ADD COLUMN IF NOT EXISTS mime_type VARCHAR(100);

-- Rename nom_document to nom_fichier
ALTER TABLE auto_ecole_documents 
RENAME COLUMN nom_document TO nom_fichier;

-- Rename created_at to uploaded_at
ALTER TABLE auto_ecole_documents 
RENAME COLUMN created_at TO uploaded_at;

-- Remove statut column if it exists (not in entity model)
ALTER TABLE auto_ecole_documents 
DROP COLUMN IF EXISTS statut;

-- ==============================================
-- FIX CANDIDAT_DOCUMENTS TABLE
-- ==============================================

-- Add missing columns to candidat_documents
ALTER TABLE candidat_documents 
ADD COLUMN IF NOT EXISTS taille_fichier BIGINT,
ADD COLUMN IF NOT EXISTS mime_type VARCHAR(100);

-- Rename nom_document to nom_fichier
ALTER TABLE candidat_documents 
RENAME COLUMN nom_document TO nom_fichier;

-- Rename created_at to uploaded_at
ALTER TABLE candidat_documents 
RENAME COLUMN created_at TO uploaded_at;

-- Remove statut column if it exists (not in entity model)
ALTER TABLE candidat_documents 
DROP COLUMN IF EXISTS statut;

-- ==============================================
-- SUCCESS MESSAGE
-- ==============================================

DO $$
BEGIN
    RAISE NOTICE '==============================================';
    RAISE NOTICE 'Database Migration Completed Successfully!';
    RAISE NOTICE '==============================================';
    RAISE NOTICE 'Fixed tables: auto_ecoles, candidats, auto_ecole_documents, candidat_documents';
    RAISE NOTICE 'Added columns to auto_ecoles: directeur_nom, directeur_telephone, numero_agrement, date_agrement, date_expiration, user_id';
    RAISE NOTICE 'Added columns to candidats: nom_famille, nom_jeune_fille, nationalite, photo, numero_dossier, numero_license, commentaire';
    RAISE NOTICE 'Added columns to documents: taille_fichier, mime_type';
    RAISE NOTICE 'Renamed columns: nom_document -> nom_fichier, created_at -> uploaded_at, nom -> nom_famille';
    RAISE NOTICE 'Removed columns: statut (not in entity models)';
    RAISE NOTICE '==============================================';
END $$;