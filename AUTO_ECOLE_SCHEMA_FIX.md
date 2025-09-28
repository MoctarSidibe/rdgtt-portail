# Auto-Ecole Service Schema Fix

## Issue Description

The auto-ecole service is failing to start due to database schema mismatches between the JPA entity models and the actual database tables.

### Error
```
Schema-validation: missing column [taille_fichier] in table [auto_ecole_documents]
```

## Root Cause

The JPA entity models in the auto-ecole service expect certain columns that don't exist in the database schema:

1. **auto_ecole_documents table**: Missing `taille_fichier`, `mime_type` columns
2. **candidat_documents table**: Missing `taille_fichier`, `mime_type` columns  
3. **auto_ecoles table**: Missing `directeur_nom`, `directeur_telephone`, `numero_agrement`, `date_agrement`, `date_expiration`, `user_id` columns
4. **candidats table**: Missing `nom_famille`, `nom_jeune_fille`, `nationalite`, `photo`, `numero_dossier`, `numero_license`, `commentaire` columns

## Solution

### Option 1: Apply Database Migration (Recommended)

Run the migration script to update the existing database:

```bash
# Make the script executable
chmod +x fix-auto-ecole-schema.sh

# Run the fix script
./fix-auto-ecole-schema.sh
```

### Option 2: Manual Database Update

If you have direct database access, run the migration SQL:

```bash
psql -U rdgtt_user -d rdgtt_portail -f database/migration-fix-documents.sql
```

### Option 3: Recreate Database

If you can recreate the database, use the updated schema:

1. Drop the existing database
2. Run the updated `database/init.sql` script
3. Restart all services

## Files Modified

- `database/migration-fix-documents.sql` - Migration script for existing databases
- `fix-auto-ecole-schema.sh` - Automated fix script

## Verification

After applying the fix, verify the auto-ecole service starts successfully:

```bash
docker compose logs auto-ecole-service
```

The service should start without schema validation errors.

## Entity Model Changes

The following entity models were analyzed and their expected database columns documented:

- `AutoEcoleDocument` - Document management for auto-ecoles
- `CandidatDocument` - Document management for candidates  
- `AutoEcole` - Auto-ecole business entity
- `Candidat` - Candidate/student entity

All database tables now match the JPA entity field mappings.