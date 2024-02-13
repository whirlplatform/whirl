/**
 * Устанавливает заголовок, текст и тип отображаемого пользователю сообщения.
 * Типы сообщений: 'INFO', 'WARN', 'ERROR'
 *
 * @param p_result          Переменная результата выполнения события
 * @param p_title           Заголовок - строковая переменная, без ограничений
 * @param p_message         Тело сообщения - строковая переменная, без ограничений
 * @param p_message_type    Тип сообщения - строковая переменная с заданным по умолчанию типом 'INFO'
 *
 * @return Переданную переменную с новыми параметрами
 */
create or replace function set_message (
    p_result function_result,
    p_title varchar,
    p_message varchar,
    p_message_type varchar default 'INFO')
    RETURNS function_result
    LANGUAGE plpgsql
AS $function$
begin
    p_result.title := p_title;
    p_result.message := p_message;
    p_result.message_type := p_message_type;
    return p_result;
end;
$function$
;