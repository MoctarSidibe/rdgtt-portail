-- Manual migration script to fix auto_ecole_documents table
-- Run this directly on your PostgreSQL database

-- Add the missing column that's causing the error
ALTER TABLE auto_ecole_documents 
ADD COLUMN IF NOT EXISTS taille_fichier BIGINT;

-- Add other missing columns that might be needed
ALTER TABLE auto_ecole_documents 
ADD COLUMN IF NOT EXISTS mime_type VARCHAR(100);

-- Rename columns to match entity model
ALTER TABLE auto_ecole_documents 
RENAME COLUMN nom_document TO nom_fichier;

ALTER TABLE auto_ecole_documents 
RENAME COLUMN created_at TO uploaded_at;

-- Remove statut column if it exists (not in entity model)
ALTER TABLE auto_ecole_documents 
DROP COLUMN IF EXISTS statut;

-- Also fix candidat_documents table if it has the same issue
ALTER TABLE candidat_documents 
ADD COLUMN IF NOT EXISTS taille_fichier BIGINT;

ALTER TABLE candidat_documents 
ADD COLUMN IF NOT EXISTS mime_type VARCHAR(100);

ALTER TABLE candidat_documents 
RENAME COLUMN nom_document TO nom_fichier;

ALTER TABLE candidat_documents 
RENAME COLUMN created_at TO uploaded_at;

ALTER TABLE candidat_documents 
DROP COLUMN IF EXISTS statut;

-- Success message
DO $$
BEGIN
    RAISE NOTICE 'Migration completed successfully!';
    RAISE NOTICE 'Added taille_fichier and mime_type columns to document tables';
    RAISE NOTICE 'Renamed columns to match entity models';
END $$;
