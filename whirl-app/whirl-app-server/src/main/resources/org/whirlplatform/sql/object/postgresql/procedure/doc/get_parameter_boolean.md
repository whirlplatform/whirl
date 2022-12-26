# **GET_PARAMETER_BOOLEAN Function**
Возвращает значение параметра типа "boolean" по коду
Если параметр по указанному коду отсутствует, то возвращается null

### Parameters
| Name     | Description                                  |
|----------|----------------------------------------------|
| p_input  | Переменная входных данных вызванного события |
| p_code   | Код параметра                                |
| *return* | true/false                                   |

### Syntax
     CREATE OR REPLACE FUNCTION get_parameter_boolean(p_input function_input, p_code character varying)
    RETURNS boolean
    LANGUAGE plpgsql
    AS $function$
    declare
    v_bool boolean;
    v_input text;
    begin
    v_input:= p_input.parameter_value -> p_code;
    v_bool := v_input::boolean;
    return v_bool;
    EXCEPTION
    WHEN OTHERS THEN
    RAISE NOTICE 'Invalid date value: "%".  Returning NULL.', v_input;
    return null;
    END;
