CREATE OR REPLACE FUNCTION parse_row_list(pfvalue text)
    RETURNS text
    LANGUAGE plpgsql
AS $function$
declare
    v_result  row_list;
    v_row     row_value;

    v_rows    index_map;
    v_els_str varchar(32767);
    v_els     index_map;
BEGIN

    if pfvalue is null or trim(pfvalue) = '' then
        return v_result;
    end if;

    v_rows.map_element = split_to_map(pfvalue, ':');

    for i in 1 .. (array_length(v_rows.map_element, 1)) loop
            v_els_str = v_rows.map_element[i];
            v_els.map_element = split_to_map(v_els_str, ';');

            v_row.fid       = v_els.map_element[1];
            v_row.selected = v_els.map_element[2];
            v_row.checked  = v_els.map_element[3];
            v_row.expanded = v_els.map_element[4];

            v_result.list_name[i] := v_row;
        end loop;

    return v_result.list_name;
END;
$function$
;
