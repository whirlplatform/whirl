[Содержание](index.md)

# **AS_RESULT Function**
Функция формирует ответ возвращаемый в качестве результата выполнения события 
Сериализует тип "function_result" в текстовое представление

### Parameters
| Name      | Description                                        |
|-----------|----------------------------------------------------|
| p_result  | Переменная результата выполнения события           |
| *return*  | Текст со значением параметров входящей переменной  |

### Syntax
     CREATE OR REPLACE FUNCTION as_result(p_result function_result)
    RETURNS text
    LANGUAGE plpgsql
    AS $function$
    DECLARE
    v_result          hstore;
    v_parameter_array hstore[];
    v_parameter       hstore;
    v_parameter_rec   record;
    v_parameter_index integer;
    v_parameter_code  varchar(4000);
    v_parameter_type  varchar(4000);
    v_out             text;
    BEGIN
    v_result := ''::hstore;

    IF p_result.message IS NOT NULL
    THEN
        v_result := v_result || hstore('message', p_result.message);
        v_result := v_result || hstore('title', p_result.title);
        v_result := v_result || hstore('messageType', p_result.message_type);
    END IF;

    IF p_result.next_event IS NOT NULL
    THEN
        v_result := v_result || hstore ('nextEvent', p_result.next_event);
    END IF;

    v_parameter_array := '{}';
    v_parameter_index := 0;

    FOR v_parameter_rec IN (SELECT *
                            FROM each (p_result.parameter_index))
        LOOP
            v_parameter_index := v_parameter_index + 1;

            v_parameter := ''::hstore;

            v_parameter :=
                        v_parameter || hstore ('index', v_parameter_index::varchar);

            v_parameter_code := p_result.parameter_index -> v_parameter_rec.key;
            v_parameter := v_parameter || hstore ('code', v_parameter_code);

            IF exist (p_result.parameter_component, v_parameter_code)
            THEN
                v_parameter := v_parameter || hstore('component', p_result.parameter_component -> v_parameter_code);
            ELSE
                v_parameter_type := p_result.parameter_type -> v_parameter_code;
                v_parameter := v_parameter || hstore ('type', v_parameter_type);

                IF v_parameter_type = 'LIST'
                THEN
                    v_parameter := v_parameter || hstore('title', p_result.parameter_list_title -> v_parameter_code);
                END IF;

                v_parameter := v_parameter || hstore('value', p_result.parameter_value -> v_parameter_code);
            END IF;

            v_parameter_array[v_parameter_index] := v_parameter;
        END LOOP;
    v_out := to_json(v_result)::varchar;
    RETURN '{"result": ' || (case when v_out != '{}' then rtrim(v_out, '}') || ', ' else '' end) ||  '"parameters": ' || to_json (v_parameter_array) || '}}';
    END;
