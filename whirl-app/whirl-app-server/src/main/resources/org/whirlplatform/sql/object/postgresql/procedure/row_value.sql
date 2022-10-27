CREATE TYPE row_value AS
(
    id varchar(32767),
    selected boolean,
    checked boolean,
    expanded boolean
);

/*DO $$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'row_list') THEN
            CREATE TYPE row_value AS
            (
                id varchar(32767),
                selected boolean,
                checked boolean,
                expanded boolean
            );
        END IF;
    END$$;*/