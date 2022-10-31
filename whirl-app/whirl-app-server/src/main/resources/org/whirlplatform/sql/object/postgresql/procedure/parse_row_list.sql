CREATE OR REPLACE FUNCTION parse_row_list(pfvalue text)
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

    if pfvalue is null or trim(pfvalue) = '' then
        return v_result;
    end if;

    v_rows = string_to_array(pfvalue, ':');

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
$function$
;
