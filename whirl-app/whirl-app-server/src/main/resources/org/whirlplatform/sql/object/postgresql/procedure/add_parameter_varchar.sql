/**
 * Добавляет к переменной типа "function_result" значения "STRING" из переданных  переменных
 *
 * @param p_result          Переменная типа "function_result"
 * @param p_code            Переменная типа "varchar" без ограничений
 * @param p_value           Переменная типа "varchar" без ограничений
 *
 * @return Возвращает переданная переменная типа "function_result" с новыми значениями
 */
CREATE OR REPLACE FUNCTION add_parameter_varchar (
    p_result function_result,
    p_code varchar,
    p_value varchar)
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
          FROM skeys (p_result.parameter_value)) > 0
    THEN
        p_result.parameter_value := p_result.parameter_value::hstore || hstore (p_code, p_value);
    ELSE
        p_result.parameter_value := hstore (p_code, p_value);
    END IF;

    IF (SELECT count (*)
          FROM skeys (p_result.parameter_type)) > 0
    THEN
        p_result.parameter_type := p_result.parameter_type::hstore || hstore (p_code, 'STRING');
    ELSE
        p_result.parameter_type := hstore (p_code, 'STRING');
    END IF;

    RETURN p_result;
END;
$$
LANGUAGE plpgsql
