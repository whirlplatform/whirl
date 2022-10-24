DO $$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'index_map') THEN
            CREATE TYPE index_map AS (
                map_element _varchar);
        END IF;
    END$$;