# **GET_PARAMETER_COUNT Function**
Считает количество параметров входящей переменной

### Parameters
| Name     | Description                                  |
|----------|----------------------------------------------|
| p_input  | Переменная входных данных вызванного события |
| *return* | Количество параметров                        |

### Syntax
     CREATE OR REPLACE FUNCTION get_parameters_count(p_input function_input)
    RETURNS numeric
    LANGUAGE plpgsql
    AS $function$
    declare
    v_index numeric;
    begin
    SELECT count (*) INTO v_index FROM skeys(p_input.parameter_index);
    return v_index;
    END;
