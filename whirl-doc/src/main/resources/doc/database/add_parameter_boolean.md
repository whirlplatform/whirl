[Содержание](index.md)

# **ADD_PARAMETER_BOOLEAN Function**
Добавляет параметр типа "boolean" к результату выполнения события 

### Parameters
| Name      | Description                                                      |
|-----------|------------------------------------------------------------------|
| p_result  | Переменная результата выполнения события                         |
| p_code    | Код параметра                                                    |
| p_value   | Значение параметра                                               |
| *return*  | Переданную переменную типа "function_result" с новыми значениями |

### Syntax
     CREATE OR REPLACE FUNCTION add_parameter_boolean (
    p_result function_result,
    p_code varchar,
    p_value boolean)
    RETURNS function_result
    AS
    $$
    DECLARE
    v_index integer;
    v_value varchar;
    BEGIN
    v_index := internal_next_index (p_result);

    IF p_value IS NOT NULL
    THEN
    v_value := p_value::varchar;
    END IF;

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
    p_result.parameter_value :=
    p_result.parameter_value::hstore || hstore (p_code, v_value);
    ELSE
    p_result.parameter_value := hstore (p_code, v_value);
    END IF;

    IF (SELECT count (*)
    FROM skeys (p_result.parameter_type)) > 0
    THEN
    p_result.parameter_type := p_result.parameter_type::hstore || hstore (p_code, 'BOOLEAN');
    ELSE
    p_result.parameter_type := hstore (p_code, 'BOOLEAN');
    END IF;

    RETURN p_result;
    END;
