[Содержание](index.md)

# **PARSE_ROW_LIST Function**
Парсит входящую переменную типа текст и заполняет поля переменной типа "row_value"

### Parameters
| Name     | Description                                     |
|----------|-------------------------------------------------|
| p_value  | Переменная типа текст без ограничений           |
| *return* | Текст с заполненной переменной типа "row_value" |

### Syntax
     CREATE OR REPLACE FUNCTION parse_row_list(p_value text)
    RETURNS text
    LANGUAGE plpgsql
    AS $function$
    declare
    v_result  row_list;
    v_row     row_value;

    v_rows    varchar[];
    v_els_str varchar(32767);
    v_els     varchar[];
    BEGIN

    if p_value is null or trim(p_value) = '' then
        return v_result;
    end if;

    v_rows = string_to_array(p_value, ':');

    for i in 1 .. (array_length(v_rows, 1)) loop
            v_els_str = v_rows[i];
            v_els = string_to_array(v_els_str, ';');

            v_row.id       = v_els[1];
            v_row.selected = v_els[2];
            v_row.checked  = v_els[3];
            v_row.expanded = v_els[4];

            v_result.list_name[i] := v_row;
        end loop;

    return v_result.list_name;
    END;