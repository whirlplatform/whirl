/**
 * Возвращает значение параметра типа text по коду
 *
 * @param p_input           Переменная входных данных вызванного события
 * @param p_code            Код параметра
 *
 * @return Текст со значением параметра
 */
CREATE OR REPLACE FUNCTION get_parameter_text(p_input function_input, p_code character varying)
    RETURNS text
    LANGUAGE plpgsql
AS $function$
BEGIN
    RETURN (p_input.parameter_value ->> p_code);
END;
$function$
;