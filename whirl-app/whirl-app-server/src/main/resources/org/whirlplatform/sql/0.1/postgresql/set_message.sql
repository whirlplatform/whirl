CREATE OR REPLACE FUNCTION set_message (
    p_result function_result,
    p_title varchar,
    p_message varchar,
    p_message_type varchar DEFAULT 'INFO')
RETURNS function_result
AS
$$
BEGIN
    p_result.title := p_title;
    p_result.message := p_message;
    p_result.message_type := p_message_type;
    RETURN p_result;
END;
$$
LANGUAGE plpgsql