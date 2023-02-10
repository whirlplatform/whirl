[Содержание](index.md)

# **ADD_PARAMETER_VARCHAR Function**
Добавляет параметр типа "boolean" к результату выполнения события

### Parameters
| Name      | Description                                                      |
|-----------|------------------------------------------------------------------|
| p_result  | Переменная результата выполнения события                         |
| p_code    | Код параметра                                                    |
| p_value   | Значение параметра                                               |
| *return*  | Переданную переменную типа "function_result" с новыми значениями |

### Syntax
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