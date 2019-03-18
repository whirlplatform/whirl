CREATE OR REPLACE FUNCTION add_parameter_list (
    p_result function_result,
    p_code varchar,
    p_list_title varchar,
    p_list_value varchar)
RETURNS function_result
AS
$$
DECLARE
    v_index integer;
BEGIN
    v_index := internal_next_index (p_result);

    IF (SELECT count (*)
          FROM skeys (p_result.parameter_index)) > 0
    THEN
        p_result.parameter_index := p_result.parameter_index::hstore || hstore (v_index::varchar, p_code);
    ELSE
        p_result.parameter_index := hstore (v_index::varchar, p_code);
    END IF;

    IF (SELECT count (*)
          FROM skeys (p_result.parameter_list_title)) > 0
    THEN
        p_result.parameter_list_title := p_result.parameter_list_title::hstore || hstore (p_code, p_list_title);
    ELSE
        p_result.parameter_list_title := hstore (p_code, p_list_title);
    END IF;

    IF (SELECT count (*)
          FROM skeys (p_result.parameter_value)) > 0
    THEN
        p_result.parameter_value := p_result.parameter_value::hstore || hstore (p_code, p_list_value);
    ELSE
        p_result.parameter_value := hstore (p_code, p_list_value);
    END IF;

    IF (SELECT count (*)
          FROM skeys (p_result.parameter_type)) > 0
    THEN
        p_result.parameter_type := p_result.parameter_type::hstore || hstore (p_code, 'LIST');
    ELSE
        p_result.parameter_type := hstore (p_code, 'LIST');
    END IF;

    RETURN p_result;
END;
$$
LANGUAGE plpgsql
