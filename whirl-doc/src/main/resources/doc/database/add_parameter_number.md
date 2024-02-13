[Содержание](index.md)

# **ADD_PARAMETER_NUMBER Function**
Добавляет параметр типа "numeric" к результату выполнения события

### Parameters
| Name      | Description                                                      |
|-----------|------------------------------------------------------------------|
| p_result  | Переменная результата выполнения события                         |
| p_code    | Код параметра                                                    |
| p_value   | Значение параметра                                               |
| *return*  | Переданную переменную типа "function_result" с новыми значениями |

### Syntax
    CREATE OR REPLACE FUNCTION add_parameter_number (
        p_result function_result,
        p_code varchar,
        p_value numeric)
        RETURNS function_result
        LANGUAGE plpgsql
    AS $$
    DECLARE
        v_index integer;
    BEGIN
        SELECT count(*) INTO v_index FROM jsonb_object_keys(p_result.parameter_index);
    
        p_result.parameter_index := jsonb_set(p_result.parameter_index, array[v_index + 1]::text[], to_jsonb(p_code));
    
        IF p_value IS NOT NULL THEN
            p_result.parameter_value := jsonb_set(p_result.parameter_value, array[p_code], to_jsonb(p_value));
        END IF;
    
        p_result.parameter_type := jsonb_set(p_result.parameter_type, array[p_code], to_jsonb('NUMBER'::varchar));
    
        RETURN p_result;
    END;
    $$;

