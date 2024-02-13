[Содержание](index.md)

# **GET_PARAMETER_DATE Function**
Возвращает значение параметра "DATA" по коду. 
Если параметр по указанному коду отсутствует, то возвращается null.

### Parameters
| Name     | Description                                    |
|----------|------------------------------------------------|
| p_input  | Переменная входных данных вызванного события   |
| p_code   | Код параметра                                  |
| *return* | Переменную типа "timestamp" без часового пояса |

### Syntax
    CREATE OR REPLACE FUNCTION get_parameter_date(
        p_input function_input, 
        p_code character varying)
        RETURNS timestamp without time zone
        LANGUAGE plpgsql
    AS $$
    DECLARE
        v_time timestamp;
        v_input text;
    BEGIN
        v_input := (p_input.parameter_value ->> p_code);
        v_time := to_timestamp(v_input, 'DD.MM.YYYY HH24:MI:SS');
        RETURN v_time;
        EXCEPTION
            WHEN OTHERS THEN
                RAISE NOTICE 'Invalid date value: "%". Returning NULL.', v_input;
                    RETURN NULL;
    END;
    $$;
