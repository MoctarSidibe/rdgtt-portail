-- Fix process_steps table schema to match ProcessStep entity

-- Rename delai_max_jours to delai_jours if it exists
DO $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name = 'process_steps' AND column_name = 'delai_max_jours'
    ) THEN
        ALTER TABLE process_steps RENAME COLUMN delai_max_jours TO delai_jours;
        RAISE NOTICE 'Renamed delai_max_jours to delai_jours';
    END IF;
END $$;

-- Add delai_jours if it doesn't exist
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name = 'process_steps' AND column_name = 'delai_jours'
    ) THEN
        ALTER TABLE process_steps ADD COLUMN delai_jours INTEGER DEFAULT 5;
        RAISE NOTICE 'Added delai_jours column';
    END IF;
END $$;

-- Remove columns that are not in the entity model
DO $$
BEGIN
    -- Remove obligatoire if it exists (not in entity)
    IF EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name = 'process_steps' AND column_name = 'obligatoire'
    ) THEN
        ALTER TABLE process_steps DROP COLUMN obligatoire;
        RAISE NOTICE 'Dropped obligatoire column';
    END IF;

    -- Remove peut_rejeter if it exists (not in entity)
    IF EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name = 'process_steps' AND column_name = 'peut_rejeter'
    ) THEN
        ALTER TABLE process_steps DROP COLUMN peut_rejeter;
        RAISE NOTICE 'Dropped peut_rejeter column';
    END IF;

    -- Remove peut_rediriger if it exists (not in entity)
    IF EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name = 'process_steps' AND column_name = 'peut_rediriger'
    ) THEN
        ALTER TABLE process_steps DROP COLUMN peut_rediriger;
        RAISE NOTICE 'Dropped peut_rediriger column';
    END IF;
END $$;

SELECT 'Process steps table schema fixed successfully!' AS status;

