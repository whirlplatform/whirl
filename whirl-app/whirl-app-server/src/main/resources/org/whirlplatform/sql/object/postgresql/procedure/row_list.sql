CREATE TYPE row_list AS (
    list_name _row_value);
/*
DO $$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'row_list') THEN
            CREATE TYPE row_list AS (
                list_name _row_value);
        END IF;
    END$$;*/