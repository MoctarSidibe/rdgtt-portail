-- Migrate auto-ecole examens.id from BIGSERIAL (BIGINT) to UUID
-- Safe to run multiple times

DO $$
BEGIN
    -- Ensure uuid extension is available
    PERFORM 1 FROM pg_extension WHERE extname = 'uuid-ossp';
    IF NOT FOUND THEN
        CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
    END IF;
END $$;

DO $$
DECLARE
    pk_name text;
BEGIN
    -- Add temporary UUID column if not exists
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name = 'examens' AND column_name = 'id_uuid'
    ) THEN
        ALTER TABLE examens ADD COLUMN id_uuid UUID;
        UPDATE examens SET id_uuid = uuid_generate_v4() WHERE id_uuid IS NULL;
        ALTER TABLE examens ALTER COLUMN id_uuid SET NOT NULL;
    END IF;

    -- Drop primary key constraint if present
    SELECT tc.constraint_name INTO pk_name
    FROM information_schema.table_constraints tc
    WHERE tc.table_name = 'examens' AND tc.constraint_type = 'PRIMARY KEY'
    LIMIT 1;

    IF pk_name IS NOT NULL THEN
        EXECUTE format('ALTER TABLE examens DROP CONSTRAINT %I', pk_name);
    END IF;

    -- Drop old id column if still bigint
    IF EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name = 'examens' AND column_name = 'id' AND data_type IN ('bigint', 'integer')
    ) THEN
        ALTER TABLE examens DROP COLUMN id;
    END IF;

    -- Rename id_uuid -> id if needed
    IF EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name = 'examens' AND column_name = 'id_uuid'
    ) AND NOT EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name = 'examens' AND column_name = 'id' AND data_type = 'uuid'
    ) THEN
        ALTER TABLE examens RENAME COLUMN id_uuid TO id;
    END IF;

    -- Recreate primary key
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.table_constraints 
        WHERE table_name = 'examens' AND constraint_type = 'PRIMARY KEY'
    ) THEN
        ALTER TABLE examens ADD CONSTRAINT examens_pkey PRIMARY KEY (id);
    END IF;

END $$;

-- Clean up any sequence left over (ignore if not exists)
DO $$
BEGIN
    PERFORM 1 FROM pg_class WHERE relkind = 'S' AND relname = 'examens_id_seq';
    IF FOUND THEN
        DROP SEQUENCE IF EXISTS examens_id_seq;
    END IF;
END $$;

SELECT 'examens.id migrated to UUID successfully' AS status;


