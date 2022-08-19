CREATE OR REPLACE FUNCTION set_next_event(
    p_result function_result,
    p_next_event varchar)
    RETURNS function_result
AS
$$
BEGIN
    p_result.next_event := p_next_event;
    RETURN p_result;
END;
$$
    LANGUAGE plpgsql