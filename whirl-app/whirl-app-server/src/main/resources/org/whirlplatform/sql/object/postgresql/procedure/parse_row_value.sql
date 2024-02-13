/**
 * Парсит входящую переменную типа текст и заполняет поля переменной типа "row_value"
 *
 * @param p_value           Переменная типа текст без ограничений
 *
 * @return                Массив типа "row_value"
 */
CREATE OR REPLACE FUNCTION whirl.parse_row_value(p_value text)
    RETURNS whirl.row_value[]
    LANGUAGE plpgsql
AS $function$
declare
    v_result  whirl.row_value[];
    v_row     whirl.row_value;

    v_rows    varchar[];
    v_els_str varchar(32767);
    v_els     varchar[];
BEGIN

    if p_value is null or trim(p_value) = '' then
        return v_result;
    end if;

    v_rows = string_to_array(p_value, ':');

    for i in 1 .. (array_length(v_rows, 1))
        loop
            v_els_str = v_rows[i];
            v_els = string_to_array(v_els_str, ';');

            v_row.id       = v_els[1];
            v_row.selected = v_els[2];
            v_row.checked  = v_els[3];
            v_row.expanded = v_els[4];

            v_result[i] := v_row;
        end loop;

    return v_result;
END;
$function$
;
