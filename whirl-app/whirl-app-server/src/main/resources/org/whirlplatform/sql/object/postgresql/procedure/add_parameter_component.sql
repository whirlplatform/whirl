/**
 * Добавляет к параметру "parameter_component" переменной типа "function_result" значения из переданных  переменных
 *
 * @param p_result          Переменная типа "function_result"
 * @param p_code            Переменная типа "varchar" без ограничений
 * @param p_component_code  Переменная типа "varchar" без ограничений
 *
 * @return Возвращает переданная переменная типа "function_result" с новыми значениями
 */
CREATE OR REPLACE FUNCTION add_parameter_component (
    p_result function_result,
    p_code varchar,
    p_component_code varchar)
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
          FROM skeys (p_result.parameter_component)) > 0
    THEN
        p_result.parameter_component := p_result.parameter_component::hstore || hstore (p_code, p_component_code);
    ELSE
        p_result.parameter_component := hstore (p_code, p_component_code);
    END IF;

    RETURN p_result;
END;
$$
LANGUAGE plpgsql