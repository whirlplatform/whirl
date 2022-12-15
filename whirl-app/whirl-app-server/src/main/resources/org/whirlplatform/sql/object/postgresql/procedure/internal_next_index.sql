/**
 * Считает количество ключей  в параметре p_result.parameter_index
 *
 * @param p_result          Переменная типа "function_result"
 *
 * @return Возвращает количество ключей из переданной переменной
 */
CREATE OR REPLACE FUNCTION internal_next_index (
    p_result function_result)
RETURNS integer
AS
$$
DECLARE
    v_result integer;
BEGIN
    SELECT count (*) INTO v_result FROM skeys (p_result.parameter_index);

    RETURN v_result;
END;
$$
LANGUAGE plpgsql