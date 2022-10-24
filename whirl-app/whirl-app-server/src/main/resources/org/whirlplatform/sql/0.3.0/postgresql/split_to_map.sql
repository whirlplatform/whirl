CREATE OR REPLACE FUNCTION split_to_map(pfvalue text, pfdelimeter text DEFAULT ','::text)
    RETURNS text
    LANGUAGE plpgsql
AS $function$
declare
    v_result  index_map;
    v_list    text := pfvalue;
    v_idx     int;
    v_counter int := 1;
BEGIN
    loop
        v_idx := position(pfdelimeter in v_list);

        if v_idx > 0
        then
            v_result.map_element[v_counter] := (substr(v_list, 1, v_idx - 1));
            v_counter := v_counter + 1;
            v_list := substr(v_list, v_idx + length(pfdelimeter));
        else
            v_result.map_element[v_counter] := v_list;
            v_counter := v_counter + 1;
            exit;
        end if;
    end loop;
    return v_result.map_element;
END;
$function$
;
