CREATE FUNCTION wrl_set_next_event(
    p_result JSON,
    p_next_event varchar(4000))
    RETURNS JSON
    LANGUAGE SQL
BEGIN
    RETURN JSON_SET(p_result, '$.nextEvent', p_next_event);
END