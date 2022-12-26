# **GET_PARAMETER_LIST_ID Function**
Возвращает значение идентификатора переметра по коду

### Parameters
| Name     | Description                                  |
|----------|----------------------------------------------|
| p_input  | Переменная входных данных вызванного события |
| p_code   | Код параметра                                |
| *return* | Id в виде текста                             |

### Syntax
     CREATE OR REPLACE FUNCTION get_parameter_list_id(p_input function_input, p_code character varying)
    RETURNS text
    LANGUAGE plpgsql
    AS $function$
    declare
    v_val text;
    begin
    v_val := (p_input.parameter_value -> p_code)::text;
    return v_val;
    END;
